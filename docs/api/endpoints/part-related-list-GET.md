---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/related/ — List Part Related
fetched: 2026-04-13
---

# `GET /api/part/related/` — List Part Related

**Operation ID:** `part_related_list`

**Tags:** `part`

**Description:** API endpoint for accessing a list of PartRelated objects.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `r:view:part`

## Query Parameters

| Name       | In    | Type    | Required | Description                                                 |
| ---------- | ----- | ------- | -------- | ----------------------------------------------------------- |
| `limit`    | query | integer | **yes**  | Number of results to return per page.                       |
| `offset`   | query | integer | no       | The initial index from which to return the results.         |
| `ordering` | query | string  | no       | Which field to use when ordering the results.               |
| `part`     | query | integer | no       | Part                                                        |
| `part_1`   | query | integer | no       |                                                             |
| `part_2`   | query | integer | no       |                                                             |
| `search`   | query | string  | no       | A search term. Searched fields: part_1**name, part_2**name. |

## Responses

| Status Code | Description | Schema                                                                       |
| ----------- | ----------- | ---------------------------------------------------------------------------- |
| 200         | OK          | [PaginatedPartRelationList](../part-api-schema.md#paginatedpartrelationlist) |
