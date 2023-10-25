package util;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UserUtil {

    public static boolean isValidId(String userId) {
        return userId.length() == 24;
    }

    public static boolean isValidDate(String dateStr) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            dateFormatter.parse(dateStr);
            return true;

        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
