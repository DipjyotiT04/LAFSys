# Item.java - Data Model

**Path:** `src/Item.java`
**Lines:** 40
**Purpose:** Plain Old Java Object (POJO) representing a lost-and-found item

---

## Overview

`Item.java` is a simple data model class that represents a single lost-or-found item. It holds all item attributes as private fields with getters, a setter for `status`, and methods for serialization (`toFileLine`) and display formatting (`getDisplayText`).

---

## Class Structure

```java
public class Item {
    private int id;
    private String type;
    private String name;
    private String category;
    private String date;
    private String location;
    private String contact;
    private String status;

    // Constructor
    public Item(int id, String type, String name, String category,
                String date, String location, String contact, String status)

    // Getters
    public int getId()
    public String getType()
    public String getName()
    public String getCategory()
    public String getDate()
    public String getLocation()
    public String getContact()
    public String getStatus()

    // Setter
    public void setStatus(String status)

    // Serialization
    public String toFileLine()

    // Display
    public String getDisplayText()
}
```

---

## Fields

| Field | Type | Description | Example |
|-------|------|-------------|---------|
| `id` | `int` | Unique identifier (auto-incremented) | `1` |
| `type` | `String` | Item type: `"LOST"` or `"FOUND"` | `"LOST"` |
| `name` | `String` | Description of the item | `"Wallet"` |
| `category` | `String` | Item category | `"Bags"` |
| `date` | `String` | Date in `dd-mm-yyyy` format | `"01-09-2026"` |
| `location` | `String` | Where item was lost/found | `"Library"` |
| `contact` | `String` | Contact number | `"9876543210"` |
| `status` | `String` | `"OPEN"` or `"RESOLVED"` | `"OPEN"` |

---

## Methods Breakdown

### Constructor
**Line:** 11

```java
public Item(int id, String type, String name, String category,
            String date, String location, String contact, String status)
```

Initializes all fields. Called by `ItemService.postItem()` and `StorageManager.loadItems()`.

### Getters (Lines 22-29)

Standard getter methods for all fields. No setters except for `status` - making the item mostly immutable after creation.

### `setStatus(String status)`
**Line:** 31

The only setter. Used when claiming an item (changes status from `OPEN` to `RESOLVED`).

### `toFileLine()`
**Line:** 33 | **Purpose:** Serialize item for file storage

```java
public String toFileLine() {
    return id + " | " + type + " | " + name + " | " + category + " | "
           + date + " | " + location + " | " + contact + " | " + status;
}
```

Returns a pipe-delimited string for writing to `data/items.txt`.

**Output format:**
```
1 | LOST | pl | book | 20-12-2006 | tinsukia | 210016664 | OPEN
```

### `getDisplayText()`
**Line:** 37 | **Purpose:** Format item for console display

```java
public String getDisplayText() {
    return String.format("%-5d %-24s %-12s %-20s %-12s %-9s",
            id, name, category, location, date, status);
}
```

Returns a formatted string aligned in columns for table display in the console.

**Output format:**
```
1    pl                       book         tinsukia            20-12-2006   OPEN
```

---

## Design Decisions

1. **Immutable by default** - Only `status` has a setter, preventing accidental field modifications
2. **String-based dates** - Stored as `dd-mm-yyyy` strings rather than `java.time.LocalDate` for simplicity
3. **No validation in model** - Validation is handled by `Validator` and `ItemService`, keeping the model clean
4. **Dual serialization** - `toFileLine()` for persistence, `getDisplayText()` for UI

---

## File Format Mapping

```
File: 1 | LOST | pl | book | 20-12-2006 | tinsukia | 210016664 | OPEN
       │    │     │    │       │            │           │          │
       ▼    ▼     ▼    ▼       ▼            ▼           ▼          ▼
      id  type  name category date      location     contact    status
```
