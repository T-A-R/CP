package irongate.checkpot.view.screens.player.map;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.List;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Location;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.model.User;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.screens.common.place.PlaceFragment;
import irongate.checkpot.view.screens.player.event.EventFragment;

public class MapListViewFragment extends SmartFragment implements RaffleCardViewHolder.Listener {
    private View panel;
    private Button btnMap;
    private Button btnList;
    private RecyclerView recyclerMapRaffles;
    private RelativeLayout contListRaffles;

    private IMap map;
    private ListListener listener;
    private boolean panelShown = false;
    private LatLng nortWest;
    private LatLng southEast;
    private boolean listMode = false;
    private RaffleCardAdapter adapter;

    public MapListViewFragment() {
        super(R.layout.fragment_map_list_view);
    }

    public void setMap(IMap map) {
        this.map = map;
    }

    public void setListListener(ListListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onReady() {
        panel = findViewById(R.id.panel);
        btnMap = (Button) findViewById(R.id.btn_map);
        btnList = (Button) findViewById(R.id.btn_list);
        contListRaffles = (RelativeLayout) findViewById(R.id.cont_list_raffles);

        recyclerMapRaffles = (RecyclerView) findViewById(R.id.recycler_map_raffles);
        recyclerMapRaffles.setHasFixedSize(true);
        recyclerMapRaffles.setLayoutManager(new LinearLayoutManager(getContext()));

        btnMap.setTransformationMethod(null);
        btnList.setTransformationMethod(null);

        panel.setVisibility(panelShown ? View.VISIBLE : View.GONE);

        contListRaffles.setOnClickListener(v -> {
        });
        btnList.setOnClickListener(v -> showList());
        btnMap.setOnClickListener(v -> hideList());
        setActiveBtn(btnMap);
    }

    public void showPanel() {
        if (panel == null) {
            panelShown = true;
            return;
        }
        panel.setVisibility(View.VISIBLE);
        panel.setAnimation(Anim.getAppearSlide(getContext()));
    }

    private void showList() {
        if (listMode)
            return;

        listMode = true;
        setActiveBtn(btnList);
        contListRaffles.clearAnimation();
        contListRaffles.setAnimation(Anim.getAppear(getContext()));
        contListRaffles.setVisibility(View.VISIBLE);

        listener.onListShown();

        map.getCameraBounds((nortWest, southEast) -> {
            this.nortWest = nortWest;
            this.southEast = southEast;
            updateList();
        });
    }

    public void updateList() {
        if (!listMode)
            return;

        List<Place> places = User.getUser().getCityPlaces();
        if (places == null)
            return;

        ArrayList<Event> eventsList = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            Place place = places.get(i);
            Location location = place.getLocation();
            if (location.getLat() > nortWest.getLatitude() ||
                    location.getLat() < southEast.getLatitude() ||
                    location.getLng() < nortWest.getLongitude() ||
                    location.getLng() > southEast.getLongitude())
                continue;

            List<Event> events = place.getEvents();
            if (events == null)
                continue;

            for (int j = 0; j < events.size(); j++) {
                Event event = events.get(j);
                if (event.getIsDone() || event.getBanned())
                    continue;

                event.setPlace(place);
                eventsList.add(event);
            }
        }
        adapter = new RaffleCardAdapter(eventsList, MapListViewFragment.this);
        recyclerMapRaffles.setAdapter(adapter);
    }

    private void hideList() {
        if (!listMode)
            return;

        listMode = false;
        setActiveBtn(btnMap);
        contListRaffles.startAnimation(Anim.getDisappear(getContext(), () -> {
            if (!listMode) {
                contListRaffles.clearAnimation();
                contListRaffles.setVisibility(View.GONE);
            }
        }));

        listener.onListHiden();
    }

    private void setActiveBtn(Button btn) {
        setBtnPressed(btnMap, btn == btnMap);
        setBtnPressed(btnList, btn == btnList);
    }

    private void setBtnPressed(Button btn, boolean pressed) {
        btn.setBackgroundResource(!pressed ? R.drawable.bg_btn_trans : R.drawable.bg_btn_green_22);
        btn.setTextColor(!pressed ? Color.BLACK : Color.WHITE);
        btn.setEnabled(!pressed);
    }

    @Override
    public void onRestBtn(Place place) {
        listener.openScreen(new PlaceFragment().setPlace(place));
    }

    @Override
    public void onCardClick(Event event) {
        listener.openScreen(new EventFragment().setEvent(event));
    }

    interface IMap {
        void getCameraBounds(MapFragment.CameraBoundsCallback callback);
    }

    interface ListListener {
        void onListShown();

        void onListHiden();

        void openScreen(ScreenFragment screen);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) adapter.notifyDataSetChanged();
    }
}