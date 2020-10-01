package irongate.checkpot.appMetrica;

import android.content.Context;
import android.content.SharedPreferences;

import com.yandex.metrica.YandexMetrica;

import irongate.checkpot.BuildConfig;
import irongate.checkpot.MainActivity;

import static irongate.checkpot.MainActivity.API_METRICA_KEY;

public class MetricaLogger {

    private Context context;
    private SharedPreferences preferences;

    public MetricaLogger(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences("METRICA", Context.MODE_PRIVATE);
    }

    public void log(String message) {
        if (MainActivity.ENABLE_YANDEX_METRICA) {
        if (!preferences.contains(message)) {
            YandexMetrica.getReporter(context, API_METRICA_KEY).reportEvent(message);
            preferences.edit()
                    .putString(message, message)
                    .apply();
        }
    }
}
}
