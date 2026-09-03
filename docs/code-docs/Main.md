# Main.java - Application Entry Point

**Path:** `src/Main.java`
**Lines:** 201
**Purpose:** Console-based menu-driven UI and application entry point

---

## Overview

`Main.java` is the entry point of the LAFSys application. It implements a text-based menu system using `java.util.Scanner` for user input. The class orchestrates all user interactions and delegates business logic to `ItemService` and validation to `Validator`.

---

## Class Structure

```java
import java.util.List;
import java.util.Scanner;

public class Main {
    private static ItemService service = new ItemService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) { ... }
    private static void showMenu() { ... }
    private static void updateStatus() { ... }
    private static void printHeader() { ... }
    private static void printItems(List<Item> items) { ... }
    private static void postLost() { ... }
    private static void postFound() { ... }
    private static void postItem(String type) { ... }
    private static void viewLost() { ... }
    private static void viewFound() { ... }
    private static void viewResolved() { ... }
    private static void searchItems() { ... }
    private static void claimItem() { ... }
}
```

---

## Dependencies

| Dependency | Type | Purpose |
|------------|------|---------|
| `ItemService` | Class | Business logic for item operations |
| `Item` | Class | Data model for display |
| `Validator` | Class | Input validation |
| `java.util.Scanner` | JDK | User input |
| `java.util.List` | JDK | Collection handling |

---

## Methods Breakdown

### `main(String[] args)`
**Line:** 8 | **Purpose:** Application entry point

- Prints the welcome banner
- Runs the main menu loop (`while (running)`)
- Reads user choice and dispatches to appropriate method via `switch`
- Option 8 asks for confirmation before exit
- Closes the scanner on exit

### `showMenu()`
**Line:** 41 | **Purpose:** Display the menu options

Prints 8 menu options:
1. Post Lost Item
2. Post Found Item
3. View Lost Items
4. View Found Items
5. Search Items
6. Claim Item
7. View Resolved Items
8. Exit

Also calls `updateStatus()` to show current counts.

### `updateStatus()`
**Line:** 56 | **Purpose:** Show summary statistics

Displays the count of Lost, Found, and Resolved items by calling the respective `ItemService` methods. Shown on every menu refresh.

### `printHeader()`
**Line:** 63 | **Purpose:** Print formatted table header

Outputs a formatted header row with columns: ID, NAME, CATEGORY, LOCATION, DATE, STATUS using `String.format` with fixed widths.

### `printItems(List<Item> items)`
**Line:** 69 | **Purpose:** Display items in a formatted table

- If list is empty, prints "No items to show"
- Otherwise prints header + each item's `getDisplayText()`
- Shows total record count at the end

### `postLost()` / `postFound()`
**Lines:** 81, 85 | **Purpose:** Thin wrappers

Simply call `postItem()` with the appropriate type constant (`TYPE_LOST` or `TYPE_FOUND`).

### `postItem(String type)`
**Line:** 89 | **Purpose:** Collect and save a new item

Workflow:
1. Prompts for: Item Name, Category, Date, Location, Contact Number
2. Validates required fields using `Validator.isNotEmpty()`
3. Validates date format using `Validator.isValidDate()`
4. Calls `service.postItem()` to generate ID and persist
5. Prints confirmation with the new item ID

**Validation checks:**
- Name, Location, Contact must not be empty
- Date must match `dd-mm-yyyy` format

### `viewLost()` / `viewFound()` / `viewResolved()`
**Lines:** 121, 126, 131 | **Purpose:** Display filtered item lists

Each prints a section header and calls `printItems()` with the filtered list from `ItemService`.

### `searchItems()`
**Line:** 136 | **Purpose:** Search items by keyword or category

Sub-menu with two options:
1. **Search by keyword** - Calls `service.searchByKeyword()` (case-insensitive)
2. **Search by category** - Calls `service.searchByCategory()` (exact match)

Displays results with count.

### `claimItem()`
**Line:** 165 | **Purpose:** Mark an item as resolved

Workflow:
1. Prompts for item ID
2. Parses integer (handles `NumberFormatException`)
3. Looks up item via `service.getItemById()`
4. Checks if item exists and is not already resolved
5. Displays item details and contact
6. Asks for confirmation before marking as RESOLVED

---

## Menu Options Summary

| Option | Action | Method |
|--------|--------|--------|
| 1 | Post Lost Item | `postLost()` |
| 2 | Post Found Item | `postFound()` |
| 3 | View Lost Items | `viewLost()` |
| 4 | View Found Items | `viewFound()` |
| 5 | Search Items | `searchItems()` |
| 6 | Claim Item | `claimItem()` |
| 7 | View Resolved Items | `viewResolved()` |
| 8 | Exit | Exit loop |

---

## Usage Example

```
========================================
    CAMPUS LOST & FOUND BOARD
========================================
----------------------------------------
1. Post Lost Item
2. Post Found Item
...
----------------------------------------
Lost: 8 | Found: 7 | Resolved: 2
Choose an option: 1
--- Post LOST Item ---
Item Name: Wallet
Categories: Electronics, Books, ID Card, Bags, Keys, Clothes, Other
Category: Bags
Date (dd-mm-yyyy): 01-09-2026
Location: Library
Contact Number: 9876543210
Saved. Your item id is 16. Keep it safe for claiming.
```
