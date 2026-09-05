# LAFSys - Code Documentation

**Project:** Campus Lost and Found Board
**Language:** Java 8+

---

## Project Structure

```
LAFSys/
├── src/                  # Java source code
│   ├── Main.java         # Entry point / Console UI
│   ├── Item.java         # Data model
│   ├── ItemService.java  # Business logic
│   ├── StorageManager.java # File I/O
│   ├── Validator.java    # Input validation
│   └── Tests.java        # Test harness
├── data/
│   └── items.txt         # Flat-file database
├── docs/
│   └── code-docs/        # This documentation
└── README.md
```

---

## Architecture

```
Main.java  →  ItemService.java  →  StorageManager.java  →  data/items.txt
    │              │
    ├── Validator   └── Item.java
    └── Item.java
```

---

## File Documentation

| File | Description | Lines |
|------|-------------|-------|
| [Main.java](./Main.md) | Console menu UI | 115 |
| [Item.java](./Item.md) | Data model | 38 |
| [ItemService.java](./ItemService.md) | Business logic | 68 |
| [StorageManager.java](./StorageManager.md) | File I/O | 55 |
| [Validator.java](./Validator.md) | Date validation | 7 |
| [Tests.java](./Tests.md) | Test harness | 58 |

---

## Build & Run

```bash
javac -d bin src/*.java
java -cp bin Main
```

---

## Data Format

```
id|type|name|category|date|location|contact|status
```

Example: `1|LOST|pl|book|20-12-2006|tinsukia|210016664|OPEN`
