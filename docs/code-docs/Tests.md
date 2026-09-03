# Tests.java - Manual Test Harness

**Path:** `src/Tests.java`
**Lines:** 104
**Purpose:** Manual acceptance/integration tests for the application

---

## Overview

`Tests.java` is a hand-rolled test harness that validates the application's core functionality. It contains a series of acceptance tests (Tests 2-9) that verify record counts, posting, searching, claiming, date validation, error handling, and data integrity. The tests use a simple pass/fail counter and restore the data file to its original state after completion.

---

## Class Structure

```java
import java.io.*;
import java.util.List;

public class Tests {
    static int passed = 0;
    static int failed = 0;

    static void check(String name, boolean condition) { ... }
    public static void main(String[] args) throws Exception { ... }
}
```

---

## Dependencies

| Dependency | Purpose |
|------------|---------|
| `ItemService` | Business logic under test |
| `Item` | Data model |
| `Validator` | Date validation under test |
| `StorageManager` | File I/O under test |
| `java.io.File` | File manipulation for Test 8 |
| `java.util.List` | Collection handling |
| `java.util.ArrayList` | List restoration in Test 9 |

---

## Test Infrastructure

### `check(String name, boolean condition)`
**Line:** 8 | **Purpose:** Assert a condition and track results

```java
static void check(String name, boolean condition) {
    if (condition) {
        System.out.println("PASS: " + name);
        passed++;
    } else {
        System.out.println("FAIL: " + name);
        failed++;
    }
}
```

Simple assertion that prints PASS/FAIL and increments counters.

---

## Test Cases

### Test 2: Record Counts
**Lines:** 19-29 | **Purpose:** Verify initial data state

| Sub-test | Condition | Expected |
|----------|-----------|----------|
| 2a | `all.size() == 15` | 15 total records |
| 2b | `lost.size() == 8` | 8 LOST items |
| 2c | `found.size() == 7` | 7 FOUND items |
| 2d | `resolved.size() == 2` | 2 RESOLVED items |

**Prerequisite:** `data/items.txt` must contain exactly 15 records.

---

### Test 3: Filter Counts
**Lines:** 32-34 | **Purpose:** Verify filtering methods return correct counts

| Sub-test | Condition | Expected |
|----------|-----------|----------|
| 3a | `getLostItems().size() == 8` | 8 LOST |
| 3b | `getFoundItems().size() == 7` | 7 FOUND |
| 3c | `getResolvedItems().size() == 2` | 2 RESOLVED |

---

### Test 4: Post Item
**Lines:** 37-40 | **Purpose:** Verify new item creation and ID generation

| Sub-test | Condition | Expected |
|----------|-----------|----------|
| 4a | `newId == 16` | Next ID after 15 existing |
| 4b | `afterPost.size() == 16` | File has 16 lines |

**Action:** Posts a new "Test Pen" item.

---

### Test 5: Search
**Lines:** 43-47 | **Purpose:** Verify case-insensitive search

| Sub-test | Condition | Expected |
|----------|-----------|----------|
| 5a | `ringLower.size() == ringUpper.size()` | Case-insensitive match |
| 5b | `zzz.size() == 0` | No results for invalid keyword |

---

### Test 6: Claim Item
**Lines:** 50-57 | **Purpose:** Verify item claiming workflow

| Sub-test | Condition | Expected |
|----------|-----------|----------|
| 6a | `claimItem(16) == true` | First claim succeeds |
| 6b | `claimItem(16) == false` | Second claim fails (already resolved) |
| 6c | `item.getStatus() == "RESOLVED"` | Status updated |
| 6d | `resolvedCount == 3` | Resolved count increased |

---

### Test 7: Date Validation
**Lines:** 60-62 | **Purpose:** Verify Validator.isValidDate()

| Sub-test | Input | Expected |
|----------|-------|----------|
| 7a | `"01-08-2026"` | `true` |
| 7b | `"1/8/26"` | `false` |
| 7c | `"abc"` | `false` |

---

### Test 8: Missing File Handling
**Lines:** 65-71 | **Purpose:** Verify graceful handling of missing data file

**Workflow:**
1. Renames `data/items.txt` to `data/items_backup.txt`
2. Calls `sm.loadItems()` - should return empty list without crashing
3. Renames backup back to original

---

### Test 9: Data Restoration
**Lines:** 74-98 | **Purpose:** Restore data file to original state

**Workflow:**
1. Removes the test item (ID 16)
2. Restores original statuses for items 6 and 10 (RESOLVED)
3. Rewrites the file
4. Verifies restored state: 15 records, 8 LOST, 7 FOUND, 2 RESOLVED

---

## Test Results Output

```
PASS: Test 2a: 15 records in items.txt
PASS: Test 2b: 8 LOST items
PASS: Test 2c: 7 FOUND items
PASS: Test 2d: 2 RESOLVED items
PASS: Test 3: getLostItems()=8
PASS: Test 3: getFoundItems()=7
PASS: Test 3: getResolvedItems()=2
PASS: Test 4a: postItem returns id 16
PASS: Test 4b: file has 16 lines after post
PASS: Test 5a: searchByKeyword case-insensitive
PASS: Test 5b: searchByKeyword('zzz') finds none
PASS: Test 6a: claimItem(16) returns true
PASS: Test 6b: second claimItem(16) returns false
PASS: Test 6c: item 16 status is RESOLVED
PASS: Test 6d: resolved count becomes 3
PASS: Test 7a: isValidDate('01-08-2026') true
PASS: Test 7b: isValidDate('1/8/26') false
PASS: Test 7c: isValidDate('abc') false
PASS: Test 8: loading missing file returns empty list
PASS: Test 9: restored to 15 records
PASS: Test 9: restored 8 LOST
PASS: Test 9: restored 7 FOUND
PASS: Test 9: restored 2 RESOLVED

========================================
Results: 23 passed, 0 failed
========================================
```

---

## Running Tests

```bash
# Compile
javac -d bin src/*.java

# Run tests
java -cp bin Tests
```

**Important:** Tests modify `data/items.txt` but restore it to the original state on completion.

---

## Limitations

1. **No test framework** - Uses manual pass/fail counting
2. **Sequential tests** - Tests depend on execution order
3. **File system dependency** - Requires specific initial data state
4. **No isolation** - Tests share state via the data file
5. **Silent failures** - No detailed error messages on failure
