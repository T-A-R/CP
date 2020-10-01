package irongate.checkpot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.mapbox.mapboxsdk.Mapbox;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import irongate.checkpot.appMetrica.BaseActivity;
import irongate.checkpot.model.User;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.delegate.addedRaffle.PaymentFragment;
import irongate.checkpot.view.screens.delegate.tariffAndPayment.ChooseTariffFragment;

public class MainActivity extends BaseActivity implements ViewTreeObserver.OnGlobalLayoutListener {

    @SuppressWarnings("FieldCanBeLocal")

    static public boolean DEBUG_API_ADDRESS = true;
//    static public boolean ENABLE_YANDEX_METRICA = false;
    static public boolean ENABLE_YANDEX_METRICA = true;
    static public String DEBUG_AUTO_ENTER_PHONE;
//    static public String DEBUG_AUTO_ENTER_PHONE = "+79635341237"; // Максим
//    static public String DEBUG_AUTO_ENTER_PHONE = "+79090132059"; // Савелий
//    static public String DEBUG_AUTO_ENTER_PHONE = "+79104550076"; // Тарас
//    static public String DEBUG_AUTO_ENTER_PHONE = "+79090132933"; // Илья
//    static public String DEBUG_AUTO_ENTER_PHONE = "+79998876720"; // Илья2
//    static public String DEBUG_AUTO_ENTER_PHONE = "+79585029676"; // Илья3
//    static public String DEBUG_AUTO_ENTER_PHONE = "+79014031679"; // Илья3
    static public boolean DEBUG_MAP_PLACES;// = true;
    static public ScreenFragment DEBUG_TEST_SCREEN;// = new ChooseTariffFragment();

    static public boolean EVOTOR_MODE = false;
    static public boolean FULL_MODE = false;

    static public String TAG = "CHECKPOTLOGS";

    static public final String MAPBOX_TOKEN = "pk.eyJ1IjoiY2hlY2twb3QiLCJhIjoiY2prMjZjbWdnMHAyMTNwbXR1eWQ5dW1mNSJ9.AM0ycv9cR5G-sMYNI7X3kQ";
    static public final int MAX_LOGO_SIZE = 200;
    static public final int MAX_PHOTO_WIDTH = 800;
    static public final String OPENCAGEDATA_TOKEN = "db2126505fb84a849a1f4e1cd017d203";
    static public String API_METRICA_KEY = "e09a04c5-afe4-4e51-83b9-e6adfab414e3";

    private FirebaseAnalytics mFirebaseAnalytics;
    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MainActivity.onCreate()");
//        Log.d(Requests.TAG, "MainActivity.onCreate()");

        Mapbox.getInstance(this, MAPBOX_TOKEN);
        setContentView(R.layout.activity_main);

        Fonts.init(this);

        Preferences preferences = new Preferences(getApplicationContext());
        getUser().setPreferences(preferences);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        GPS gps = new GPS(getApplicationContext(), locationManager);
        getUser().setGps(gps);

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main);

        View view = mainFragment.getView();
        if (mainFragment == null || view == null)
            Log.d(TAG, "MainActivity.onCreate() WTF? view == null");
        else
            view.post(() -> view.getViewTreeObserver().addOnGlobalLayoutListener(MainActivity.this));

        if (User.getUser().getCityName() == null)
            requestGPSPermission();



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        App.getMetricaLogger().log( "Вход");
    }

    @Override
    public void onGlobalLayout() {
        View view = mainFragment.getView();
        if (view == null) {
            return;
        }

        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        view.post(() -> mainFragment.startScreens());
    }

    @Override
    public void onBackPressed() {
        if (mainFragment.onBackPressed())
            return;

        super.onBackPressed();
    }

    private User getUser() {
        return User.getUser();
    }

    public void requestGPSPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getUser().onGPSPermission(true);
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getUser().onGPSPermission(false);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, User.REQUEST_CODE_GPS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == User.REQUEST_CODE_GPS)
            getUser().onGPSPermission(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        switch (requestCode) {
//            case ChooseTariffFragment.PAYMENT_REQUEST_CODE:
//                ScreenFragment curFragment = mainFragment.getScreensManager().getCurFragment();
//                if (curFragment instanceof ChooseTariffFragment)
//                    ((ChooseTariffFragment) curFragment).onTinkoffResult(resultCode, data);
//                break;
//        }

        switch (requestCode) {
            case PaymentFragment.PAYMENT_REQUEST_CODE:
                ScreenFragment curFragment = mainFragment.getScreensManager().getCurFragment();
                if (curFragment instanceof PaymentFragment)
                    ((PaymentFragment) curFragment).onTinkoffResult(resultCode, data);
                break;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent event) {

        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int location[] = new int[2];
            w.getLocationOnScreen(location);
            float x = event.getRawX() + w.getLeft() - location[0];
            float y = event.getRawY() + w.getTop() - location[1];
            if (event.getAction() == MotionEvent.ACTION_DOWN
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }
}
