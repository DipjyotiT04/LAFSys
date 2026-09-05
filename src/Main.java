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
            System.out.println("\nLost: " + lost.size() + " | Found: " + found.size() + " | Resolved: " + resolved.size());
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
        System.out.printf("%-4s %-20s %-12s %-15s %-12s %-9s\n", "ID", "NAME", "CATEGORY", "LOCATION", "DATE", "STATUS");
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
