# Main.java - Entry Point

**Path:** `src/Main.java`

Console-based menu UI. Uses `y/n` for confirmations.

## Menu Options

1. Post Lost Item
2. Post Found Item
3. View Lost Items
4. View Found Items
5. Search Items (keyword or category)
6. Claim Item (mark resolved)
7. View Resolved
8. Exit

## Key Methods

- `main()` - Main loop
- `postItem(type)` - Collect input and save
- `showItems(title, items)` - Display table
- `search()` - Search by keyword or category
- `claim()` - Mark item resolved with y/n confirmation

## Usage

```
> 1
Name: Wallet
Category: Bags
Date: 01-09-2026
Location: Library
Contact: 9876543210
Saved. ID: 16
```
