# Basic Notes
* Implement Dagger dependencies
* Threads
* Refer to Design Document
* UPER - Understand, Plan, Execute, Refelct
* Add Mocks to some tests.

# Endpoints
### GetBook
* Composite primary key bookId & version
* Books are not deleted or modified to preserve versions.
* Instead of updating a book a new version is created and made active.

### RemoveBookFromCatalog
* Makes inactive; does NOT delete from database
* Return empty response object

### SubmitBookForPublishing
* no bookID assumes a new book and random ID is to be created
* add a record to PublishingStatus table
* default state is QUEUED >> IN_PROGRESS >> then SUCCESSFUL or FAILED
* return publishingRecordId

### GetPublishingStatus
* takes a publishingRecordId

