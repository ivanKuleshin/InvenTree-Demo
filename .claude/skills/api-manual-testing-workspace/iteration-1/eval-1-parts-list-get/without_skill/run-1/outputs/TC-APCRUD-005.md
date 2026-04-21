# TC-APCRUD-005 — GET /api/part/ With ?parameters=true Embeds Parameter Array

## Metadata
- **Endpoint**: GET /api/part/
- **Priority**: P1
- **Auth**: Required (Basic or Token)
- **Observed on**: 2026-04-18 against https://demo.inventree.org (API v479, InvenTree v1.4.0 dev)

## Objective
Verify that the `parameters=true` query flag causes each part object in the list response to include an embedded `parameters` array containing the part's parameter values and template details.

## Preconditions
- Authenticated as `reader` role
- At least one part has parameters defined (e.g., pk=82 has 4 parameters: Length, Width, Height, Color)
- At least one part has no parameters (e.g., pk=69 "530470210" has 0 parameters)

## Request
```
GET /api/part/?limit=5&parameters=true&format=json
Authorization: Basic cmVhZGVyOnJlYWRvbmx5
```

## Expected Response

**Status**: 200 OK

**Each part object additionally contains**:
```json
"parameters": [
  {
    "pk": <integer>,
    "template": <integer>,
    "model_type": "part.part",
    "model_id": <integer — matches part pk>,
    "data": "<string>",
    "data_numeric": <number or null>,
    "note": "<string>",
    "updated": <string or null>,
    "updated_by": <integer or null>,
    "template_detail": {
      "pk": <integer>,
      "name": "<string>",
      "units": "<string>",
      "description": "<string>",
      "model_type": <null or string>,
      "checkbox": <boolean>,
      "choices": "<string>",
      "selectionlist": <null or integer>,
      "enabled": <boolean>
    },
    "updated_by_detail": <null or object>
  }
]
```

## Assertions
1. Status code is 200.
2. Each part object contains a `parameters` key.
3. For a part WITH parameters: `parameters` is a non-empty array.
4. For a part WITHOUT parameters: `parameters` is an empty array `[]` (key is present, value is empty list).
5. Each parameter entry contains: `pk`, `template`, `model_type`, `model_id`, `data`, `data_numeric`, `note`, `updated`, `updated_by`, `template_detail`, `updated_by_detail`.
6. `model_type` is always `"part.part"`.
7. `model_id` matches the parent part's `pk`.
8. `template_detail` is an object with keys: `pk`, `name`, `units`, `description`, `model_type`, `checkbox`, `choices`, `selectionlist`, `enabled`.
9. Without `parameters=true`, the `parameters` key is absent from the response.

## Observed Sample (pk=82, parameters=true)

Parameters array contains 4 items:
- `pk=14495`, template=235, name="Length", units="mm", data="35", data_numeric=35.0
- `pk=14496`, template=237, name="Width", units="mm", data="20", data_numeric=20.0
- `pk=14497`, template=238, name="Height", units="mm", data="15", data_numeric=15.0
- `pk=14518`, template=239, name="Color", units="", data="Black", data_numeric=null, choices="Red, Blue, Green, ..."

Part pk=69 ("530470210") without parameters: `"parameters": []` (empty array, key present)

## Notes
- `data` is always a string; `data_numeric` is a float when the value is numeric, null otherwise.
- `choices` on the template_detail is a comma-separated string of valid values for selection-type parameters.
