package irongate.checkpot.view.screens.delegate.editPlace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import irongate.checkpot.App;
import irongate.checkpot.GPS;
import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.Location;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.checkpotAPI.models.PlaceWithBitmaps;
import irongate.checkpot.checkpotAPI.models.Restaurant;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.utils.TimeUtils;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.screens.common.place.PlaceFragment;
import irongate.checkpot.view.screens.player.map.MapFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class EditPlaceFragment extends ScreenFragment implements SmartFragment.Listener, View.OnFocusChangeListener, TextWatcher, OnMapReadyCallback {
    static final public int REQUEST_LOGO_GALLERY = 1;
    static final public int REQUEST_LOGO_CAMERA = 10;
    static final public int REQUEST_PHOTO_GALLERY = 2;
    static final public int REQUEST_PHOTO_CAMERA = 20;

    private ImageView imgLogo;
    private ArrayList<EditPlaceMiniFragment> minis = new ArrayList<>();
    private EditText editName;
    private EditText editAddress;
//    private EditText editDesc;
    private MapView map;
    private ScrollView scroll;

    private Bitmap bmpLogo;
    Address address;
    private Marker currentMarker;
    private MapboxMap mapboxMap;

    public EditPlaceFragment() {
        super(R.layout.fragment_edit_place);
    }

    @Override
    public boolean isMenuShown() {
        return getUser().getPlace() != null;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onReady() {
        scroll = (ScrollView) findViewById(R.id.scroll);
        TextView title = (TextView) findViewById(R.id.title);
        View panel = findViewById(R.id.panel);
        TextView titleLogo = (TextView) findViewById(R.id.title_logo);
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        ImageView imgUploadIco = (ImageView) findViewById(R.id.ico_upload_logo);
        TextView titleName = (TextView) findViewById(R.id.title_name);
        editName = (EditText) findViewById(R.id.edit_name);
        TextView titlePhoto = (TextView) findViewById(R.id.title_photo);
        ImageView imgPhoto = (ImageView) findViewById(R.id.img_photo);
        ImageButton btnLoadPhoto = (ImageButton) findViewById(R.id.btn_load_photo);
        TextView titleLoadPhoto = (TextView) findViewById(R.id.title_load_photo);
        TextView titleAddress = (TextView) findViewById(R.id.title_address);
        editAddress = (EditText) findViewById(R.id.edit_address);
        TextView titleDesc = (TextView) findViewById(R.id.title_desc);
//        editDesc = (EditText) findViewById(R.id.edit_desc);
        Button btnSave = (Button) findViewById(R.id.btn_save);
        Button btnPreview = (Button) findViewById(R.id.btn_preview);
        map = (MapView) findViewById(R.id.place_map);

        title.setTypeface(Fonts.getFuturaPtBook());
        titleLogo.setTypeface(Fonts.getFuturaPtBook());
        titleName.setTypeface(Fonts.getFuturaPtBook());
        editName.setTypeface(Fonts.getFuturaPtBook());
        titlePhoto.setTypeface(Fonts.getFuturaPtBook());
        titleLoadPhoto.setTypeface(Fonts.getFuturaPtBook());
        titleAddress.setTypeface(Fonts.getFuturaPtBook());
        editAddress.setTypeface(Fonts.getFuturaPtBook());
        titleDesc.setTypeface(Fonts.getFuturaPtBook());
//        editDesc.setTypeface(Fonts.getFuturaPtBook());
        btnSave.setTypeface(Fonts.getFuturaPtBook());
        btnPreview.setTypeface(Fonts.getFuturaPtBook());
        btnSave.setTransformationMethod(null);
        btnPreview.setTransformationMethod(null);

        map.onCreate(null);
        map.getMapAsync(this);

        title.startAnimation(Anim.getAppearSlide(getContext()));
        panel.startAnimation(Anim.getAppear(getContext(), 500));
        titleLogo.startAnimation(Anim.getAppearSlide(getContext(), 700));
        imgLogo.startAnimation(Anim.getAppearSlide(getContext(), 700));
        imgUploadIco.startAnimation(Anim.getAppearSlide(getContext(), 700));
        titleName.startAnimation(Anim.getAppearSlide(getContext(), 900));
        editName.startAnimation(Anim.getAppearSlide(getContext(), 900));
        titlePhoto.startAnimation(Anim.getAppearSlide(getContext(), 1100));
        imgPhoto.startAnimation(Anim.getAppearSlide(getContext(), 1100));
        btnLoadPhoto.startAnimation(Anim.getAppearSlide(getContext(), 1100));
        titleAddress.startAnimation(Anim.getAppearSlide(getContext(), 1300));
        editAddress.startAnimation(Anim.getAppearSlide(getContext(), 1300));

        editName.setOnFocusChangeListener(this);
        editAddress.setOnFocusChangeListener(this);
//        editDesc.setOnFocusChangeListener(this);

        editAddress.addTextChangedListener(this);

        map.setOnTouchListener(this::onMapTouch);
        imgLogo.setOnClickListener(v -> showLoadImageAlert(REQUEST_LOGO_CAMERA, REQUEST_LOGO_GALLERY));
        btnLoadPhoto.setOnClickListener(v -> onBtnLoadPhoto());
        btnSave.setOnClickListener(v -> onBtnSave());
        btnPreview.setOnClickListener(v -> onBtnPreview());
        App.getMetricaLogger().log("Оунер на экране регистрации заведения");
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        populate();
        mapboxMap.addOnMapClickListener(this::onMapClick);
    }

    private void populate() {
        Place place = getUser().getPlace();
        if (place == null) {
            editAddress.setText(getUser().getCityName());
            GPS.getAddress(getContext(), getUser().getCityName(), address -> {
                EditPlaceFragment.this.address = address;
                updateMap();
            });
            return;
        }

        editName.setText(firstUpperCase(getUser().getPlace().getName()));
        editAddress.setText(place.getAddress());
//        editDesc.setText(firstUpperCase(place.getDescription()));

        if (place.getLogo() != null) {
            ImageUtils.getBitmap(place.getLogo(), this::setBmpLogo);
        }

        List<String> photos = place.getPhotos();
        if (photos != null) {
            for (int i = 0; i < photos.size(); i++) {
                ImageUtils.getBitmap(photos.get(i), this::addPhoto);
            }
        }

        Location location = place.getLocation();
        GPS.getAddress(getContext(), location.getLat(), location.getLng(), address -> {
            this.address = address;
            updateMap();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getContext();
        if (context == null || resultCode != Activity.RESULT_OK) {
            return;
        }

        Bitmap bitmap = null;
        if (requestCode == REQUEST_LOGO_GALLERY || requestCode == REQUEST_PHOTO_GALLERY) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
            } catch (IOException e) {
                Log.d("IRON", "EditPlaceFragment.onActivityResult() " + e);
                return;
            }
        } else if (requestCode == REQUEST_LOGO_CAMERA || requestCode == REQUEST_PHOTO_CAMERA) {
            bitmap = getCameraBitmap();
        }

        if (bitmap == null) {
            Log.d("IRON", "EditPlaceFragment.onActivityResult() bitmap=null request=" + requestCode);
            return;
        }

        switch (requestCode) {
            case REQUEST_LOGO_GALLERY:
            case REQUEST_LOGO_CAMERA:
                App.getMetricaLogger().log("Оунер загрузил лого");
                setBmpLogo(bitmap);
                break;

            case REQUEST_PHOTO_GALLERY:
            case REQUEST_PHOTO_CAMERA:
                App.getMetricaLogger().log("Оунер загрузил фото");
                addPhoto(bitmap);
                break;
        }
    }

    private void setBmpLogo(Bitmap bitmap) {
        if (bitmap == null || getContext() == null)
            return;

        imgLogo.post(() -> {
            int size = Math.min(Math.min(bitmap.getWidth(), bitmap.getHeight()), MainActivity.MAX_LOGO_SIZE);
            Bitmap cropedBitmap = ImageUtils.getCropedBitmap(bitmap, size, size);
            bmpLogo = ImageUtils.getRoundedCornerBitmap(cropedBitmap, size / 2f);
            imgLogo.setImageBitmap(bmpLogo);
        });
    }

    private void addPhoto(Bitmap photo) {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        EditPlaceMiniFragment fragment = new EditPlaceMiniFragment();
        fragment.setImage(photo);
        activity.getSupportFragmentManager().beginTransaction().add(R.id.linear_mini, fragment).commit();
        minis.add(fragment);
        fragment.setListener(this);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public void fragmentIntent(SmartFragment fragment, String intent) {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        minis.remove(fragment);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus)
            view.setBackgroundResource(R.drawable.bg_selector_blue);
        else {
            boolean validated = isValidEdit((EditText) view);
            view.setBackgroundResource(validated ? R.drawable.bg_selector_blue : R.drawable.bg_frame_red_8_input);
            if (((EditText) view).length() > 0) {
                switch (view.getId()) {
                    case R.id.edit_name:
                        App.getMetricaLogger().log("Оунер записал название");
                        break;
                    case R.id.edit_address:
                        App.getMetricaLogger().log("Оунер записал адрес");
                        break;
                    case R.id.edit_desc:
                        App.getMetricaLogger().log("Оунер записал описание");
                        break;
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = editAddress.getText().toString();
        GPS.getAddress(getContext(), str, address -> {
            this.address = address;
            View view = getView();
            if (view != null) {
                view.post(this::updateMap);
            }
        });
    }

    private void onMapClick(LatLng point) {
        GPS.getAddress(getContext(), point.getLatitude(), point.getLongitude(), address -> {
            this.address = address;
            editAddress.post(() -> editAddress.setText(getAddressString()));
        });
    }

    private void updateMap() {
        if (address == null)
            return;

        moveMapTo(address.getLatitude(), address.getLongitude());
    }

    private void moveMapTo(double lat, double lng) {
        CameraPosition position = new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(16).build();
        map.post(() -> {
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            if (currentMarker != null) {
                mapboxMap.removeMarker(currentMarker);
            }
            currentMarker = mapboxMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
        });
    }

    private String getAddressString() {
        if (address == null)
            return "";

        String line = address.getAddressLine(0);
        if (line == null)
            return "";

        int index = line.lastIndexOf(",");
        if (index == -1)
            return line;

        line = line.substring(0, index);

        index = line.lastIndexOf(",");
        if (index == -1)
            return line;

        line = line.substring(0, index);
        return line;
    }

    private boolean isValidEdit(EditText edit) {
        String str = edit.getText().toString();
        return str.length() != 0;
    }

    private boolean isAllValid() {
        if (editName.getText().length() == 0) {
            Toast.makeText(getContext(), R.string.edit_place_need_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (editAddress.getText().length() == 0) {
            Toast.makeText(getContext(), R.string.edit_place_need_address, Toast.LENGTH_SHORT).show();
            return false;
        }

//        if (editDesc.getText().length() == 0) {
//            Toast.makeText(getContext(), R.string.edit_place_need_desc, Toast.LENGTH_SHORT).show();
//            return false;
//        }

        if (address == null) {
            Toast.makeText(getContext(), R.string.edit_place_null_address, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void submit() {
        Restaurant restarant = getUser().getRestaurant();
        if (restarant == null) {
            Toast.makeText(getContext(), "У вас еще нет ресторана", Toast.LENGTH_SHORT).show();
            Log.d("IRON", "EditPlaceFragment.submit() restaurant == null");
            return;
        }
        showScreensaver(R.string.submiting, true);

        Place place = getUser().getPlace();
        if (place != null)
            new Thread(() -> CheckpotAPI.patchPlace(place.getUuid(), getFormPlace(), this::onSendPlace)).start();
        else
            new Thread(() -> CheckpotAPI.putPlace(restarant.getUuid(), getFormPlace(), this::onSendPlace)).start();
    }

    private PlaceWithBitmaps getFormPlace() {
        PlaceWithBitmaps place = new PlaceWithBitmaps();
        place.setLocation(new Location(address.getLatitude(), address.getLongitude()));
        place.setAddress(getAddressString());
        place.setName(editName.getText().toString());
        place.setRestaurant(getUser().getRestaurant());
//        place.setDescription(editDesc.getText().toString());
        place.setDescription(" ");
        place.setCreatedAt(getUser().getPlace() != null ? getUser().getPlace().getCreatedAt() : TimeUtils.toYYYYMMDD(TimeUtils.getCurrent()));
        place.setUpdatedAt(TimeUtils.toYYYYMMDD(TimeUtils.getCurrent()));

        place.setLogoBmp(bmpLogo);

        ArrayList<Bitmap> photos = new ArrayList<>();
        for (int i = 0; i < minis.size(); i++) {
            photos.add(minis.get(i).getBitmap());
        }
        place.setPhotosBmp(photos);

        return place;
    }

    public void onSendPlace(final String uuid) {
        editName.post(() -> {
            hideScreensaver();
            if (uuid == null) {
                Toast.makeText(getContext(), R.string.error_submit, Toast.LENGTH_SHORT).show();
                return;
            }

            getUser().updateUser(ok -> {
                Toast.makeText(getContext(), R.string.submit_ok, Toast.LENGTH_SHORT).show();
                replaceFragment(new RafflesFragment());
            });
        });
    }

    private void onBtnLoadPhoto() {
        if (minis.size() >= 5) {
            Toast.makeText(getContext(), R.string.edit_place_toast_limit, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoadImageAlert(REQUEST_PHOTO_CAMERA, REQUEST_PHOTO_GALLERY);
    }

    private void onBtnSave() {
        if (!isAllValid()) {
            Toast.makeText(getContext(), R.string.need_all_valid, Toast.LENGTH_SHORT).show();
            return;
        }

        submit();
    }

    private void onBtnPreview() {
        if (!isAllValid()) {
            Toast.makeText(getContext(), R.string.need_all_valid, Toast.LENGTH_SHORT).show();
            return;
        }

        openScreenInNewActivity(new PlaceFragment().setPlace(getFormPlace()).setDelegateScreen(true));
    }

    @Override
    public boolean onBackPressed() {
        if (getUser().getPlace() != null) {
            replaceFragment(new RafflesFragment());
        } else {
            getUser().setDelegateMode(false);
            replaceFragment(new MapFragment());
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        map.onDestroy();
    }

    @SuppressWarnings("unused")
    private boolean onMapTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                scroll.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                scroll.requestDisallowInterceptTouchEvent(false);
                break;
        }
        return map.onTouchEvent(event);
    }

    public String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return ""; //или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}
