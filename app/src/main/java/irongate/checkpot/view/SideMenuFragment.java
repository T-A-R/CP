package irongate.checkpot.view;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Restaurant;
import irongate.checkpot.checkpotAPI.models.Tariff;
import irongate.checkpot.model.User;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.view.screens.common.contacts.ContactsFragment;
import irongate.checkpot.view.screens.delegate.CalculatorFragment;
import irongate.checkpot.view.screens.delegate.PromoFragment;
import irongate.checkpot.view.screens.delegate.editPlace.EditPlaceFragment;
import irongate.checkpot.view.screens.delegate.tariffAndPayment.ChooseTariffFragment;
import irongate.checkpot.view.screens.player.ProfileFragment;
import irongate.checkpot.view.screens.player.advantage.AdvantageFragment;
import irongate.checkpot.view.screens.player.map.MapFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;
import irongate.checkpot.view.screens.player.registration.Reg1Fragment;
import irongate.checkpot.view.screens.player.registration.WelcomeFragment;


// Не используется!
public class SideMenuFragment extends SmartFragment implements View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener, SwipeDetector.Listener, User.UserUpdateListener {

    private Listener listener;
    private View bg;
    private View panel;
    private View linearDelegate;
    private View linearPlayer;
    private TextView txtDelegate;
    private TextView txtEdit;
    private TextView txtCity;
    private ImageView imgMarker;
    private ImageView imgDrop;
    private View lineDelegate;
    private TextView txtContactsDelegate;
    private TextView txtPromo;
    private TextView txtContacts;
    private TextView txtTariffs;
    private TextView txtCalculator;
    private View lineNotify;
    private TextView txtNotufy;
    private View lineExit;
    private TextView txtId;
    private TextView txtName;
    private ImageView img;
    private ImageView ico;
    private View scroll;
    private TextView txtRaffles;
    private TextView txtDate;
    private Restaurant restaurant;

    private boolean opened = false;

    public SideMenuFragment() {
        super(R.layout.fragment_side_menu);
    }

    @Override
    protected void onReady() {
        bg = findViewById(R.id.bg);
        scroll = findViewById(R.id.scroll);
        panel = findViewById(R.id.panel);
        img = (ImageView) findViewById(R.id.img);
        ico = (ImageView) findViewById(R.id.ico);
        linearDelegate = findViewById(R.id.linear_delegate);
        linearPlayer = findViewById(R.id.linear_player);
        lineDelegate = findViewById(R.id.line_delegate);
        lineNotify = findViewById(R.id.line_notify);
        lineExit = findViewById(R.id.line_exit);
        txtName = (TextView) findViewById(R.id.txt_name);
        txtId = (TextView) findViewById(R.id.txt_id);
        txtEdit = (TextView) findViewById(R.id.txt_edit);
        imgMarker = (ImageView) findViewById(R.id.img_marker);
        txtCity = (TextView) findViewById(R.id.txt_city);
        imgDrop = (ImageView) findViewById(R.id.img_drop);
        txtContacts = (TextView) findViewById(R.id.txt_contacts);
        txtTariffs = (TextView) findViewById(R.id.txt_tariffs);
        txtCalculator = (TextView) findViewById(R.id.txt_calculator);
        txtContactsDelegate = (TextView) findViewById(R.id.txt_contacts_delegate);
        txtPromo = (TextView) findViewById(R.id.txt_promo);
        txtNotufy = (TextView) findViewById(R.id.txt_notify);
        txtDelegate = (TextView) findViewById(R.id.txt_delegate);
        txtRaffles = (TextView) findViewById(R.id.txt_raffles);
        txtDate = (TextView) findViewById(R.id.txt_date);

        bg.setOnClickListener(this);
        lineDelegate.setOnClickListener(this);
        lineNotify.setOnClickListener(this);
        lineExit.setOnClickListener(this);
        txtContacts.setOnClickListener(this);
        txtTariffs.setOnClickListener(this);
        txtCalculator.setOnClickListener(this);
        txtContactsDelegate.setOnClickListener(this);
        txtPromo.setOnClickListener(this);
        imgMarker.setOnClickListener(this);
        txtCity.setOnClickListener(this);
        imgDrop.setOnClickListener(this);
        txtEdit.setOnClickListener(this);

        txtNotufy.setText(getUser().isNotify() ? R.string.side_menu_notify_off : R.string.side_menu_notify_on);

        txtCalculator.setVisibility(View.GONE);// TODO: 19.02.2019 Временно убрано 

        if (MainActivity.EVOTOR_MODE)
            txtTariffs.setVisibility(View.GONE);

        bg.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getUser().setUserUpdateListener(this);
    }

    @Override
    public void onGlobalLayout() {
        bg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        scroll.setOnTouchListener(new SwipeDetector(this, scroll.getWidth() / 2));
        bg.setVisibility(View.GONE);
        panel.setVisibility(View.GONE);
    }

    public void show() {
        if (opened)
            return;

        restaurant = getUser().getRestaurant();

        txtCity.setText(getUser().getCityName());
        lineExit.setVisibility(getUser().isAuthorized() ? View.VISIBLE : View.GONE);

        if (!getUser().isAuthorized()) {
            txtId.setText("");
            txtName.setText(getString(R.string.side_menu_unknown_name));
        } else if (!getUser().isDelegateMode()) {
            String digitalId = getUser().getCurrentUser().getDigitalId();
            txtId.setText(getString(R.string.side_menu_id, digitalId != null ? digitalId : getUser().getUserUuid()));
            txtName.setText(getUser().getCurrentUser() != null ? getUser().getCurrentUser().getName() : getString(R.string.side_menu_unknown_name));
        } else {
            txtId.setText(getString(R.string.side_menu_id, getUser().getRestaurantContractId()));
            txtName.setText(restaurant != null ? restaurant.getLegalName() : "");

            if (restaurant == null || restaurant.getTariff() == null) {
                txtRaffles.setText(R.string.side_menu_no_tariff);
                txtDate.setVisibility(View.GONE);
            } else {
                Tariff tariff = restaurant.getTariff();
                int numEvents = tariff.getCount().intValue();
                txtRaffles.setText(String.format("Осталось %s", getResources().getQuantityString(R.plurals.raffles, numEvents, numEvents)));

                if (tariff.getTitle().equals("Простой")) {
                    txtDate.setVisibility(View.GONE);
                } else {
                    int days = (int) (30f / tariff.getAmout() * restaurant.getBalance());
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.DATE, days);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    String date = sdf1.format(c.getTime());
                    txtDate.setVisibility(View.VISIBLE);
                    txtDate.setText(String.format("%s %s", getString(R.string.side_menu_date), date));
                }
            }
        }

        bg.clearAnimation();
        panel.clearAnimation();
        bg.setVisibility(View.VISIBLE);
        panel.setVisibility(View.VISIBLE);
        bg.startAnimation(Anim.getAnimation(getContext(), R.anim.appear));
        panel.startAnimation(Anim.getAnimation(getContext(), R.anim.side_show));

        opened = true;
    }

    public void hide() {
        if (!opened)
            return;

        bg.startAnimation(Anim.getAnimation(getContext(), R.anim.disappear));
        panel.startAnimation(Anim.getAnimation(getContext(), R.anim.side_hide, () -> {
            if (!opened) {
                bg.setVisibility(View.GONE);
                panel.setVisibility(View.GONE);
                bg.clearAnimation();
                panel.clearAnimation();
            }
        }));

        opened = false;
        listener.onSideMenuHide();
    }

    public void hideImmediately() {
        if (!opened)
            return;

        bg.clearAnimation();
        panel.clearAnimation();
        bg.setVisibility(View.GONE);
        panel.setVisibility(View.GONE);

        opened = false;
        listener.onSideMenuHide();
    }

    public void onModeChanged() {
        boolean delegate = User.getUser().isDelegateMode();

        updatePhoto();

        int colorId = delegate ? R.color.blue : R.color.green;
        int color = getResources().getColor(colorId);
        txtEdit.setTextColor(color);
        txtCity.setTextColor(color);

        imgMarker.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        imgDrop.setColorFilter(color, PorterDuff.Mode.SRC_IN);

        linearDelegate.setVisibility(delegate ? View.VISIBLE : View.GONE);
        linearPlayer.setVisibility(delegate ? View.GONE : View.VISIBLE);
        txtDelegate.setText(delegate ? R.string.side_menu_player : R.string.side_menu_delegate);
    }

    private void updatePhoto() {
        boolean delegate = User.getUser().isDelegateMode();
        User user = getUser();
        if (delegate && user.getPlace() != null && user.getPlace().getLogo() != null) {
            ImageUtils.getBitmap(getUser().getPlace().getLogo(), bitmap -> img.post(() -> setPhoto(bitmap)));
        } else {
            setPhoto(null);
        }
    }

    private void setPhoto(Bitmap bitmap) {
        if (bitmap == null) {
            img.setImageResource(R.drawable.bg_circle_gray);
            ico.setVisibility(View.VISIBLE);
            return;
        }

        int size = Math.min(Math.min(bitmap.getWidth(), bitmap.getHeight()), MainActivity.MAX_LOGO_SIZE);
        Bitmap cropedBitmap = ImageUtils.getCropedBitmap(bitmap, size, size);
        Bitmap rounded = ImageUtils.getRoundedCornerBitmap(cropedBitmap, size / 2);
        img.setImageBitmap(rounded);

        ico.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (!opened)
            return;

        if (view == imgMarker || view == txtCity || view == imgDrop) {
            if (getUser().isDelegateMode()) {
                hideImmediately();
                User.getUser().setDelegateMode(!User.getUser().isDelegateMode());
            } else {
                hide();
            }
            listener.onSideScreen(new MapFragment().setNeedEnterCity(true), true);
        } else if (view == txtContacts || view == txtContactsDelegate) {
            hide();
            listener.onSideScreen(new ContactsFragment().setDelegateScreen(view == txtContactsDelegate));
        } else if (view == txtPromo) {
            hide();
            listener.onSideScreen(new PromoFragment());
        } else if (view == txtTariffs) {
            hide();
            listener.onSideScreen(new ChooseTariffFragment());
        } else if (view == txtCalculator) {
            hide();
            listener.onSideScreen(new CalculatorFragment());
        } else if (view == lineNotify) {
            getUser().setNotify(!getUser().isNotify());
            txtNotufy.setText(getUser().isNotify() ? R.string.side_menu_notify_off : R.string.side_menu_notify_on);
        } else if (view == lineDelegate) {

            if (restaurant != null) {
                hideImmediately();
                User.getUser().setDelegateMode(!User.getUser().isDelegateMode());
                listener.onSideScreen(User.getUser().isDelegateMode() ? (getUser().getPlace() != null ? new RafflesFragment() : new EditPlaceFragment()) : new MapFragment(), true);
            } else {
                hide();
                if (!getUser().isAuthorized())
                    listener.onSideScreen(new Reg1Fragment().setEnter(true));
                else listener.onSideScreen(new AdvantageFragment());
            }
        } else if (view == lineExit) {
            hide();
            getUser().logout();
            listener.onSideScreen(new WelcomeFragment().setEnter(true));
        } else if (view == txtEdit) {
            hide();
            if (!getUser().isAuthorized())
                listener.onSideScreen(new Reg1Fragment().setEnter(true));
            else if (!getUser().isDelegateMode())
                listener.onSideScreen(new ProfileFragment());
            else
                listener.onSideScreen(new EditPlaceFragment());
        }
    }

    private User getUser() {
        return User.getUser();
    }

    @Override
    public boolean onBackPressed() {
        if (opened) {
            hide();
            return true;
        }
        return false;
    }

    @Override
    public void onSwipeRight() {

    }

    @Override
    public void onSwipeLeft() {
        hide();
    }

    @Override
    public void onSwipeUp() {

    }

    @Override
    public void onSwipeDown() {

    }

    public boolean isOpened() {
        return opened;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onUserUpdated() {
        updatePhoto();
    }

    public interface Listener {
        void onSideScreen(ScreenFragment fragment);

        void onSideScreen(ScreenFragment fragment, boolean force);

        void onSideMenuHide();
    }
}
