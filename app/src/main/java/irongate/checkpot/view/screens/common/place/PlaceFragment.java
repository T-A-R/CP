package irongate.checkpot.view.screens.common.place;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.List;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Location;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.checkpotAPI.models.PlaceWithBitmaps;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.utils.TimeUtils;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.PageIndicator;
import irongate.checkpot.view.screens.common.CardNotYetFragment;
import irongate.checkpot.view.screens.delegate.editPlace.EditPlaceFragment;
import irongate.checkpot.view.screens.player.complain.ComplainFragment;
import irongate.checkpot.view.screens.player.event.EventFragment;
import irongate.checkpot.view.screens.player.map.MapFragment;
import irongate.checkpot.view.screens.player.rafles.RaffleCardFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class PlaceFragment extends ScreenFragment implements PopupMenu.OnMenuItemClickListener, ViewPager.OnPageChangeListener, RaffleCardFragment.CardListener {
    private ImageButton btnMenu;
    private ImageButton btnMap;
    private ViewPager pager;
    private PageIndicator indicator;
    private View linear;
    private RelativeLayout contInfo;
    private ImageView imgLogo;
    private MapView map;

    private Place place;
    private boolean mapMode = false;
    private Marker currentMarker;

    public PlaceFragment() {
        super(R.layout.fragment_place);
    }

    public PlaceFragment setPlace(Place place) {
        this.place = place;
        return this;
    }

    @Override
    protected void onReady() {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        contInfo = (RelativeLayout) findViewById(R.id.cont_info);
        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (PageIndicator) findViewById(R.id.indicator);
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnMenu = (ImageButton) findViewById(R.id.btn_menu);
        btnMap = (ImageButton) findViewById(R.id.btn_map);
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        TextView title = (TextView) findViewById(R.id.title);
        TextView desc = (TextView) findViewById(R.id.desc);
        TextView txtAddress = (TextView) findViewById(R.id.txt_address);
        TextView txtNumRaffles = (TextView) findViewById(R.id.txt_num_raffles);
        TextView txtNumMembers = (TextView) findViewById(R.id.txt_num_members);
        TextView txtNumDone = (TextView) findViewById(R.id.txt_num_done);
        TextView titleNumRaffles = (TextView) findViewById(R.id.title_num_raffles);
        TextView titleNumMembers = (TextView) findViewById(R.id.title_num_members);
        TextView titleNumDone = (TextView) findViewById(R.id.title_num_done);
        linear = findViewById(R.id.linear);
        TextView titleRaffles = (TextView) findViewById(R.id.title_raffles);
        map = (MapView) findViewById(R.id.map);

        title.setTypeface(Fonts.getFuturaPtMedium());
        desc.setTypeface(Fonts.getFuturaPtBook());
        txtAddress.setTypeface(Fonts.getFuturaPtBook());
        txtNumRaffles.setTypeface(Fonts.getFuturaPtMedium());
        txtNumMembers.setTypeface(Fonts.getFuturaPtMedium());
        txtNumDone.setTypeface(Fonts.getFuturaPtMedium());
        titleNumRaffles.setTypeface(Fonts.getFuturaPtBook());
        titleNumMembers.setTypeface(Fonts.getFuturaPtBook());
        titleNumDone.setTypeface(Fonts.getFuturaPtBook());
        titleRaffles.setTypeface(Fonts.getFuturaPtBook());

        MainFragment.disableSideMenu();

        map.onCreate(null);

        if (isDelegateScreen()) {
            btnMenu.setVisibility(View.GONE);
            btnMap.setBackgroundResource(R.drawable.bg_circle_blue);
        }

        if(!getUser().isAuthorized()) {
            btnMenu.setVisibility(View.GONE);
        }

        String logoUrl = place.getLogo();
        if (logoUrl != null) {
            ImageUtils.getBitmap(logoUrl, this::setLogo);
        } else if (place instanceof PlaceWithBitmaps && ((PlaceWithBitmaps) place).getLogoBmp() != null) {
            setLogo(((PlaceWithBitmaps) place).getLogoBmp());
        }

        List<String> photoUrls = place.getPhotos();
        List<Bitmap> photosBmp = (place instanceof PlaceWithBitmaps) ? ((PlaceWithBitmaps) place).getPhotosBmp() : null;
        pager.setAdapter(new FragmentStatePagerAdapter(activity.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return photosBmp != null ? new PlacePageFragment().setBmp(photosBmp.get(position)) : new PlacePageFragment().setUrl(photoUrls.get(position));
            }

            @Override
            public int getCount() {
                return photosBmp != null ? photosBmp.size() : photoUrls.size();
            }
        });
        pager.addOnPageChangeListener(this);

        indicator.setCursorColor(ContextCompat.getColor(activity, isDelegateScreen() ? R.color.blue : R.color.green));
        indicator.setNumPages(photosBmp != null ? photosBmp.size() : photoUrls.size());

        List<Event> events = place.getEvents();
        title.setText(firstUpperCase(place.getName()));
        desc.setText(firstUpperCase(place.getDescription()));
        txtAddress.setText(String.format("%s %s", getString(R.string.place_address), place.getAddress()));

        int numActive = 0;
        int numDone = 0;
        int numMem = 0;
        if (!isDelegateScreen() && events.size() > 0) {
            titleRaffles.setVisibility(View.VISIBLE);
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);

                if (event.getIsDone()) {
                    numDone++;
                    continue;
                }

                if (event.getBanned())
                    continue;

                event.setPlace(place);
                RaffleCardFragment card = new RaffleCardFragment();
                card.setHeightDp(383);
                card.setEvent(event);
                getFragmentManager().beginTransaction().add(R.id.linear_events, card).commit();
                card.setCardListener(this);
                numActive++;
                numMem += event.getNumSuccMembers();
            }
        }

        if (getUser().isDelegateMode() && events.size() == 0 && !isDelegateScreen()) {
            titleRaffles.setVisibility(View.VISIBLE);
            addNotYetCard();
        }

        txtNumRaffles.setText(String.valueOf(numActive));
        txtNumMembers.setText(String.valueOf(numMem));
        txtNumDone.setText(String.valueOf(numDone));

        pager.startAnimation(Anim.getAppear(getContext()));
        indicator.startAnimation(Anim.getAppear(getContext()));
        btnBack.startAnimation(Anim.getAppearSlide(getContext(), 500));
        btnMenu.startAnimation(Anim.getAppearSlide(getContext(), 900));
        title.startAnimation(Anim.getAppearSlide(getContext(), 1400));
        desc.startAnimation(Anim.getAppearSlide(getContext(), 1600));
        linear.startAnimation(Anim.getAppearSlide(getContext(), 1800));
        btnMap.startAnimation(Anim.getAppearSlide(getContext(), 2000));

        pager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                initScroll();
            }
        });

        btnBack.setOnClickListener(v -> onBackPressed());
        btnMenu.setOnClickListener(v -> showPopupMenu());
        btnMap.setOnClickListener(v -> onBtnMap());
    }

    private void setLogo(Bitmap bitmap) {
        if (imgLogo == null || bitmap == null || bitmap.getWidth() <= 10 || bitmap.getHeight() <= 10)
            return;

        Log.d("TARLOGS", "setLogo 2: " + imgLogo.getWidth() + " x " + imgLogo.getHeight());
        Bitmap cropedBitmap = ImageUtils.getCropedBitmap(bitmap, 48, 48);
        Bitmap roundedCornerBitmap = ImageUtils.getRoundedCornerBitmap(cropedBitmap, 48 / 2f);
        imgLogo.post(() -> imgLogo.setImageBitmap(roundedCornerBitmap));
    }

    private void moveMap() {
        Location loc = place.getLocation();
        moveCamera(loc.getLat(), loc.getLng());
        moveCurrentMarker(loc.getLat(), loc.getLng());
    }

    private void addNotYetCard() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null)
            return;

        CardNotYetFragment card = new CardNotYetFragment();
        card.setDelegate(true);
        fragmentManager.beginTransaction().add(linear.getId(), card).commit();
    }

    private void initScroll() {
//        HashSet<View> items = new HashSet<>();
//        items.add(contBtn);
//        items.add(contCheck);
//        items.add(contTime);
//        items.add(contGuarant);
//
//        items.add(linearAnother); // TODO: 07.08.2018
//
//        int offset = (int) (-cont.getHeight() + linear.getY() + 50 * getDensity());
//        scroll.getViewTreeObserver().addOnScrollChangedListener(new ScrollAppearsListener(scroll, items, offset));
    }

    public void onBtnMap() {
        if (!mapMode) {
            contInfo.setVisibility(View.INVISIBLE);
            map.setVisibility(View.VISIBLE);
            moveMap();
            btnMap.setImageResource(R.drawable.ico_x);
            mapMode = true;
        } else {
            map.setVisibility(View.INVISIBLE);
            contInfo.setVisibility(View.VISIBLE);
            btnMap.setImageResource(R.drawable.ico_marker);
            mapMode = false;
        }
    }

    private void showPopupMenu() {
        Context context = getContext();
        if (context == null)
            return;

        PopupMenu popupMenu = new PopupMenu(context, btnMenu);
        popupMenu.inflate(R.menu.popup_place);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.details_menu_complain:
                replaceFragment(new ComplainFragment().setPlace(place));
                return true;
        }
        return false;
    }

    @Override
    public boolean onBackPressed() {
        if (needCloseActivity() && getActivity() != null) {
            getActivity().finish();
            return false;
        }

        if (getPrevClass() == null) {
            replaceFragment(getUser().isDelegateMode() ? new RafflesFragment() : new MapFragment());
            return true;
        }

        if (getPrevClass() == EditPlaceFragment.class)
            replaceFragment(new EditPlaceFragment());
        else if (getPrevClass() == MapFragment.class)
            replaceFragment(new MapFragment());
        else
            replaceFragment(new RafflesFragment());

        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        indicator.setPositionOffset(positionOffset + position);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void moveCamera(double lat, double lng) {
        CameraPosition position = new CameraPosition.Builder().target(new LatLng(lat, lng)).zoom(16).build();
        map.getMapAsync(mapboxMap -> map.post(() -> mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position))));
    }

    private void moveCurrentMarker(double lat, double lng) {
        map.getMapAsync((MapboxMap mapboxMap) -> map.post(() -> {
            if (currentMarker != null) {
                mapboxMap.removeMarker(currentMarker);
            }
            currentMarker = mapboxMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
        }));
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

    @Override
    public void onCardIinited() {

    }

    @Override
    public void onRestBtn(RaffleCardFragment card) {

    }

    @Override
    public void onPickBtn(RaffleCardFragment card) {

    }

    @Override
    public void onEditBtn(RaffleCardFragment card) {

    }

    @Override
    public void onResultBtn(RaffleCardFragment card) {

    }

    @Override
    public void onCardClick(RaffleCardFragment card) {
        replaceFragment(new EventFragment().setEvent(card.getEvent()));
    }

    public String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return ""; //или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}
