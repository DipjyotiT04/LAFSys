import java.util.List;
import java.util.Scanner;

public class Main {
    private static ItemService service = new ItemService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    CAMPUS LOST & FOUND BOARD");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            showMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": postLost(); break;
                case "2": postFound(); break;
                case "3": viewLost(); break;
                case "4": viewFound(); break;
                case "5": searchItems(); break;
                case "6": claimItem(); break;
                case "7": viewResolved(); break;
                case "8":
                    System.out.print("Are you sure? (yes/no): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                        System.out.println("Goodbye!");
                        running = false;
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void showMenu() {
        System.out.println("----------------------------------------");
        System.out.println("1. Post Lost Item");
        System.out.println("2. Post Found Item");
        System.out.println("3. View Lost Items");
        System.out.println("4. View Found Items");
        System.out.println("5. Search Items");
        System.out.println("6. Claim Item");
        System.out.println("7. View Resolved Items");
        System.out.println("8. Exit");
        System.out.println("----------------------------------------");
        updateStatus();
        System.out.print("Choose an option: ");
    }

    private static void updateStatus() {
        int lost = service.getLostItems().size();
        int found = service.getFoundItems().size();
        int resolved = service.getResolvedItems().size();
        System.out.println("Lost: " + lost + " | Found: " + found + " | Resolved: " + resolved);
    }

    private static void printHeader() {
        System.out.println(String.format("%-5s %-24s %-12s %-20s %-12s %-9s",
                "ID", "NAME", "CATEGORY", "LOCATION", "DATE", "STATUS"));
        System.out.println("------------------------------------------------------------------");
    }

    private static void printItems(List<Item> items) {
        if (items.isEmpty()) {
            System.out.println("  No items to show.");
            return;
        }
        printHeader();
        for (Item item : items) {
            System.out.println(item.getDisplayText());
        }
        System.out.println("Records found: " + items.size());
    }

    private static void postLost() {
        postItem(ItemService.TYPE_LOST);
    }

    private static void postFound() {
        postItem(ItemService.TYPE_FOUND);
    }

    private static void postItem(String type) {
        System.out.println("--- Post " + type + " Item ---");

        System.out.print("Item Name: ");
        String name = scanner.nextLine().trim();

        System.out.println("Categories: Electronics, Books, ID Card, Bags, Keys, Clothes, Other");
        System.out.print("Category: ");
        String category = scanner.nextLine().trim();

        System.out.print("Date (dd-mm-yyyy): ");
        String date = scanner.nextLine().trim();

        System.out.print("Location: ");
        String location = scanner.nextLine().trim();

        System.out.print("Contact Number: ");
        String contact = scanner.nextLine().trim();

        if (!Validator.isNotEmpty(name) || !Validator.isNotEmpty(location) || !Validator.isNotEmpty(contact)) {
            System.out.println("ERROR: Name, Location, and Contact cannot be empty.");
            return;
        }
        if (!Validator.isValidDate(date)) {
            System.out.println("ERROR: Invalid date. Use dd-mm-yyyy format.");
            return;
        }

        int id = service.postItem(type, name, category, date, location, contact);
        System.out.println("Saved. Your item id is " + id + ". Keep it safe for claiming.");
    }

    private static void viewLost() {
        System.out.println("--- LOST ITEMS ---");
        printItems(service.getLostItems());
    }

    private static void viewFound() {
        System.out.println("--- FOUND ITEMS ---");
        printItems(service.getFoundItems());
    }

    private static void viewResolved() {
        System.out.println("--- RESOLVED ITEMS ---");
        printItems(service.getResolvedItems());
    }

    private static void searchItems() {
        System.out.println("--- Search Items ---");
        System.out.println("1. Search by keyword");
        System.out.println("2. Search by category");
        System.out.print("Choose: ");
        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            System.out.print("Enter keyword: ");
            String keyword = scanner.nextLine().trim();
            if (keyword.isEmpty()) {
                System.out.println("ERROR: Type a keyword first.");
                return;
            }
            List<Item> results = service.searchByKeyword(keyword);
            System.out.println("SEARCH RESULTS FOR \"" + keyword.toUpperCase() + "\" (" + results.size() + " FOUND)");
            printItems(results);
        } else if (choice.equals("2")) {
            System.out.println("Categories: Electronics, Books, ID Card, Bags, Keys, Clothes, Other");
            System.out.print("Enter category: ");
            String category = scanner.nextLine().trim();
            List<Item> results = service.searchByCategory(category);
            System.out.println("CATEGORY: " + category.toUpperCase() + " (" + results.size() + " FOUND)");
            printItems(results);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void claimItem() {
        System.out.println("--- Claim Item ---");
        System.out.print("Enter item ID to claim: ");
        String input = scanner.nextLine().trim();

        int id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Please enter a valid number.");
            return;
        }

        Item item = service.getItemById(id);
        if (item == null) {
            System.out.println("No item with id " + id);
            return;
        }
        if (item.getStatus().equals(ItemService.STATUS_RESOLVED)) {
            System.out.println("Item " + id + " is already RESOLVED.");
            return;
        }

        System.out.println("Item: " + item.getDisplayText());
        System.out.println("Contact: " + item.getContact());
        System.out.print("Mark as RESOLVED? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        if (confirm.equalsIgnoreCase("yes")) {
            boolean success = service.claimItem(id);
            if (success) {
                System.out.println("Item " + id + " claimed successfully!");
            }
        } else {
            System.out.println("Claim cancelled.");
        }
    }
}
