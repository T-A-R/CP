package irongate.checkpot.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static Date getCurrent() {
        return Calendar.getInstance().getTime();
    }

    public static String fromLongDateToShort(String str) {
        if (str == null || str.length() == 0)
            return null;

        SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        SimpleDateFormat tfDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            Date date = tf.parse(str);
            return tfDate.format(date);
        } catch (ParseException e) {
            Log.d("IRON", "TimeUtils.fromString() " + e);
        }
        return null;
    }

    public static Date fromDDMMYYYY(String str) {
        if (str == null || str.length() == 0)
            return null;

        SimpleDateFormat tf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            return tf.parse(str);
        } catch (ParseException e) {
            Log.d("IRON", "TimeUtils.fromDDMMYYYY() " + e);
        }
        return null;
    }

    public static Date fromYYYYMMDD(String str) {
        if (str == null || str.length() == 0)
            return null;

        SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return tf.parse(str);
        } catch (ParseException e) {
            Log.d("IRON", "TimeUtils.fromYYYYMMDD() " + e);
        }
        return null;
    }

    public static String toYYYYMMDD(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    }

    public static int differenceDays(Date d1, Date d2) {
        long diff = d1.getTime() - d2.getTime();
        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    public static int differenceHours(Date d1, Date d2) {
        long diff = d1.getTime() - d2.getTime();
        return (int) (diff / (60 * 60 * 1000));
    }

    public static int differenceMin(Date d1, Date d2) {
        long diff = d1.getTime() - d2.getTime();
        return (int) (diff / (60 * 1000));
    }

    public static int differenceSec(Date d1, Date d2) {
        long diff = d1.getTime() - d2.getTime();
        return (int) (diff / (1000));
    }

    public static String toHHMM(long time) {
        int min = (int) ((time / (60 * 1000)) % 60);
        int hours = (int) (time / (60 * 60 * 1000));
        return hours + ":" + (min < 10 ? "0" : "") + min;
    }
}
