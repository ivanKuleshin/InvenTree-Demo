---
source: https://docs.inventree.org/en/stable/api/schema/part/
component: api
topic: GET /api/part/thumbs/ — List Part Thumbnails
fetched: 2026-04-13
---

# `GET /api/part/thumbs/` — List Part Thumbnails

**Operation ID:** `part_thumbs_list`

**Tags:** `part`

**Description:** API endpoint for retrieving information on available Part thumbnails.

## Authentication

**Required.** Any of:

- `tokenAuth` — Header: `Authorization: Token <token>`
- `basicAuth` — HTTP Basic authentication
- `cookieAuth` — Cookie: `sessionid`
- `oauth2` — scopes: `g:read`

## Query Parameters

| Name     | In    | Type    | Required | Description                                                                                   |
| -------- | ----- | ------- | -------- | --------------------------------------------------------------------------------------------- |
| `limit`  | query | integer | **yes**  | Number of results to return per page.                                                         |
| `offset` | query | integer | no       | The initial index from which to return the results.                                           |
| `search` | query | string  | no       | A search term. Searched fields: IPN, category\_\_name, description, keywords, name, revision. |

## Responses

| Status Code | Description | Schema                                                                 |
| ----------- | ----------- | ---------------------------------------------------------------------- |
| 200         | OK          | [PaginatedPartThumbList](../part-api/part-api-schema.md#paginatedpartthumblist) |
