import java.util.ArrayList;
import java.util.List;

public class ItemService {
    public static final String TYPE_LOST = "LOST";
    public static final String TYPE_FOUND = "FOUND";
    public static final String STATUS_OPEN = "OPEN";
    public static final String STATUS_RESOLVED = "RESOLVED";
    public static final String[] CATEGORIES = {"Electronics", "Books", "ID Card", "Bags", "Keys", "Clothes", "Other"};

    private StorageManager storage = new StorageManager();

    public int postItem(String type, String name, String category, String date, String location, String contact) {
        List<Item> items = storage.loadItems();
        int maxId = 0;
        for (Item i : items) {
            if (i.getId() > maxId) maxId = i.getId();
        }
        int newId = maxId + 1;
        Item item = new Item(newId, type, name, category, date, location, contact, STATUS_OPEN);
        storage.saveItem(item);
        return newId;
    }

    public List<Item> searchByKeyword(String word) {
        List<Item> results = new ArrayList<>();
        String lower = word.toLowerCase();
        for (Item item : storage.loadItems()) {
            if (item.getName().toLowerCase().contains(lower)
                    || item.getLocation().toLowerCase().contains(lower)
                    || item.getCategory().toLowerCase().contains(lower)) {
                results.add(item);
            }
        }
        return results;
    }

    public List<Item> searchByCategory(String category) {
        List<Item> results = new ArrayList<>();
        for (Item item : storage.loadItems()) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                results.add(item);
            }
        }
        return results;
    }

    public Item getItemById(int id) {
        for (Item item : storage.loadItems()) {
            if (item.getId() == id) return item;
        }
        return null;
    }

    public boolean claimItem(int id) {
        List<Item> items = storage.loadItems();
        for (Item item : items) {
            if (item.getId() == id && item.getStatus().equals(STATUS_OPEN)) {
                item.setStatus(STATUS_RESOLVED);
                storage.rewriteAll(items);
                return true;
            }
        }
        return false;
    }

    public List<Item> getLostItems() {
        List<Item> lost = new ArrayList<>();
        for (Item item : storage.loadItems()) {
            if (item.getType().equals(TYPE_LOST)) lost.add(item);
        }
        return lost;
    }

    public List<Item> getFoundItems() {
        List<Item> found = new ArrayList<>();
        for (Item item : storage.loadItems()) {
            if (item.getType().equals(TYPE_FOUND)) found.add(item);
        }
        return found;
    }

    public List<Item> getResolvedItems() {
        List<Item> resolved = new ArrayList<>();
        for (Item item : storage.loadItems()) {
            if (item.getStatus().equals(STATUS_RESOLVED)) resolved.add(item);
        }
        return resolved;
    }

    public List<Item> getAllItems() {
        return storage.loadItems();
    }
}
