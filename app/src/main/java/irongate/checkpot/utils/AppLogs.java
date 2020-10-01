package irongate.checkpot.utils;

public class AppLogs {
    private static String smsSubmit;
    private static String onGetSms;
    private static String mapLogs;
    private static String getUserLogs;
    private static String createEventLogs;

    public static String getGetUserLogs() {
        return getUserLogs;
    }

    public static void setGetUserLogs(String getUserLogs) {
        AppLogs.getUserLogs = getUserLogs;
    }

    public static String getMapLogs() {
        return mapLogs;
    }

    public static void setMapLogs(String mapLogs) {
        AppLogs.mapLogs = mapLogs;
    }

    public static String getSmsSubmit() {
        return smsSubmit;
    }

    public static void setSmsSubmit(String smsSubmit) {
        AppLogs.smsSubmit = smsSubmit;
    }

    public static String getCreateEventLogs() {
        return createEventLogs;
    }

    public static void setCreateEventLogs(String createEventLogs) {
        AppLogs.createEventLogs = createEventLogs;
    }

    public static String getOnGetSms() {
        return onGetSms;
    }

    public static void setOnGetSms(String onGetSms) {
        AppLogs.onGetSms = onGetSms;
    }
}
