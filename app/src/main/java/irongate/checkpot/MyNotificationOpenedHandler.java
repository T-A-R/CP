package irongate.checkpot;

import android.content.Context;
import android.content.Intent;

import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import irongate.checkpot.view.MainFragment;


public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    private Context applicationContext;

    MyNotificationOpenedHandler(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        if (!MainActivity.EVOTOR_MODE) {
            checkResult(result.notification.payload.additionalData);

            String action = result.notification.payload.additionalData.optString("action", null);
            String event = result.notification.payload.additionalData.optString("event", null);

            if(action != null)
            if (action.equals("incorrect") || action.equals("success") || action.equals("event_end")) {
                MainFragment.showNotification(result.toString(), action, event);
            } else {
                Intent intent = new Intent(applicationContext, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(intent);
            }
        }
    }

    private void checkResult(JSONObject data) {
        if (data == null)
            return;

        String action = data.optString("action", null);
        if (action == null)
            return;

        if (action.equals("account_activated"))
            MainFragment.wasNotify = MainFragment.NotifyType.Activated;
    }
}
