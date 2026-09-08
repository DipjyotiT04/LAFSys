# LAFSys - Complete Beginner Guide

A campus lost and found system built in Java. Post lost items, post found items, search, and claim them.

---

## Table of Contents

1. [What This Project Does](#1-what-this-project-does)
2. [How It Works (Big Picture)](#2-how-it-works-big-picture)
3. [Project Files](#3-project-files)
4. [File 1: Item.java - The Data Model](#4-itemjava)
5. [File 2: Validator.java - Checking Inputs](#5-validatorjava)
6. [File 3: StorageManager.java - Saving to File](#6-storagemanagerjava)
7. [File 4: ItemService.java - The Brain](#7-itemservicejava)
8. [File 5: Main.java - The Menu](#8-mainjava)
9. [File 6: Tests.java - Making Sure It Works](#9-testsjava)
10. [Data File: items.txt](#10-data-file)
11. [How to Run](#11-how-to-run)

---

## 1. What This Project Does

This is a console app (text-based, no graphics) for a college campus. Students can:

- **Post a lost item** - "I lost my wallet in the library"
- **Post a found item** - "I found a pen in Room 101"
- **Search** - Find items by name or category
- **Claim** - Mark a lost item as found (resolved)
- **View** - See all lost, found, or resolved items

Data is saved in a text file so it lasts between runs.

---

## 2. How It Works (Big Picture)

```
You type in the console
        |
        v
   Main.java          <-- Shows menu, takes your input
        |
        v
   ItemService.java   <-- Decides what to do
        |
        v
   StorageManager.java <-- Reads or writes the file
        |
        v
   data/items.txt     <-- Where all items are stored
```

When you do something (like post an item):
1. Main.java asks you for details
2. ItemService.java creates the item and saves it
3. StorageManager.java writes it to the file

When you view items:
1. Main.java asks what to show
2. ItemService.java reads from the file
3. StorageManager.java loads the file and returns items

---

## 3. Project Files

```
LAFSys/
  src/
    Main.java           <-- Start here (the menu)
    Item.java           <-- What an item looks like
    ItemService.java    <-- Business logic
    StorageManager.java <-- File reading/writing
    Validator.java      <-- Input checks
    Tests.java          <-- Tests to verify everything works
  data/
    items.txt           <-- The database (text file)
```

---

## 4. Item.java

**What it does:** Defines what a "lost and found item" looks like.

**Full code:**

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

| Line | Code | What It Means |
|------|------|---------------|
| 1 | `public class Item {` | Creates a class called `Item`. A class is a blueprint. |
| 2 | `int id;` | A whole number to uniquely identify each item (like a receipt number). |
| 3 | `String type, name, category, date, location, contact, status;` | Text fields. `type` is "LOST" or "FOUND", `status` is "OPEN" or "RESOLVED". |
| 5-13 | Constructor | Creates a new Item when you write `new Item(1, "LOST", "Wallet", ...)` |
| 5-13 | `this.id = id;` | `this` means "this object's field". It takes the value passed in and stores it. |
| 16-23 | `getId()`, `getName()`, etc. | Getter methods. Let other classes read the fields. |
| 24 | `setStatus(String s)` | Setter method. Only `status` can be changed after creation. |
| 26-28 | `toLine()` | Converts the item to a text line like `1|LOST|Wallet|Bags|01-09-2026|Library|987|OPEN` |
| 30-32 | `display()` | Formats the item for pretty printing in the console table. |

### Example

```java
Item item = new Item(1, "LOST", "Wallet", "Bags", "01-09-2026", "Library", "9876543210", "OPEN");

item.getId();        // returns 1
item.getName();      // returns "Wallet"
item.getStatus();    // returns "OPEN"
item.setStatus("RESOLVED");
item.getStatus();    // returns "RESOLVED"
item.toLine();       // returns "1|LOST|Wallet|Bags|01-09-2026|Library|9876543210|RESOLVED"
item.display();      // returns "1    Wallet               Bags         Library         01-09-2026   RESOLVED"
```

---

## 5. Validator.java

**What it does:** Checks if user input is valid.

**Full code:**

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

| Line | Code | What It Means |
|------|------|---------------|
| 1 | `public class Validator {` | A utility class with helper methods. |
| 2 | `public static boolean isNotEmpty(String text)` | A method that returns `true` or `false`. `static` means you call it like `Validator.isNotEmpty(x)` without creating an object. |
| 3 | `return text != null && !text.trim().isEmpty();` | Returns true if: text is not null AND text is not empty after trimming spaces. |
| 6 | `public static boolean isValidDate(String text)` | Checks if a date string matches the pattern `dd-mm-yyyy`. |
| 7 | `if (text == null) return false;` | If text is null, it is not valid. |
| 8 | `return text.matches("\\d{2}-\\d{2}-\\d{4}");` | Uses regex (pattern matching) to check format. |

### Regex Explained: `\\d{2}-\\d{2}-\\d{4}`

| Part | Meaning |
|------|---------|
| `\\d` | Any digit (0-9) |
| `{2}` | Exactly 2 of the previous |
| `-` | Literal hyphen |
| `{4}` | Exactly 4 digits |

So `01-09-2026` matches (2 digits, hyphen, 2 digits, hyphen, 4 digits).
But `1/9/26` does NOT match (wrong separators, wrong lengths).

### Examples

```java
Validator.isNotEmpty("Wallet");    // true
Validator.isNotEmpty("");          // false
Validator.isNotEmpty("   ");      // false (only spaces)
Validator.isNotEmpty(null);        // false

Validator.isValidDate("01-09-2026"); // true
Validator.isValidDate("1/9/26");     // false
Validator.isValidDate("abc");        // false
Validator.isValidDate(null);         // false
```

---

## 6. StorageManager.java

**What it does:** Reads and writes items to/from the text file `data/items.txt`.

**Full code:**

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

#### The FILE constant

```java
static final String FILE = "data" + File.separator + "items.txt";
```

- `static` - Belongs to the class, not an object
- `final` - Cannot be changed (it is a constant)
- `File.separator` - On Windows this is `\`, on Linux/Mac it is `/`. Makes it work on all systems.

#### The `load()` method (reads the file)

| Line | Code | What It Means |
|------|------|---------------|
| 9 | `List<Item> items = new ArrayList<>();` | Create an empty list to hold items. |
| 10 | `File f = new File(FILE);` | Create a File object pointing to `data/items.txt`. |
| 11 | `if (!f.exists()) return items;` | If the file does not exist, return empty list (no crash). |
| 13 | `try (BufferedReader br = ...)` | Open the file for reading. `try-with-resources` auto-closes it. |
| 15 | `while ((line = br.readLine()) != null)` | Read one line at a time until end of file. |
| 17 | `if (line.isEmpty()) continue;` | Skip blank lines. |
| 18 | `String[] p = line.split("\\|");` | Split the line by `|` into an array of 8 parts. |
| 19 | `if (p.length != 8) continue;` | Skip lines that are not properly formatted. |
| 21-23 | `items.add(new Item(...))` | Create an Item object from the parts and add to list. |
| 25 | `catch (NumberFormatException e) { }` | If the ID is not a number, skip that line silently. |

**How splitting works:**

```
Line:    "1|LOST|Wallet|Bags|01-09-2026|Library|987|OPEN"
Split:   ["1", "LOST", "Wallet", "Bags", "01-09-2026", "Library", "987", "OPEN"]
Index:    [0]   [1]     [2]      [3]       [4]           [5]       [6]    [7]
```

#### The `append()` method (adds one item)

```java
public void append(Item item) {
    ensureDir();                    // make sure data/ folder exists
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
        bw.write(item.toLine());    // write "1|LOST|Wallet|..."
        bw.newLine();               // add a new line after it
    } catch (IOException e) {
        e.printStackTrace();        // print error if something goes wrong
    }
}
```

- `FileWriter(FILE, true)` - The `true` means append mode (add to end of file, do not erase).

#### The `rewrite()` method (overwrites entire file)

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

- `FileWriter(FILE, false)` - The `false` means overwrite mode (erase file first).
- Used when claiming an item (status changes, so we rewrite everything).

#### The `ensureDir()` method

```java
private void ensureDir() {
    File dir = new File("data");
    if (!dir.exists()) dir.mkdirs();
}
```

- Creates the `data/` folder if it does not exist.
- `mkdirs()` creates the folder and any parent folders if needed.

---

## 7. ItemService.java

**What it does:** The brain of the app. Handles all operations (post, search, claim, filter).

**Full code:**

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

#### Constants (lines 6-8)

```java
static final String TYPE_LOST = "LOST";
static final String TYPE_FOUND = "FOUND";
static final String STATUS_RESOLVED = "RESOLVED";
```

These are fixed values used throughout the app. Instead of typing `"LOST"` everywhere, we use `TYPE_LOST`. If we ever need to change it, we change it in one place.

#### The `postItem()` method (creates a new item)

```java
public int postItem(String type, String name, String category,
                    String date, String location, String contact) {
    List<Item> items = storage.load();                              // 1. Load all items from file
    int maxId = items.stream().mapToInt(Item::getId).max().orElse(0); // 2. Find the biggest ID
    int newId = maxId + 1;                                          // 3. New ID = biggest + 1
    Item item = new Item(newId, type, name, category, date, location, contact, "OPEN"); // 4. Create item
    storage.append(item);                                           // 5. Save to file
    return newId;                                                   // 6. Return the new ID
}
```

**Java Streams explained (line 14):**

```java
items.stream()                    // Convert list to a stream (like a pipeline)
     .mapToInt(Item::getId)       // Extract the ID from each item (converts to int stream)
     .max()                       // Find the maximum value
     .orElse(0);                  // If list is empty, use 0 as default
```

**Example:**
```
Items in file: [Item(id=3), Item(id=7), Item(id=15)]
stream -> [3, 7, 15]
max -> 15
newId -> 16
```

#### The `searchByKeyword()` method

```java
public List<Item> searchByKeyword(String word) {
    String lower = word.toLowerCase();         // Make search term lowercase
    return storage.load().stream()             // Load all items as a stream
            .filter(i ->                       // Keep items that match:
                    i.getName().toLowerCase().contains(lower)       // name contains keyword
                    || i.getLocation().toLowerCase().contains(lower) // OR location contains it
                    || i.getCategory().toLowerCase().contains(lower)) // OR category contains it
            .collect(Collectors.toList());     // Put matching items back into a list
}
```

**How `filter` works:** Think of it like a sieve. Items go in, only matching ones come out.

```
Search: "ring"
Items:  ["Earring", "Ring Light", "Notebook", "Keyring"]
Filter: ["Earring", "Ring Light", "Keyring"]  (all contain "ring")
```

**Why `.toLowerCase()`?** So searching "RING" finds "Earring" too (case-insensitive).

#### The `searchByCategory()` method

```java
public List<Item> searchByCategory(String cat) {
    return storage.load().stream()
            .filter(i -> i.getCategory().equalsIgnoreCase(cat)) // Exact match (case-insensitive)
            .collect(Collectors.toList());
}
```

#### The `getItemById()` method

```java
public Item getItemById(int id) {
    return storage.load().stream()
            .filter(i -> i.getId() == id)   // Find item with matching ID
            .findFirst()                     // Get the first match
            .orElse(null);                   // Return null if nothing found
}
```

#### The `claimItem()` method (marks item as resolved)

```java
public boolean claimItem(int id) {
    List<Item> items = storage.load();           // Load all items
    for (Item i : items) {                       // Loop through each item
        if (i.getId() == id                      // Found the right ID
                && !i.getStatus().equals(STATUS_RESOLVED)) { // And it is not already resolved
            i.setStatus(STATUS_RESOLVED);        // Change status to RESOLVED
            storage.rewrite(items);              // Save all items back to file
            return true;                         // Success
        }
    }
    return false;  // Item not found or already resolved
}
```

#### The filter methods (`getLostItems`, `getFoundItems`, `getResolvedItems`)

All three work the same way - load all items, filter by type or status:

```java
public List<Item> getLostItems() {
    return storage.load().stream()
            .filter(i -> i.getType().equals("LOST"))  // Keep only LOST items
            .collect(Collectors.toList());
}
```

---

## 8. Main.java

**What it does:** The user interface. Shows a menu, takes input, calls ItemService.

**Full code:**

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

#### The two static variables (lines 5-6)

```java
static ItemService service = new ItemService();
static Scanner sc = new Scanner(System.in);
```

- `service` - An instance of ItemService that does all the work
- `sc` - A Scanner that reads what you type in the console
- `static` - Means these belong to the class and can be used in all static methods

#### The `main()` method (lines 8-46)

```java
public static void main(String[] args) {
```

This is where Java starts running. Every Java program needs this.

**The menu loop:**

```java
while (true) {   // Run forever (until user picks Exit)
    // ... show menu ...
    // ... get input ...
    // ... do something ...
}
```

**Reading input:**

```java
String ch = sc.nextLine().trim();
```

- `sc.nextLine()` - Reads a line of text from the user
- `.trim()` - Removes spaces from start and end

**The if-else chain:**

```java
if (ch.equals("1")) postItem("LOST");         // User picked 1
else if (ch.equals("2")) postItem("FOUND");   // User picked 2
else if (ch.equals("3")) ...                  // etc.
```

**Exit confirmation:**

```java
else if (ch.equals("8")) {
    System.out.print("Exit? (y/n): ");
    if (sc.nextLine().trim().toLowerCase().equals("y")) break;
}
```

- `.toLowerCase()` - Converts to lowercase so "Y" and "y" both work
- `.equals("y")` - Checks if user typed "y"
- `break` - Exits the while loop (ends the program)

#### The `postItem()` method (lines 48-77)

```java
static void postItem(String type) {
    System.out.println("\n--- Post " + type + " Item ---");

    System.out.print("Name: ");
    String name = sc.nextLine().trim();
    // ... same for category, date, location, contact ...

    // Validation
    if (name.isEmpty() || loc.isEmpty() || contact.isEmpty()) {
        System.out.println("Name, location and contact are required.");
        return;  // Stop here, do not save
    }
    if (!Validator.isValidDate(date)) {
        System.out.println("Bad date format. Use dd-mm-yyyy.");
        return;
    }

    int id = service.postItem(type, name, cat, date, loc, contact);
    System.out.println("Saved. ID: " + id);
}
```

#### The `showItems()` method (lines 79-91)

```java
static void showItems(String title, List<Item> items) {
    System.out.println("\n--- " + title + " ---");
    if (items.isEmpty()) {
        System.out.println("Nothing here.");
        return;
    }
    // Print table header
    System.out.printf("%-4s %-20s %-12s %-15s %-12s %-9s\n",
            "ID", "NAME", "CATEGORY", "LOCATION", "DATE", "STATUS");
    System.out.println("--------------------------------------------------------------------");
    // Print each item
    for (Item i : items) {
        System.out.println(i.display());
    }
    System.out.println("Total: " + items.size());
}
```

**`printf` format codes:**

| Code | Meaning |
|------|---------|
| `%s` | String |
| `%d` | Integer |
| `%-4s` | String, left-aligned, 4 characters wide |
| `%-20s` | String, left-aligned, 20 characters wide |

#### The `search()` method (lines 93-117)

Two options: search by keyword (checks name, location, category) or by category (exact match).

#### The `claim()` method (lines 120-151)

```java
static void claim() {
    System.out.print("\nItem ID to claim: ");
    String input = sc.nextLine().trim();

    int id;
    try {
        id = Integer.parseInt(input);     // Convert "16" to 16
    } catch (NumberFormatException e) {   // If conversion fails
        System.out.println("Enter a valid number.");
        return;
    }

    Item item = service.getItemById(id);
    if (item == null) {                   // No item with that ID
        System.out.println("No item with ID " + id);
        return;
    }
    if (item.getStatus().equals("RESOLVED")) {  // Already claimed
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
```

---

## 9. Tests.java

**What it does:** Runs checks to make sure everything works correctly.

**Full code:**

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

| Test | What It Checks |
|------|----------------|
| 2a | File has exactly 15 records |
| 2b-d | Correct counts: 8 lost, 7 found, 2 resolved |
| 3 | Filter methods return correct counts |
| 4a | New item gets ID 16 (next after 15) |
| 4b | File now has 16 lines |
| 5a | "ring" and "RING" find the same results |
| 5b | "zzz" finds nothing |
| 6a | Claiming an open item works |
| 6b | Claiming already-resolved item fails |
| 6c | Status changed to RESOLVED |
| 6d | Resolved count is now 3 |
| 7a-c | Date validation works correctly |
| 8 | Missing file returns empty list (no crash) |
| 9 | Data restored to original state |

---

## 10. Data File

**File:** `data/items.txt`

Each line is one item. Fields are separated by `|`.

```
id|type|name|category|date|location|contact|status
```

**Example lines:**

```
1|LOST|pl|book|20-12-2006|tinsukia|210016664|OPEN
2|FOUND|Keys|Keys|01-09-2026|Library|9876543210|OPEN
6|LOST|Wallet|Bags|15-08-2026|Cafeteria|1234567890|RESOLVED
```

**Fields:**

| Field | Example | Meaning |
|-------|---------|---------|
| id | `1` | Unique number |
| type | `LOST` or `FOUND` | What happened |
| name | `Wallet` | Description |
| category | `Bags` | Type of item |
| date | `01-09-2026` | When lost/found |
| location | `Library` | Where |
| contact | `9876543210` | Phone number |
| status | `OPEN` or `RESOLVED` | Current state |

---

## 11. How to Run

**Compile:**

```bash
javac -d bin src/*.java
```

**Run the app:**

```bash
java -cp bin Main
```

**Run the tests:**

```bash
java -cp bin Tests
```

---

## Quick Reference: How Classes Talk to Each Other

```
Main.java                 You type here
    |
    v
ItemService.java          Decides what to do
    |
    +--> StorageManager.java   Reads/writes the file
    |         |
    |         v
    |    data/items.txt        The data
    |
    +--> Item.java             The data model
```

**When you POST an item:**
1. Main asks for details
2. ItemService finds the next ID
3. ItemService creates an Item
4. StorageManager appends it to the file

**When you VIEW items:**
1. Main asks what to show
2. ItemService asks StorageManager to load the file
3. StorageManager reads each line and creates Item objects
4. ItemService filters the list (lost/found/resolved)
5. Main displays the table

**When you CLAIM an item:**
1. Main asks for the ID
2. ItemService finds the item
3. ItemService changes status to RESOLVED
4. StorageManager rewrites the entire file with updated data
