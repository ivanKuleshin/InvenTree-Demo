# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Test Automation Framework (TAF) for [InvenTree](https://demo.inventree.org/) — an open-source inventory management product. The framework targets the **Parts** domain via both UI and API automation, driven by an AI-assisted agentic workflow.

- InvenTree docs: https://docs.inventree.org/en/stable/part/#part-attributes
- InvenTree API docs: https://docs.inventree.org/en/stable/api/

## Build Commands

```bash
mvn clean install     # Clean build
mvn test              # Run tests
mvn package           # Package
```

Java version: JDK 21 (Amazon Corretto 21). CheckStyle is configured with Google/Sun profiles (v13.4.0).

## Architecture

The project follows a three-phase agentic workflow:

1. **Documentation phase** — An AI agent reads InvenTree docs and writes `.md` snapshot files to `.github/instructions/` to serve as a local documentation database for downstream agents.

2. **Manual test case phase** — A manual tester AI agent (with UI + API testing skills) reads those docs and generates test cases in Markdown format. Each functional area is a separate test suite, mirroring the coverage areas below.

3. **Automation phase** — Two automation agents implement the manual test cases in code, maintaining 1:1 structural traceability:
   - **API automation agent** → Java (Maven, REST Assured or equivalent)
   - **UI automation agent** → JavaScript (Playwright or equivalent)

## Coverage Areas

**UI**: Part creation (manual entry and import), Part detail tabs (Stock, BOM, Allocated, Build Orders, Parameters, Variants, Revisions, Attachments, Related Parts, Test Templates), Part categories (hierarchy, filtering, parametric tables), Part attributes (Virtual, Template, Assembly, Component, Trackable, Purchasable, Salable, Active/Inactive), Units of measure, Part revisions, negative/boundary scenarios.

**API**: CRUD on Parts and Part Categories, filtering/pagination/search, field-level validation, relational integrity (category, default location, supplier linkage), edge cases (invalid payloads, unauthorized access, conflicts).

## Current State

The project is at the scaffolding stage. `pom.xml` has no test dependencies yet. The `src/main/resources/archetype-resources/` subtree is a Maven Archetype template (excluded from compilation) — not production source code. Real test framework dependencies (e.g., REST Assured, JUnit 5, Selenium/Playwright) need to be added to `pom.xml` before implementing tests.
