# Part 1: Introduction, Problem and Objectives

**Student 1**

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Background](#2-background)
3. [Problem Statement](#3-problem-statement)
4. [Why This Problem Matters](#4-why-this-problem-matters)
5. [Objectives](#5-objectives)
6. [Scope](#6-scope)
7. [Project Overview](#7-project-overview)
8. [Features](#8-features)
9. [Technology Stack](#9-technology-stack)
10. [Target Users](#10-target-users)
11. [Project Structure](#11-project-structure)
12. [Team Roles](#12-team-roles)

---

## 1. Introduction

### What is LAFSys?

LAFSys stands for **Lost And Found System**. It is a computer program (application) designed for college campuses to help people report and find lost items.

Imagine this: You are walking across campus and you find a wallet on the ground. What do you do? You could take it to the admin office, but the owner might not know to look there. Or you could post about it on social media, but it might get buried under other posts.

LAFSys solves this by creating a **central place** where anyone on campus can:
- Report a lost item ("I lost my wallet near the library")
- Report a found item ("I found a wallet near the library")
- Search for their lost item
- Claim an item when found

### What Does the Application Look Like?

LAFSys is a **console application**. This means it runs in a text window (not a graphical window with buttons and images). You type numbers to choose options and type text to enter information.

**Example of what you see when you run it:**

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

Lost: 3 | Found: 2 | Resolved: 1

>
```

You type `1` to post a lost item, `2` to post a found item, and so on.

---

## 2. Background

### The Problem in Real Life

Every college campus deals with lost and found items daily. Students lose:
- **Wallets** and **ID cards** - Most critical, contains money and identification
- **Electronics** - Phones, USB drives, earbuds, calculators
- **Books** and **notebooks** - Expensive to replace, contain personal notes
- **Keys** - Dorm keys, bike keys, car keys
- **Bags** and **backpacks** - Sometimes containing laptops
- **Clothes** - Jackets, scarves, hats

### Current Ways Campuses Handle This

Most campuses use one of these methods:

| Method | How It Works | Problem |
|--------|-------------|---------|
| **Physical board** | Paper notes pinned on a wall | Not searchable, gets messy, only visible in one location |
| **Admin office** | Items turned into office | Centralized but slow, requires physical visit |
| **Social media** | Posts on college Facebook groups | Gets buried, no organization, not permanent |
| **WhatsApp groups** | Messages in class groups | Temporary, not searchable, only reaches some people |
| **Word of mouth** | Tell friends "I lost my wallet" | Limited reach, unreliable |

### Why These Methods Fail

1. **No central location** - Information is scattered across different places
2. **Not searchable** - You cannot search a physical board efficiently
3. **No persistence** - Social media posts disappear, WhatsApp messages get deleted
4. **No organization** - Items are not categorized (electronics, books, etc.)
5. **No tracking** - Once an item is found, there is no way to mark it as resolved
6. **Limited reach** - Only people who see the post know about the item

---

## 3. Problem Statement

### The Core Problem

College campuses lack a **centralized, digital system** for managing lost and found items. This leads to:

1. **Lost items go unclaimed** - People do not know where to look or report
2. **Found items pile up** - No system to match found items with lost reports
3. **Time wasted** - Students walk around asking if anyone has seen their item
4. **Duplicate reports** - Multiple people report the same item in different places
5. **No record keeping** - Once information is lost, it cannot be recovered

### Formal Problem Statement

> "There is no efficient, centralized, and accessible system for college students to report lost items, report found items, search for items by keyword or category, and claim items when they are recovered. This results in lost items remaining unclaimed and found items remaining unreturned to their owners."

### Example Scenario

**Without LAFSys:**
```
Rahul loses his wallet in the library on Monday.
He asks the librarian - "Has anyone turned in a wallet?" - No.
He posts on the college Facebook group - Gets a few replies, nothing useful.
He asks friends to keep an eye out.
On Wednesday, Priya finds the wallet in the library.
She does not know Rahul lost it. She turns it in at the admin office.
Rahul never checks the admin office. The wallet sits there for weeks.
```

**With LAFSys:**
```
Rahul loses his wallet in the library on Monday.
He opens LAFSys and posts: "LOST - Wallet - Bags - 01-09-2026 - Library - 9876543210"
The item is saved with ID 15 and status OPEN.

On Wednesday, Priya finds the wallet.
She opens LAFSys and searches for "wallet" in Lost Items.
She sees Rahul's post. She calls him using the contact number.
Rahul claims the item. Status changes to RESOLVED.
Problem solved in minutes.
```

---

## 4. Why This Problem Matters

### Impact on Students

| Area | Impact |
|------|--------|
| **Financial** | Lost wallets, phones, laptops cost thousands to replace |
| **Academic** | Lost notes, textbooks, USB drives with assignments |
| **Safety** | Lost ID cards can be misused, lost keys create security issues |
| **Emotional** | Stress and anxiety from losing important personal items |
| **Time** | Hours spent searching, asking around, filing reports |

### Real Statistics (General)

- Average student loses 2-3 items per semester
- 30% of lost items on campuses are never recovered
- Most students do not report lost items because there is no easy way

---

## 5. Objectives

### Primary Objectives

| # | Objective | Why It Matters |
|---|-----------|---------------|
| 1 | Create a centralized system for reporting lost items | One place to report, not scattered across social media |
| 2 | Create a centralized system for reporting found items | Found items are recorded systematically |
| 3 | Enable search by keyword | Quickly find items by name, location, or category |
| 4 | Enable search by category | Browse items by type (electronics, books, etc.) |
| 5 | Enable claiming items | Mark items as resolved when owners are found |
| 6 | Persist data in a file | Information survives between sessions |
| 7 | Provide a simple console interface | Easy to use without technical knowledge |

### Secondary Objectives

| # | Objective | Benefit |
|---|-----------|---------|
| 8 | Validate user input | Prevent bad data from entering the system |
| 9 | Auto-generate item IDs | Users do not need to create IDs manually |
| 10 | Display status summary | See counts of lost, found, and resolved items |
| 11 | Handle errors gracefully | App does not crash on bad input or missing files |

### What This Project is NOT

- NOT a website (it is a console application)
- NOT a mobile app
- NOT a database-backed system (uses text files)
- NOT a multi-user networked system (runs on one computer)
- NOT a replacement for campus lost and found offices (complementary tool)

---

## 6. Scope

### What IS Included

```
LAFSys Scope:
  +-- Post lost items (with details: name, category, date, location, contact)
  +-- Post found items (same details)
  +-- View all lost items in a table
  +-- View all found items in a table
  +-- Search items by keyword (searches name, location, category)
  +-- Search items by category (exact match)
  +-- Claim items (change status from OPEN to RESOLVED)
  +-- View resolved items
  +-- Data persistence (saved to text file)
  +-- Input validation (required fields, date format)
  +-- Status summary (counts on menu)
```

### What is NOT Included

```
LAFSys does NOT include:
  -- User accounts or login
  -- Graphical interface (buttons, images)
  -- Database (MySQL, SQLite, etc.)
  -- Email or SMS notifications
  -- Photo upload for items
  -- Multi-user support (network)
  -- Admin panel
  -- Reporting or analytics
  -- Mobile app version
```

---

## 7. Project Overview

### How the System Works

```
                    LAFSys System Overview

  +------------------+     +------------------+     +------------------+
  |                  |     |                  |     |                  |
  |    Main.java     |---->|  ItemService.java|---->| StorageManager   |
  |  (User Interface)|     |  (Business Logic)|     | (File I/O)       |
  |                  |     |                  |     |                  |
  +------------------+     +------------------+     +------------------+
          |                        |                        |
          v                        v                        v
   User types in          Decisions are made         Data is read
   the console           (search, filter, claim)    from/written to
                                                    data/items.txt
```

### Data Flow

**When posting a lost item:**
1. User chooses option 1 (Post Lost Item)
2. System asks for: Name, Category, Date, Location, Contact
3. User types in the information
4. System validates the input (checks required fields, date format)
5. System generates a unique ID (next number in sequence)
6. System saves the item to `data/items.txt`
7. System shows confirmation with the new ID

**When searching:**
1. User chooses option 5 (Search Items)
2. User picks keyword or category search
3. System loads all items from file
4. System filters items matching the search
5. System displays matching items in a table

**When claiming:**
1. User chooses option 6 (Claim Item)
2. User enters the item ID
3. System finds the item and shows details
4. User confirms with y/n
5. System changes status to RESOLVED
6. System saves all items back to file

---

## 8. Features

### Feature Details

#### Feature 1: Post Lost Item
- User enters item name, category, date, location, and contact number
- System validates required fields (name, location, contact cannot be empty)
- System validates date format (must be dd-mm-yyyy)
- System auto-generates a unique ID
- Item is saved with status "OPEN"

#### Feature 2: Post Found Item
- Same as posting a lost item, but with type "FOUND"
- Useful when someone finds an item and wants to announce it

#### Feature 3: View Lost Items
- Displays all items with type "LOST" in a formatted table
- Shows: ID, Name, Category, Location, Date, Status
- Shows total count at the bottom

#### Feature 4: View Found Items
- Same as view lost, but shows only "FOUND" items

#### Feature 5: Search Items
- **Keyword search:** Searches across name, location, and category (case-insensitive)
- **Category search:** Filters by exact category match
- Shows results in a table with count

#### Feature 6: Claim Item
- User enters an item ID
- System shows item details and asks for confirmation
- Changes status from "OPEN" to "RESOLVED"
- Prevents claiming already-resolved items

#### Feature 7: View Resolved
- Shows all items that have been claimed
- Useful for tracking what has been returned

#### Feature 8: Exit
- Asks for confirmation (y/n) before exiting
- Prevents accidental exits

---

## 9. Technology Stack

### Why Java?

| Factor | Explanation |
|--------|-------------|
| **Object-Oriented** | Java uses classes and objects, which is how real-world things are modeled |
| **Easy to learn** | Simple syntax, good for beginners |
| **Platform-independent** | "Write once, run anywhere" - works on Windows, Mac, Linux |
| **Built-in libraries** | File I/O, collections, and other tools come built-in |
| **No external dependencies** | The entire project uses only Java's standard library |

### Components Used

| Component | What It Is | How It Is Used |
|-----------|-----------|---------------|
| **Scanner** | Java class for reading input | Reads what the user types in the console |
| **List** | Java collection (dynamic array) | Stores lists of items |
| **ArrayList** | Implementation of List | Creates resizable lists |
| **BufferedReader** | Reads text files efficiently | Reads items from data/items.txt |
| **BufferedWriter** | Writes text files efficiently | Writes items to data/items.txt |
| **File** | Represents a file or directory | Checks if files exist, creates directories |
| **Streams** | Java 8 feature for processing collections | Filters and searches items |
| **String.format** | Formats text with alignment | Creates formatted tables in the console |

### Why Not a Database?

For a campus-scale application with hundreds (not millions) of items, a text file is:
- Simpler to implement
- Easier to understand
- No setup required (no database server)
- Portable (just copy the file)
- Sufficient for the project scope

---

## 10. Target Users

### Primary Users

| User | How They Use LAFSys |
|------|-------------------|
| **Students who lost items** | Post lost item, search for found items, claim their items |
| **Students who found items** | Post found item so the owner can find it |
| **Campus staff** | Help students use the system, turn in found items |

### User Requirements

- Basic computer knowledge (typing, using a keyboard)
- Access to a computer with Java installed
- Knowledge of where they lost/found the item
- A contact number for communication

---

## 11. Project Structure

```
LAFSys/
|
|-- src/                    Source Code
|   |-- Main.java           Entry point and user interface (152 lines)
|   |-- Item.java           Data model for items (33 lines)
|   |-- ItemService.java    Business logic (75 lines)
|   |-- StorageManager.java File reading/writing (57 lines)
|   |-- Validator.java      Input validation (10 lines)
|   |-- Tests.java          Test suite (76 lines)
|
|-- data/                   Data Storage
|   |-- items.txt           Persistent item database
|
|-- docs/                   Documentation
|   |-- FULL_EXPLANATION.md Complete code explanation
|   |-- code-docs/          Individual file documentation
|
|-- bin/                    Compiled Java files (.class)
|
|-- README.md               Project overview
|-- LICENSE                 Apache 2.0 license
```

---

## 12. Team Roles

### Division of Work

| Student | Responsibility | Files Covered |
|---------|---------------|---------------|
| **Student 1** | Introduction, Problem, Objectives | This document |
| **Student 2** | Class Design and Java Concepts | Class diagrams, OOP concepts, Java features used |
| **Student 3** | Implementation and Code Explanation | Line-by-line code walkthrough |
| **Student 4** | Live Demonstration, Testing, Conclusion | Running the app, test results, summary |

### How the Team Collaborates

1. **Student 1** defines WHAT the system does and WHY
2. **Student 2** designs HOW the classes are structured and what Java concepts are used
3. **Student 3** writes and explains the actual CODE
4. **Student 4** demonstrates the system works and summarizes results

---

## Summary

LAFSys is a simple but effective solution to a real problem on college campuses. It provides a centralized, digital system for managing lost and found items. The project uses Java's object-oriented features, file I/O capabilities, and stream processing to create a clean, maintainable application. While it is a console application (not a full web or mobile app), it demonstrates fundamental programming concepts and solves a practical problem.
