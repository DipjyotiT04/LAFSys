# Validator.java - Input Validation Utility

**Path:** `src/Validator.java`
**Lines:** 10
**Purpose:** Static utility methods for input validation

---

## Overview

`Validator` is a minimal utility class with two static methods for validating user input. It checks for non-empty strings and valid date formats. The class has no state or dependencies.

---

## Class Structure

```java
public class Validator {
    public static boolean isNotEmpty(String text) { ... }
    public static boolean isValidDate(String text) { ... }
}
```

---

## Dependencies

**None.** Pure utility class with no imports.

---

## Methods Breakdown

### `isNotEmpty(String text)`
**Line:** 2 | **Purpose:** Check if a string is non-null and non-blank

```java
public static boolean isNotEmpty(String text) {
    return text != null && !text.trim().isEmpty();
}
```

**Logic:**
1. Returns `false` if `text` is `null`
2. Trims whitespace and checks if result is empty
3. Returns `true` only if string has content after trimming

**Examples:**
| Input | Result | Reason |
|-------|--------|--------|
| `"Wallet"` | `true` | Has content |
| `"  "` | `false` | Only whitespace |
| `""` | `false` | Empty string |
| `null` | `false` | Null reference |

**Used by:** `Main.postItem()` to validate Name, Location, and Contact fields.

---

### `isValidDate(String text)`
**Line:** 6 | **Purpose:** Validate date format as `dd-mm-yyyy`

```java
public static boolean isValidDate(String text) {
    if (text == null) return false;
    return text.matches("\\d{2}-\\d{2}-\\d{4}");
}
```

**Logic:**
1. Returns `false` if `text` is `null`
2. Uses regex pattern `\d{2}-\d{2}-\d{4}` to match format

**Regex Breakdown:**
| Pattern | Meaning |
|---------|---------|
| `\d{2}` | Exactly 2 digits (day or month) |
| `-` | Literal hyphen separator |
| `\d{4}` | Exactly 4 digits (year) |

**Examples:**
| Input | Result | Reason |
|-------|--------|--------|
| `"01-08-2026"` | `true` | Matches `dd-mm-yyyy` |
| `"20-12-2006"` | `true` | Matches `dd-mm-yyyy` |
| `"1/8/26"` | `false` | Wrong separators |
| `"abc"` | `false` | Not digits |
| `"01-08-26"` | `false` | 2-digit year |
| `"1-8-2026"` | `false` | 1-digit day/month |
| `null` | `false` | Null reference |

**Limitation:** The regex validates format only, not logical validity (e.g., `99-99-9999` would pass).

**Used by:** `Main.postItem()` to validate the date field.

---

## Usage in Application

```java
// In Main.postItem():
if (!Validator.isNotEmpty(name) || !Validator.isNotEmpty(location) || !Validator.isNotEmpty(contact)) {
    System.out.println("ERROR: Name, Location, and Contact cannot be empty.");
    return;
}
if (!Validator.isValidDate(date)) {
    System.out.println("ERROR: Invalid date. Use dd-mm-yyyy format.");
    return;
}
```

---

## Design Notes

1. **Static methods only** - No instantiation needed
2. **No dependencies** - Self-contained utility
3. **Format-only validation** - Date validation doesn't check day/month ranges
4. **Fail-fast approach** - Returns `false` for null inputs rather than throwing exceptions
