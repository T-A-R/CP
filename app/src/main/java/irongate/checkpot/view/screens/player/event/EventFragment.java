package irongate.checkpot.view.screens.player.event;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.checkpotAPI.models.Prize;
import irongate.checkpot.checkpotAPI.models.PrizeWithBitmaps;
import irongate.checkpot.utils.CompareCheck;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.utils.TimeUtils;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.PageIndicator;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.ScrollAppearsListener;
import irongate.checkpot.view.screens.common.place.PlaceFragment;
import irongate.checkpot.view.screens.player.complain.ComplainFragment;
import irongate.checkpot.view.screens.player.complain.ParticipFragment;
import irongate.checkpot.view.screens.player.map.MapFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;
import irongate.checkpot.view.screens.player.registration.Reg1Fragment;

public class EventFragment extends ScreenFragment implements PopupMenu.OnMenuItemClickListener, ViewTreeObserver.OnGlobalLayoutListener, ViewPager.OnPageChangeListener {

    private View cont;
    private View contBtn;
    private View contDesc;
    private View contCheck;
    private View linear;
    private View contTime;
    private View contDays;
    private View contSuccess;
    private ScrollView scroll;
    private ImageButton btnMenu;
    private ImageView imgRest;
    private TextView txtName;
    private TextView txtPlace;
    private TextView txtPrizes;
    private TextView txtSuccess;
    private TextView txtMemCur;
    private TextView txtCheck;
    private TextView txtDesc;
    private TextView txtdays;
    private TextView txtDetailsTimeHours;
    private TextView txtDetailsTimeMin;
    private TextView txtDetailsTimeSec;
    private TextView txtMemMax;
    private TextView txtMoreRaffles;
    private TextView txtAlready;
    private TextView txtAgain;
    private Bitmap bmpImg;
    private Button btnPart;
    private ViewPager pager;
    private PageIndicator indicator;
    private ViewGroup transitionsContainer;

    private Event event;
    private int averageColor = -1;
    private int scrollY;
    private LinearLayout linearAnother;
    private MoreRafflesFragment moreRafflesFragment;
    ImageView img;
    ImageView imgGradient;
    View bg;
    View contImg;

    private ArrayList<Prize> prizeArrayList = new ArrayList<>();
    private FragmentActivity activity;
    private ImageEventFragment imageEventFragment;
    private int successCounter;

    public EventFragment() {
        super(R.layout.fragment_event);
    }

    public EventFragment setEvent(Event event) {
        this.event = event;
        return this;
    }

    public Event getEvent() {
        return event;
    }

    @Override
    protected void onReady() {

        activity = getActivity();
        if (activity == null)
            return;

        cont = findViewById(R.id.cont_details);
        scroll = (ScrollView) findViewById(R.id.scroll_details);
        contTime = findViewById(R.id.cont_details_time);
        linear = findViewById(R.id.linear_details);
        linearAnother = (LinearLayout) findViewById(R.id.linear_details_another_prizes);
        contBtn = findViewById(R.id.cont_details_btn);
        contDesc = findViewById(R.id.cont_details_desc);
        contCheck = findViewById(R.id.cont_details_check);
        contDays = findViewById(R.id.cont_days);
        contSuccess = findViewById(R.id.cont_success);

        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_details_back);
        btnMenu = (ImageButton) findViewById(R.id.btn_details_menu);
        btnPart = (Button) findViewById(R.id.btn_details_part);

        imgRest = (ImageView) findViewById(R.id.img_rest);
        LinearLayout contNum = (LinearLayout) findViewById(R.id.cont_details_num);

        txtPrizes = (TextView) findViewById(R.id.txt_details_prizes);
        txtSuccess = (TextView) findViewById(R.id.txt_success_checks);
        txtMemMax = (TextView) findViewById(R.id.txt_details_mem_max);
        txtdays = (TextView) findViewById(R.id.txt_details_days);
        txtMemCur = (TextView) findViewById(R.id.txt_details_mem_cur);
        TextView txtTitlePrizes = (TextView) findViewById(R.id.txt_details_title_prizes);
        TextView txtTitleSuccess = (TextView) findViewById(R.id.txt_title_success_checks);
        TextView txtTitleMemMax = (TextView) findViewById(R.id.txt_details_title_mem_max);
        TextView txtTitleDays = (TextView) findViewById(R.id.txt_details_title_days);
        TextView txtTitleMemCur = (TextView) findViewById(R.id.txt_details_title_mem_cur);
        txtDesc = (TextView) findViewById(R.id.txt_details_desc);
        txtCheck = (TextView) findViewById(R.id.txt_details_check);
        txtDetailsTimeHours = (TextView) findViewById(R.id.txt_details_time_hours);
        TextView txtDetailsTimeDots1 = (TextView) findViewById(R.id.txt_details_time_dots1);
        txtDetailsTimeMin = (TextView) findViewById(R.id.txt_details_time_min);
        TextView txtDetailsTimeDots2 = (TextView) findViewById(R.id.txt_details_time_dots2);
        txtDetailsTimeSec = (TextView) findViewById(R.id.txt_details_time_sec);
        TextView txtDetailsTimeTitleHours = (TextView) findViewById(R.id.txt_details_time_title_hours);
        TextView txtDetailsTimeTitleMin = (TextView) findViewById(R.id.txt_details_time_title_min);
        TextView txtDetailsTimeTitleSec = (TextView) findViewById(R.id.txt_details_time_title_sec);
        TextView txtDetailsGuarantTitle = (TextView) findViewById(R.id.txt_details_guarant_title);
        TextView txtDetailsGuarantDesc = (TextView) findViewById(R.id.txt_details_guarant_desc);
        txtMoreRaffles = (TextView) findViewById(R.id.txt_more_raffles);
        txtAlready = (TextView) findViewById(R.id.txt_already);
        txtAgain = (TextView) findViewById(R.id.txt_again);

        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (PageIndicator) findViewById(R.id.indicator);
        transitionsContainer = (ViewGroup) findViewById(R.id.transitions_container);
        txtName = (TextView) transitionsContainer.findViewById(R.id.txt_details_name);
        txtPlace = (TextView) transitionsContainer.findViewById(R.id.txt_details_position);

        MainFragment.disableSideMenu();

        View linearMore = findViewById(R.id.linear_more);
        moreRafflesFragment = (MoreRafflesFragment) findChildFragmentById(R.id.fragment_more_raffles);

        txtName.setTypeface(Fonts.getFuturaPtMedium());
        txtPrizes.setTypeface(Fonts.getFuturaPtMedium());
        txtSuccess.setTypeface(Fonts.getFuturaPtMedium());
        txtMemMax.setTypeface(Fonts.getFuturaPtMedium());
        txtdays.setTypeface(Fonts.getFuturaPtMedium());
        txtMemCur.setTypeface(Fonts.getFuturaPtMedium());
        txtTitlePrizes.setTypeface(Fonts.getFuturaPtBook());
        txtTitleSuccess.setTypeface(Fonts.getFuturaPtBook());
        txtTitleMemMax.setTypeface(Fonts.getFuturaPtBook());
        txtTitleDays.setTypeface(Fonts.getFuturaPtBook());
        txtTitleMemCur.setTypeface(Fonts.getFuturaPtBook());
        btnPart.setTypeface(Fonts.getFuturaPtBook());
        btnPart.setTransformationMethod(null);
        txtCheck.setTypeface(Fonts.getFuturaPtBook());
        txtDesc.setTypeface(Fonts.getFuturaPtBook());
        txtDetailsTimeHours.setTypeface(Fonts.getFuturaPtMedium());
        txtDetailsTimeDots1.setTypeface(Fonts.getFuturaPtMedium());
        txtDetailsTimeMin.setTypeface(Fonts.getFuturaPtMedium());
        txtDetailsTimeDots2.setTypeface(Fonts.getFuturaPtMedium());
        txtDetailsTimeSec.setTypeface(Fonts.getFuturaPtMedium());
        txtDetailsTimeTitleHours.setTypeface(Fonts.getFuturaPtBook());
        txtDetailsTimeTitleMin.setTypeface(Fonts.getFuturaPtBook());
        txtDetailsTimeTitleSec.setTypeface(Fonts.getFuturaPtBook());
        txtDetailsGuarantTitle.setTypeface(Fonts.getFuturaPtBook());
        txtDetailsGuarantDesc.setTypeface(Fonts.getFuturaPtBook());

        img = (ImageView) findViewById(R.id.img_details);
        imgGradient = (ImageView) findViewById(R.id.img_details_gradient);
        bg = findViewById(R.id.bg_details);
        contImg = findViewById(R.id.cont_details_img);

        btnBack.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        imgRest.startAnimation(Anim.getAppearSlide(getContext(), 1200));
        txtName.startAnimation(Anim.getAppearSlide(getContext(), 1600));
        txtPlace.startAnimation(Anim.getAppearSlide(getContext(), 1600));
        contNum.startAnimation(Anim.getAppearSlide(getContext(), 1800));
        linear.startAnimation(Anim.getAppearSlide(getContext(), 2000));

        if (!isDelegateScreen()) {
            if (event != null) {
                btnMenu.startAnimation(Anim.getAppearSlide(getContext(), 1400));
                imgRest.setOnClickListener(v -> replaceFragment(new PlaceFragment().setPlace(event.getPlace())));
            } else Toast.makeText(activity, R.string.check_connection, Toast.LENGTH_SHORT).show();
        } else {
            btnMenu.setVisibility(View.INVISIBLE);
            linearMore.setVisibility(View.INVISIBLE);
        }

        btnBack.setOnClickListener(v -> onBackPressed());
        btnMenu.setOnClickListener(v -> showPopupMenu());
        btnPart.setOnClickListener(v -> participate());

        int active = 0;
        if (event != null) {
            if (event.getPlace() != null) {
                if (event.getPlace().getEvents() != null) {
                    if (!isDelegateScreen() && event.getPlace().getEvents().size() > 0) {
                        for (int i = 0; i < event.getPlace().getEvents().size(); i++) {
                            if (event.getPlace().getEvents().get(i).getIsDone())
                                continue;

                            if (event.getPlace().getEvents().get(i).getBanned())
                                continue;

                            if (event.getPlace().getEvents().get(i).getUuid().equals(event.getUuid()))
                                continue;

                            active++;
                        }
                    }
                }
            }
        }

        if (active < 2)
            txtMoreRaffles.setVisibility(View.GONE);


        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(txtName, 1, 40, 1,
                TypedValue.COMPLEX_UNIT_DIP);

        cont.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        cont.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (event != null)
            populate();
        initScroll();
    }

    private void populate() {
        Place place = event.getPlace();
        if (place != null) {
            ImageUtils.getBitmap(place.getLogo(), this::setLogo);
        }

        loadImages();
        getSuccessCounter();

        if (event.getEnd() != null && event.getEnd() > 0) {
            Date curTime = TimeUtils.getCurrent();
            Date end = new Date(event.getEnd() * 1000);

            txtdays.setText(String.valueOf(TimeUtils.differenceDays(end, curTime)));
            txtDetailsTimeHours.setText(String.valueOf(TimeUtils.differenceHours(end, curTime)));
            txtDetailsTimeMin.setText(String.valueOf(TimeUtils.differenceMin(end, curTime) % 60));
            txtDetailsTimeSec.setText(String.valueOf(TimeUtils.differenceSec(end, curTime) % 60));

            findViewById(R.id.cont_mem_max).setVisibility(View.GONE);
        } else {
            txtMemMax.setText(String.valueOf(event.getMembersCount()));

            contDays.setVisibility(View.GONE);
            contTime.setVisibility(View.GONE);
        }

        if (event.getMainPrize() != null && event.getMainPrize().getName() != null && !event.getMainPrize().getName().isEmpty()) {
            txtName.setText(firstUpperCase(event.getMainPrize().getName()));
        }

        if (event.getPrizes() != null) {
            int prizeCounter = 0;
            for (int i = 0; i < event.getPrizes().size(); i++) {
                if (event.getPrizes().get(i).getIsRandom()) prizeCounter++;
            }
            txtPrizes.setText(String.valueOf(prizeCounter));
        }

        txtMemCur.setText(String.valueOf(event.getNumSuccMembers()));

        if (getUser().isDelegateMode()) {
            contSuccess.setVisibility(View.GONE);
            btnPart.setEnabled(false);
            btnPart.setBackgroundResource(R.drawable.bg_gray_36);
        } else {
            contSuccess.setVisibility(View.VISIBLE);
            txtSuccess.setText(String.valueOf(successCounter));
            if (isAlreadyPart()) {
                btnPart.setText(R.string.details_success);
            }
        }

        Log.d(MainActivity.TAG, "isAlreadyPart(): " + isAlreadyPart());

        txtDesc.setText(firstUpperCase(event.getDescription()));

        if (getActivity() == null || getActivity().getSupportFragmentManager() == null)
            return;

        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        List<Prize> randomPrizes = event.getRandomPrizes();
        if (randomPrizes.size() > 1) {
            for (int i = 0; i < randomPrizes.size(); i++) {
                DetailsAnotherFragment fragment = new DetailsAnotherFragment();
                fragment.setPrize(randomPrizes.get(i));
                fragment.setPosition(i + 1);
                supportFragmentManager.beginTransaction().add(R.id.linear_details_another_prizes, fragment).commit();
            }
        }

        if (event.getMainPrize() != null) {
            txtCheck.setText(String.format(getString(R.string.event_min_check), event.getMainPrize().getMinReceipt()));
        }

        if (!isDelegateScreen())
            moreRafflesFragment.setEvent(event);
    }

    private void setLogo(Bitmap bitmap) {
        if (bitmap == null || bitmap.getWidth() <= 10)
            return;

        Bitmap cropedBitmap = ImageUtils.getCropedBitmap(bitmap, imgRest.getWidth(), imgRest.getHeight());
        Bitmap roundedCornerBitmap = ImageUtils.getRoundedCornerBitmap(cropedBitmap, imgRest.getWidth() / 2f);
        imgRest.post(() -> imgRest.setImageBitmap(roundedCornerBitmap));
    }

    private boolean isAlreadyPart() {

        if (!getUser().isDelegateMode())
            if (event != null)
                if (event.getMembers() != null)
                    if (getUser().getCurrentUser() != null) {
                        for (int k = event.getMembers().size() - 1; k >= 0; k--) {
                            if (event.getMembers().get(k).getUser().getUuid().equals(getUser().getCurrentUser().getUuid())) {
                                if (event.getMembers().get(k).getCheck() != null) {
                                    if (event.getMembers().get(k).getCheck().getStatus() != null) {
                                        Log.d(MainActivity.TAG, "Check status: (" + k + ") " + event.getMembers().get(k).getCheck().getStatus());
                                        if (event.getMembers().get(k).getCheck().getStatus().equals("SENT") || event.getMembers().get(k).getCheck().getStatus().equals("FAIL")) {
                                            btnPart.setVisibility(View.GONE);
                                            txtAlready.setVisibility(View.VISIBLE);
                                            txtAgain.setVisibility(View.INVISIBLE);
                                            txtAlready.setText(R.string.details_checking);
                                            //return true;
                                            return false;
                                        }
                                        if (!event.getMembers().get(k).getCheck().getStatus().equals("SUCCESS")) {
                                            txtAgain.setVisibility(View.VISIBLE);
                                            txtAgain.setText(R.string.details_incorrect);
                                            btnPart.setText(R.string.details_again);
                                            return false;
                                        } else {
                                            Log.d(MainActivity.TAG, "getCheck().getStatus(): " + event.getMembers().get(k).getCheck().getStatus());
                                            return true;
                                        }
                                    } else {
                                        txtAgain.setVisibility(View.INVISIBLE);
                                        btnPart.setVisibility(View.GONE);
                                        txtAlready.setVisibility(View.VISIBLE);
                                        txtAlready.setText(R.string.details_checking);
                                        //return true;
                                        return false;
                                    }
                                } else if (!event.getMembers().get(k).getReceipt().isEmpty()) {
                                    return true;
                                }
                                Log.d(MainActivity.TAG, "getCheck().getStatus(): " + event.getMembers().get(k).getCheck().getStatus());
                            }
                        }
                    }

        if (getUser().getCurrentUser() == null) return false;
        if (getUser().getCurrentUser().getEvents() == null) return false;

        List<Event> events = getUser().getCurrentUser().getEvents();
        if (events != null) {
            for (int i = 0; i < events.size(); i++) {
                if (events.get(i).getUuid().equals(event.getUuid()))
                    return true;
            }
        }

        return false;
    }

    private void initScroll() {
        HashSet<View> items = new HashSet<>();
        items.add(contBtn);
        items.add(contDesc);
        items.add(linearAnother);
        items.add(contCheck);
        if (contTime.getVisibility() == View.VISIBLE)
            items.add(contTime);

        int offset = (int) (-cont.getHeight() + linear.getY() + 20 * getDensity());
        scroll.getViewTreeObserver().addOnScrollChangedListener(new ScrollAppearsListener(scroll, items, offset));
    }

    private void participate() {
        if (!getUser().isAuthorized()) {
            replaceFragment(new Reg1Fragment().setEnter(true).setDesc(true));
            return;
        }
        scrollY = scroll.getScrollY();
        ParticipFragment part = new ParticipFragment().
                setEvent(event).
                setImage(getImage()).
                setAverageColor(getAverageColor()).
                setScrollY(getScrollY());
        replaceFragment(part);
    }

    private void showPopupMenu() {
        Context context = getContext();
        if (context == null)
            return;

        PopupMenu popupMenu = new PopupMenu(context, btnMenu);
        popupMenu.inflate(R.menu.popup_details);
        popupMenu.setOnMenuItemClickListener(this);
        if (!getUser().isAuthorized()) {
            popupMenu.getMenu().getItem(0).setVisible(false);
        } else {
            popupMenu.getMenu().getItem(0).setVisible(true);
        }
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.details_menu_complain:
                replaceFragment(new ComplainFragment().setEvent(event));
                return true;
            case R.id.details_menu_details:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(CheckpotAPI.getEventRulesUrl(event.getUuid())));
                startActivity(browserIntent);
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
            replaceFragment(new RafflesFragment());
//            replaceFragment(getUser().isDelegateMode() ? new RafflesFragment() : new MapFragment());
            return true;
        }

        replaceFragment(new RafflesFragment());
//        replaceFragment(getPrevClass() == MapFragment.class ? new MapFragment() : new RafflesFragment());
        return true;
    }

    public int getAverageColor() {
        return averageColor;
    }

    public int getScrollY() {
        return scrollY;
    }

    public Bitmap getImage() {
        return bmpImg;
    }

    public String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return ""; //или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public List<String> getUrlImages() {
        ArrayList<String> images = new ArrayList<>();
        if (event.getPrizes() == null)
            return images;

        for (int i = 0; i < event.getPrizes().size(); i++) {
            Prize prize = event.getPrizes().get(i);
            if (prize.getIsRandom() && prize.getPhotos() != null && prize.getPhotos().get(0) != null) {
                images.add(prize.getPhotos().get(0));
                prizeArrayList.add(prize);
            }
        }
        if (images.size() < 2) indicator.setVisibility(View.INVISIBLE);
        return images;
    }

    public List<Bitmap> getBmpImages() {
        ArrayList<Bitmap> images = new ArrayList<>();

        for (int i = 0; i < event.getPrizes().size(); i++) {
            PrizeWithBitmaps prize = (PrizeWithBitmaps) event.getPrizes().get(i);
            if (prize.getIsRandom() && prize.getPhotosBmp() != null && prize.getPhotosBmp().get(0) != null) {
                images.add(prize.getPhotosBmp().get(0));
                prizeArrayList.add(prize);
            }
        }

        if (images.size() < 2) indicator.setVisibility(View.INVISIBLE);
        return images;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPageScrolled(int position, float positionOffset, int i1) {
        indicator.setPositionOffset(positionOffset + position);

    }

    @Override
    public void onPageSelected(int position) {
        TransitionManager.beginDelayedTransition(transitionsContainer,
                new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
        txtName.setText(firstUpperCase(prizeArrayList.get(position).getName()));
        txtPlace.setText((position + 1) + " " + getString(R.string.prize_pos));
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void loadImages() {

        List<String> photosUrl = (event.getMainPrize() instanceof PrizeWithBitmaps) ? null : getUrlImages();
        List<Bitmap> photosBmp = (event.getMainPrize() instanceof PrizeWithBitmaps) ? getBmpImages() : null;

        imageEventFragment = new ImageEventFragment();

        pager.setAdapter(new FragmentStatePagerAdapter(activity.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (photosBmp != null)
                    imageEventFragment = new ImageEventFragment().setBmp(photosBmp.get(position));
                else imageEventFragment = new ImageEventFragment().setUrl(photosUrl.get(position));
                return imageEventFragment;
                //return photosBmp != null ? new ImageEventFragment().setBmp(photosBmp.get(position)) : new ImageEventFragment().setUrl(photosUrl.get(position));
            }

            @Override
            public int getCount() {
                return photosBmp != null ? photosBmp.size() : photosUrl.size();
            }
        });
        pager.addOnPageChangeListener(this);

        indicator.setCursorColor(ContextCompat.getColor(activity, isDelegateScreen() ? R.color.blue : R.color.green));
        int counter = 0;
        if (photosUrl != null) counter = photosUrl.size();
        else counter = photosBmp.size();

        indicator.setNumPages(counter);
    }

    public void getSuccessCounter() {
        CompareCheck checkDB = new CompareCheck(event);
        successCounter = checkDB.getSuccessfullCkecksCounter();
    }
}
