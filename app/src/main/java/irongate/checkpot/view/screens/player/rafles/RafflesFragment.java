package irongate.checkpot.view.screens.player.rafles;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.ScrollAppearsListener;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.screens.common.CardNotYetFragment;
import irongate.checkpot.view.screens.common.place.PlaceFragment;
import irongate.checkpot.view.screens.delegate.NotActivatedFragment;
import irongate.checkpot.view.screens.delegate.addedRaffle.AddRaffleFragment;
import irongate.checkpot.view.screens.delegate.companyRegistration.AddSignFragment;
import irongate.checkpot.view.screens.delegate.information.EventInformationFragment;
import irongate.checkpot.view.screens.player.event.EventFragment;
import irongate.checkpot.view.screens.player.map.MapFragment;
import irongate.checkpot.view.screens.player.results.GuarantFragment;
import irongate.checkpot.view.screens.player.results.LoserFragment;
import irongate.checkpot.view.screens.player.results.WinnerFragment;

import static irongate.checkpot.utils.Requests.TAG;

public class RafflesFragment extends ScreenFragment implements RaffleCardFragment.CardListener, SmartFragment.Listener {
    private ArrayList<SmartFragment> cards;

    private RelativeLayout cont;
    private ScrollView scroll;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private LinearLayout linear;

    private RafflesPickFragment pickFragment;
    private SmartFragment openedFragment;

    private boolean wins = false;
    private boolean done = false;
    private boolean cardsInited = false;
    private int curTab;
    private boolean wasBackPressed = false;

    private List<Event> events;

    public RafflesFragment() {
        super(R.layout.fragment_raffles);
    }

    @Override
    protected void onReady() {

        if (pickFragment != null) {
            pickFragment.onBackPressed();
        }

        cont = (RelativeLayout) findViewById(R.id.cont);
        scroll = (ScrollView) findViewById(R.id.scroll);
        TextView txtTitle = (TextView) findViewById(R.id.title);
        btn1 = (Button) findViewById(R.id.btn_1);
        btn2 = (Button) findViewById(R.id.btn_2);
        btn3 = (Button) findViewById(R.id.btn_3);
        linear = (LinearLayout) findViewById(R.id.linear);

        txtTitle.setTypeface(Fonts.getFuturaPtMedium());
        btn1.setTypeface(Fonts.getFuturaPtBook());
        btn1.setTransformationMethod(null);
        btn2.setTypeface(Fonts.getFuturaPtBook());
        btn2.setTransformationMethod(null);
        btn3.setTypeface(Fonts.getFuturaPtBook());
        btn3.setTransformationMethod(null);

        btn1.setText(isDelegateScreen() ? R.string.raffles_btn_maquette : R.string.raffles_btn_current);
        btn2.setText(isDelegateScreen() ? R.string.raffles_btn_active : R.string.raffles_btn_win);

        btn1.setBackgroundResource(isDelegateScreen() ? R.drawable.bg_btn_blue_22 : R.drawable.bg_btn_green_22);
        btn2.setBackgroundResource(isDelegateScreen() ? R.drawable.bg_btn_blue_22 : R.drawable.bg_btn_green_22);
        btn3.setBackgroundResource(isDelegateScreen() ? R.drawable.bg_btn_blue_22 : R.drawable.bg_btn_green_22);

        txtTitle.startAnimation(Anim.getAppearSlide(getContext()));
        btn1.startAnimation(Anim.getAppearSlide(getContext(), 500));
        btn2.startAnimation(Anim.getAppearSlide(getContext(), 700));
        btn3.startAnimation(Anim.getAppearSlide(getContext(), 900));
        linear.startAnimation(Anim.getAppear(getContext(), 1500));

        btn1.setOnClickListener(v -> setTab(1));
        btn2.setOnClickListener(v -> setTab(2));
        btn3.setOnClickListener(v -> setTab(3));

        MainFragment.enableSideMenu();

        setTab(done ? 3 : wins ? 2 : 1);
    }

    @Override
    public boolean isDelegateScreen() {
        return getUser().isDelegateMode();
    }

    @Override
    public boolean isMenuShown() {
        return true;
    }

    public boolean isWins() {
        return wins;
    }

    public RafflesFragment setWins(boolean wins) {
        this.wins = wins;
        return this;
    }

    public RafflesFragment setDone(boolean done) {
        this.done = done;
        return this;
    }

    public void updateList() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null)
            return;

        if (pickFragment != null) pickFragment.onBackPressed();

        removeCardFragments();

        events = new ArrayList<>();
        boolean delegateMode = getUser().isDelegateMode();
        if (!delegateMode) {
            if (getUser() != null && getUser().getCurrentUser() != null) {
                List<Event> userEvents = getUser().getCurrentUser().getEvents();
                if (userEvents != null)
                    for (int i = 0; i < userEvents.size(); i++) {
                        Event event = userEvents.get(i);
                        if (!event.getBanned() && ((!btn1.isEnabled() && !event.getIsDone()) ||
                                (!btn2.isEnabled() && event.getIsDone() && isImWinner(event)) ||
                                (!btn3.isEnabled() && event.getIsDone() && !isImWinner(event))))
                            addNoDub(event);
                    }
            }
        } else {
            Place place = getUser().getPlace();
            if (place != null) {
                if (getUser().getRestaurant().getInnkpp() != null) {

                    if (getUser().getRestaurant().getDocuments() == null || getUser().getRestaurant().getDocuments().size() == 0) {
                        MainFragment.disableSideMenu();
                        hideMenu();
                        AddSignFragment addSignFragment = new AddSignFragment();
                        addSignFragment.setiMainFragment(getMain());
                        showSignFragment(addSignFragment);
                    } else {
                        Log.d(MainActivity.TAG, "SIGN is OK: ");
                    }
                } else {
                    Log.d(MainActivity.TAG, "INNKPP is NULL");
                }
                List<Event> delegEvents = place.getEvents();
                for (int i = 0; i < delegEvents.size(); i++) {
                    Event event = delegEvents.get(i);
                    event.setPlace(place);
                    if ((!btn1.isEnabled() && event.getBanned()) ||
                            (!btn2.isEnabled() && !event.getIsDone()) && !event.getBanned() ||
                            (!btn3.isEnabled() && event.getIsDone()))
                        addNoDub(event);
                }
            }
        }

        cards = new ArrayList<>();
        if (events.size() > 0) {
            for (int i = events.size() - 1; i >= 0; i--) {
                Event event = events.get(i);
                RaffleCardFragment card = new RaffleCardFragment();
                card.setHeightDp(390);
                card.setEvent(event);
                fragmentManager.beginTransaction().add(linear.getId(), card).commitAllowingStateLoss();
                card.setCardListener(this);

                if (!delegateMode && curTab == 2) {
                    card.setLockPrize(event.getMyPrize());
                    if (!event.getMyPrize().getIsRandom()) {
                        card.setHeightDp(200);
                    }
                } else if (curTab == 3)
                    card.setLockPrize(event.getMainPrize());

                cards.add(card);
            }
        } else {
            CardNotYetFragment card = null;
            if (!delegateMode && curTab != 3) {
                card = new CardNotYetFragment();
            } else if (delegateMode && curTab != 3 && getUser().getPlace().getEvents().size() == 0) {
                card = new CardNotYetFragment().setDelegate(true);
            }

            if (card != null) {
                fragmentManager.beginTransaction().add(linear.getId(), card).commitAllowingStateLoss();
                card.setCardListener(() -> {
                    if(!delegateMode) {
                        replaceFragment(new MapFragment());
                    } else {
                        replaceFragment(getUser().isActive() ? new AddRaffleFragment() : new NotActivatedFragment());
                    }

                });
                cards.add(card);
            }
        }
    }

    private boolean isImWinner(Event event) {
        return event.getMyRandomPrize() != null || event.getMyGuarantedPrize() != null;
    }

    private void removeCardFragments() {
        FragmentManager fragmentManager = getFragmentManager();
        if (cards == null || fragmentManager == null) {
            return;
        }

        for (SmartFragment frag : cards) {
            fragmentManager.beginTransaction().remove(frag).commitAllowingStateLoss();
        }
        cards = null;
        cardsInited = false;
    }

    public void setTab(int i) {
        curTab = i;
        setBtnPressed(btn1, i == 1);
        setBtnPressed(btn2, i == 2);
        setBtnPressed(btn3, i == 3);

        if (!getUser().isDelegateMode()) {
            setMenuCursor(i == 2 ? 3 : 2);
        }

        updateList();
        if (curTab == 1) {
            showScreensaver(false);
            getUser().updateUser(ok -> {
                hideScreensaver();
                Context context = getContext();
                if (!ok && context != null) {
                    Log.d(MainActivity.TAG, "USER UPDATE: ERROR");
                    Toast.makeText(context, R.string.raffles_fail, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(MainActivity.TAG, "USER UPDATE: OK");
                }

                updateList();
            });
        }
    }

    private void setBtnPressed(Button btn, boolean pressed) {
        btn.setBackgroundResource(!pressed ? R.drawable.bg_btn_trans : (isDelegateScreen() ? R.drawable.bg_btn_blue_22 : R.drawable.bg_btn_green_22));
        btn.setTextColor(!pressed ? Color.BLACK : Color.WHITE);
        btn.setEnabled(!pressed);
    }

    @Override
    public void onCardIinited() {
        if (cardsInited)
            return;

        cardsInited = true;

        HashSet<View> scrollItems = new HashSet<>();
        for (int i = 0; i < cards.size(); i++) {
            scrollItems.add(cards.get(i).getView());
        }
        int offset = (int) (-cont.getHeight() + linear.getY() * 1.5f);
        ScrollAppearsListener scrollListener = new ScrollAppearsListener(scroll, scrollItems, offset);
        scroll.getViewTreeObserver().addOnScrollChangedListener(scrollListener);
    }

    @Override
    public void onRestBtn(RaffleCardFragment card) {
        replaceFragment(new PlaceFragment().setPlace(card.getEvent().getPlace()));
    }

    @Override
    public void onPickBtn(RaffleCardFragment card) {
        showPickFragment(card.getEvent());
    }

    @Override
    public void onEditBtn(RaffleCardFragment card) {
        showAddRaffleFragment(card.getEvent());
    }

    @Override
    public void onResultBtn(RaffleCardFragment card) {
        replaceFragment(new EventInformationFragment().setEvent(card.getEvent()));
    }

    @Override
    public void onCardClick(RaffleCardFragment card) {
        Event event = card.getEvent();

        if (getUser().isDelegateMode() && curTab == 3) {
        } else {
            if (!event.getIsDone()) {
                replaceFragment(new EventFragment().setEvent(card.getEvent()));
                return;
            }

            if (event.getMyRandomPrize() != null) {
                replaceFragment(new WinnerFragment().setEvent(event));
                return;
            }

            if (event.getMyGuarantedPrize() != null) {
                replaceFragment(new GuarantFragment().setPrize(event.getMyGuarantedPrize()));
                return;
            }

            replaceFragment(new LoserFragment());
        }
    }

    private void showPickFragment(Event event) {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        pickFragment = new RafflesPickFragment().setEvent(event);
        hideMenu();
        activity.getSupportFragmentManager().beginTransaction().add(R.id.cont_pick, pickFragment).commitAllowingStateLoss();
        pickFragment.setListener(this);
    }

    private void showAddRaffleFragment(Event event) {
        replaceFragment(new AddRaffleFragment().setEvent(event));
    }

    @Override
    public void fragmentIntent(SmartFragment fragment, String intent) {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        pickFragment.setListener(null);
        activity.getSupportFragmentManager().beginTransaction().remove(pickFragment).commitAllowingStateLoss();
        showMenu();
        pickFragment = null;
    }

    @Override
    public boolean onBackPressed() {
        if (pickFragment != null) {
            pickFragment.onBackPressed();
            showMenu();
            return true;
        }

        if (!getUser().isDelegateMode()) {
            replaceFragment(new MapFragment());
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
    public void onResume() {
        super.onResume();
        showMenu();
        MainFragment.enableSideMenu();
    }

    private void showSignFragment(SmartFragment fragment) {

        FragmentActivity activity = getActivity();
        if (activity == null || openedFragment != null) {
            return;
        }
        activity.getSupportFragmentManager().beginTransaction().add(R.id.cont, fragment, "123").commitAllowingStateLoss();
        fragment.setListener(this);
        openedFragment = fragment;

    }

    public void addNoDub(Event event) {
        boolean dublicate = false;
        for (int i = 0; i < events.size(); i++) {

            if (event.getUuid().equals(events.get(i).getUuid())) {
                dublicate = true;
            }
        }
        if (!dublicate) {
            Log.d("ADDLOGS", "addNoDub: " + event.getMainPrize().getName() + " " + event.getBanned() + event.getPrice());
            if (event.getBanned() && event.getPrice() > 0) {
                //Пропускаем новый эвент ожидающий оплату
                Log.d("ADDLOGS", "addNoDub: " + event.getUuid() +" " + event.getMainPrize().getName() + " " + event.getBanned() + " " + event.getPrice() +
                        " " + event.getMembersCount() + " " + event.getEnd());
            } else
                events.add(event);
        }
    }
}
