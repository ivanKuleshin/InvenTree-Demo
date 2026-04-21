#!/usr/bin/env python3
"""Research toolkit for the requirements-researcher agent.

Covers the data-retrieval request types the agent handles:

  - OpenAPI schema (primary source): list endpoints, get one endpoint, get
    a component schema, list/search schema names. Pulled live from the
    demo instance because the GitHub-hosted `docs/docs/api/schema/*.md`
    files only contain `[OAD(...)]` directives that point at
    build-time-generated YAML not committed to the repo.
  - Rendered documentation pages: fetch `docs.inventree.org` first, fall
    back to the GitHub-rendered `github.com/inventree/InvenTree/blob/...`
    page, never the raw variant. Output is plain-text extracted from the
    HTML so the researcher can scan headings/content without shipping
    full HTML into its context window.
  - GitHub raw Markdown: last-resort direct source (kept because some
    pages 403 on docs.inventree.org).
  - Local docs inventory: enumerate MD files already present under
    `docs/{component}/` so the researcher can decide whether to
    re-fetch or reuse existing coverage.
  - Demo credentials page: fetches `inventree.org/demo.html` and
    extracts the plain-text block.

Only Python stdlib is used so the agent can run it without install
steps. Schema JSON is cached to `/tmp` between calls.

Run `python inventree_research.py --help` for the subcommand list.
"""

from __future__ import annotations

import argparse
import json
import os
import re
import ssl
import sys
import urllib.error
import urllib.parse
import urllib.request
from html.parser import HTMLParser
from pathlib import Path
from typing import Any

REPO_ROOT = Path(__file__).resolve().parents[4]
DOCS_ROOT = REPO_ROOT / "docs"
SCHEMA_URL = "https://demo.inventree.org/api/schema/"
SCHEMA_CACHE = Path("/tmp/inventree-openapi-schema.json")
DOCS_WEB_BASE = "https://docs.inventree.org/en/stable/"
DOCS_GITHUB_BASE = "https://github.com/inventree/InvenTree/blob/master/docs/docs/"
DOCS_RAW_BASE = "https://raw.githubusercontent.com/inventree/InvenTree/master/docs/docs/"
DEMO_URL = "https://inventree.org/demo.html"
USER_AGENT = "inventree-research-toolkit/1.0"


def _build_ssl_context() -> ssl.SSLContext:
    try:
        import certifi

        return ssl.create_default_context(cafile=certifi.where())
    except ImportError:
        pass
    for candidate in ("/etc/ssl/cert.pem", "/etc/ssl/certs/ca-certificates.crt"):
        if os.path.exists(candidate):
            return ssl.create_default_context(cafile=candidate)
    return ssl.create_default_context()


_SSL_CTX = _build_ssl_context()


def _http_get(url: str, accept: str = "text/html") -> tuple[int, str]:
    req = urllib.request.Request(
        url,
        headers={"Accept": accept, "User-Agent": USER_AGENT},
    )
    try:
        with urllib.request.urlopen(req, timeout=30, context=_SSL_CTX) as resp:
            return resp.status, resp.read().decode("utf-8", errors="replace")
    except urllib.error.HTTPError as exc:
        return exc.code, exc.read().decode("utf-8", errors="replace") if exc.fp else ""


def load_schema(force_refresh: bool = False) -> dict[str, Any]:
    if SCHEMA_CACHE.exists() and not force_refresh:
        age = os.path.getmtime(SCHEMA_CACHE)
        if (os.path.getmtime(SCHEMA_CACHE) - age) <= 86_400:
            with SCHEMA_CACHE.open("r", encoding="utf-8") as f:
                return json.load(f)
    status, body = _http_get(SCHEMA_URL, accept="application/json")
    if status != 200:
        raise SystemExit(f"schema fetch failed: HTTP {status}")
    SCHEMA_CACHE.write_text(body, encoding="utf-8")
    return json.loads(body)


def cmd_schema_info(args: argparse.Namespace) -> None:
    schema = load_schema(force_refresh=args.refresh)
    info = schema.get("info", {})
    paths = schema.get("paths", {})
    components = schema.get("components", {}).get("schemas", {})
    print(f"title:        {info.get('title')}")
    print(f"version:      {info.get('version')}")
    print(f"api version:  {info.get('x-api-version') or info.get('description', '')[:80]}")
    print(f"path count:   {len(paths)}")
    print(f"schema count: {len(components)}")
    print(f"cache file:   {SCHEMA_CACHE}")


def cmd_endpoints(args: argparse.Namespace) -> None:
    schema = load_schema(force_refresh=args.refresh)
    paths = schema.get("paths", {})
    prefix = args.prefix
    rows: list[tuple[str, str, str]] = []
    for path, methods in sorted(paths.items()):
        if prefix and not path.startswith(prefix):
            continue
        for method, op in methods.items():
            if method.startswith("x-") or method == "parameters":
                continue
            summary = (op.get("summary") or op.get("operationId") or "").strip()
            rows.append((method.upper(), path, summary))
    if not rows:
        print(f"(no endpoints match prefix {prefix!r})")
        return
    width_m = max(len(r[0]) for r in rows)
    width_p = max(len(r[1]) for r in rows)
    for method, path, summary in rows:
        print(f"{method:<{width_m}}  {path:<{width_p}}  {summary}")
    print(f"\n{len(rows)} endpoint(s) matching prefix {prefix!r}")


def cmd_endpoint(args: argparse.Namespace) -> None:
    schema = load_schema(force_refresh=args.refresh)
    methods = schema.get("paths", {}).get(args.path)
    if not methods:
        raise SystemExit(f"path {args.path!r} not found in schema")
    op = methods.get(args.method.lower())
    if not op:
        raise SystemExit(f"method {args.method} not defined on {args.path}")
    print(json.dumps(op, indent=2, sort_keys=True))


def cmd_schema_show(args: argparse.Namespace) -> None:
    schema = load_schema(force_refresh=args.refresh)
    components = schema.get("components", {}).get("schemas", {})
    if args.name not in components:
        matches = [n for n in components if args.name.lower() in n.lower()]
        raise SystemExit(
            f"schema {args.name!r} not found. Closest: {matches[:10]}"
        )
    print(json.dumps(components[args.name], indent=2, sort_keys=True))


def cmd_schema_list(args: argparse.Namespace) -> None:
    schema = load_schema(force_refresh=args.refresh)
    names = sorted(schema.get("components", {}).get("schemas", {}).keys())
    pat = args.pattern
    if pat:
        regex = re.compile(pat, re.IGNORECASE)
        names = [n for n in names if regex.search(n)]
    for n in names:
        print(n)
    print(f"\n{len(names)} schema(s)")


class _TextExtractor(HTMLParser):
    _NOISE_TAGS = {"script", "style", "nav", "footer", "header", "aside"}

    def __init__(self, scope_tag: str | None = "article") -> None:
        super().__init__()
        self._parts: list[str] = []
        self._noise_depth = 0
        self._scope_tag = scope_tag
        self._scope_depth = 0
        self._in_scope = scope_tag is None

    def handle_starttag(self, tag: str, attrs: list[tuple[str, str | None]]) -> None:
        if self._scope_tag and tag == self._scope_tag:
            self._scope_depth += 1
            self._in_scope = True
        if not self._in_scope:
            return
        if tag in self._NOISE_TAGS:
            self._noise_depth += 1
            return
        if self._noise_depth > 0:
            return
        if tag in {"p", "br", "li", "h1", "h2", "h3", "h4", "h5", "h6", "tr", "pre"}:
            self._parts.append("\n")
        if tag in {"h1", "h2", "h3", "h4", "h5", "h6"}:
            level = int(tag[1])
            self._parts.append("#" * level + " ")
        if tag == "li":
            self._parts.append("- ")
        if tag == "code":
            self._parts.append("`")
        if tag == "img":
            alt = next((v for k, v in attrs if k == "alt" and v), "image")
            src = next((v for k, v in attrs if k == "src" and v), "")
            self._parts.append(f"[IMAGE: {alt} ({src})]")

    def handle_endtag(self, tag: str) -> None:
        if self._scope_tag and tag == self._scope_tag and self._scope_depth > 0:
            self._scope_depth -= 1
            if self._scope_depth == 0:
                self._in_scope = False
        if tag in self._NOISE_TAGS and self._noise_depth > 0:
            self._noise_depth -= 1
            return
        if not self._in_scope or self._noise_depth > 0:
            return
        if tag == "code":
            self._parts.append("`")

    def handle_data(self, data: str) -> None:
        if self._in_scope and self._noise_depth == 0:
            self._parts.append(data)

    def text(self) -> str:
        raw = "".join(self._parts)
        return re.sub(r"\n{3,}", "\n\n", raw).strip()


def _extract_page_text(html: str) -> str:
    """Extract readable text from an HTML page, preferring <article> scope."""
    for scope in ("article", "main", None):
        parser = _TextExtractor(scope_tag=scope)
        parser.feed(html)
        text = parser.text()
        if len(text) > 200:
            return text
    return text


def cmd_docs_web(args: argparse.Namespace) -> None:
    path = args.path.lstrip("/")
    url = DOCS_WEB_BASE + path
    status, body = _http_get(url)
    if status == 200:
        source = url
    else:
        if path.endswith("/") or "." not in path.rsplit("/", 1)[-1]:
            gh_path = path.rstrip("/") + "/index.md" if path else "index.md"
            gh_path = gh_path.lstrip("/")
        elif path.endswith(".html"):
            gh_path = path[:-5] + ".md"
        else:
            gh_path = path
        url = DOCS_GITHUB_BASE + gh_path
        status, body = _http_get(url)
        if status != 200:
            raise SystemExit(
                f"both docs.inventree.org and github fallback failed "
                f"(docs.inventree.org/en/stable/{path}, {url}, last HTTP {status})"
            )
        source = url
    print(f"# source: {source}\n")
    print(_extract_page_text(body))


def cmd_docs_raw(args: argparse.Namespace) -> None:
    url = DOCS_RAW_BASE + args.path.lstrip("/")
    status, body = _http_get(url)
    if status != 200:
        raise SystemExit(f"raw fetch failed: HTTP {status} {url}")
    print(f"# source: {url}\n")
    print(body)


def cmd_local_scan(args: argparse.Namespace) -> None:
    target = DOCS_ROOT if not args.component else DOCS_ROOT / args.component
    if not target.exists():
        print(f"(no local docs at {target})")
        return
    files = sorted(target.rglob("*.md"))
    if not files:
        print(f"(no .md files under {target})")
        return
    for p in files:
        rel = p.relative_to(REPO_ROOT)
        size = p.stat().st_size
        print(f"{size:>7}  {rel}")
    print(f"\n{len(files)} file(s) under {target.relative_to(REPO_ROOT)}")


def cmd_demo(args: argparse.Namespace) -> None:
    status, body = _http_get(DEMO_URL)
    if status != 200:
        raise SystemExit(f"demo fetch failed: HTTP {status}")
    print(f"# source: {DEMO_URL}\n")
    print(_extract_page_text(body))


def build_parser() -> argparse.ArgumentParser:
    p = argparse.ArgumentParser(description="InvenTree research toolkit for the requirements-researcher agent.")
    sub = p.add_subparsers(dest="command", required=True)

    sp = sub.add_parser("schema-info", help="Show schema metadata and cache path")
    sp.add_argument("--refresh", action="store_true", help="Force re-download of the schema")
    sp.set_defaults(func=cmd_schema_info)

    sp = sub.add_parser("endpoints", help="List endpoints, optionally filtered by path prefix")
    sp.add_argument("--prefix", default="", help='Path prefix filter, e.g. "/api/stock/"')
    sp.add_argument("--refresh", action="store_true")
    sp.set_defaults(func=cmd_endpoints)

    sp = sub.add_parser("endpoint", help="Print full OpenAPI spec for a single path+method")
    sp.add_argument("--path", required=True)
    sp.add_argument("--method", required=True, choices=["get", "post", "put", "patch", "delete", "options", "head"])
    sp.add_argument("--refresh", action="store_true")
    sp.set_defaults(func=cmd_endpoint)

    sp = sub.add_parser("schema-list", help="List component schema names; --pattern filters by regex")
    sp.add_argument("--pattern", default="")
    sp.add_argument("--refresh", action="store_true")
    sp.set_defaults(func=cmd_schema_list)

    sp = sub.add_parser("schema-show", help="Print a named component schema")
    sp.add_argument("--name", required=True)
    sp.add_argument("--refresh", action="store_true")
    sp.set_defaults(func=cmd_schema_show)

    sp = sub.add_parser("docs-web", help="Fetch a docs.inventree.org page (falls back to GitHub-rendered)")
    sp.add_argument("--path", required=True, help='e.g. "stock/" or "part/test/"')
    sp.set_defaults(func=cmd_docs_web)

    sp = sub.add_parser("docs-raw", help="Fetch raw Markdown from GitHub (last resort)")
    sp.add_argument("--path", required=True, help='e.g. "stock/index.md"')
    sp.set_defaults(func=cmd_docs_raw)

    sp = sub.add_parser("local-scan", help="List local .md files under docs/{component}/")
    sp.add_argument("--component", default="", help='e.g. "stock" or "parts"; omit for whole docs/ tree')
    sp.set_defaults(func=cmd_local_scan)

    sp = sub.add_parser("demo", help="Fetch demo credentials page")
    sp.set_defaults(func=cmd_demo)

    return p


def main(argv: list[str] | None = None) -> int:
    parser = build_parser()
    args = parser.parse_args(argv)
    args.func(args)
    return 0


if __name__ == "__main__":
    sys.exit(main())
