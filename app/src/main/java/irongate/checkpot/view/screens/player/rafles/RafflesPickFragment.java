package irongate.checkpot.view.screens.player.rafles;


import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Location;
import irongate.checkpot.checkpotAPI.models.Prize;
import irongate.checkpot.model.User;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.SmartFragment;


public class RafflesPickFragment extends SmartFragment implements View.OnClickListener, OnMapReadyCallback {
    static final public String INTENT_BACK = "INTENT_BACK";

    private RelativeLayout cont;
    private ScrollView scroll;
    private ImageButton btnClose;
    private MapView map;

    private Event event;
    boolean isClosed = false;

    public RafflesPickFragment() {
        super(R.layout.fragment_raffles_pick);
    }

    public RafflesPickFragment setEvent(Event event) {
        this.event = event;
        return this;
    }

    @Override
    protected void onReady() {
        cont = (RelativeLayout) findViewById(R.id.cont);
        scroll = (ScrollView) findViewById(R.id.scroll);
        TextView txtAddress = (TextView) findViewById(R.id.txt_address);
        TextView txtIdr = (TextView) findViewById(R.id.txt_idr);
        TextView txtIdu = (TextView) findViewById(R.id.txt_idu);
        TextView txtMin = (TextView) findViewById(R.id.txt_min);
        RelativeLayout panel = (RelativeLayout) findViewById(R.id.panel);
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        map = (MapView) findViewById(R.id.map);

        txtAddress.setText(String.format("%s %s", getString(R.string.raffles_pick_address), event.getPlace().getAddress()));
        txtIdr.setText(event.getDigitalId() != null ? event.getDigitalId() : event.getUuid());
        String digitalId = User.getUser().getCurrentUser().getDigitalId();
        txtIdu.setText(digitalId != null ? digitalId : User.getUser().getUserUuid());
        Prize mainPrize = event.getMainPrize();
        txtMin.setText(String.valueOf(mainPrize == null ? "--" : mainPrize.getMinReceipt()));

        MainFragment.disableSideMenu();

        map.onCreate(null);
        map.getMapAsync(this);

        cont.startAnimation(Anim.getAppear(getContext()));
        panel.startAnimation(Anim.getAppearSlide(getContext(), 200));

        btnClose.setOnClickListener(this);
        map.setOnTouchListener(this::onMapTouch);

    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        if (getContext() == null)
            return;

        Location location = event.getPlace().getLocation();
        double lat = location.getLat();
        double lng = location.getLng();
        CameraPosition position = new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(16).build();
        map.post(() -> {
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
            mapboxMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
        });
    }

    private void hide() {
        if (isClosed) return;
        isClosed = true;
        cont.startAnimation(Anim.getDisappear(getContext(), () -> dispatchIntent(INTENT_BACK)));
    }

    @Override
    public void onClick(View view) {
        if (isClosed) return;
        if (view == btnClose) onBackPressed();
    }

    @Override
    public boolean onBackPressed() {
        if (!isClosed) hide();
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
}
