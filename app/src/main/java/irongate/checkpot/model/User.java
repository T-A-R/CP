package irongate.checkpot.model;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import irongate.checkpot.GPS;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.checkpotAPI.models.Restaurant;
import irongate.checkpot.checkpotAPI.models.UserModel;

public class User {
    static private final String KEY_FIRST_START = "firstStart";
    static private final String KEY_FIRST_START_MAP = "firstStartMap";
    static private final String KEY_TOKEN = "token";
    static private final String KEY_NOTIFY = "notify";
    static private final String KEY_CITY = "cityName";
    static private final String KEY_DELEGATE_MODE = "delegateMode";

    static public final int REQUEST_CODE_GPS = 123;

    static private final String DEF_CITY = "Москва";

    static private User inst;
    private IPreferences preferences;
    private IGPS gps;

    private ModeChangeListener modeChangeListener;
    private CityDetectListener cityDetectListener;
    private PlaceUpdateListener placeUpdateListener;
    private UserUpdateListener userUpdateListener;

    private UserModel currentUser;
    private List<Place> cityPlaces = new ArrayList<>();

    static public User getUser() {
        if (inst == null)
            inst = new User();

        return inst;
    }

    private User() {

    }

    public void setPreferences(IPreferences preferences) {
        this.preferences = preferences;
    }

    public void setGps(IGPS gps) {
        this.gps = gps;
    }

    public void updateUser(UpdateUserCallback callback) {
        new Thread(() -> CheckpotAPI.getCurrentUser(data -> {
            if (data == null) {
                if (callback != null) {
                    callback.onUserUpdated(false);
                }
                return;
            }
            currentUser = data;

            if (userUpdateListener != null)
                userUpdateListener.onUserUpdated();

            if (callback != null)
                callback.onUserUpdated(true);

        })).start();
    }

    public interface UpdateUserCallback {
        void onUserUpdated(boolean ok);
    }

    public UserModel getCurrentUser() {
        return currentUser;
    }

    public String getUserUuid() {
        return currentUser == null ? null : currentUser.getUuid();
    }

    public String getUserId() {
        return currentUser == null ? null : currentUser.getId();
    }

    public String getRestaurantContractId() {
        Restaurant restarant = getRestaurant();
        return restarant != null ? String.valueOf(restarant.getContractId()) : null;
    }

    public Restaurant getRestaurant() {
        if (currentUser != null && currentUser.getRestaurants() != null && currentUser.getRestaurants().size() > 0)
            return currentUser.getRestaurants().get(0);

        return null;
    }

    public boolean isActive() {
        return (getRestaurant() != null && getRestaurant().getIsActive());
    }

    public boolean isWaitForm() {
        return getRestaurant() != null && getRestaurant().getLegalAddress() == null;
    }

    public irongate.checkpot.checkpotAPI.models.Place getPlace() {
        if (currentUser != null && currentUser.getPermissions().getOwner().size() != 0)
            return currentUser.getPermissions().getOwner().get(0);

        return null;
    }

    public List<Place> getCityPlaces() {
        return cityPlaces;
    }

    public void logout() {
        setAuthToken(null);
        setDelegateMode(false);
    }

    public boolean isAuthorized() {
        return getAuthToken() != null;
    }

    public boolean isDelegateMode() {
        return preferences.getBoolean(KEY_DELEGATE_MODE, false);
    }

    public void setDelegateMode(boolean delegate) {
        if (isDelegateMode() == delegate)
            return;

        preferences.putBoolean(KEY_DELEGATE_MODE, delegate);
        modeChangeListener.onModeChanged();
    }

    public void setModeChangeListener(ModeChangeListener listener) {
        modeChangeListener = listener;
    }

    public void setCityDetectListener(CityDetectListener callback) {
        this.cityDetectListener = callback;
    }

    public boolean isFirstStart() {
        return preferences.getBoolean(KEY_FIRST_START, true);
    }

    public void setFirstStart(boolean notify) {
        preferences.putBoolean(KEY_FIRST_START, notify);
    }

    public boolean isFirstStartMap() {
        return preferences.getBoolean(KEY_FIRST_START_MAP, true);
    }

    public void setFirstStartMap(boolean notify) {
        preferences.putBoolean(KEY_FIRST_START_MAP, notify);
    }

    public String getAuthToken() {
        return preferences.getString(KEY_TOKEN, null);
    }

    public void setAuthToken(String token) {
        preferences.putString(KEY_TOKEN, token);
    }

    public boolean isNotify() {
        return preferences.getBoolean(KEY_NOTIFY, true);
    }

    public void setNotify(boolean notify) {
        preferences.putBoolean(KEY_NOTIFY, notify);
    }

    public String getCityName() {
        return preferences.getString(KEY_CITY, null);
    }

    public void setCityName(String cityName) {
        preferences.putString(KEY_CITY, cityName);
    }

    public void getCurrentLocation(User.CurrentLocationCallback callback) {
        gps.getCurrentLocation(callback);
    }

    public int getRafflesRemain() {
        if (getPlace() == null || getRestaurant() == null || getRestaurant().getTariff() == null)
            return 0;

        return getRestaurant().getTariff().getCount().intValue();
    }

    public void updatePlacesByCurrentCity() {
        gps.getAddress(getCityName(), address -> {
            if (address == null) {
                cityPlaces = null;
                if (placeUpdateListener != null) {
                    placeUpdateListener.onPlacesUpdated();
                }
                return;
            }

            CheckpotAPI.getPlacesListViaGeo(address.getLatitude(), address.getLongitude(), list -> {
                cityPlaces = list;
                if (placeUpdateListener != null)
                    placeUpdateListener.onPlacesUpdated();
            });
        });
    }

    public void setPlaceUpdateListener(PlaceUpdateListener placeUpdateListener) {
        this.placeUpdateListener = placeUpdateListener;
    }

    public void setUserUpdateListener(UserUpdateListener userUpdateListener) {
        this.userUpdateListener = userUpdateListener;
    }

    public void onGPSPermission(boolean granted) {
        if (getCityName() != null)  // Вызывается только раз, пока город ни разу не определялся
            return;

        if (!granted) {
            setCityName(DEF_CITY);
            if (cityDetectListener != null) {
                cityDetectListener.onCityDetected();
            }
            cityDetectListener = null;
            return;
        }

        gps.getCurrentLocation(location -> {
            if (location == null) {
                setCityName(DEF_CITY);
                if (cityDetectListener != null)
                    cityDetectListener.onCityDetected();

                cityDetectListener = null;
                return;
            }

            gps.getAddress(location.getLatitude(), location.getLongitude(), address -> {
                String cityName =  address != null ? address.getLocality() : null;
                setCityName(cityName != null ? cityName : DEF_CITY);

                if (cityDetectListener != null)
                    cityDetectListener.onCityDetected();

                cityDetectListener = null;
            });
        });
    }

    public interface IGPS {
        void getCurrentLocation(User.CurrentLocationCallback callback);
        void getAddress(double lat, double lng, GPS.AddressCallback callback);
        void getAddress(String name, GPS.AddressCallback callback);
    }

    public interface CurrentLocationCallback {
        void onCurrentLocation(Location location);
    }

    public interface ModeChangeListener {
        void onModeChanged();
    }

    public interface CityDetectListener {
        void onCityDetected();
    }

    public interface PlaceUpdateListener {
        void onPlacesUpdated();
    }

    public interface UserUpdateListener {
        void onUserUpdated();
    }
}
