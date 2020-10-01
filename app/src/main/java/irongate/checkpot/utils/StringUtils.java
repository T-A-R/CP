package irongate.checkpot.utils;

import android.util.Log;

public class StringUtils {
    static public boolean validEmail(String email) {
        String[] split = email.split("@");
        if (split.length != 2 || split[0].length() == 0)
            return false;

        split = split[1].split("\\.");
        return split.length == 2 && split[0].length() != 0 && split[1].length() >= 2;
    }

    static public boolean validVkLink(String link) {
        String[] split = link.split("https://vk.com/");
        return split.length == 2 && split[0].isEmpty() && !split[1].isEmpty();
    }
}
