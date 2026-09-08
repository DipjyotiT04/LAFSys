# Part 2: Class Design and Java Concepts

**Student 2**

---

## Table of Contents

1. [Overview](#1-overview)
2. [Class Diagram](#2-class-diagram)
3. [Class 1: Item (Data Model)](#3-class-1-item)
4. [Class 2: Validator (Utility)](#4-class-2-validator)
5. [Class 3: StorageManager (Persistence)](#5-class-3-storagemanager)
6. [Class 4: ItemService (Business Logic)](#6-class-4-itemservice)
7. [Class 5: Main (User Interface)](#7-class-5-main)
8. [Class 6: Tests (Testing)](#8-class-6-tests)
9. [Java Concepts Used](#9-java-concepts)
10. [Object-Oriented Principles](#10-oop-principles)
11. [Data Flow Between Classes](#11-data-flow)
12. [Design Decisions](#12-design-decisions)

---

## 1. Overview

### What is Class Design?

Class design is the blueprint of how the program is organized. Before writing code, we decide:
- What classes do we need?
- What does each class do?
- How do classes talk to each other?
- What data does each class hold?
- What methods does each class provide?

### Why Class Design Matters

| Without Good Design | With Good Design |
|--------------------|--------------------|
| Everything in one file | Each class has one job |
| Hard to fix bugs | Bug is in one specific class |
| Hard to add features | Add new class without breaking others |
| Confusing to read | Clear structure, easy to follow |
| Code duplication | Each piece written once |

---

## 2. Class Diagram

### Visual Overview

```
+-------------------+        +-------------------+
|      Main         |        |     Validator      |
|-------------------|        |-------------------|
| - service         |        | + isNotEmpty()    |
| - sc              |        | + isValidDate()   |
|-------------------|        +-------------------+
| + main()          |
| - postItem()      |        +-------------------+
| - showItems()     |        |      Item          |
| - search()        |        |-------------------|
| - claim()         |        | - id              |
+-------------------+        | - type            |
        |                    | - name            |
        v                    | - category        |
+-------------------+        | - date            |
|    ItemService     |<------| - location        |
|-------------------|        | - contact         |
| - storage         |        | - status          |
|-------------------|        |-------------------|
| + postItem()      |        | + getId()         |
| + searchByKeyword |        | + toLine()        |
| + searchByCategory|        | + display()       |
| + claimItem()     |        +-------------------+
| + getLostItems()  |
| + getFoundItems() |        +-------------------+
| + getResolvedItems|        |  StorageManager   |
| + getItemById()   |        |-------------------|
| + getAllItems()    |------->| - FILE            |
+-------------------+        |-------------------|
                             | + load()          |
                             | + append()        |
                             | + rewrite()       |
                             | - ensureDir()     |
                             +-------------------+
```

### Class Relationships

| Relationship | Type | Explanation |
|-------------|------|-------------|
| Main **uses** ItemService | Association | Main calls ItemService methods |
| Main **uses** Validator | Association | Main calls Validator for input checks |
| ItemService **uses** StorageManager | Association | ItemService calls StorageManager for file I/O |
| StorageManager **creates** Item | Dependency | StorageManager creates Item objects from file data |
| ItemService **creates** Item | Dependency | ItemService creates new Item objects |
| ItemService **returns** Item to Main | Association | Item objects flow from service to UI |

---

## 3. Class 1: Item

### Purpose

Item is a **data model** class. It represents a single lost-and-found item. It holds data and provides ways to access and format that data.

### What is a POJO?

POJO stands for **Plain Old Java Object**. It is a simple class that:
- Holds data (fields)
- Has a constructor to set the data
- Has getters to read the data
- Has minimal logic (just data formatting)

### Class Design

```
+---------------------------+
|         Item              |
+---------------------------+
| Fields:                   |
|   id : int                |
|   type : String           |
|   name : String           |
|   category : String       |
|   date : String           |
|   location : String       |
|   contact : String        |
|   status : String         |
+---------------------------+
| Methods:                  |
|   Item(...)               |  <- Constructor
|   getId() : int           |  <- Getter
|   getType() : String      |  <- Getter
|   getName() : String      |  <- Getter
|   getCategory() : String  |  <- Getter
|   getDate() : String      |  <- Getter
|   getLocation() : String  |  <- Getter
|   getContact() : String   |  <- Getter
|   getStatus() : String    |  <- Getter
|   setStatus(s : String)   |  <- Setter
|   toLine() : String       |  <- Serialization
|   display() : String      |  <- Formatting
+---------------------------+
```

### Fields Explained

| Field | Type | What It Stores | Example |
|-------|------|---------------|---------|
| `id` | `int` | Unique number for each item | `1`, `15`, `42` |
| `type` | `String` | "LOST" or "FOUND" | `"LOST"` |
| `name` | `String` | What the item is | `"Wallet"` |
| `category` | `String` | Type of item | `"Bags"` |
| `date` | `String` | When lost/found | `"01-09-2026"` |
| `location` | `String` | Where lost/found | `"Library"` |
| `contact` | `String` | Phone number | `"9876543210"` |
| `status` | `String` | "OPEN" or "RESOLVED" | `"OPEN"` |

### Methods Explained

**Constructor:**
```java
public Item(int id, String type, String name, String category,
            String date, String location, String contact, String status) {
    this.id = id;
    this.type = type;
    // ... sets all fields
}
```
- Called when creating a new Item: `new Item(1, "LOST", "Wallet", ...)`
- `this` keyword refers to "this object's field"
- Without `this`, the parameter name would shadow the field name

**Getters:**
```java
public int getId() { return id; }
public String getName() { return name; }
// ... one for each field
```
- Allow other classes to READ the data
- No setter for most fields = data is mostly immutable after creation

**Setter:**
```java
public void setStatus(String s) { this.status = s; }
```
- Only `status` can be changed after creation
- This is intentional: once an item is created, only its status changes (when claimed)

**Serialization (`toLine`):**
```java
public String toLine() {
    return id + "|" + type + "|" + name + "|" + category + "|"
           + date + "|" + location + "|" + contact + "|" + status;
}
```
- Converts the item to a text line for saving to file
- Example output: `"1|LOST|Wallet|Bags|01-09-2026|Library|987|OPEN"`

**Display (`display`):**
```java
public String display() {
    return String.format("%-4d %-20s %-12s %-15s %-12s %-9s",
            id, name, category, location, date, status);
}
```
- Formats the item for console table display
- `%-4d` means: left-aligned integer, 4 characters wide
- `%-20s` means: left-aligned string, 20 characters wide

---

## 4. Class 2: Validator

### Purpose

Validator is a **utility class**. It has no data (fields). It only has static methods that check if input is valid.

### Why Use a Separate Class?

Without Validator, validation code would be inside Main.java. By separating it:
- Validation logic is in one place
- Can be reused anywhere
- Easier to test (Tests.java tests it directly)
- Main.java stays focused on UI

### Class Design

```
+---------------------------+
|        Validator          |
+---------------------------+
| Fields: (none)            |
+---------------------------+
| Methods:                  |
| + isNotEmpty(text) : bool |  <- Check for empty strings
| + isValidDate(text) : bool|  <- Check date format
+---------------------------+
```

### The `static` Keyword

Both methods are `static`. This means:
- You do NOT need to create a Validator object
- You call them like: `Validator.isValidDate("01-09-2026")`
- They belong to the class, not to an instance

### Method: isNotEmpty

```java
public static boolean isNotEmpty(String text) {
    return text != null && !text.trim().isEmpty();
}
```

**Logic breakdown:**
```
text != null          -> Is the text not null?
&&                    -> AND
!text.trim().isEmpty() -> After removing spaces, is it not empty?
```

**Decision table:**

| Input | `text != null` | `trim().isEmpty()` | `!isEmpty()` | Result |
|-------|---------------|-------------------|-------------|--------|
| `"Wallet"` | true | false | true | **true** |
| `""` | true | true | false | **false** |
| `"   "` | true | true (after trim) | false | **false** |
| `null` | false | - | - | **false** |

### Method: isValidDate

```java
public static boolean isValidDate(String text) {
    if (text == null) return false;
    return text.matches("\\d{2}-\\d{2}-\\d{4}");
}
```

**Regex breakdown: `\\d{2}-\\d{2}-\\d{4}`**

| Part | Meaning | Example Match |
|------|---------|--------------|
| `\\d` | Any digit (0-9) | `0`, `5`, `9` |
| `{2}` | Exactly 2 of the previous | `01`, `25`, `99` |
| `-` | Literal hyphen character | `-` |
| `{4}` | Exactly 4 of the previous | `2026`, `1999` |

**Full pattern:** 2 digits, hyphen, 2 digits, hyphen, 4 digits

| Input | Matches? | Why |
|-------|---------|-----|
| `"01-09-2026"` | Yes | 2 digits, hyphen, 2 digits, hyphen, 4 digits |
| `"20-12-2006"` | Yes | Same pattern |
| `"1-9-2026"` | No | Only 1 digit for day and month |
| `"01/09/2026"` | No | Forward slashes instead of hyphens |
| `"01-09-26"` | No | Only 2 digits for year |
| `"abc"` | No | Not digits |
| `"01-09-20261"` | No | 5 digits for year |
| `null` | No | Null check returns false |

---

## 5. Class 3: StorageManager

### Purpose

StorageManager handles **all file operations**. It reads items from the text file and writes items to the text file. No other class touches the file directly.

### Why Separate File I/O?

- If we change the file format (e.g., switch to CSV), we only change StorageManager
- If we switch to a database later, we only change StorageManager
- Other classes do not need to know about files

### Class Design

```
+---------------------------+
|      StorageManager       |
+---------------------------+
| Fields:                   |
|   FILE : String (const)   |  <- Path to data file
+---------------------------+
| Methods:                  |
| + load() : List<Item>     |  <- Read all items from file
| + append(item : Item)     |  <- Add one item to file
| + rewrite(list : List)    |  <- Overwrite entire file
| - ensureDir()             |  <- Create data/ folder if needed
+---------------------------+
```

### The FILE Constant

```java
static final String FILE = "data" + File.separator + "items.txt";
```

| Keyword | Meaning |
|---------|---------|
| `static` | Belongs to the class, not an object |
| `final` | Cannot be changed (constant) |
| `File.separator` | `/` on Linux/Mac, `\` on Windows |

So `FILE` is always the correct path to the data file, regardless of operating system.

### Method: load()

This method reads the entire file and returns a list of Item objects.

**Step-by-step logic:**

```
1. Create empty list
2. Check if file exists
   - If not, return empty list
3. Open file for reading
4. For each line in the file:
   a. Trim whitespace
   b. Skip if empty
   c. Split line by "|" into parts
   d. Skip if not exactly 8 parts
   e. Parse each part into Item fields
   f. Create Item object
   g. Add to list
5. Return list
```

**How line splitting works:**

```
File line: "1|LOST|Wallet|Bags|01-09-2026|Library|987|OPEN"
                         |
                         v split("\\|")
                         |
Result:   ["1", "LOST", "Wallet", "Bags", "01-09-2026", "Library", "987", "OPEN"]
Index:     [0]   [1]     [2]      [3]       [4]           [5]       [6]    [7]
```

**Error handling:**
- File does not exist → return empty list
- Line is empty → skip it
- Line has wrong number of parts → skip it
- ID is not a number → skip that line
- IO error → return whatever was loaded

### Method: append()

Adds one item to the end of the file.

```java
public void append(Item item) {
    ensureDir();
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
        bw.write(item.toLine());
        bw.newLine();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

| Part | What It Does |
|------|-------------|
| `ensureDir()` | Creates `data/` folder if it does not exist |
| `new FileWriter(FILE, true)` | Opens file in **append mode** (adds to end) |
| `bw.write(item.toLine())` | Writes the item as a text line |
| `bw.newLine()` | Adds a line break after the item |

### Method: rewrite()

Overwrites the entire file with a new list of items.

```java
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
```

| Part | What It Does |
|------|-------------|
| `new FileWriter(FILE, false)` | Opens file in **overwrite mode** (erases first) |
| `for (Item i : list)` | Loops through every item |
| `bw.write(i.toLine())` | Writes each item |

**When is rewrite used?** When claiming an item (status changes from OPEN to RESOLVED). Since every line in the file contains the status, we must rewrite everything.

### Method: ensureDir()

```java
private void ensureDir() {
    File dir = new File("data");
    if (!dir.exists()) dir.mkdirs();
}
```

- `mkdirs()` creates the folder and any missing parent folders
- Called before every write operation to prevent "directory not found" errors

---

## 6. Class 4: ItemService

### Purpose

ItemService is the **business logic layer**. It handles all operations: creating items, searching, filtering, claiming. It sits between the user interface (Main) and the file storage (StorageManager).

### Why Have a Service Layer?

Without ItemService, Main.java would directly call StorageManager. This would make Main.java too complex. By having a service:
- Main.java only handles UI (displaying menus, reading input)
- ItemService handles logic (creating, searching, claiming)
- StorageManager only handles files

This is called **Separation of Concerns**.

### Class Design

```
+-------------------------------+
|         ItemService           |
+-------------------------------+
| Constants:                    |
|   TYPE_LOST = "LOST"          |
|   TYPE_FOUND = "FOUND"        |
|   STATUS_RESOLVED = "RESOLVED"|
+-------------------------------+
| Fields:                       |
|   storage : StorageManager    |
+-------------------------------+
| Methods:                      |
| + postItem(...) : int         |
| + searchByKeyword(w) : List   |
| + searchByCategory(c) : List  |
| + getItemById(id) : Item      |
| + claimItem(id) : boolean     |
| + getLostItems() : List       |
| + getFoundItems() : List      |
| + getResolvedItems() : List   |
| + getAllItems() : List         |
+-------------------------------+
```

### Constants

```java
static final String TYPE_LOST = "LOST";
static final String TYPE_FOUND = "FOUND";
static final String STATUS_RESOLVED = "RESOLVED";
```

**Why use constants instead of strings?**

Without constants:
```java
if (item.getType().equals("LOST"))  // What if you type "Lost"? Bug!
```

With constants:
```java
if (item.getType().equals(TYPE_LOST))  // Always correct
```

If we ever change "LOST" to something else, we change it in ONE place (the constant), not everywhere in the code.

### Java Streams

ItemService uses Java 8 Streams for filtering. Here is how streams work:

**Without streams (old way):**
```java
public List<Item> getLostItems() {
    List<Item> lost = new ArrayList<>();
    for (Item i : storage.load()) {
        if (i.getType().equals("LOST")) {
            lost.add(i);
        }
    }
    return lost;
}
```

**With streams (new way):**
```java
public List<Item> getLostItems() {
    return storage.load().stream()
            .filter(i -> i.getType().equals("LOST"))
            .collect(Collectors.toList());
}
```

**Stream pipeline explained:**

```
storage.load()          Load all items from file
    |
    v
.stream()               Convert list to a stream (pipeline)
    |
    v
.filter(i -> ...)       Keep only items that match the condition
    |
    v
.collect(toList())      Put matching items back into a List
```

**Think of it like an assembly line:**
1. Items go in (all items from file)
2. Filter removes unwanted items
3. Remaining items come out (filtered list)

### Method: postItem()

```java
public int postItem(String type, String name, String category,
                    String date, String location, String contact) {
    List<Item> items = storage.load();
    int maxId = items.stream().mapToInt(Item::getId).max().orElse(0);
    int newId = maxId + 1;
    Item item = new Item(newId, type, name, category, date, location, contact, "OPEN");
    storage.append(item);
    return newId;
}
```

**Step-by-step:**

| Step | Code | What Happens |
|------|------|-------------|
| 1 | `storage.load()` | Read all items from file |
| 2 | `items.stream().mapToInt(Item::getId).max().orElse(0)` | Find the highest ID |
| 3 | `maxId + 1` | New ID = highest + 1 |
| 4 | `new Item(...)` | Create the item with status "OPEN" |
| 5 | `storage.append(item)` | Save to file |
| 6 | `return newId` | Tell the caller the new ID |

**Stream chain explained:**
```
items.stream()       -> Stream of Item objects
.mapToInt(Item::getId) -> Extract IDs (now a stream of integers)
.max()               -> Find the maximum value
.orElse(0)           -> If empty, use 0
```

### Method: searchByKeyword()

```java
public List<Item> searchByKeyword(String word) {
    String lower = word.toLowerCase();
    return storage.load().stream()
            .filter(i -> i.getName().toLowerCase().contains(lower)
                    || i.getLocation().toLowerCase().contains(lower)
                    || i.getCategory().toLowerCase().contains(lower))
            .collect(Collectors.toList());
}
```

**How it searches:**

```
User searches: "ring"
Lower case:   "ring"

Items to check:
  "Earring"    -> toLowerCase -> "earring"    -> contains "ring"? YES
  "Ring Light" -> toLowerCase -> "ring light" -> contains "ring"? YES
  "Notebook"   -> toLowerCase -> "notebook"   -> contains "ring"? NO
  "Keyring"    -> toLowerCase -> "keyring"    -> contains "ring"? YES

Result: ["Earring", "Ring Light", "Keyring"]
```

The `||` means OR, so it checks name OR location OR category.

### Method: claimItem()

```java
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
```

**Logic flow:**

```
Load all items
    |
    v
Loop through each item
    |
    v
Is this the right ID?  ----No----> Continue to next item
    |
   Yes
    |
    v
Is status already RESOLVED? ----Yes----> Return false
    |
   No
    |
    v
Change status to RESOLVED
    |
    v
Rewrite all items to file
    |
    v
Return true (success)
```

---

## 7. Class 5: Main

### Purpose

Main is the **user interface**. It shows menus, reads user input, and displays results. It does NOT contain business logic.

### Class Design

```
+---------------------------+
|          Main             |
+---------------------------+
| Fields:                   |
| - service : ItemService   |
| - sc : Scanner            |
+---------------------------+
| Methods:                  |
| + main(args)              |  <- Entry point
| - postItem(type)          |  <- Post item UI
| - showItems(title, items) |  <- Display table
| - search()                |  <- Search UI
| - claim()                 |  <- Claim UI
+---------------------------+
```

### The main() Method

```java
public static void main(String[] args) {
```

This is where Java starts. Every Java application needs exactly one `main()` method.

**The menu loop:**
```java
while (true) {
    // Show menu
    // Read input
    // Do something
}
```

`while (true)` creates an infinite loop. The program runs until the user chooses to exit (option 8), which uses `break` to exit the loop.

### Input Handling

```java
String ch = sc.nextLine().trim();
```

| Part | What It Does |
|------|-------------|
| `sc` | The Scanner object (reads from keyboard) |
| `.nextLine()` | Reads a full line of text |
| `.trim()` | Removes leading/trailing spaces |

### The if-else Chain

```java
if (ch.equals("1")) postItem("LOST");
else if (ch.equals("2")) postItem("FOUND");
else if (ch.equals("3")) showItems("LOST ITEMS", service.getLostItems());
// ...
else if (ch.equals("8")) {
    System.out.print("Exit? (y/n): ");
    if (sc.nextLine().trim().toLowerCase().equals("y")) break;
}
else {
    System.out.println("Invalid option.");
}
```

**Why `equals()` and not `==`?**

In Java, `==` compares object references (memory addresses). `equals()` compares actual content. For strings, we always use `equals()`.

```java
String a = "hello";
String b = "hello";
a == b       // Might be true or false (depends on string pool)
a.equals(b)  // Always true (same content)
```

### The postItem() Method

```java
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

    // Validation
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
```

**Key points:**
- `System.out.print()` - Prints without a newline (cursor stays on same line)
- `sc.nextLine().trim()` - Reads input and removes extra spaces
- Validation happens BEFORE saving
- `return` exits the method early if validation fails

### The showItems() Method

```java
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
```

**`printf` format codes:**

| Code | Meaning | Example |
|------|---------|---------|
| `%s` | String | `"Hello"` |
| `%d` | Integer | `42` |
| `%-4s` | Left-aligned string, 4 chars wide | `"ID  "` |
| `%-20s` | Left-aligned string, 20 chars wide | `"Wallet             "` |

---

## 8. Class 6: Tests

### Purpose

Tests verifies that the application works correctly. It runs a series of checks and reports PASS or FAIL for each.

### Class Design

```
+---------------------------+
|          Tests            |
+---------------------------+
| Fields:                   |
| - passed : int            |
| - failed : int            |
+---------------------------+
| Methods:                  |
| - check(name, ok)         |  <- Assert a condition
| + main(args)              |  <- Run all tests
+---------------------------+
```

### The check() Method

```java
static void check(String name, boolean ok) {
    if (ok) { System.out.println("PASS: " + name); passed++; }
    else { System.out.println("FAIL: " + name); failed++; }
}
```

This is a simple assertion. If the condition is true, it prints PASS. If false, it prints FAIL.

### Test Categories

| Test | What It Verifies |
|------|-----------------|
| 2 | Initial data state (15 records, correct counts) |
| 3 | Filter methods return correct counts |
| 4 | PostItem creates new item with correct ID |
| 5 | Search is case-insensitive, no results for invalid input |
| 6 | ClaimItem works, idempotent (cannot claim twice) |
| 7 | Date validation works correctly |
| 8 | Missing file does not crash the app |
| 9 | Data can be restored to original state |

---

## 9. Java Concepts Used

### Concept 1: Classes and Objects

**Class:** A blueprint. Defines what data and methods an object has.
```java
public class Item {
    int id;
    String name;
}
```

**Object:** An instance of a class. A real thing created from the blueprint.
```java
Item item = new Item();  // Create an object from the Item class
item.id = 1;             // Set its data
item.name = "Wallet";    // Set its data
```

**Analogy:**
- Class = Cookie cutter (defines the shape)
- Object = Cookie (the actual thing made from the cutter)

### Concept 2: Encapsulation

Encapsulation means hiding internal details and only exposing what is necessary.

In Item.java:
- Fields are package-private (no `private` keyword, but not `public` either)
- Getters allow reading data
- Only `setStatus()` allows changing data
- External code cannot directly modify `name`, `type`, etc.

### Concept 3: Static Methods and Fields

**Static field:** Belongs to the class, not to any object.
```java
static final String TYPE_LOST = "LOST";  // Shared by all
```

**Static method:** Can be called without creating an object.
```java
Validator.isValidDate("01-09-2026");  // No new Validator() needed
```

### Concept 4: Inheritance (Not Used)

This project does NOT use inheritance. All classes are independent. This is intentional - the project is simple enough that inheritance would add unnecessary complexity.

### Concept 5: Interfaces (Not Used)

Similarly, interfaces are not used. The project is too small to benefit from them.

### Concept 6: Java Streams (Java 8+)

Streams allow processing collections in a functional style.

**Traditional loop:**
```java
List<Item> lost = new ArrayList<>();
for (Item i : items) {
    if (i.getType().equals("LOST")) {
        lost.add(i);
    }
}
```

**Stream:**
```java
List<Item> lost = items.stream()
        .filter(i -> i.getType().equals("LOST"))
        .collect(Collectors.toList());
```

**Stream operations used in this project:**

| Operation | What It Does | Example |
|-----------|-------------|---------|
| `.stream()` | Converts list to stream | `items.stream()` |
| `.filter()` | Keeps matching elements | `.filter(i -> i.getType().equals("LOST"))` |
| `.mapToInt()` | Extracts int values | `.mapToInt(Item::getId)` |
| `.max()` | Finds maximum | `.max()` |
| `.findFirst()` | Gets first element | `.findFirst()` |
| `.collect()` | Converts back to list | `.collect(Collectors.toList())` |
| `.orElse()` | Default value | `.orElse(0)` |

### Concept 7: Lambda Expressions

A lambda is an anonymous (unnamed) function. Used with streams.

```java
.filter(i -> i.getType().equals("LOST"))
       ^   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
       |   Lambda expression (the condition)
       |
       Parameter (each item in the stream)
```

**Equivalent without lambda:**
```java
.filter(new Predicate<Item>() {
    public boolean test(Item i) {
        return i.getType().equals("LOST");
    }
})
```

Lambdas make the code shorter and more readable.

### Concept 8: Try-With-Resources

```java
try (BufferedReader br = new BufferedReader(new FileReader(f))) {
    // use br
}  // br is automatically closed here
```

Without try-with-resources, you would need:
```java
BufferedReader br = null;
try {
    br = new BufferedReader(new FileReader(f));
    // use br
} finally {
    if (br != null) br.close();
}
```

Try-with-resources is shorter and safer (guarantees the resource is closed).

### Concept 9: Exception Handling

```java
try {
    id = Integer.parseInt(input);  // Might fail
} catch (NumberFormatException e) {
    System.out.println("Enter a valid number.");
    return;
}
```

| Part | Meaning |
|------|---------|
| `try` | Try this code, it might cause an error |
| `catch` | If this specific error happens, do this instead |
| `NumberFormatException` | Error when trying to convert text to a number |

### Concept 10: String Methods

| Method | What It Does | Example |
|--------|-------------|---------|
| `.trim()` | Removes leading/trailing spaces | `"  hi  ".trim()` → `"hi"` |
| `.toLowerCase()` | Converts to lowercase | `"HELLO".toLowerCase()` → `"hello"` |
| `.equals()` | Compares content | `"a".equals("a")` → `true` |
| `.isEmpty()` | Checks if empty | `"".isEmpty()` → `true` |
| `.contains()` | Checks if contains substring | `"hello".contains("ell")` → `true` |
| `.matches()` | Regex matching | `"01-09-2026".matches("\\d{2}-\\d{2}-\\d{4}")` → `true` |

---

## 10. Object-Oriented Principles

### Principle 1: Single Responsibility

Each class has ONE job:

| Class | Responsibility |
|-------|---------------|
| Item | Hold data about an item |
| Validator | Check if input is valid |
| StorageManager | Read/write files |
| ItemService | Business logic (create, search, claim) |
| Main | User interface (menus, display) |
| Tests | Verify everything works |

### Principle 2: Separation of Concerns

The UI (Main) does not know about files. The file handler (StorageManager) does not know about menus. Each layer handles its own concern.

```
Main           -> "Show me lost items"
ItemService    -> "Let me filter for LOST items"
StorageManager -> "Let me read the file"
```

### Principle 3: DRY (Don't Repeat Yourself)

Instead of writing the same filtering code in Main, we put it in ItemService. If we need to filter differently, we change one place.

### Principle 4: KISS (Keep It Simple, Stupid)

- No inheritance hierarchy
- No interfaces
- No design patterns
- Just simple, readable classes

---

## 11. Data Flow Between Classes

### Flow 1: Posting an Item

```
User types "1" (Post Lost Item)
    |
    v
Main.postItem("LOST")
    |  - Asks user for details
    |  - Validates input
    v
ItemService.postItem("LOST", "Wallet", "Bags", "01-09-2026", "Library", "987")
    |  - Loads existing items
    |  - Finds next ID
    |  - Creates new Item object
    v
StorageManager.append(item)
    |  - Creates data/ directory if needed
    |  - Opens file in append mode
    |  - Writes item.toLine()
    v
data/items.txt (new line added)
```

### Flow 2: Viewing Lost Items

```
User types "3" (View Lost Items)
    |
    v
Main.showItems("LOST ITEMS", service.getLostItems())
    |
    v
ItemService.getLostItems()
    |  - Calls storage.load()
    v
StorageManager.load()
    |  - Opens data/items.txt
    |  - Reads each line
    |  - Creates Item objects
    |  - Returns List<Item>
    v
ItemService.getLostItems() (continued)
    |  - Filters items where type == "LOST"
    |  - Returns filtered list
    v
Main.showItems() (continued)
    |  - Prints table header
    |  - Loops through items, calls item.display()
    |  - Prints total count
    v
User sees table on screen
```

### Flow 3: Claiming an Item

```
User types "6" (Claim Item), enters ID 15
    |
    v
Main.claim()
    |  - Parses ID
    |  - Calls service.getItemById(15)
    v
ItemService.getItemById(15)
    |  - Loads all items
    |  - Finds item with ID 15
    |  - Returns Item object (or null)
    v
Main.claim() (continued)
    |  - Checks if item exists
    |  - Checks if already resolved
    |  - Shows item details
    |  - Asks "Mark resolved? (y/n)"
    |  - User types "y"
    v
ItemService.claimItem(15)
    |  - Loads all items
    |  - Finds item with ID 15
    |  - Changes status to "RESOLVED"
    |  - Calls storage.rewrite(items)
    v
StorageManager.rewrite(items)
    |  - Opens file in overwrite mode
    |  - Writes ALL items (with updated status)
    v
data/items.txt (item 15 now shows RESOLVED)
```

---

## 12. Design Decisions

### Decision 1: Text File Instead of Database

**Chosen:** Text file (`data/items.txt`)

**Why:**
- No setup required
- Easy to understand
- Portable (just copy the file)
- Sufficient for campus-scale data

**Trade-off:** No concurrent access, no complex queries

### Decision 2: Pipe Delimiter (`|`)

**Chosen:** `|` as field separator

**Why:**
- Rarely appears in item names
- Easy to split in Java: `line.split("\\|")`
- Visually clear in the file

**Alternative considered:** CSV (commas), but commas appear in names

### Decision 3: Package-Private Fields in Item

**Chosen:** No `private` keyword on Item fields

**Why:**
- Simpler code
- No need for full encapsulation in a small project
- Getters still provided for clean access

**Trade-off:** Less strict access control

### Decision 4: Streams for Filtering

**Chosen:** Java 8 Streams

**Why:**
- Shorter, more readable code
- Functional style
- Easy to chain operations

**Alternative:** Traditional for loops

### Decision 5: No User Accounts

**Chosen:** Single-user system

**Why:**
- Simplifies the project significantly
- Campus setting does not require login
- Contact number serves as identification

### Decision 6: y/n for Confirmations

**Chosen:** `y/n` instead of `yes/no`

**Why:**
- Faster for users
- Less typing
- More natural

---

## Summary

The class design of LAFSys follows good object-oriented principles:
- Each class has a single responsibility
- Classes communicate through well-defined methods
- File I/O is isolated in StorageManager
- Business logic is centralized in ItemService
- The UI is clean and focused in Main

The Java concepts used include classes, objects, encapsulation, static methods, streams, lambda expressions, try-with-resources, exception handling, and string methods. The design is intentionally simple (KISS principle) - no unnecessary inheritance, interfaces, or design patterns.
