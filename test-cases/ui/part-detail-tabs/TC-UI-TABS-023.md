### TC-UI-TABS-023: Upload a file attachment from the Attachments tab [MUTATING]

**Type**: UI
**Priority**: P1
**Page URL**: `https://demo.inventree.org/web/part/77/attachments`
**Preconditions**:

- [ ] Logged in as `admin` [AUTH REQUIRED]
- [ ] A local test file is available for upload (e.g., a small PDF or TXT file named `test-datasheet.pdf`)
- [ ] Navigate to: `https://demo.inventree.org/web/part/77/attachments`
- [ ] The "Attachments" tab is active

**Steps**:

1. Locate the "Add Attachment" or upload action button in the Attachments tab toolbar.
2. Click the "Add Attachment" button.
3. Observe that a file upload dialog or form opens.
4. In the dialog, click the file input field and select the local file `test-datasheet.pdf`.
5. Optionally, locate the "Comment" or "Description" field and enter `Test datasheet upload TC-023`.
6. Click the "Save" or "Submit" button.
7. Observe the Attachments table after the dialog closes.

**Expected Result**:
- An upload dialog opens containing a file input field and an optional "Comment" or "Description" field.
- After selecting the file and clicking "Submit", the dialog closes and the table refreshes.
- A new row appears in the Attachments table showing the filename `test-datasheet.pdf`, the comment, upload date (today), and uploader username "admin".
- A download link and a delete icon are visible on the new row.

**Observed** (filled during exploration):

- Element confirmed: "Attachments" tab — present
- Actual behavior: Tab present; upload dialog not directly exercised (session expired)
- Matches docs: Yes (docs describe "Upload new file attachments" with filename, description, date, uploader metadata)

**Notes**: Post-test cleanup: delete the uploaded `test-datasheet.pdf` attachment after this TC to keep the demo environment clean. Docs describe attachment metadata columns as: file name, comment/description, upload date, uploaded by.
