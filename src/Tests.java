import java.io.*;
import java.util.List;

public class Tests {
    static int passed = 0;
    static int failed = 0;

    static void check(String name, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + name);
            passed++;
        } else {
            System.out.println("FAIL: " + name);
            failed++;
        }
    }

    public static void main(String[] args) throws Exception {
        // Test 2: items.txt has 15 records: 8 LOST, 7 FOUND, 2 RESOLVED
        ItemService service = new ItemService();
        List<Item> all = service.getAllItems();
        check("Test 2a: 15 records in items.txt", all.size() == 15);

        List<Item> lost = service.getLostItems();
        List<Item> found = service.getFoundItems();
        List<Item> resolved = service.getResolvedItems();
        check("Test 2b: 8 LOST items", lost.size() == 8);
        check("Test 2c: 7 FOUND items", found.size() == 7);
        check("Test 2d: 2 RESOLVED items", resolved.size() == 2);

        // Test 3: filter counts
        check("Test 3: getLostItems()=8", service.getLostItems().size() == 8);
        check("Test 3: getFoundItems()=7", service.getFoundItems().size() == 7);
        check("Test 3: getResolvedItems()=2", service.getResolvedItems().size() == 2);

        // Test 4: postItem returns id 16, file has 16 lines
        int newId = service.postItem("LOST", "Test Pen", "Other", "10-08-2026", "Office", "1234567890");
        check("Test 4a: postItem returns id 16", newId == 16);
        List<Item> afterPost = service.getAllItems();
        check("Test 4b: file has 16 lines after post", afterPost.size() == 16);

        // Test 5: searchByKeyword case-insensitive
        List<Item> ringLower = service.searchByKeyword("ring");
        List<Item> ringUpper = service.searchByKeyword("RING");
        check("Test 5a: searchByKeyword case-insensitive", ringLower.size() == ringUpper.size());
        List<Item> zzz = service.searchByKeyword("zzz");
        check("Test 5b: searchByKeyword('zzz') finds none", zzz.size() == 0);

        // Test 6: claimItem
        boolean claim1 = service.claimItem(16);
        check("Test 6a: claimItem(16) returns true", claim1);
        boolean claim2 = service.claimItem(16);
        check("Test 6b: second claimItem(16) returns false", !claim2);
        Item claimed = service.getItemById(16);
        check("Test 6c: item 16 status is RESOLVED", claimed != null && claimed.getStatus().equals("RESOLVED"));
        int resolvedCount = service.getResolvedItems().size();
        check("Test 6d: resolved count becomes 3", resolvedCount == 3);

        // Test 7: isValidDate
        check("Test 7a: isValidDate('01-08-2026') true", Validator.isValidDate("01-08-2026"));
        check("Test 7b: isValidDate('1/8/26') false", !Validator.isValidDate("1/8/26"));
        check("Test 7c: isValidDate('abc') false", !Validator.isValidDate("abc"));

        // Test 8: rename items.txt, load returns empty list, no crash
        File dataFile = new File("data/items.txt");
        File backup = new File("data/items_backup.txt");
        dataFile.renameTo(backup);
        StorageManager sm = new StorageManager();
        List<Item> empty = sm.loadItems();
        check("Test 8: loading missing file returns empty list", empty.size() == 0);
        backup.renameTo(dataFile);

        // Test 9: restore data/items.txt to original 15 records
        // Rewrite file with original 15 records (remove the added test item)
        List<Item> finalList = service.getAllItems();
        // Remove the test item (id 16) by rewriting only items with id <= 15
        java.util.List<Item> original = new java.util.ArrayList<>();
        for (Item i : finalList) {
            if (i.getId() <= 15) original.add(i);
        }
        // Restore original statuses for items 6 and 10 (they were RESOLVED originally)
        for (Item i : original) {
            if (i.getId() == 6 || i.getId() == 10) {
                i.setStatus("RESOLVED");
            }
        }
        sm.rewriteAll(original);
        List<Item> restored = service.getAllItems();
        check("Test 9: restored to 15 records", restored.size() == 15);
        int restoredLost = 0, restoredFound = 0, restoredResolved = 0;
        for (Item i : restored) {
            if (i.getType().equals("LOST")) restoredLost++;
            if (i.getType().equals("FOUND")) restoredFound++;
            if (i.getStatus().equals("RESOLVED")) restoredResolved++;
        }
        check("Test 9: restored 8 LOST", restoredLost == 8);
        check("Test 9: restored 7 FOUND", restoredFound == 7);
        check("Test 9: restored 2 RESOLVED", restoredResolved == 2);

        System.out.println("\n========================================");
        System.out.println("Results: " + passed + " passed, " + failed + " failed");
        System.out.println("========================================");
    }
}
