package irongate.checkpot.view.screens.player.event;

import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.screens.common.place.PlaceFragment;
import irongate.checkpot.view.screens.player.rafles.RaffleCardFragment;

public class MoreRafflesFragment extends ScreenFragment implements RaffleCardFragment.CardListener {
    private Event event;

    public MoreRafflesFragment() {
        super(R.layout.fragment_more_raffles);
    }

    public MoreRafflesFragment setEvent(Event event) {
        this.event = event;
        setCardData();
        return this;
    }

    @Override
    protected void onReady() {

    }

    private void setCardData() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null)
            return;

        Place place = event.getPlace();
        if (event != null && event.getPlace() != null) {
            List<Event> events = place.getEvents();
            ArrayList<SmartFragment> cards = new ArrayList<>();

            for (int i = 0; i < events.size(); i++) {

                Log.d(MainActivity.TAG, "^^^^^^^^^^^^^^^^^^^^^^^^^ setCardData: " + events.get(i).getUuid().equals(event.getUuid()));
                if (events.get(i) == event)
                    continue;
                if(events.get(i).getUuid().equals(event.getUuid()))
                    continue;
                if (!events.get(i).getBanned() && !events.get(i).getIsDone()) {
                    RaffleCardFragment card = new RaffleCardFragment();
                    card.setHeightDp(383);
                    card.setEvent(events.get(i));
                    card.setCardListener(this);
                    fragmentManager.beginTransaction().add(R.id.linear_more, card).commit();
                    cards.add(card);
                }
            }
        }
    }

    @Override
    public void onCardIinited() {

    }

    @Override
    public void onRestBtn(RaffleCardFragment card) {
        replaceFragment(new PlaceFragment().setPlace(card.getEvent().getPlace()));
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
}
