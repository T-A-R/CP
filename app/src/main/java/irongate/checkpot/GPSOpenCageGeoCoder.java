package irongate.checkpot;

import android.location.Address;
import android.util.Log;

import com.ktopencage.OpenCageGeoCoder;
import com.ktopencage.model.OpenCageRequest;
import com.ktopencage.model.OpenCageResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static irongate.checkpot.MainActivity.OPENCAGEDATA_TOKEN;

public class GPSOpenCageGeoCoder {

    static public void getAdresses(String name, AddressCallback callback) {
        new Thread(() -> {
            OpenCageGeoCoder geoCoder = new OpenCageGeoCoder(OPENCAGEDATA_TOKEN);
            OpenCageRequest request = new OpenCageRequest(name);
            request.setMinConfidence(1);

            List<Address> addresses = getAddresses(geoCoder, request);
            if (addresses == null || addresses.size() == 0) {
                callback.onAddress(null);
                return;
            }
            callback.onAddress(addresses.get(0));
        }).start();
    }

    static public void getAdresses(double lat, double lng, AddressCallback callback) {
        new Thread(() -> {
            OpenCageGeoCoder geoCoder = new OpenCageGeoCoder(OPENCAGEDATA_TOKEN);
            OpenCageRequest request = new OpenCageRequest(lat, lng);
            request.setMinConfidence(1);

            List<Address> addresses = getAddresses(geoCoder, request);
            if (addresses == null || addresses.size() == 0) {
                callback.onAddress(null);
                return;
            }
            callback.onAddress(addresses.get(0));
        }).start();
    }

    private static List<Address> getAddresses(OpenCageGeoCoder geoCoder, OpenCageRequest request) {
        List<Address> addresses = geoCoder.requestObservable(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(response -> !response.getResults().isEmpty())
                .map(response -> response.getResults())
                .filter(openCageResults -> !openCageResults.isEmpty())
                .map(results -> {
                    List<Address> addressList = new ArrayList<>();
                    for (int i = 0; i < results.size(); i++) {
                        Address adress = new Address(Locale.getDefault());
                        adress.setAddressLine(0, results.get(i).getFormatted());
                        adress.setLocality(results.get(i).getFormatted());
                        adress.setLatitude(results.get(i).getGeometry().getLat());
                        adress.setLongitude(results.get(i).getGeometry().getLng());
                        addressList.add(adress);
                    }
                    return addressList;
                })
                .onErrorReturnItem(new ArrayList<>())
                .blockingLast(new ArrayList<>());
        return addresses;
    }

    public interface AddressCallback {
        void onAddress(Address address);
    }

}
