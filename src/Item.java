public class Item {
    int id;
    String type, name, category, date, location, contact, status;

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
    public void setStatus(String s) { this.status = s; }

    public String toLine() {
        return id + "|" + type + "|" + name + "|" + category + "|" + date + "|" + location + "|" + contact + "|" + status;
    }

    public String display() {
        return String.format("%-4d %-20s %-12s %-15s %-12s %-9s", id, name, category, location, date, status);
    }
}
