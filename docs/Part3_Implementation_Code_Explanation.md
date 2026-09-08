# Part 3: Implementation and Code Explanation

**Student 3**

---

## Table of Contents

1. [Overview](#1-overview)
2. [Implementation Order](#2-implementation-order)
3. [Item.java Walkthrough](#3-itemjava)
4. [Validator.java Walkthrough](#4-validatorjava)
5. [StorageManager.java Walkthrough](#5-storagemanagerjava)
6. [ItemService.java Walkthrough](#6-itemservicejava)
7. [Main.java Walkthrough](#7-mainjava)
8. [Tests.java Walkthrough](#8-testsjava)
9. [Data File Format](#9-data-file-format)
10. [How to Compile and Run](#10-compile-and-run)

---

## 1. Overview

This document explains every line of code in the LAFSys project, file by file, line by line.

### Implementation Order

Files were written in dependency order:

```
1. Item.java           (Data model - no dependencies)
2. Validator.java      (Utility - no dependencies)
3. StorageManager.java (File I/O - depends on Item)
4. ItemService.java    (Logic - depends on Item, StorageManager)
5. Main.java           (UI - depends on Item, ItemService, Validator)
6. Tests.java          (Tests - depends on everything)
```

---

## 2. Implementation Approach

### Code Style Choices

| Choice | Why |
|--------|-----|
| Short variable names (`sc`, `cat`, `kw`) | Keeps code concise |
| Package-private fields | Simpler than private + getters |
| Static methods in Validator | No need to create objects |
| Streams for filtering | Cleaner than traditional loops |
| y/n for confirmations | Faster user experience |

---

## 3. Item.java Walkthrough

### Full Source Code

```java
public class Item {
    int id;
    String type, name, category, date, location, contact, status;

    public Item(int id, String type, String name, String category,
                String date, String location, String contact, String status) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.category = category;
        this.date = date;
        this.location = location;
        this.contact = contact;
        this.status = status;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
    public String getContact() { return contact; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }

    public String toLine() {
        return id + "|" + type + "|" + name + "|" + category + "|"
               + date + "|" + location + "|" + contact + "|" + status;
    }

    public String display() {
        return String.format("%-4d %-20s %-12s %-15s %-12s %-9s",
                id, name, category, location, date, status);
    }
}
```

### Line-by-Line Explanation

**Line 1: Class Declaration**
```java
public class Item {
```
Defines a class called `Item`. A class is a blueprint for creating objects.

**Line 2-3: Field Declarations**
```java
int id;
String type, name, category, date, location, contact, status;
```

| Field | Type | Stores | Example |
|-------|------|--------|---------|
| `id` | `int` | Unique number | `1`, `15` |
| `type` | `String` | "LOST" or "FOUND" | `"LOST"` |
| `name` | `String` | Item description | `"Wallet"` |
| `category` | `String` | Item type | `"Bags"` |
| `date` | `String` | dd-mm-yyyy | `"01-09-2026"` |
| `location` | `String` | Where lost/found | `"Library"` |
| `contact` | `String` | Phone number | `"9876543210"` |
| `status` | `String` | "OPEN" or "RESOLVED" | `"OPEN"` |

**Why `String` for date?**
- Simpler than `Date` or `LocalDate`
- No date arithmetic needed
- Matches file format directly

**Lines 5-14: Constructor**
```java
public Item(int id, String type, String name, String category,
            String date, String location, String contact, String status) {
    this.id = id;
    this.type = type;
    // ... sets all fields
}
```

A constructor runs when you create a new object:
```java
Item item = new Item(1, "LOST", "Wallet", "Bags", "01-09-2026", "Library", "987", "OPEN");
```

The `this` keyword refers to "this object's field":
```
this.id = id;
  |       |
  v       v
field   parameter
```

**Lines 16-23: Getters**
```java
public int getId() { return id; }
public String getName() { return name; }
```

Getters let other classes READ the data. Only `setStatus()` is provided as a setter (line 24) because only `status` changes after creation.

**Lines 26-28: toLine()**
```java
public String toLine() {
    return id + "|" + type + "|" + name + "|" + category + "|"
           + date + "|" + location + "|" + contact + "|" + status;
}
```

Converts item to pipe-delimited text for file storage:
```
"1|LOST|Wallet|Bags|01-09-2026|Library|987|OPEN"
```

**Lines 30-32: display()**
```java
public String display() {
    return String.format("%-4d %-20s %-12s %-15s %-12s %-9s",
            id, name, category, location, date, status);
}
```

Formats item for console table. Format codes:
- `%-4d` = integer, 4 chars wide, left-aligned
- `%-20s` = string, 20 chars wide, left-aligned

---

## 4. Validator.java Walkthrough

### Full Source Code

```java
public class Validator {
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public static boolean isValidDate(String text) {
        if (text == null) return false;
        return text.matches("\\d{2}-\\d{2}-\\d{4}");
    }
}
```

### Line-by-Line Explanation

**Lines 2-4: isNotEmpty()**
```java
public static boolean isNotEmpty(String text) {
    return text != null && !text.trim().isEmpty();
}
```

| Part | Meaning |
|------|---------|
| `text != null` | Not null |
| `&&` | AND |
| `text.trim()` | Remove whitespace |
| `.isEmpty()` | Check if empty |
| `!` | NOT |

Truth table:

| Input | Result | Why |
|-------|--------|-----|
| `"Wallet"` | true | Has content |
| `""` | false | Empty |
| `"   "` | false | Only spaces |
| `null` | false | Null |

**Lines 6-9: isValidDate()**
```java
public static boolean isValidDate(String text) {
    if (text == null) return false;
    return text.matches("\\d{2}-\\d{2}-\\d{4}");
}
```

Regex pattern `\\d{2}-\\d{2}-\\d{4}`:
- `\\d{2}` = exactly 2 digits
- `-` = literal hyphen
- `\\d{4}` = exactly 4 digits

Examples:
- `"01-09-2026"` = true
- `"1/9/26"` = false
- `"abc"` = false

---

## 5. StorageManager.java Walkthrough

### Full Source Code

```java
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    static final String FILE = "data" + File.separator + "items.txt";

    public List<Item> load() {
        List<Item> items = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return items;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split("\\|");
                if (p.length != 8) continue;
                try {
                    items.add(new Item(
                        Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(),
                        p[3].trim(), p[4].trim(), p[5].trim(), p[6].trim(), p[7].trim()
                    ));
                } catch (NumberFormatException e) { }
            }
        } catch (IOException e) { }
        return items;
    }

    public void append(Item item) {
        ensureDir();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write(item.toLine());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rewrite(List<Item> list) {
        ensureDir();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, false))) {
            for (Item i : list) {
                bw.write(i.toLine());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureDir() {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
    }
}
```

### Line-by-Line Explanation

**Line 6: FILE Constant**
```java
static final String FILE = "data" + File.separator + "items.txt";
```
- `static final` = constant (cannot change)
- `File.separator` = `/` on Linux, `\` on Windows

**Lines 8-29: load() - Read All Items**

Step by step:
1. Create empty list (line 9)
2. Check if file exists (line 11) - return empty if not
3. Open file with BufferedReader (line 13)
4. Read each line (line 15)
5. Trim whitespace (line 16)
6. Skip empty lines (line 17)
7. Split by `|` (line 18)
8. Skip malformed lines (line 19)
9. Parse parts into Item object (lines 21-24)
10. Return list (line 29)

How splitting works:
```
"1|LOST|Wallet|Bags|01-09-2026|Library|987|OPEN"
   split by |
   v
["1", "LOST", "Wallet", "Bags", "01-09-2026", "Library", "987", "OPEN"]
 [0]   [1]     [2]      [3]       [4]           [5]       [6]    [7]
```

**Lines 31-39: append() - Add One Item**

| Part | What It Does |
|------|-------------|
| `ensureDir()` | Create data/ if needed |
| `FileWriter(FILE, true)` | Append mode (add to end) |
| `bw.write(item.toLine())` | Write item text |
| `bw.newLine()` | New line after |

**Lines 41-51: rewrite() - Overwrite All Items**

Same as append but with `FileWriter(FILE, false)` = overwrite mode. Used when claiming items (status changes).

**Lines 53-56: ensureDir()**

Creates `data/` folder if it does not exist.

---

## 6. ItemService.java Walkthrough

### Full Source Code

```java
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemService {
    static final String TYPE_LOST = "LOST";
    static final String TYPE_FOUND = "FOUND";
    static final String STATUS_RESOLVED = "RESOLVED";

    StorageManager storage = new StorageManager();

    public int postItem(String type, String name, String category,
                        String date, String location, String contact) {
        List<Item> items = storage.load();
        int maxId = items.stream().mapToInt(Item::getId).max().orElse(0);
        int newId = maxId + 1;
        Item item = new Item(newId, type, name, category, date, location, contact, "OPEN");
        storage.append(item);
        return newId;
    }

    public List<Item> searchByKeyword(String word) {
        String lower = word.toLowerCase();
        return storage.load().stream()
                .filter(i -> i.getName().toLowerCase().contains(lower)
                        || i.getLocation().toLowerCase().contains(lower)
                        || i.getCategory().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Item> searchByCategory(String cat) {
        return storage.load().stream()
                .filter(i -> i.getCategory().equalsIgnoreCase(cat))
                .collect(Collectors.toList());
    }

    public Item getItemById(int id) {
        return storage.load().stream()
                .filter(i -> i.getId() == id)
                .findFirst().orElse(null);
    }

    public boolean claimItem(int id) {
        List<Item> items = storage.load();
        for (Item i : items) {
            if (i.getId() == id && !i.getStatus().equals(STATUS_RESOLVED)) {
                i.setStatus(STATUS_RESOLVED);
                storage.rewrite(items);
                return true;
            }
        }
        return false;
    }

    public List<Item> getLostItems() {
        return storage.load().stream()
                .filter(i -> i.getType().equals(TYPE_LOST))
                .collect(Collectors.toList());
    }

    public List<Item> getFoundItems() {
        return storage.load().stream()
                .filter(i -> i.getType().equals(TYPE_FOUND))
                .collect(Collectors.toList());
    }

    public List<Item> getResolvedItems() {
        return storage.load().stream()
                .filter(i -> i.getStatus().equals(STATUS_RESOLVED))
                .collect(Collectors.toList());
    }

    public List<Item> getAllItems() {
        return storage.load();
    }
}
```

### Line-by-Line Explanation

**Lines 6-8: Constants**
```java
static final String TYPE_LOST = "LOST";
static final String TYPE_FOUND = "FOUND";
static final String STATUS_RESOLVED = "RESOLVED";
```
Constants prevent typos. If "LOST" is used in 5 places and needs to change, change it once here.

**Line 10: StorageManager Instance**
```java
StorageManager storage = new StorageManager();
```
ItemService creates its own StorageManager for file operations.

**Lines 12-19: postItem()**

| Step | Code | What Happens |
|------|------|-------------|
| 1 | `storage.load()` | Read all items |
| 2 | `items.stream().mapToInt(Item::getId).max().orElse(0)` | Find highest ID |
| 3 | `maxId + 1` | Next ID |
| 4 | `new Item(...)` | Create item with status "OPEN" |
| 5 | `storage.append(item)` | Save to file |
| 6 | `return newId` | Return ID to caller |

Stream chain explained:
```
items.stream()           -> Stream of Items
  .mapToInt(Item::getId) -> Extract IDs (int stream)
  .max()                 -> Find maximum
  .orElse(0)             -> Default to 0 if empty
```

**Lines 21-28: searchByKeyword()**

```java
String lower = word.toLowerCase();  // Make search case-insensitive
return storage.load().stream()
        .filter(i -> i.getName().toLowerCase().contains(lower)
                || i.getLocation().toLowerCase().contains(lower)
                || i.getCategory().toLowerCase().contains(lower))
        .collect(Collectors.toList());
```

Searches name, location, OR category. The `||` means OR.

Example:
```
Search: "ring"
"earring".contains("ring") -> true
"keyring".contains("ring") -> true
"notebook".contains("ring") -> false
```

**Lines 30-34: searchByCategory()**

Uses `equalsIgnoreCase()` for case-insensitive exact match.

**Lines 36-40: getItemById()**

Uses `findFirst().orElse(null)` - returns first match or null.

**Lines 42-52: claimItem()**

Why a for loop instead of streams? Because we need to MODIFY the item (setStatus) AND rewrite the file. Streams are for reading/filtering, not for modifying.

Logic:
1. Load all items
2. Find item with matching ID
3. Check it is not already resolved
4. Change status to RESOLVED
5. Rewrite entire file
6. Return true (success) or false (not found/already resolved)

**Lines 54-70: Filter Methods**

All three work the same way:
```java
return storage.load().stream()
        .filter(i -> i.getType().equals(TYPE_LOST))  // or TYPE_FOUND or STATUS_RESOLVED
        .collect(Collectors.toList());
```

---

## 7. Main.java Walkthrough

### Full Source Code

```java
import java.util.List;
import java.util.Scanner;

public class Main {
    static ItemService service = new ItemService();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n===== LOST & FOUND BOARD =====\n");

        while (true) {
            System.out.println("1. Post Lost Item");
            System.out.println("2. Post Found Item");
            System.out.println("3. View Lost Items");
            System.out.println("4. View Found Items");
            System.out.println("5. Search Items");
            System.out.println("6. Claim Item");
            System.out.println("7. View Resolved");
            System.out.println("8. Exit");

            List<Item> lost = service.getLostItems();
            List<Item> found = service.getFoundItems();
            List<Item> resolved = service.getResolvedItems();
            System.out.println("\nLost: " + lost.size() + " | Found: " + found.size()
                             + " | Resolved: " + resolved.size());
            System.out.print("\n> ");

            String ch = sc.nextLine().trim();

            if (ch.equals("1")) postItem("LOST");
            else if (ch.equals("2")) postItem("FOUND");
            else if (ch.equals("3")) showItems("LOST ITEMS", service.getLostItems());
            else if (ch.equals("4")) showItems("FOUND ITEMS", service.getFoundItems());
            else if (ch.equals("5")) search();
            else if (ch.equals("6")) claim();
            else if (ch.equals("7")) showItems("RESOLVED", service.getResolvedItems());
            else if (ch.equals("8")) {
                System.out.print("Exit? (y/n): ");
                if (sc.nextLine().trim().toLowerCase().equals("y")) break;
            } else {
                System.out.println("Invalid option.");
            }
            System.out.println();
        }
        sc.close();
        System.out.println("Bye!");
    }

    static void postItem(String type) {
        System.out.println("\n--- Post " + type + " Item ---");

        System.out.print("Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Category (Electronics/Books/ID Card/Bags/Keys/Clothes/Other): ");
        String cat = sc.nextLine().trim();

        System.out.print("Date (dd-mm-yyyy): ");
        String date = sc.nextLine().trim();

        System.out.print("Location: ");
        String loc = sc.nextLine().trim();

        System.out.print("Contact: ");
        String contact = sc.nextLine().trim();

        if (name.isEmpty() || loc.isEmpty() || contact.isEmpty()) {
            System.out.println("Name, location and contact are required.");
            return;
        }
        if (!Validator.isValidDate(date)) {
            System.out.println("Bad date format. Use dd-mm-yyyy.");
            return;
        }

        int id = service.postItem(type, name, cat, date, loc, contact);
        System.out.println("Saved. ID: " + id);
    }

    static void showItems(String title, List<Item> items) {
        System.out.println("\n--- " + title + " ---");
        if (items.isEmpty()) {
            System.out.println("Nothing here.");
            return;
        }
        System.out.printf("%-4s %-20s %-12s %-15s %-12s %-9s\n",
                "ID", "NAME", "CATEGORY", "LOCATION", "DATE", "STATUS");
        System.out.println("--------------------------------------------------------------------");
        for (Item i : items) {
            System.out.println(i.display());
        }
        System.out.println("Total: " + items.size());
    }

    static void search() {
        System.out.println("\n1. Keyword");
        System.out.println("2. Category");
        System.out.print("> ");
        String ch = sc.nextLine().trim();

        if (ch.equals("1")) {
            System.out.print("Keyword: ");
            String kw = sc.nextLine().trim();
            if (kw.isEmpty()) {
                System.out.println("Type something first.");
                return;
            }
            List<Item> res = service.searchByKeyword(kw);
            System.out.println("\nResults for \"" + kw + "\" (" + res.size() + ")");
            for (Item i : res) System.out.println(i.display());
        } else if (ch.equals("2")) {
            System.out.print("Category: ");
            String cat = sc.nextLine().trim();
            List<Item> res = service.searchByCategory(cat);
            System.out.println("\nCategory: " + cat + " (" + res.size() + ")");
            for (Item i : res) System.out.println(i.display());
        } else {
            System.out.println("Invalid option.");
        }
    }

    static void claim() {
        System.out.print("\nItem ID to claim: ");
        String input = sc.nextLine().trim();

        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
            return;
        }

        Item item = service.getItemById(id);
        if (item == null) {
            System.out.println("No item with ID " + id);
            return;
        }
        if (item.getStatus().equals("RESOLVED")) {
            System.out.println("Already resolved.");
            return;
        }

        System.out.println(item.display());
        System.out.print("Mark resolved? (y/n): ");
        if (sc.nextLine().trim().toLowerCase().equals("y")) {
            if (service.claimItem(id)) {
                System.out.println("Done.");
            }
        } else {
            System.out.println("Cancelled.");
        }
    }
}
```

### Line-by-Line Explanation

**Lines 5-6: Static Variables**
```java
static ItemService service = new ItemService();
static Scanner sc = new Scanner(System.in);
```
- `service` = business logic handler
- `sc` = reads user input from keyboard
- `static` = shared across all methods

**Lines 8-46: main() - The Menu Loop**

`while (true)` = infinite loop until `break`.

**Reading input:**
```java
String ch = sc.nextLine().trim();
```
- `nextLine()` reads a full line
- `trim()` removes extra spaces

**If-else chain:**
```java
if (ch.equals("1")) postItem("LOST");
```
Uses `.equals()` not `==` for string comparison.

**Exit (option 8):**
```java
if (sc.nextLine().trim().toLowerCase().equals("y")) break;
```
- `.toLowerCase()` = "Y" becomes "y"
- `.equals("y")` = check content
- `break` = exit the while loop

**Lines 48-77: postItem()**

1. Asks for each field using `System.out.print()` (no newline)
2. Reads input with `sc.nextLine().trim()`
3. Validates required fields (name, location, contact)
4. Validates date format
5. Calls `service.postItem()` to save
6. Shows confirmation with ID

**Lines 79-91: showItems()**

```java
System.out.printf("%-4s %-20s %-12s %-15s %-12s %-9s\n",
        "ID", "NAME", "CATEGORY", "LOCATION", "DATE", "STATUS");
```

`printf` formats the table header with aligned columns.

**Lines 93-117: search()**

Two options: keyword or category. Each loads results and displays them.

**Lines 120-151: claim()**

1. Reads ID as string
2. Converts to int with `Integer.parseInt()`
3. Catches `NumberFormatException` if input is not a number
4. Finds item by ID
5. Checks if item exists and is not resolved
6. Shows details and asks for confirmation
7. Calls `service.claimItem()` if confirmed

---

## 8. Tests.java Walkthrough

### Full Source Code

```java
import java.io.*;
import java.util.List;

public class Tests {
    static int passed = 0, failed = 0;

    static void check(String name, boolean ok) {
        if (ok) { System.out.println("PASS: " + name); passed++; }
        else { System.out.println("FAIL: " + name); failed++; }
    }

    public static void main(String[] args) throws Exception {
        ItemService svc = new ItemService();
        StorageManager sm = new StorageManager();

        // Test 2: initial state
        List<Item> all = svc.getAllItems();
        check("2a: 15 records", all.size() == 15);
        check("2b: 8 LOST", svc.getLostItems().size() == 8);
        check("2c: 7 FOUND", svc.getFoundItems().size() == 7);
        check("2d: 2 RESOLVED", svc.getResolvedItems().size() == 2);

        // Test 3: filter counts
        check("3: getLostItems=8", svc.getLostItems().size() == 8);
        check("3: getFoundItems=7", svc.getFoundItems().size() == 7);
        check("3: getResolvedItems=2", svc.getResolvedItems().size() == 2);

        // Test 4: post
        int newId = svc.postItem("LOST", "Test Pen", "Other", "10-08-2026", "Office", "1234567890");
        check("4a: id=16", newId == 16);
        check("4b: 16 lines", svc.getAllItems().size() == 16);

        // Test 5: search
        check("5a: case-insensitive",
              svc.searchByKeyword("ring").size() == svc.searchByKeyword("RING").size());
        check("5b: no match", svc.searchByKeyword("zzz").size() == 0);

        // Test 6: claim
        check("6a: claim OK", svc.claimItem(16));
        check("6b: already resolved", !svc.claimItem(16));
        Item claimed = svc.getItemById(16);
        check("6c: status RESOLVED",
              claimed != null && claimed.getStatus().equals("RESOLVED"));
        check("6d: resolved=3", svc.getResolvedItems().size() == 3);

        // Test 7: date validation
        check("7a: valid date", Validator.isValidDate("01-08-2026"));
        check("7b: bad format", !Validator.isValidDate("1/8/26"));
        check("7c: not a date", !Validator.isValidDate("abc"));

        // Test 8: missing file
        File dataFile = new File("data/items.txt");
        File backup = new File("data/items_backup.txt");
        dataFile.renameTo(backup);
        check("8: empty list", sm.load().size() == 0);
        backup.renameTo(dataFile);

        // Test 9: restore
        List<Item> finalList = svc.getAllItems();
        java.util.List<Item> original = new java.util.ArrayList<>();
        for (Item i : finalList) if (i.getId() <= 15) original.add(i);
        for (Item i : original) if (i.getId() == 6 || i.getId() == 10) i.setStatus("RESOLVED");
        sm.rewrite(original);
        List<Item> restored = svc.getAllItems();
        check("9: 15 records", restored.size() == 15);
        int rLost = 0, rFound = 0, rResolved = 0;
        for (Item i : restored) {
            if (i.getType().equals("LOST")) rLost++;
            if (i.getType().equals("FOUND")) rFound++;
            if (i.getStatus().equals("RESOLVED")) rResolved++;
        }
        check("9: 8 LOST", rLost == 8);
        check("9: 7 FOUND", rFound == 7);
        check("9: 2 RESOLVED", rResolved == 2);

        System.out.println("\n" + passed + " passed, " + failed + " failed");
    }
}
```

### What Each Test Does

| Test | Checks | Expected |
|------|--------|----------|
| 2a | Total records | 15 |
| 2b | Lost items | 8 |
| 2c | Found items | 7 |
| 2d | Resolved items | 2 |
| 3 | Filter methods | Same counts |
| 4a | New item ID | 16 |
| 4b | File after post | 16 lines |
| 5a | Case-insensitive search | Same results |
| 5b | No match for "zzz" | 0 results |
| 6a | Claim works | true |
| 6b | Double claim fails | false |
| 6c | Status changed | RESOLVED |
| 6d | Resolved count | 3 |
| 7a | Valid date | true |
| 7b | Bad format | false |
| 7c | Not a date | false |
| 8 | Missing file | Empty list, no crash |
| 9 | Restore data | 15 records, original counts |

### Test 8 Explained (Missing File)

```java
File dataFile = new File("data/items.txt");
File backup = new File("data/items_backup.txt");
dataFile.renameTo(backup);           // Rename file away
check("8: empty list", sm.load().size() == 0);  // Load should return empty
backup.renameTo(dataFile);           // Rename back
```

This tests that `StorageManager.load()` handles missing files gracefully.

### Test 9 Explained (Data Restore)

After tests modify the data, this test restores the original 15 records by:
1. Removing the test item (ID 16)
2. Restoring original statuses for items 6 and 10
3. Rewriting the file

---

## 9. Data File Format

**File:** `data/items.txt`

Each line is one item:
```
id|type|name|category|date|location|contact|status
```

Example:
```
1|LOST|pl|book|20-12-2006|tinsukia|210016664|OPEN
```

| Field | Position | Example |
|-------|----------|---------|
| id | 0 | `1` |
| type | 1 | `LOST` |
| name | 2 | `pl` |
| category | 3 | `book` |
| date | 4 | `20-12-2006` |
| location | 5 | `tinsukia` |
| contact | 6 | `210016664` |
| status | 7 | `OPEN` |

---

## 10. How to Compile and Run

### Compile

```bash
javac -d bin src/*.java
```

This compiles all `.java` files and puts `.class` files in `bin/`.

### Run the App

```bash
java -cp bin Main
```

### Run the Tests

```bash
java -cp bin Tests
```

### Expected Test Output

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
