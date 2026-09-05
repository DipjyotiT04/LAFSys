# ItemService.java - Business Logic

**Path:** `src/ItemService.java`

Handles all item operations using Java streams.

## Methods

- `postItem(...)` - Create item, auto-generate ID, save
- `searchByKeyword(word)` - Case-insensitive search
- `searchByCategory(cat)` - Exact category match
- `getItemById(id)` - Find single item
- `claimItem(id)` - Mark resolved
- `getLostItems()` / `getFoundItems()` / `getResolvedItems()` - Filter lists
- `getAllItems()` - Return all

## Constants

- `TYPE_LOST = "LOST"`
- `TYPE_FOUND = "FOUND"`
- `STATUS_RESOLVED = "RESOLVED"`
