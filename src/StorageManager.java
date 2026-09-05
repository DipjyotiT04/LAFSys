import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    static final String FILE = "data" + File.separator + "items.txt";

    public List<Item> load() {
        List<Item> items = new ArrayList<>();
        File f = new File(FILE);
        if (!f.exists()) return items;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split("\\|");
                if (p.length != 8) continue;
                try {
                    items.add(new Item(
                        Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(),
                        p[3].trim(), p[4].trim(), p[5].trim(), p[6].trim(), p[7].trim()
                    ));
                } catch (NumberFormatException e) { }
            }
        } catch (IOException e) { }
        return items;
    }

    public void append(Item item) {
        ensureDir();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write(item.toLine());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rewrite(List<Item> list) {
        ensureDir();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, false))) {
            for (Item i : list) {
                bw.write(i.toLine());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureDir() {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdirs();
    }
}
