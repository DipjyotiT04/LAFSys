# Item.java - Data Model

**Path:** `src/Item.java`

Simple POJO for lost-and-found items.

## Fields

- `id` (int) - Unique ID
- `type` - LOST or FOUND
- `name` - Item description
- `category` - Electronics, Books, ID Card, Bags, Keys, Clothes, Other
- `date` - dd-mm-yyyy format
- `location` - Where lost/found
- `contact` - Phone number
- `status` - OPEN or RESOLVED

## Methods

- `toLine()` - Serialize to `|`-delimited string
- `display()` - Format for console table
- Getters and `setStatus()`
