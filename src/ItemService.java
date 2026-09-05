import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemService {
    static final String TYPE_LOST = "LOST";
    static final String TYPE_FOUND = "FOUND";
    static final String STATUS_RESOLVED = "RESOLVED";

    StorageManager storage = new StorageManager();

    public int postItem(String type, String name, String category, String date, String location, String contact) {
        List<Item> items = storage.load();
        int maxId = items.stream().mapToInt(Item::getId).max().orElse(0);
        int newId = maxId + 1;
        Item item = new Item(newId, type, name, category, date, location, contact, "OPEN");
        storage.append(item);
        return newId;
    }

    public List<Item> searchByKeyword(String word) {
        String lower = word.toLowerCase();
        return storage.load().stream()
                .filter(i -> i.getName().toLowerCase().contains(lower)
                        || i.getLocation().toLowerCase().contains(lower)
                        || i.getCategory().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Item> searchByCategory(String cat) {
        return storage.load().stream()
                .filter(i -> i.getCategory().equalsIgnoreCase(cat))
                .collect(Collectors.toList());
    }

    public Item getItemById(int id) {
        return storage.load().stream()
                .filter(i -> i.getId() == id)
                .findFirst().orElse(null);
    }

    public boolean claimItem(int id) {
        List<Item> items = storage.load();
        for (Item i : items) {
            if (i.getId() == id && !i.getStatus().equals(STATUS_RESOLVED)) {
                i.setStatus(STATUS_RESOLVED);
                storage.rewrite(items);
                return true;
            }
        }
        return false;
    }

    public List<Item> getLostItems() {
        return storage.load().stream()
                .filter(i -> i.getType().equals(TYPE_LOST))
                .collect(Collectors.toList());
    }

    public List<Item> getFoundItems() {
        return storage.load().stream()
                .filter(i -> i.getType().equals(TYPE_FOUND))
                .collect(Collectors.toList());
    }

    public List<Item> getResolvedItems() {
        return storage.load().stream()
                .filter(i -> i.getStatus().equals(STATUS_RESOLVED))
                .collect(Collectors.toList());
    }

    public List<Item> getAllItems() {
        return storage.load();
    }
}
