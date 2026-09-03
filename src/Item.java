public class Item {
    private int id;
    private String type;
    private String name;
    private String category;
    private String date;
    private String location;
    private String contact;
    private String status;

    public Item(int id, String type, String name, String category, String date, String location, String contact, String status) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.category = category;
        this.date = date;
        this.location = location;
        this.contact = contact;
        this.status = status;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
    public String getContact() { return contact; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String toFileLine() {
        return id + " | " + type + " | " + name + " | " + category + " | " + date + " | " + location + " | " + contact + " | " + status;
    }

    public String getDisplayText() {
        return String.format("%-5d %-24s %-12s %-20s %-12s %-9s", id, name, category, location, date, status);
    }
}
