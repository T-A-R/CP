package irongate.checkpot;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import irongate.checkpot.view.MainFragment;

public class MyNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {

    private Context applicationContext;

    public MyNotificationReceivedHandler(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        String customKey;
        String customMessage;
        String event;

        Log.d(MainActivity.TAG, "customkey set with value: " + notification.payload.body);

        if (!MainActivity.EVOTOR_MODE)
            if (data != null) {
                customKey = data.optString("action", "");
                event = data.optString("event", "");
                if (notification.payload.body != null)
                    customMessage = "     " + notification.payload.title + ". " + notification.payload.body;
                else customMessage = null;
                if (customMessage != null) {
                    Log.d(MainActivity.TAG, "<><><><><><><><> NOTIFICATION EVENT ID <><><><><><><><> : " + event + " action: " + customKey);
                    MainFragment.showNotification(customMessage, customKey, event);
                }
            }
    }
}
