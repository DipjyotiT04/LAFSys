# LAFSys - Code Documentation Index

**Project:** Campus Lost and Found Board (LAFSys)
**Language:** Java 8+
**Architecture:** Console-based MVC (Model-View-Controller)

---

## Project Structure

```
LAFSys/
├── src/                  # Java source code
│   ├── Main.java         # Entry point / Console UI
│   ├── Item.java         # Data model (POJO)
│   ├── ItemService.java  # Business logic layer
│   ├── StorageManager.java # File I/O persistence layer
│   ├── Validator.java    # Input validation utility
│   └── Tests.java        # Manual test harness
├── data/
│   └── items.txt         # Flat-file database (pipe-delimited)
├── docs/
│   └── code-docs/        # This documentation
├── bin/                  # Compiled .class files
└── README.md
```

---

## Architecture Overview

```
Main.java  (Console UI / Entry Point)
   │
   ├──► Validator.java    (Input Validation)
   │
   └──► ItemService.java  (Business Logic)
            │
            ├──► StorageManager.java  (File I/O)
            │        │
            │        └──► data/items.txt  (Flat-file DB)
            │
            └──► Item.java  (Data Model)
```

---

## File Documentation

| # | File | Description | Lines |
|---|------|-------------|-------|
| 1 | [Main.java](./Main.md) | Application entry point with console menu UI | 201 |
| 2 | [Item.java](./Item.md) | Data model / POJO for lost-and-found items | 40 |
| 3 | [ItemService.java](./ItemService.md) | Business logic and service layer | 94 |
| 4 | [StorageManager.java](./StorageManager.md) | File I/O persistence layer | 68 |
| 5 | [Validator.java](./Validator.md) | Input validation utility | 10 |
| 6 | [Tests.java](./Tests.md) | Manual test harness | 104 |

---

## Class Dependency Graph

```
Tests ──────────► ItemService ──► StorageManager ──► Item
   │                  │
   ├──► Item ◄────────┘
   ├──► Validator
   └──► StorageManager

Main ──► ItemService ──► StorageManager ──► Item
  │         │
  ├──► Item ◄┘
  └──► Validator
```

---

## Key Constants (ItemService)

| Constant | Value | Purpose |
|----------|-------|---------|
| `TYPE_LOST` | `"LOST"` | Item type identifier |
| `TYPE_FOUND` | `"FOUND"` | Item type identifier |
| `STATUS_OPEN` | `"OPEN"` | Default item status |
| `STATUS_RESOLVED` | `"RESOLVED"` | Claimed/resolved status |
| `CATEGORIES` | `{"Electronics", "Books", "ID Card", "Bags", "Keys", "Clothes", "Other"}` | Valid item categories |

---

## Data Format (items.txt)

Each line represents one item in pipe-delimited format:

```
id | type | name | category | date | location | contact | status
```

**Example:**
```
1 | LOST | pl | book | 20-12-2006 | tinsukia | 210016664 | OPEN
```

---

## Build & Run

```bash
# Compile all source files
javac -d bin src/*.java

# Run the application
java -cp bin Main

# Run tests
java -cp bin Tests
```
