package irongate.checkpot;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import irongate.checkpot.model.User;

public class GPS implements User.IGPS, LocationListener {
    static private final long MIN_TIME = 1000;
    static private final long MIN_DISTANCE = 1;
    static private final long MAX_TIME = 5000;

    private Context context;
    private LocationManager locationManager;

    private Timer timer;
    private User.CurrentLocationCallback callback;
    private Location inaccurateLocation;

    GPS(Context context, LocationManager locationManager) {
        this.context = context;
        this.locationManager = locationManager;
    }

    @Override
    public void getCurrentLocation(User.CurrentLocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("IRON", "GPS.getCurrentLocation() WTF! No permissions!");
            callback.onCurrentLocation(null);
            return;
        }

        this.callback = callback;

        if (timer != null) {
            return;
        }

        if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            inaccurateLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            inaccurateLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            Log.d(MainActivity.TAG, "getCurrentLocation: GPS_PROVIDER");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
            onTimeUp();
            return;
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onTimeUp();
            }
        }, MAX_TIME);
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            Log.d(MainActivity.TAG, "getCurrentLocation: NETWORK_PROVIDER");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
            return;
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        String provider = location.getProvider();
        switch (provider) {
            case "network":
                inaccurateLocation = location;
                break;

            case "gps":
                onGPSLocation(location);
                break;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    private void onGPSLocation(Location location) {
        stopDetecting();
        callback.onCurrentLocation(location);
    }

    private void onTimeUp() {
        stopDetecting();
        callback.onCurrentLocation(inaccurateLocation);
    }

    private void stopDetecting() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void getAddress(double lat, double lng, AddressCallback callback) {
        GPS.getAddress(context, lat, lng, callback);
    }

    static public void getAddress(Context context, double lat, double lng, AddressCallback callback) {
        new Thread(() -> {
            Geocoder geocoder = new Geocoder(context, getLocale());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1);
            } catch (IOException e) {
                Log.d("IRON", "GPS.getAddress() " + e);
                GPSOpenCageGeoCoder.getAdresses(lat, lng, callback::onAddress);
                return;
            }

            if (addresses.size() == 0) {
                GPSOpenCageGeoCoder.getAdresses(lat, lng, callback::onAddress);
                return;
            }

            callback.onAddress(addresses.get(0));
        }).start();
    }

    @Override
    public void getAddress(String name, AddressCallback callback) {
        GPS.getAddress(context, name, callback);
    }

    static public void getAddress(Context context, String name, AddressCallback callback) {
        new Thread(() -> {
            Geocoder geocoder = new Geocoder(context, getLocale());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocationName(name, 1);
            } catch (IOException e) {
                Log.d("IRON", "GPS.getAddress() " + e + " str:" + name);
                GPSOpenCageGeoCoder.getAdresses(name, callback::onAddress);
                return;
            }

            if (addresses == null || addresses.size() == 0) {
                GPSOpenCageGeoCoder.getAdresses(name, callback::onAddress);
                return;
            }

            callback.onAddress(addresses.get(0));
        }).start();
    }

    private static Locale getLocale() {
        return new Locale("ru", "RU");
    }

    public interface AddressCallback {
        void onAddress(Address address);
    }
}
