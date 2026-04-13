---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/test-template/ — List Part Test Templates
fetched: 2026-04-13
---

# `GET /api/part/test-template/` — List Part Test Templates

**Operation ID:** `part_test_template_list`

**Tags:** `part`

**Description:** Override the GET method to determine export options.

## Authentication

**Required.** Any of:
- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part`

## Query Parameters

| Name | In | Type | Required | Description |
|------|----|------|----------|-------------|
| `enabled` | query | boolean | no |  |
| `has_results` | query | boolean | no | Has Results |
| `key` | query | string | no |  |
| `limit` | query | integer | **yes** | Number of results to return per page. |
| `offset` | query | integer | no | The initial index from which to return the results. |
| `ordering` | query | enum | no | Which field to use when ordering the results. Values: `enabled`, `-enabled`, `required`, `-required`, `requires_value`, `-requires_value`, `requires_attachment`, `-requires_attachment`, `results`, `-results`, `test_name`, `-test_name` |
| `part` | query | integer | no | Part |
| `required` | query | boolean | no |  |
| `requires_attachment` | query | boolean | no |  |
| `requires_value` | query | boolean | no |  |
| `search` | query | string | no | A search term. Searched fields: description, test_name. |

## Responses

| Status Code | Description | Schema |
|-------------|-------------|--------|
| 200 | OK | [PaginatedPartTestTemplateList](../part-api-schema.md#paginatedparttesttemplatelist) |
