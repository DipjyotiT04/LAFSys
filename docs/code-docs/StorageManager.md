# StorageManager.java - File I/O

**Path:** `src/StorageManager.java`

Reads/writes `data/items.txt` (pipe-delimited).

## Methods

- `load()` - Read all items from file
- `append(item)` - Add one item to end
- `rewrite(list)` - Overwrite entire file
- `ensureDir()` - Create data/ if missing

## Error Handling

- Missing file → empty list
- Malformed line → skip
- IO error → stack trace
