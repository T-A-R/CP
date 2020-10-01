package irongate.checkpot;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onesignal.OneSignal;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import irongate.checkpot.appMetrica.MetricaLogger;
import irongate.checkpot.checkpotAPI.MyResultObjectAdapterFactory;
import irongate.checkpot.checkpotAPI.RetrofitCheckpotAPI;
import irongate.checkpot.checkpotAPI.RetrofitDaDataCheckpotAPI;
import irongate.checkpot.model.User;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static irongate.checkpot.checkpotAPI.CheckpotAPI.API_URL;

public class App extends MultiDexApplication {
    public static RetrofitDaDataCheckpotAPI retrofitDaDataCheckpotAPI;
    private static RetrofitCheckpotAPI retrofitCheckpotAPI;
    private static GoogleAnalytics sAnalytics;
    private static Context context;
    private static MetricaLogger metricaLogger;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        sAnalytics = GoogleAnalytics.getInstance(this);
        OkHttpClient client;
        if (!MainActivity.EVOTOR_MODE) {
            client = new OkHttpClient.Builder()
                    .addInterceptor((new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)))
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
        } else {
            client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request request = chain.request().newBuilder().addHeader("eauth", getAuthToken()).build();
                        return chain.proceed(request);
                    })
                    .hostnameVerifier((hostname, session) -> {
                        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                        return hv.verify("api.checkpot.fun", session);
                    })
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new MyResultObjectAdapterFactory())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        retrofitCheckpotAPI = retrofit.create(RetrofitCheckpotAPI.class);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/futura_pt_book.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
        metricaLogger = new MetricaLogger(this);
        if (MainActivity.ENABLE_YANDEX_METRICA) {
            YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(MainActivity.API_METRICA_KEY).build();
            // Инициализация AppMetrica SDK.
            YandexMetrica.activate(getApplicationContext(), config);
            // Отслеживание активности пользователей.
            YandexMetrica.enableActivityAutoTracking(this);
            YandexMetrica.getReporter(this, MainActivity.API_METRICA_KEY).reportEvent("Updates installed");
        }

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.None);

        OneSignal.startInit(getApplicationContext())
                .setNotificationReceivedHandler(new MyNotificationReceivedHandler(getApplicationContext()))
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(getApplicationContext()))
                .init();

        OkHttpClient clientDaData = new OkHttpClient.Builder()
                .addInterceptor((new HttpLoggingInterceptor()
                        .setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE)))
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Request newRequest;
                    newRequest = request.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json")
                            .addHeader("Authorization", "Token ".concat(BuildConfig.DADATA_API_KEY))
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();

        Retrofit retrofitDaData = new Retrofit.Builder()
                .client(clientDaData)
                .baseUrl("https://suggestions.dadata.ru")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        retrofitDaDataCheckpotAPI = retrofitDaData.create(RetrofitDaDataCheckpotAPI.class);


    }

    public static RetrofitCheckpotAPI getCheckpotApi() {
        return retrofitCheckpotAPI;
    }

    public static MetricaLogger getMetricaLogger() {
        return metricaLogger;
    }

    static private String getAuthToken() {
        if (User.getUser().getAuthToken() != null) {
            return User.getUser().getAuthToken();
        } else {
            return "";
        }
    }
}
