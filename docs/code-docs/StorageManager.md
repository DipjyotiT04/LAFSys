# StorageManager.java - File I/O Persistence Layer

**Path:** `src/StorageManager.java`
**Lines:** 68
**Purpose:** Reads and writes item data to/from a flat text file

---

## Overview

`StorageManager` is the persistence layer responsible for all file I/O operations. It reads from and writes to `data/items.txt`, a pipe-delimited flat file. The class handles file creation, reading, appending, and full rewrites, with graceful error handling for missing files or I/O errors.

---

## Class Structure

```java
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    private static final String FILE_PATH = "data" + File.separator + "items.txt";

    public List<Item> loadItems() { ... }
    public void saveItem(Item item) { ... }
    public void rewriteAll(List<Item> list) { ... }
}
```

---

## Constants

| Constant | Value | Purpose |
|----------|-------|---------|
| `FILE_PATH` | `"data" + File.separator + "items.txt"` | Path to the data file (OS-independent) |

---

## Dependencies

| Dependency | Purpose |
|------------|---------|
| `java.io.File` | File path handling and existence checks |
| `java.io.BufferedReader` | Efficient line-by-line reading |
| `java.io.BufferedWriter` | Efficient line-by-line writing |
| `java.io.FileReader` | Character stream input |
| `java.io.FileWriter` | Character stream output |
| `Item` | Data model for deserialization |

---

## Methods Breakdown

### `loadItems()`
**Line:** 8 | **Purpose:** Read all items from disk

```java
public List<Item> loadItems()
```

**Workflow:**
1. Creates empty `ArrayList<Item>`
2. Checks if `data/items.txt` exists; returns empty list if not
3. Opens `BufferedReader` with try-with-resources
4. For each line:
   - Trims whitespace, skips empty lines
   - Splits by `|` delimiter
   - Skips lines that don't have exactly 8 parts
   - Parses `id` as integer (skips on `NumberFormatException`)
   - Creates `Item` object and adds to list
5. Returns list (empty if file missing or any IO error)

**Error Handling:**
- Missing file → returns empty list (no exception)
- Malformed line → silently skipped
- IO error → returns whatever was loaded so far

**Parsing Example:**
```
Input:  "1 | LOST | pl | book | 20-12-2006 | tinsukia | 210016664 | OPEN"
Split:  ["1 ", " LOST ", " pl ", " book ", " 20-12-2006 ", " tinsukia ", " 210016664 ", " OPEN"]
Trim:   ["1", "LOST", "pl", "book", "20-12-2006", "tinsukia", "210016664", "OPEN"]
Item:   Item(1, "LOST", "pl", "book", "20-12-2006", "tinsukia", "210016664", "OPEN")
```

---

### `saveItem(Item item)`
**Line:** 41 | **Purpose:** Append a single item to the file

```java
public void saveItem(Item item)
```

**Workflow:**
1. Creates `data/` directory if it doesn't exist (`mkdirs()`)
2. Opens `BufferedWriter` in append mode (`FileWriter(path, true)`)
3. Writes item's `toFileLine()` output
4. Writes a newline character

**File append mode:** `FileWriter(FILE_PATH, true)` - adds to end of file without overwriting.

---

### `rewriteAll(List<Item> list)`
**Line:** 54 | **Purpose:** Overwrite entire file with given items

```java
public void rewriteAll(List<Item> list)
```

**Workflow:**
1. Creates `data/` directory if it doesn't exist
2. Opens `BufferedWriter` in overwrite mode (`FileWriter(path, false)`)
3. Iterates through all items, writing each with `toFileLine()` + newline

**Use case:** Called when claiming an item (status change requires rewriting all records).

---

## File Operations Summary

| Method | File Mode | Purpose |
|--------|-----------|---------|
| `loadItems()` | Read | Read all items |
| `saveItem()` | Append | Add new item to end |
| `rewriteAll()` | Overwrite | Replace entire file |

---

## Data File Format

**File:** `data/items.txt`
**Delimiter:** ` | ` (pipe with spaces)
**Encoding:** UTF-8 (default)

```
id | type | name | category | date | location | contact | status
```

**Example lines:**
```
1 | LOST | pl | book | 20-12-2006 | tinsukia | 210016664 | OPEN
2 | FOUND | Keys | Keys | 01-09-2026 | Library | 9876543210 | OPEN
6 | LOST | Wallet | Bags | 15-08-2026 | Cafeteria | 1234567890 | RESOLVED
```

---

## Directory Creation

Both `saveItem()` and `rewriteAll()` check for the `data/` directory:

```java
File dir = new File("data");
if (!dir.exists()) {
    dir.mkdirs();
}
```

This ensures the application works even if the `data/` folder is deleted.

---

## Error Handling

| Scenario | Behavior |
|----------|----------|
| File doesn't exist | Returns empty list (load) or creates file (save/rewrite) |
| Malformed line | Silently skipped during load |
| IO error during read | Returns partial results |
| IO error during write | Prints stack trace (`e.printStackTrace()`) |
