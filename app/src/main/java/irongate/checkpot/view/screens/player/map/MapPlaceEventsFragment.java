package irongate.checkpot.view.screens.player.map;

import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.screens.common.place.PlaceFragment;
import irongate.checkpot.view.screens.player.event.EventFragment;
import irongate.checkpot.view.screens.player.rafles.RaffleCardFragment;
import irongate.checkpot.view.screens.player.rafles.RaffleCardNotActiveFragment;

public class MapPlaceEventsFragment extends SmartFragment implements SmartFragment.Listener, RaffleCardFragment.CardListener, RaffleCardFragment.SliderListener {
    private LinearLayout linearPlaceRaffles;
    private LockableScrollView scrollPlaceRaffles;

    private PlaceEventsListener listener;
    private ArrayList<RaffleCardFragment> cards;
    private RaffleCardNotActiveFragment notActiveCard;

    public MapPlaceEventsFragment() {
        super(R.layout.fragment_map_place_events);
    }

    public void setPlaceEventListener(PlaceEventsListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onReady() {
        scrollPlaceRaffles = (LockableScrollView) findViewById(R.id.scroll);
        linearPlaceRaffles = (LinearLayout) findViewById(R.id.linear);
    }

    public void showEvents(List<Event> events, Place place) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null)
            return;

        hideEvents();

        scrollPlaceRaffles.setVisibility(View.VISIBLE);
        scrollPlaceRaffles.requestDisallowInterceptTouchEvent(true);

        cards = new ArrayList<>();
        if (events.size() == 0) {
            notActiveCard = new RaffleCardNotActiveFragment();
            notActiveCard.setPlace(place);
            notActiveCard.setWidthDp(334);
            notActiveCard.setListener(this);
            fragmentManager.beginTransaction().add(linearPlaceRaffles.getId(), notActiveCard).commit();
        } else {
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                event.setPlace(place);
                RaffleCardFragment card = new RaffleCardFragment();
                card.setWidthDp(290);
                card.setHeightDp(310);
                card.setWidthCardImageDp(290);
                card.setHeighCardImageDp(140);
                card.setFontSize(31);
                card.setPrizeNameSize(80);
                card.setEvent(event);
                card.setListener(this);
                card.setCardListener(this);
                card.setSliderListener(this);
                fragmentManager.beginTransaction().add(linearPlaceRaffles.getId(), card).commit();
                cards.add(card);
            }
        }
    }

    public void hideEvents() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null) return;

        if (cards != null) {
            for (SmartFragment frag : cards) {
                fragmentManager.beginTransaction().remove(frag).commit();
            }
            cards = null;
        }

        if (notActiveCard != null) {
            fragmentManager.beginTransaction().remove(notActiveCard).commit();
            notActiveCard = null;
        }

        scrollPlaceRaffles.setVisibility(View.GONE);
    }

    @Override
    public void onSliderTouchEvent(RaffleCardFragment card, MotionEvent event) {
        if (card.getImages().size() <= 1) return;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scrollPlaceRaffles.setScrollingEnabled(false);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                scrollPlaceRaffles.setScrollingEnabled(true);
                break;
        }
    }

    @Override
    public void fragmentIntent(SmartFragment fragment, String intent) {
        if (fragment instanceof RaffleCardNotActiveFragment && intent.equals(RaffleCardNotActiveFragment.INTENT_PLACE)) {
            listener.openScreen(new PlaceFragment().setPlace(((RaffleCardNotActiveFragment) fragment).getPlace()));
        }
    }

    @Override
    public void onCardIinited() {
    }

    @Override
    public void onRestBtn(RaffleCardFragment card) {
        listener.openScreen(new PlaceFragment().setPlace(card.getEvent().getPlace()));
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
        listener.openScreen(new EventFragment().setEvent(card.getEvent()));
    }

    interface PlaceEventsListener {
        void openScreen(ScreenFragment screen);
    }
}