# LAFSys - Campus Lost and Found Board Management System

## Overview
LAFSys is a comprehensive Java console application designed to manage lost and found items on a college/university campus. The application provides an efficient platform for students and staff to post lost items, post found items, search for items, and claim resolved items.

## Project Information
- **Course:** MCA 1 – 2026 AUTUMN | Programming Through Java
- **University:** Assam Don Bosco University
- **Group:** Group 5
- **Project Type:** Console-based Java Application

## Team Members
| Name | Student ID | Role |
|------|-----------|------|
| VIBEK SHARMA | DC2026MCA0041 | Item Class + Post Lost Item Feature |
| DHIRAJ SHARMA | DC2026MCA0046 | Display Methods + Search Feature |
| NIMANGSHU ACHARJEE | DC2026MCA0045 | Post Found Item + Claim/Resolve Feature |
| DIPJYOTI TALUKDAR | DC2026MCA0044 | Main Class + Integration Lead |

## Features

### 1. Post a Lost Item
- Users can post details of lost items
- Includes: Title, Description, Category, Date Lost, Poster Name, Location
- Item is marked as "Active" when posted
- Unique item ID generated automatically

### 2. Post a Found Item
- Users can post details of found items
- Similar structure to lost items
- Item is marked as "Found" when posted
- Helps reunite items with owners

### 3. Display All Lost Items
- View all lost items in formatted table
- Shows: ID, Title, Category, Date, Status, Poster Name
- Helps users find their lost items
- Handles empty list gracefully

### 4. Display All Found Items
- View all found items in formatted table
- Same information as lost items
- Separated by status for clarity
- Easy browsing of found items

### 5. Search for Items
- **Search by Keyword:** Search through title and description
- **Search by Category:** Filter items by specific category
- Case-insensitive search
- Returns matching items or "No results" message
- Helps quickly locate specific items

### 6. Claim or Mark as Resolved
- Users can claim found items
- Changes item status from "Active" to "Claimed"
- Can mark items as "Resolved" when issue is settled
- Prevents duplicate claims
- Maintains item history

## Project Structure
