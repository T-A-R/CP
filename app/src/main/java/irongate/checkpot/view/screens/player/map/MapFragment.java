package irongate.checkpot.view.screens.player.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Projection;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.Source;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import irongate.checkpot.App;
import irongate.checkpot.GPS;
import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.model.User;
import irongate.checkpot.utils.AppLogs;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.screens.common.place.PlaceFragment;
import irongate.checkpot.view.screens.delegate.editPlace.EditPlaceFragment;
import irongate.checkpot.view.screens.player.advantage.AdvantageFragment;
import irongate.checkpot.view.screens.player.event.EventFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

import static com.mapbox.mapboxsdk.style.expressions.Expression.FormatOption.formatFontScale;
import static com.mapbox.mapboxsdk.style.expressions.Expression.format;
import static com.mapbox.mapboxsdk.style.expressions.Expression.formatEntry;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconTranslate;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textFont;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

public class MapFragment extends ScreenFragment implements SmartFragment.Listener, User.CityDetectListener, MapButtonsFragment.ButtonsListener, User.PlaceUpdateListener,
        RaffleCardViewHolder.Listener, MapListViewFragment.ListListener, MapListViewFragment.IMap, MapPlaceEventsFragment.PlaceEventsListener {
    private MapView map;
    private MapPlaceEventsFragment eventsFragment;
    private MapListViewFragment listFragment;
    private MapButtonsFragment buttons;
    private MapboxMap mapboxMap;
    private SmartFragment openedFragment;
    private Marker currentMarker;
    private boolean needEnterCity = false;
    private boolean wasBackPressed = false;
    private boolean orangeSymbol = false;
    int sourceIdMain = 0;
    int sourceIdSelect = 0;
    int sourceId = 0;
    int[] layers = new int[]{20, 0};
    private Place nearestPlace;
    private float defaultIconSize = 0.36f;
    private SymbolLayer markerGreen = null;
    private SymbolLayer markerOrange = null;
    private boolean isEventsShow = false;

    public MapFragment() {
        super(R.layout.fragment_map);
    }

    @Override
    public boolean isMenuShown() {
        return !getUser().isFirstStartMap();
    }

    public MapFragment setNeedEnterCity(boolean needEnterCity) {
        this.needEnterCity = needEnterCity;
        return this;
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onReady() {

        if (MainActivity.EVOTOR_MODE) {
            AppLogs.setMapLogs("MapFragment: onReady OK");
            App.getMetricaLogger().log("MapFragment: onReady OK");
            Toast.makeText(getContext(), AppLogs.getMapLogs(), Toast.LENGTH_SHORT).show();
        }

        MainFragment.enableSideMenu();

        map = (MapView) findViewById(R.id.map);
        map.onCreate(null);
        map.getMapAsync(this::onMapReady);
    }

    public void onMapReady(MapboxMap mapboxMap) {

        if (MainActivity.EVOTOR_MODE) {
            AppLogs.setMapLogs("MapFragment: onMapReady OK");
            App.getMetricaLogger().log("MapFragment: onMapReady OK");
            Toast.makeText(getContext(), AppLogs.getMapLogs(), Toast.LENGTH_SHORT).show();
        }

        this.mapboxMap = mapboxMap;

        buttons = (MapButtonsFragment) findChildFragmentById(R.id.fragment_buttons);
        buttons.setBtnListener(this);

        listFragment = (MapListViewFragment) findChildFragmentById(R.id.fragment_list);
        listFragment.setListListener(this);
        listFragment.setMap(this);

        eventsFragment = (MapPlaceEventsFragment) findChildFragmentById(R.id.fragment_place_events);
        eventsFragment.setPlaceEventListener(this);

        getUser().setPlaceUpdateListener(this);

        if (getUser().getCityName() == null) {
            showScreensaver(R.string.map_city_detecting, false);
            getUser().setCityDetectListener(this);
            return;
        }

        if (getUser().isFirstStartMap()) {
            onCityDetected();
            return;
        }

        moveCameraToCity();

        if (needEnterCity) {
            openFragment(new MapEnterCityFragment());
            return;
        }

        startFunctional();
        mapboxMap.addOnMapClickListener(this::onMapClick);
    }

    @Override
    public void onCityDetected() {
        if (getContext() == null)
            return;

        if (MainActivity.EVOTOR_MODE) {
            AppLogs.setMapLogs("MapFragment: onCityDetected OK");
            App.getMetricaLogger().log("MapFragment: onCityDetected OK");
            Toast.makeText(getContext(), AppLogs.getMapLogs(), Toast.LENGTH_SHORT).show();
        }

        hideScreensaver();
        moveCameraToCity();

        openFragment(new MapFirstStartFragment());
    }

    private void openFragment(SmartFragment fragment) {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;
        if (MainActivity.EVOTOR_MODE) {
            AppLogs.setMapLogs("MapFragment: openFragment OK");
            App.getMetricaLogger().log("MapFragment: openFragment OK");
            Toast.makeText(getContext(), AppLogs.getMapLogs(), Toast.LENGTH_SHORT).show();
        }

        activity.getSupportFragmentManager().beginTransaction().add(R.id.cont_fragments, fragment).commit();
        fragment.setListener(this);
        openedFragment = fragment;
    }

    private void closeFragment() {
        FragmentActivity activity = getActivity();
        if (openedFragment == null || activity == null) {
            return;
        }

        openedFragment.setListener(null);
        activity.getSupportFragmentManager().beginTransaction().remove(openedFragment).commit();
        hideKeyboard();
    }

    @Override
    public void fragmentIntent(SmartFragment fragment, String intent) {
        Activity activity = getActivity();
        if (activity == null)
            return;

        closeFragment();

        switch (intent) {
            case MapFirstStartFragment.INTENT_START:
                getUser().setFirstStartMap(false);
                startFunctional();
                break;

            case MapFirstStartFragment.INTENT_NOT_MINE:
                openFragment(new MapEnterCityFragment());
                break;

            case MapFirstStartFragment.INTENT_BUSINESS:
                if (getUser().getRestaurant() == null)
                    replaceFragment(new AdvantageFragment());
                else {
                    getUser().setDelegateMode(true);
                    replaceFragment(getUser().getPlace() != null ? new RafflesFragment() : new EditPlaceFragment());
                }
                break;

            case MapEnterCityFragment.INTENT_OK:
            case MapEnterCityFragment.INTENT_BACK:
                moveCameraToCity();
                if (getUser().isFirstStartMap())
                    openFragment(new MapFirstStartFragment());
                else {
                    startFunctional();
                }
        }
    }

    private void startFunctional() {
        showMenu();
        buttons.show();
        listFragment.showPanel();
        getUser().updatePlacesByCurrentCity();
    }

    @Override
    public void getCameraBounds(CameraBoundsCallback callback) {
        Projection projection = mapboxMap.getProjection();
        LatLng nortWest = projection.fromScreenLocation(new PointF(0f, 0f));
        LatLng southEast = projection.fromScreenLocation(new PointF(map.getWidth(), map.getHeight()));
        callback.onCameraBounds(nortWest, southEast);
    }

    @Override
    public void onListShown() {
        buttons.hideAim();
    }

    @Override
    public void onListHiden() {
        buttons.showAim();
    }

    @Override
    public void openScreen(ScreenFragment screen) {
        replaceFragment(screen);
    }

    public void onMapClick(@NonNull LatLng point) {
        int zoom = (int) mapboxMap.getCameraPosition().zoom;
        double clusterDistance = MetersPerPixel.getMetersPerPixel().get(zoom) * 10;

        List<Place> places = getUser().getCityPlaces();
        if (places == null || places.isEmpty())
            return;

        nearestPlace = places.get(0);
        double minDist = Double.MAX_VALUE;
        List<Place> clusterPlaces = new ArrayList<>();

        for (Place place : places) {
            if (getRelevantEvents(place.getEvents()).size() == 0)
                continue;

            double placeDist = point.distanceTo(new LatLng(place.getLocation().getLat(), place.getLocation().getLng()));
            if (clusterDistance > placeDist) {
                clusterPlaces.add(place);
            }

            if (placeDist < minDist) {
                minDist = placeDist;
                nearestPlace = place;
            }
        }

        if (clusterPlaces.size() > 1)
            moveCamera(point.getLatitude(), point.getLongitude(), zoom + 2);

        markerGreen = (SymbolLayer) mapboxMap.getLayer("selected-marker-layer-green" + sourceIdSelect);
        markerOrange = (SymbolLayer) mapboxMap.getLayer("selected-marker-layer-orange" + sourceIdSelect);
        final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);
        List<Feature> features = new ArrayList<>();
        List<Feature> clusterFeatures = new ArrayList<>();
        for (int i = 0; i < layers.length; i++) {
            clusterFeatures.addAll(mapboxMap.queryRenderedFeatures(pixel, "clusterOrange" + sourceId + "-" + i));
            clusterFeatures.addAll(mapboxMap.queryRenderedFeatures(pixel, "clusterGreen" + sourceId + "-" + i));

            features.addAll(clusterFeatures);
        }

        if (clusterPlaces.size() > 1) {
            deselectMarker(markerOrange);
            deselectMarker(markerGreen);
        }
        if (features.isEmpty()) {
            deselectMarker(markerGreen);
            deselectMarker(markerOrange);
            return;
        }

        FeatureCollection featureCollection = FeatureCollection.fromFeatures(new Feature[]{Feature.fromGeometry(features.get(0).geometry())});
        GeoJsonSource source = mapboxMap.getSourceAs("selected-marker" + sourceIdSelect);

        if (source != null)
            source.setGeoJson(featureCollection);

        if (clusterPlaces.size() > 1) {
            deselectMarker(orangeSymbol ? markerOrange : markerGreen);
        } else {
            selectMarker(markerGreen, clusterPlaces.get(0).getIsColoredOnMap());
            openEvents(nearestPlace);
        }
    }

    private List<Event> getRelevantEvents(List<Event> events) {
        List<Event> filtered = new ArrayList<>();
        for (Event e : events) {
            if (isRelevantEvent(e))
                filtered.add(e);
        }
        return filtered;
    }

    private boolean isRelevantEvent(Event event) {
        return event.getMainPrize() != null
                && !event.getBanned()
                && !event.getIsDone()
                && (buttons.getFilterReceipt() < 0 || event.getMainPrize().getMinReceipt() < buttons.getFilterReceipt());
    }

    private void selectMarker(SymbolLayer marker, boolean orange) {
        if (marker == null)
            return;

        marker.setProperties(PropertyFactory.iconImage(orange ? "my-marker-image-orange" : "my-marker-image-green"));
        marker.setProperties(PropertyFactory.iconSize(orange ? (float)(defaultIconSize * 2.85) : (float)(defaultIconSize * 1.5)));
    }

    private void openEvents(Place place) {
        List<Event> relevantEvents = getRelevantEvents(place.getEvents());
        isEventsShow = true;
        eventsFragment.showEvents(relevantEvents, place);
    }

    interface CameraBoundsCallback {
        void onCameraBounds(LatLng nortWest, LatLng southEast);
    }

    private void moveCamera(double lat, double lng) {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(8.5)
                .build();
        map.post(() -> mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position)));
    }

    @SuppressWarnings("SameParameterValue")
    private void moveCamera(double lat, double lng, int zoom) {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(zoom)
                .build();
        map.post(() -> mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position)));
    }

    private void moveCurrentMarker(double lat, double lng) {
        map.post(() -> {
            if (currentMarker != null) {
                mapboxMap.removeMarker(currentMarker);
            }
            currentMarker = mapboxMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
        });
    }

    private void moveCameraToCity() {
        GPS.getAddress(getContext(), getUser().getCityName(), address -> {
            if (address != null)
                moveCamera(address.getLatitude(), address.getLongitude());
        });
    }

    @SuppressWarnings("SameParameterValue")
    private void moveCameraToCurrent(boolean marker) {
        if (getView() == null)
            return;

        getUser().getCurrentLocation(location -> {
            if (location == null)
                return;

            moveCamera(location.getLatitude(), location.getLongitude());
            if (marker)
                moveCurrentMarker(location.getLatitude(), location.getLongitude());
        });
    }

    @Override
    public void onPlacesUpdated() {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        List<Place> cityPlaces = getUser().getCityPlaces();
        if (cityPlaces == null) {
            activity.runOnUiThread(() -> Toast.makeText(getContext(), R.string.map_city_fail_raffles, Toast.LENGTH_SHORT).show());
            return;
        }

        addClusteredGeoJsonSource();
        listFragment.updateList();
        mapboxMap.addOnMapClickListener(this::onMapClick);
    }

    private void addClusteredGeoJsonSource() {
        Context context = getContext();
        if (context == null)
            return;

        List<Place> places = getUser().getCityPlaces();
        buttons.setPlaceList(places, place -> {
            moveCamera(place.getLocation().getLat(), place.getLocation().getLng(), 16);
            buttons.hidePanel();
        });

        FeatureCollection collection = FeatureCollection.fromFeatures(getFeatures());
        FeatureCollection collectionOrange = FeatureCollection.fromFeatures(getFeaturesOrange());

        mapboxMap.addImage("plus-green-round-icon", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_oval_green)));

        mapboxMap.addImage("plus-orange-round-icon", BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.ic_orange)));

        sourceIdMain = (int) (Math.random() * Integer.MAX_VALUE);
        sourceId = (int) (Math.random() * Integer.MAX_VALUE);

        setSource(collection, collectionOrange);
        sourceIdSelect = (int) (Math.random() * Integer.MAX_VALUE);
        Bitmap iconGreen = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_oval_green);
        mapboxMap.addImage("my-marker-image-green", iconGreen);
        Bitmap iconOrange = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_orange);
        mapboxMap.addImage("my-marker-image-orange", iconOrange);

        FeatureCollection emptySource = FeatureCollection.fromFeatures(new Feature[]{});
        Source selectedMarkerSource = new GeoJsonSource("selected-marker" + sourceIdSelect, emptySource);
        mapboxMap.addSource(selectedMarkerSource);

        if (!orangeSymbol) {
            SymbolLayer selectedMarkerGreen = new SymbolLayer("selected-marker-layer-green" + sourceIdSelect, "selected-marker" + sourceIdSelect)
                    .withProperties(PropertyFactory.iconImage("my-marker-image-green"), iconSize((float)(defaultIconSize * 1.5)));
            mapboxMap.addLayer(selectedMarkerGreen);
        }

        if (orangeSymbol) {
            SymbolLayer selectedMarkerOrange = new SymbolLayer("selected-marker-layer-orange" + sourceIdSelect, "selected-marker" + sourceIdSelect)
                    .withProperties(PropertyFactory.iconImage("my-marker-image-orange"), iconSize((float) (defaultIconSize * 2.85)));
            mapboxMap.addLayer(selectedMarkerOrange);
        }
    }

    private void setSource(FeatureCollection collection, FeatureCollection collectionOrange) {

        for (int i = 0; i < layers.length; i++) {
            for (int q = 0; q < mapboxMap.getSources().size(); q++) {
                Source source = mapboxMap.getSources().get(q);
                if (source instanceof GeoJsonSource) {
                    ArrayList<Feature> features1 = new ArrayList<>();
                    FeatureCollection collection1 = FeatureCollection.fromFeatures(features1);
                    ((GeoJsonSource) source).setGeoJson(collection1);
                }
            }

            mapboxMap.addSource(
                    new GeoJsonSource("earthquakes" + i + sourceId
                            , collection,
                            new GeoJsonOptions()
                                    .withCluster(true)
                                    .withClusterMaxZoom(14)
                                    .withClusterRadius(50)
                    )
            );

            mapboxMap.addSource(
                    new GeoJsonSource("earthquakesOrange" + i + sourceId
                            , collectionOrange,
                            new GeoJsonOptions()
                                    .withCluster(true)
                                    .withClusterMaxZoom(14)
                                    .withClusterRadius(50)
                    )
            );

            SymbolLayer symbolLayerGreen = new SymbolLayer("clusterGreen" + sourceId + "-" + i, "earthquakes" + i + sourceId);
            SymbolLayer symbolLayerOrange = new SymbolLayer("clusterOrange" + sourceId + "-" + i, "earthquakesOrange" + i + sourceId);

            layerSetProperties("plus-orange-round-icon", symbolLayerOrange, (float)(defaultIconSize * 1.9));
            mapboxMap.addLayer(symbolLayerOrange);
            layerSetProperties("plus-green-round-icon", symbolLayerGreen, defaultIconSize);
            mapboxMap.addLayer(symbolLayerGreen);
        }
    }

    private List<Place> getFilteredPlaces() {
        List<Place> filteredPlaces = new ArrayList<>();
        List<Place> places = getUser().getCityPlaces();
        for (Place place : places) {
            List<Event> relevantEvents = getRelevantEvents(place.getEvents());
            if (relevantEvents.isEmpty())
                continue;

            Place filteredPlace = Place.clone(place);
            filteredPlace.setEvents(relevantEvents);
            filteredPlaces.add(filteredPlace);
        }
        return filteredPlaces;
    }

    private ArrayList<Feature> getFeatures() {
        ArrayList<Feature> features = new ArrayList<>();
        List<Place> filteredPlaces = getFilteredPlaces();
        for (Place place : filteredPlaces) {
            if (!place.getIsColoredOnMap())
                features.add(Feature.fromJson("{\"type\":\"Feature\",\"properties\":{\"name\":\"" + "ghjkj" + "\",\"is_colored\":\"" + place.getIsColoredOnMap() + "\", \"events_count\": \"" + String.valueOf(place.getEvents().size()) + "\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":" + place.getLocation().getCoordinates().toString() +
                        "}}"));
        }
        return features;
    }

    private ArrayList<Feature> getFeaturesOrange() {
        ArrayList<Feature> features = new ArrayList<>();
        List<Place> filteredPlaces = getFilteredPlaces();
        for (Place place : filteredPlaces) {
            if (place.getIsColoredOnMap())
                features.add(Feature.fromJson("{\"type\":\"Feature\",\"properties\":{\"name\":\"" + "ghjkj" + "\",\"is_colored\":\"" + place.getIsColoredOnMap() + "\", \"events_count\": \"" + String.valueOf(place.getEvents().size()) + "\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":" + place.getLocation().getCoordinates().toString() +
                        "}}"));
        }
        return features;
    }

    private void deselectMarker(SymbolLayer marker) {
        if (marker != null) {
            marker.setProperties(PropertyFactory.iconImage(" "));
        }
        eventsFragment.hideEvents();
        isEventsShow = false;
    }

    private void layerSetProperties(String imageId, SymbolLayer symbolLayer, float iconSize) {
        symbolLayer.setProperties(
                iconImage(imageId),
                iconTranslate(new Float[]{0f}),
                textField(
                        format(
                                formatEntry(Expression.toString(get("events_count")), formatFontScale(1.2)))),
                textFont(new String[]{"Roboto Bold"}),
                textSize(10f),
                iconSize(iconSize),
                textColor(Color.WHITE),
                textIgnorePlacement(true),
                textAllowOverlap(true)
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainFragment.enableSideMenu();
        map.onResume();
        if (isEventsShow) {
            openEvents(nearestPlace);
        }
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
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this::onMapClick);
        }
        map.onDestroy();
    }

    @Override
    public boolean onBackPressed() {
        if (isEventsShow) {
            deselectMarker(orangeSymbol ? markerOrange : markerGreen);
            return true;
        }
        if (openedFragment != null) {
            if (openedFragment.onBackPressed())
                return true;
        }

        if (wasBackPressed) {
            return super.onBackPressed();
        }

        wasBackPressed = true;
        Toast.makeText(getContext(), R.string.map_doubleclick_exit, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onBtnCurrent() {
        moveCameraToCurrent(true);
    }

    @Override
    public void onFilteredList() {
        addClusteredGeoJsonSource();
    }

    @Override
    public void onFilterCheck() {
        addClusteredGeoJsonSource();
    }

    @Override
    public void onMapButtonsClose() {
        addClusteredGeoJsonSource();
    }

    @Override
    public void onFilterClicked() {
        addClusteredGeoJsonSource();
    }

    @Override
    public void onRestBtn(Place place) {
        replaceFragment(new PlaceFragment().setPlace(place));
    }

    @Override
    public void onCardClick(Event event) {
        replaceFragment(new EventFragment().setEvent(event));
    }
}