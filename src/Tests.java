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
        check("5a: case-insensitive", svc.searchByKeyword("ring").size() == svc.searchByKeyword("RING").size());
        check("5b: no match", svc.searchByKeyword("zzz").size() == 0);

        // Test 6: claim
        check("6a: claim OK", svc.claimItem(16));
        check("6b: already resolved", !svc.claimItem(16));
        Item claimed = svc.getItemById(16);
        check("6c: status RESOLVED", claimed != null && claimed.getStatus().equals("RESOLVED"));
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
