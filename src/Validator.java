public class Validator {
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    public static boolean isValidDate(String text) {
        if (text == null) return false;
        return text.matches("\\d{2}-\\d{2}-\\d{4}");
    }
}
