# Part 4: Live Demonstration, Testing and Conclusion

**Student 4**

---

## Table of Contents

1. [Overview](#1-overview)
2. [Prerequisites](#2-prerequisites)
3. [How to Compile](#3-how-to-compile)
4. [Live Demonstration: Feature by Feature](#4-live-demonstration)
5. [Testing: Test Suite Walkthrough](#5-testing)
6. [Test Results](#6-test-results)
7. [Error Handling Demo](#7-error-handling)
8. [Limitations](#8-limitations)
9. [Possible Future Improvements](#9-future-improvements)
10. [Conclusion](#10-conclusion)
11. [Lessons Learned](#11-lessons-learned)
12. [References](#12-references)

---

## 1. Overview

This document demonstrates the LAFSys application in action, explains the testing process, and provides a conclusion for the project.

---

## 2. Prerequisites

Before running LAFSys, you need:

| Requirement | How to Check |
|-------------|-------------|
| Java JDK 8+ installed | Type `java -version` in terminal |
| Terminal / Command prompt | Any terminal works |
| Source files in `src/` folder | Check the project directory |

### Check Java Installation

```bash
java -version
```

Expected output:
```
openjdk version "11.0.x" 2026-xx-xx
```

If Java is not installed:
- **Windows:** Download from oracle.com
- **Linux:** `sudo apt install default-jdk`
- **Mac:** `brew install openjdk`

---

## 3. How to Compile

### Step 1: Open Terminal

Navigate to the project folder:
```bash
cd path/to/LAFSys
```

### Step 2: Compile All Java Files

```bash
javac -d bin src/*.java
```

| Part | Meaning |
|------|---------|
| `javac` | Java compiler |
| `-d bin` | Put compiled files in `bin/` folder |
| `src/*.java` | Compile all `.java` files in `src/` |

If compilation succeeds, you see no output (just a new prompt).

If there are errors, you see messages like:
```
src/Main.java:38: error: cannot find symbol
```

### Step 3: Verify Compilation

Check that `bin/` folder has `.class` files:
```bash
ls bin/
```

Expected output:
```
Item.class  ItemService.class  Main.class  StorageManager.class  Tests.class  Validator.class
```

---

## 4. Live Demonstration: Feature by Feature

### Running the Application

```bash
java -cp bin Main
```

Expected output:
```
===== LOST & FOUND BOARD =====

1. Post Lost Item
2. Post Found Item
3. View Lost Items
4. View Found Items
5. Search Items
6. Claim Item
7. View Resolved
8. Exit

Lost: 1 | Found: 0 | Resolved: 0

>
```

---

### Demo 1: Post a Lost Item

**User types:** `1`

```
> 1

--- Post LOST Item ---
Name: Wallet
Category (Electronics/Books/ID Card/Bags/Keys/Clothes/Other): Bags
Date (dd-mm-yyyy): 01-09-2026
Location: Library
Contact: 9876543210
Saved. ID: 2
```

**What happened:**
1. User chose option 1 (Post Lost Item)
2. Entered item details: Wallet, Bags, 01-09-2026, Library, 9876543210
3. System validated all fields (non-empty, valid date format)
4. System generated ID 2 (next after existing items)
5. System saved to `data/items.txt`
6. System showed confirmation

---

### Demo 2: Post a Found Item

**User types:** `2`

```
> 2

--- Post FOUND Item ---
Name: Keys
Category (Electronics/Books/ID Card/Bags/Keys/Clothes/Other): Keys
Date (dd-mm-yyyy): 01-09-2026
Location: Cafeteria
Contact: 8765432109
Saved. ID: 3
```

**What happened:**
1. User chose option 2 (Post Found Item)
2. Entered details for found keys
3. System saved with type "FOUND"

---

### Demo 3: View Lost Items

**User types:** `3`

```
> 3

--- LOST ITEMS ---
ID   NAME                 CATEGORY     LOCATION        DATE         STATUS
--------------------------------------------------------------------
1    pl                   book         tinsukia        20-12-2006   OPEN
2    Wallet               Bags         Library         01-09-2026   OPEN
Total: 2
```

**What happened:**
1. System loaded all items from file
2. Filtered items where type = "LOST"
3. Displayed in formatted table
4. Showed total count

---

### Demo 4: View Found Items

**User types:** `4`

```
> 4

--- FOUND ITEMS ---
ID   NAME                 CATEGORY     LOCATION        DATE         STATUS
--------------------------------------------------------------------
3    Keys                 Keys         Cafeteria       01-09-2026   OPEN
Total: 1
```

---

### Demo 5: Search by Keyword

**User types:** `5`

```
> 5

1. Keyword
2. Category
> 1
Keyword: wallet

Results for "wallet" (1)
2    Wallet               Bags         Library         01-09-2026   OPEN
```

**What happened:**
1. User chose keyword search
2. Entered "wallet"
3. System searched name, location, and category for "wallet"
4. Found 1 matching item

**Search is case-insensitive:**
```
Keyword: WALLET
Results for "WALLET" (1)
2    Wallet               Bags         Library         01-09-2026   OPEN

Keyword: waLlEt
Results for "waLlEt" (1)
2    Wallet               Bags         Library         01-09-2026   OPEN
```

All three produce the same result.

---

### Demo 6: Search by Category

**User types:** `5`

```
> 5

1. Keyword
2. Category
> 2
Category: Keys

Category: Keys (1)
3    Keys                 Keys         Cafeteria       01-09-2026   OPEN
```

**What happened:**
1. User chose category search
2. Entered "Keys"
3. System found all items with category = "Keys"

---

### Demo 7: Claim an Item

**User types:** `6`

```
> 6

Item ID to claim: 2
2    Wallet               Bags         Library         01-09-2026   OPEN
Mark resolved? (y/n): y
Done.
```

**What happened:**
1. User entered item ID 2
2. System found and displayed the item
3. Asked for confirmation
4. User typed "y"
5. System changed status from "OPEN" to "RESOLVED"
6. System rewrote the entire file

**Verify the change:**

```
> 3

--- LOST ITEMS ---
ID   NAME                 CATEGORY     LOCATION        DATE         STATUS
--------------------------------------------------------------------
1    pl                   book         tinsukia        20-12-2006   OPEN
Total: 1

> 7

--- RESOLVED ---
ID   NAME                 CATEGORY     LOCATION        DATE         STATUS
--------------------------------------------------------------------
2    Wallet               Bags         Library         01-09-2026   RESOLVED
Total: 1
```

Wallet moved from LOST to RESOLVED.

---

### Demo 8: View Resolved Items

**User types:** `7`

```
> 7

--- RESOLVED ---
ID   NAME                 CATEGORY     LOCATION        DATE         STATUS
--------------------------------------------------------------------
2    Wallet               Bags         Library         01-09-2026   RESOLVED
Total: 1
```

---

### Demo 9: Exit

**User types:** `8`

```
> 8
Exit? (y/n): y
Bye!
```

**If user types "n":**
```
> 8
Exit? (y/n): n
```
Program continues running.

---

### Demo 10: Status Summary on Menu

Every time the menu shows, it displays counts:

```
Lost: 1 | Found: 1 | Resolved: 1
```

This updates in real-time as items are posted and claimed.

---

## 5. Testing: Test Suite Walkthrough

### Running the Tests

```bash
java -cp bin Tests
```

### Test 2: Initial State

```
PASS: 2a: 15 records
PASS: 2b: 8 LOST
PASS: 2c: 7 FOUND
PASS: 2d: 2 RESOLVED
```

**What it checks:**
- File `data/items.txt` has exactly 15 records
- 8 are LOST, 7 are FOUND, 2 are RESOLVED

**How it works:**
```java
List<Item> all = svc.getAllItems();
check("2a: 15 records", all.size() == 15);
```
Loads all items and checks if count is 15.

---

### Test 3: Filter Methods

```
PASS: 3: getLostItems=8
PASS: 3: getFoundItems=7
PASS: 3: getResolvedItems=2
```

**What it checks:**
- `getLostItems()` returns exactly 8 items
- `getFoundItems()` returns exactly 7 items
- `getResolvedItems()` returns exactly 2 items

---

### Test 4: Post Item

```
PASS: 4a: id=16
PASS: 4b: 16 lines
```

**What it checks:**
- New item gets ID 16 (next after 15)
- File now has 16 lines

**How it works:**
```java
int newId = svc.postItem("LOST", "Test Pen", "Other", "10-08-2026", "Office", "1234567890");
check("4a: id=16", newId == 16);
```
Posts a test item and verifies the returned ID.

---

### Test 5: Search

```
PASS: 5a: case-insensitive
PASS: 5b: no match
```

**What it checks:**
- `searchByKeyword("ring")` and `searchByKeyword("RING")` return same count
- `searchByKeyword("zzz")` returns 0 results

**How it works:**
```java
check("5a: case-insensitive",
      svc.searchByKeyword("ring").size() == svc.searchByKeyword("RING").size());
check("5b: no match", svc.searchByKeyword("zzz").size() == 0);
```

---

### Test 6: Claim Item

```
PASS: 6a: claim OK
PASS: 6b: already resolved
PASS: 6c: status RESOLVED
PASS: 6d: resolved=3
```

**What it checks:**
- First claim succeeds (returns true)
- Second claim fails (returns false)
- Status changed to RESOLVED
- Resolved count increased to 3

**How it works:**
```java
check("6a: claim OK", svc.claimItem(16));        // First claim
check("6b: already resolved", !svc.claimItem(16)); // Second claim
Item claimed = svc.getItemById(16);
check("6c: status RESOLVED",
      claimed != null && claimed.getStatus().equals("RESOLVED"));
```

---

### Test 7: Date Validation

```
PASS: 7a: valid date
PASS: 7b: bad format
PASS: 7c: not a date
```

**What it checks:**
- `"01-08-2026"` is valid
- `"1/8/26"` is invalid (wrong separators and lengths)
- `"abc"` is invalid (not a date)

---

### Test 8: Missing File

```
PASS: 8: empty list
```

**What it checks:**
- If `data/items.txt` does not exist, `load()` returns empty list
- No crash occurs

**How it works:**
```java
File dataFile = new File("data/items.txt");
File backup = new File("data/items_backup.txt");
dataFile.renameTo(backup);                    // Hide the file
check("8: empty list", sm.load().size() == 0); // Should return empty
backup.renameTo(dataFile);                     // Restore the file
```

---

### Test 9: Data Restore

```
PASS: 9: 15 records
PASS: 9: 8 LOST
PASS: 9: 7 FOUND
PASS: 9: 2 RESOLVED
```

**What it checks:**
- After all tests, data is restored to original state
- 15 records, 8 LOST, 7 FOUND, 2 RESOLVED

**Why this matters:**
Tests modify the data file. This test ensures the file is restored so the next test run starts fresh.

---

## 6. Test Results

### Complete Output

```
PASS: 2a: 15 records
PASS: 2b: 8 LOST
PASS: 2c: 7 FOUND
PASS: 2d: 2 RESOLVED
PASS: 3: getLostItems=8
PASS: 3: getFoundItems=7
PASS: 3: getResolvedItems=2
PASS: 4a: id=16
PASS: 4b: 16 lines
PASS: 5a: case-insensitive
PASS: 5b: no match
PASS: 6a: claim OK
PASS: 6b: already resolved
PASS: 6c: status RESOLVED
PASS: 6d: resolved=3
PASS: 7a: valid date
PASS: 7b: bad format
PASS: 7c: not a date
PASS: 8: empty list
PASS: 9: 15 records
PASS: 9: 8 LOST
PASS: 9: 7 FOUND
PASS: 9: 2 RESOLVED

23 passed, 0 failed
```

### Test Coverage Summary

| Area | Tests | Status |
|------|-------|--------|
| Record counts | 2a-d | PASS |
| Filter methods | 3 | PASS |
| Post item | 4a-b | PASS |
| Search | 5a-b | PASS |
| Claim item | 6a-d | PASS |
| Date validation | 7a-c | PASS |
| Error handling | 8 | PASS |
| Data integrity | 9 | PASS |

**Total: 23/23 tests passed**

---

## 7. Error Handling Demo

### Demo: Empty Required Fields

```
> 1

--- Post LOST Item ---
Name: 
Category (Electronics/Books/ID Card/Bags/Keys/Clothes/Other): Bags
Date (dd-mm-yyyy): 01-09-2026
Location: Library
Contact: 9876543210
Name, location and contact are required.
```

**What happened:** Name was empty, system rejected the input.

---

### Demo: Invalid Date Format

```
> 1

--- Post LOST Item ---
Name: Phone
Category (Electronics/Books/ID Card/Bags/Keys/Clothes/Other): Electronics
Date (dd-mm-yyyy): 01/09/2026
Location: Library
Contact: 9876543210
Bad date format. Use dd-mm-yyyy.
```

**What happened:** Date used `/` instead of `-`, system rejected it.

---

### Demo: Invalid Claim ID (Not a Number)

```
> 6

Item ID to claim: abc
Enter a valid number.
```

**What happened:** `Integer.parseInt("abc")` threw `NumberFormatException`, system caught it and showed error.

---

### Demo: Claim Non-Existent Item

```
> 6

Item ID to claim: 999
No item with ID 999
```

**What happened:** `getItemById(999)` returned null, system showed error.

---

### Demo: Claim Already Resolved Item

```
> 6

Item ID to claim: 2
Already resolved.
```

**What happened:** Item 2 was already marked as RESOLVED, system rejected the claim.

---

### Demo: Empty Search

```
> 5

1. Keyword
2. Category
> 1
Keyword: 
Type something first.
```

**What happened:** Keyword was empty, system rejected it.

---

### Demo: Invalid Menu Option

```
> 9
Invalid option.
```

**What happened:** Option 9 does not exist, system showed error.

---

### Demo: Missing Data File

If `data/items.txt` is deleted:
```
> 3

--- LOST ITEMS ---
Nothing here.
```

**What happened:** `load()` returned empty list, `showItems()` displayed "Nothing here." No crash.

---

## 8. Limitations

### Current Limitations

| Limitation | Impact | Why It Exists |
|-----------|--------|---------------|
| No user accounts | Anyone can post/claim | Simple single-user design |
| No image upload | Cannot show item photos | Console application |
| No database | Limited to text file storage | Project scope |
| No network | Cannot share across computers | Single-machine app |
| No date validation logic | 99-99-9999 passes validation | Format-only check |
| Single-file storage | No concurrent access | Simplicity |
| No undo | Cannot undo a claim | No requirement |
| No pagination | Large lists scroll off screen | Console limitation |

### Why These Limitations Exist

1. **Project scope:** This is a campus project, not a production system
2. **Learning objectives:** Focus on Java fundamentals, not advanced features
3. **Simplicity:** Text files are easier to understand than databases
4. **No external dependencies:** Only Java standard library used

---

## 9. Possible Future Improvements

### Short-term (Easy to Add)

| Improvement | How to Implement |
|------------|-----------------|
| Add timestamps | Use `java.time.LocalDateTime` when posting |
| Sort by date | Use `Comparator` with streams |
| Edit items | Add edit method in ItemService |
| Better date validation | Check day/month ranges |
| Pagination | Show 10 items at a time |

### Medium-term (Moderate Effort)

| Improvement | How to Implement |
|------------|-----------------|
| CSV export | Write items to CSV format |
| GUI interface | Use Java Swing or JavaFX |
| Photo support | Save image paths, display in GUI |
| Multi-user | Add user accounts and login |

### Long-term (Major Effort)

| Improvement | How to Implement |
|------------|-----------------|
| Database backend | Replace text file with SQLite |
| Web application | Build REST API + HTML frontend |
| Mobile app | Use React Native or Flutter |
| Email notifications | Send email when item is claimed |
| Campus-wide system | Add network support, multi-campus |

---

## 10. Conclusion

### What We Built

LAFSys is a **console-based lost and found management system** for college campuses. It allows users to:

1. **Post lost items** with details (name, category, date, location, contact)
2. **Post found items** with the same details
3. **View** all lost, found, or resolved items in formatted tables
4. **Search** items by keyword (across name, location, category) or by category
5. **Claim** items by marking them as resolved
6. **Persist** all data in a text file (`data/items.txt`)

### Technical Achievements

| Achievement | Evidence |
|------------|----------|
| Clean class design | 6 classes, each with single responsibility |
| File I/O | Read/write/append to text file |
| Input validation | Required fields, date format checking |
| Java streams | Used for filtering and searching |
| Error handling | Graceful handling of missing files, bad input |
| Test suite | 23 tests, all passing |

### Project Impact

- **Solves a real problem** - Lost items on campus
- **Easy to use** - Simple console interface
- **Portable** - Works on any system with Java
- **Maintainable** - Clean, well-organized code
- **Extensible** - Easy to add new features

### Key Takeaways

1. **Good design matters** - Separating concerns made the code easy to write and maintain
2. **Simple is better** - Text files work well for this scale
3. **Testing is essential** - The test suite caught issues and verified correctness
4. **Error handling prevents crashes** - Missing files and bad input are handled gracefully

---

## 11. Lessons Learned

### What Went Well

- **Class separation** - Each class had a clear, single job
- **Incremental development** - Built one file at a time, tested as we went
- **Simple data format** - Pipe-delimited text was easy to parse
- **Stream API** - Made filtering code clean and readable

### Challenges Faced

| Challenge | How We Solved It |
|-----------|-----------------|
| File not found errors | Added `ensureDir()` and null checks |
| Date parsing complexity | Used regex for format validation |
| Claiming requires full rewrite | Used `rewrite()` method |
| Case-insensitive search | Used `toLowerCase()` on both sides |

### Advice for Future Developers

1. **Start with the data model** - Define what your data looks like first
2. **Handle errors early** - Bad input and missing files will happen
3. **Test as you go** - Don't wait until the end to test
4. **Keep it simple** - Don't add features you don't need
5. **Document your code** - Future you will thank present you

---

## 12. References

### Java Documentation

- [Java Scanner Class](https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html)
- [Java BufferedReader](https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html)
- [Java Stream API](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
- [Java String.format](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#format-java.lang.String-java.lang.Object...-)

### Project Files

| File | Purpose |
|------|---------|
| `src/Main.java` | User interface (152 lines) |
| `src/Item.java` | Data model (33 lines) |
| `src/ItemService.java` | Business logic (75 lines) |
| `src/StorageManager.java` | File I/O (57 lines) |
| `src/Validator.java` | Input validation (10 lines) |
| `src/Tests.java` | Test suite (76 lines) |
| `data/items.txt` | Data storage |

### Total Lines of Code

```
Main.java:           152 lines
Item.java:            33 lines
ItemService.java:     75 lines
StorageManager.java:  57 lines
Validator.java:       10 lines
Tests.java:           76 lines
---------------------------
Total:              403 lines
```

---

## Summary

LAFSys demonstrates that a practical, useful application can be built with just 403 lines of Java code. The project successfully addresses the real-world problem of lost and found items on college campuses while showcasing fundamental programming concepts including object-oriented design, file I/O, input validation, stream processing, and testing.
