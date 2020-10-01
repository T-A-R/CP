/**
 * Version for Android
 * © 2012–2017 YANDEX
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://yandex.com/legal/appmetrica_sdk_agreement/
 */

package irongate.checkpot.appMetrica;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import com.yandex.metrica.YandexMetrica;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();

        // It's required to call.
        //
        // This helps library to track correctly the next things:
        //  - active users
        //  - sessions duration
        //  - app usage frequency
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            YandexMetrica.resumeSession(this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        // It's required to call.
        //
        // This helps library to track correctly the next things:
        //  - active users
        //  - sessions duration
        //  - app usage frequency
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            YandexMetrica.pauseSession(this);
        }
    }

}
