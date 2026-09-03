# ItemService.java - Business Logic Layer

**Path:** `src/ItemService.java`
**Lines:** 94
**Purpose:** Orchestrates all item operations (CRUD, search, claim)

---

## Overview

`ItemService` is the service layer that contains all business logic for the application. It defines constants for item types and statuses, manages ID generation, and provides methods for posting, searching, claiming, and filtering items. It delegates all persistence to `StorageManager`.

---

## Class Structure

```java
import java.util.ArrayList;
import java.util.List;

public class ItemService {
    // Constants
    public static final String TYPE_LOST = "LOST";
    public static final String TYPE_FOUND = "FOUND";
    public static final String STATUS_OPEN = "OPEN";
    public static final String STATUS_RESOLVED = "RESOLVED";
    public static final String[] CATEGORIES = {...};

    // Dependencies
    private StorageManager storage = new StorageManager();

    // Methods
    public int postItem(String type, String name, String category,
                        String date, String location, String contact)
    public List<Item> searchByKeyword(String word)
    public List<Item> searchByCategory(String category)
    public Item getItemById(int id)
    public boolean claimItem(int id)
    public List<Item> getLostItems()
    public List<Item> getFoundItems()
    public List<Item> getResolvedItems()
    public List<Item> getAllItems()
}
```

---

## Constants

| Constant | Value | Type | Purpose |
|----------|-------|------|---------|
| `TYPE_LOST` | `"LOST"` | `String` | Identifies lost items |
| `TYPE_FOUND` | `"FOUND"` | `String` | Identifies found items |
| `STATUS_OPEN` | `"OPEN"` | `String` | Default status for new items |
| `STATUS_RESOLVED` | `"RESOLVED"` | `String` | Status after item is claimed |
| `CATEGORIES` | `{"Electronics", "Books", ...}` | `String[]` | Valid item categories |

---

## Dependencies

| Dependency | Purpose |
|------------|---------|
| `StorageManager` | File I/O for loading and saving items |
| `Item` | Data model |
| `java.util.ArrayList` | Dynamic list creation |
| `java.util.List` | List interface |

---

## Methods Breakdown

### `postItem(...)`
**Line:** 13 | **Purpose:** Create and save a new item

```java
public int postItem(String type, String name, String category,
                    String date, String location, String contact)
```

**Workflow:**
1. Loads all existing items from disk via `storage.loadItems()`
2. Finds the maximum ID among existing items
3. Generates new ID as `maxId + 1`
4. Creates new `Item` with status `STATUS_OPEN`
5. Appends item to file via `storage.saveItem()`
6. Returns the new ID

**ID Generation:** Sequential, starting from 1. If file has items with IDs 1-15, new item gets ID 16.

---

### `searchByKeyword(String word)`
**Line:** 25 | **Purpose:** Case-insensitive search across name, location, and category

```java
public List<Item> searchByKeyword(String word)
```

**Logic:**
1. Converts search term to lowercase
2. Iterates through all items from `storage.loadItems()`
3. Matches if keyword appears in: `name`, `location`, or `category` (case-insensitive)
4. Returns matching items

**Example:**
- Search for `"ring"` matches `"Ring"`, `"Earring"`, `"Rings"`, etc.
- Search for `"RING"` produces identical results (case-insensitive)

---

### `searchByCategory(String category)`
**Line:** 38 | **Purpose:** Exact category match

```java
public List<Item> searchByCategory(String category)
```

**Logic:**
- Iterates through all items
- Matches if `item.getCategory().equalsIgnoreCase(category)`
- Returns matching items

**Valid categories:** Electronics, Books, ID Card, Bags, Keys, Clothes, Other

---

### `getItemById(int id)`
**Line:** 48 | **Purpose:** Find a single item by its ID

```java
public Item getItemById(int id)
```

- Returns the first item with matching ID
- Returns `null` if no item found

---

### `claimItem(int id)`
**Line:** 55 | **Purpose:** Mark an item as resolved

```java
public boolean claimItem(int id)
```

**Workflow:**
1. Loads all items
2. Finds item with matching ID AND status `STATUS_OPEN`
3. Changes status to `STATUS_RESOLVED`
4. Rewrites entire file with updated items via `storage.rewriteAll()`
5. Returns `true` on success, `false` if item not found or already resolved

**Idempotency:** Calling `claimItem()` on an already-resolved item returns `false` without error.

---

### `getLostItems()` / `getFoundItems()` / `getResolvedItems()`
**Lines:** 67, 75, 83 | **Purpose:** Filter items by type/status

All three follow the same pattern:
1. Load all items
2. Filter by `type` or `status`
3. Return filtered list

| Method | Filter |
|--------|--------|
| `getLostItems()` | `type == TYPE_LOST` |
| `getFoundItems()` | `type == TYPE_FOUND` |
| `getResolvedItems()` | `status == STATUS_RESOLVED` |

---

### `getAllItems()`
**Line:** 91 | **Purpose:** Return all items

Direct passthrough to `storage.loadItems()`.

---

## Data Flow

```
User Input (Main)
    │
    ▼
ItemService.postItem()
    │
    ├──► storage.loadItems()    (read existing data)
    │         │
    │         ▼
    │    data/items.txt
    │
    ├──► Generate new ID
    │
    ├──► new Item(...)
    │
    └──► storage.saveItem()     (append to file)
              │
              ▼
         data/items.txt
```

---

## Error Handling

- `claimItem()` returns `false` instead of throwing exceptions
- `getItemById()` returns `null` for missing items
- All methods handle empty file/lists gracefully
