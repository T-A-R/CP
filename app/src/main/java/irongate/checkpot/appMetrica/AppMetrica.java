package irongate.checkpot.appMetrica;

import android.app.Application;
import android.content.Context;

import com.yandex.metrica.IReporter;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import irongate.checkpot.BuildConfig;
import irongate.checkpot.MainActivity;

public class AppMetrica extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Создание расширенной конфигурации библиотеки.
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(MainActivity.API_METRICA_KEY).build();
        // Инициализация AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Отслеживание активности пользователей.
        YandexMetrica.enableActivityAutoTracking(this);
        YandexMetrica.getReporter(this, MainActivity.API_METRICA_KEY).reportEvent("Updates installed");

    }

}
