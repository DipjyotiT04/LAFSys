import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    private static final String FILE_PATH = "data" + File.separator + "items.txt";

    public List<Item> loadItems() {
        List<Item> items = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return items;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length != 8) continue;
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String type = parts[1].trim();
                    String name = parts[2].trim();
                    String category = parts[3].trim();
                    String date = parts[4].trim();
                    String location = parts[5].trim();
                    String contact = parts[6].trim();
                    String status = parts[7].trim();
                    items.add(new Item(id, type, name, category, date, location, contact, status));
                } catch (NumberFormatException e) {
                    // skip broken line silently
                }
            }
        } catch (IOException e) {
            // file read error, return what we have
        }
        return items;
    }

    public void saveItem(Item item) {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(item.toFileLine());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rewriteAll(List<Item> list) {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Item item : list) {
                bw.write(item.toFileLine());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
