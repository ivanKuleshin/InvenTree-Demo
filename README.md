# This a TAF for UI and API testing using Agentic AI Workflow for InvenTree product - https://demo.inventree.org/.

- https://docs.inventree.org/en/stable/part/#part-attributes - main documentation link
- https://docs.inventree.org/en/stable/api/ - API documentation link

## Main idea is:

- Using AI agent analyze documentation and create md files with snapshot for affective use as a documentation database
- Using manual tester agent with UI and API testing skills develop and create test cases in MD format to cover main
  functionality.
- Each functionality line should be represented as a separate test suite.
- UI main functionality:
    - Part creation (manual entry and import flows)
    - Part detail view — all tabs (Stock, BOM, Allocated, Build Orders, Parameters, Variants, Revisions,
    - Attachments, Related Parts, Test Templates)
    - Part categories — hierarchy, filtering, parametric tables
    - Part attributes — Virtual, Template, Assembly, Component, Trackable, Purchasable, Salable, Active/Inactive
    - Units of measure configuration
    - Part revisions — creation, constraints (circular references, unique codes, template restrictions)
    - Negative and boundary scenarios (e.g., duplicate IPN, inactive part restrictions, revision-of-revision prevention)
- API main functionality:
    - CRUD operations on Parts and Part Categories
    - Filtering, pagination, and search on the Parts list endpoint
    - Field-level validation (required fields, max lengths, nullable constraints, read-only fields)
    - Relational integrity (category assignment, default locations, supplier linkage)
    - Edge cases (invalid payloads, unauthorized access, conflict scenarios)
- After manual test cases are created, UI automation agent (JS) and API automation agent (Java) will implement them in
  code, following the same structure and organization as the manual test cases for traceability.