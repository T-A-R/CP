package irongate.checkpot.view.screens.player.rafles;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.checkpotAPI.models.Prize;
import irongate.checkpot.model.User;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.utils.PicUrl;
import irongate.checkpot.utils.TimeUtils;
import irongate.checkpot.view.SmartFragment;

import static irongate.checkpot.MainActivity.FULL_MODE;
import static irongate.checkpot.MainActivity.TAG;

public class RaffleCardFragment extends SmartFragment implements ViewTreeObserver.OnGlobalLayoutListener {
    private RelativeLayout cont;
    private ImageView bg;
    private ImageView bgSolid;
    private Button btnPick;
    private Button btnPickGar;
    private Button btnEdit;
    private Button btnResult;
    private ImageView imgRest;
    private ImageView imgRestGar;
    private CardView imageCard;
    private CardView cardView;
    private CardView cardViewGar;

    private Event event;
    private Prize lockPrize;
    private CardListener cardListener;
    private SliderListener sliderListener;
    private boolean showPickBtn = true;
    private float widthDp = 0;
    private float heightDp = 0;
    private int widthCardImageDp = 0;
    private int heighCardImagetDp = 0;
    private int radius;
    private int fontSize;
    private int prizeNameSize;
    private TextView txtMembers;
    private TextView txtPrizeName;
    private ImageView imgMembers;
    private ImageView imgGar;
    private TextView txtGar;
    private TextView txtNotPicked;
    private TextView txtNotPickedGar;
    private ViewGroup transitionsContainer;

    static private User mUser;

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private PrizeImageSlideAdapter mPrizeImageSlideAdapter;
    private TextView[] mDots;
    private boolean fromCreated;
    private List<String> prizeList = new ArrayList<>();

    public RaffleCardFragment() {
        super(R.layout.fragment_raffle_card_2);
    }

    public RaffleCardFragment setEvent(Event event) {
        this.event = event;
        return this;
    }

    public RaffleCardFragment setLockPrize(Prize lockPrize) {
        this.lockPrize = lockPrize;
        return this;
    }

    public RaffleCardFragment setShowPickBtn(boolean showPickBtn) {
        this.showPickBtn = showPickBtn;
        return this;
    }

    /**
     * если с экрана создания розыгрыша
     */
    public RaffleCardFragment setFromCreated(boolean created) {
        this.fromCreated = created;
        return this;
    }

    public void setWidthDp(float widthDp) {
        this.widthDp = widthDp;
    }

    public void setHeightDp(float heightDp) {
        this.heightDp = heightDp;
    }

    public void setWidthCardImageDp(int widthDp) {
        this.widthCardImageDp = widthDp;
    }

    public void setHeighCardImageDp(int heightDp) {
        this.heighCardImagetDp = heightDp;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setPrizeNameSize(int prizeNameSize) {
        this.prizeNameSize = prizeNameSize;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onReady() {
        cont = (RelativeLayout) findViewById(R.id.cont_raffle_card);
        imageCard = (CardView) findViewById(R.id.image_card);
        cardView = (CardView) findViewById(R.id.cardview);
        bgSolid = (ImageView) findViewById(R.id.bg_solid);
        bg = (ImageView) findViewById(R.id.bg_raffle_card);
        imgRest = (ImageView) findViewById(R.id.img_raffle_card_rest);
        View icoTime = findViewById(R.id.ico_raffle_card_time);
        TextView txtRestName = (TextView) findViewById(R.id.txt_raffle_card_rest_name);
        transitionsContainer = (ViewGroup) findViewById(R.id.transitionsContainer);
        txtPrizeName = (TextView) transitionsContainer.findViewById(R.id.txt_raffle_card_prize_name);
        TextView txtReceipt = (TextView) findViewById(R.id.txt_min_receipt);
        TextView txtTime = (TextView) findViewById(R.id.txt_raffle_card_time);
        TextView txtPresent = (TextView) findViewById(R.id.txt_raffle_card_present);
        ImageView icoPresent = (ImageView) findViewById(R.id.ico_raffle_card_present);
        txtMembers = (TextView) findViewById(R.id.txt_raffle_card_members);
        imgMembers = (ImageView) findViewById(R.id.ico_raffle_card_members);
        btnPick = (Button) findViewById(R.id.btn_raffle_card_pick);
        btnEdit = (Button) findViewById(R.id.btn_edit);
        btnResult = (Button) findViewById(R.id.btn_result);
        cardViewGar = (CardView) findViewById(R.id.cardview_gar);
        btnPickGar = (Button) findViewById(R.id.btn_raffle_card_pick_gar);
        imgGar = (ImageView) findViewById(R.id.img_gar);
        txtGar = (TextView) findViewById(R.id.txt_raffle_card_prize_name_gar);
        imgRestGar = (ImageView) findViewById(R.id.img_raffle_card_rest_gar);
        txtNotPicked = (TextView) findViewById(R.id.txt_not_picked);
        txtNotPickedGar = (TextView) findViewById(R.id.txt_not_picked_gar);

        Log.d("EVENTS", "name: " + event.getMainPrize().getName() +
                ", uuid: " + event.getUuid() +
                ", getPaymentId(): " + event.getPaymentId() +
                ", getPrice(): " + event.getPrice() +
                ", isBanned(): " + event.getBanned() +
                ", isDone(): " + event.getIsDone() +
                ", getMembersCount(): " + event.getMembersCount() +
                ", getEnd(): " + event.getEnd() +
                ", getMembersCount(): " + event.getMembersCount());

        if (lockPrize != null)
            if (!lockPrize.getIsRandom()) {
                cardView.setVisibility(View.GONE);
                cardViewGar.setVisibility(View.VISIBLE);
                txtGar.setText(event.getMyGuarantedPrize().getName());
            } else {
                cardView.setVisibility(View.VISIBLE);
                cardViewGar.setVisibility(View.GONE);
            }

        float density = getDensity();
        radius = (int) (density * 15);

        mUser = User.getUser();
        boolean delegateMode = mUser.isDelegateMode();

        if (widthDp != 0 || heightDp != 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cont.getLayoutParams();
            if (widthDp != 0)
                params.width = (int) (density * widthDp);

            if (heightDp != 0)
                params.height = (int) (density * heightDp);

            cont.setLayoutParams(params);
            cont.requestLayout();
        }

        if (widthCardImageDp != 0 || heighCardImagetDp != 0)
            imageCard.setLayoutParams(new RelativeLayout.LayoutParams((int) (density * widthCardImageDp), (int) (density * heighCardImagetDp)));

        if (fontSize != 0)
            txtPrizeName.setTextSize(fontSize);

        if (prizeNameSize != 0) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) txtPrizeName.getLayoutParams();
            params.height = (int) (density * prizeNameSize);
            txtPrizeName.setLayoutParams(params);
        }

        btnPick.setTransformationMethod(null);
        btnPickGar.setTransformationMethod(null);
        btnEdit.setTransformationMethod(null);

        if (event != null) {
            if (event.getPlace() != null) {
                try {
                    txtRestName.setText(firstUpperCase(event.getPlace().getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String prizeName = null;

            if (lockPrize != null)
                prizeName = lockPrize.getName();
            else if (event.getMainPrize() != null)
                prizeName = event.getMainPrize().getName();

            if (prizeName != null)
                txtPrizeName.setText(firstUpperCase(prizeName));

            if (event.getMainPrize() != null) {
                try {
                    txtReceipt.setText(getString(R.string.raffles_card_min_receipt, event.getMainPrize().getMinReceipt()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (event.getIsDone()) {
                findViewById(R.id.cont_finished).setVisibility(View.VISIBLE);
                if ((event.getMyRandomPrize() != null || event.getMyGuarantedPrize() != null) && showPickBtn) {
                    btnPick.setVisibility(View.VISIBLE);
//                    checkTwoWeeks();
                }

                txtMembers.setVisibility(View.GONE);
                imgMembers.setVisibility(View.GONE);
                txtTime.setVisibility(View.GONE);
                icoTime.setVisibility(View.GONE);
                txtPresent.setVisibility(View.GONE);
                icoPresent.setVisibility(View.GONE);

            } else if (event.getBanned() && !fromCreated) {
                findViewById(R.id.cont_banned).setVisibility(View.VISIBLE);
                findViewById(R.id.linear_params).setVisibility(View.INVISIBLE);
//                Log.d(TAG, "isBanned(): " + event.getMainPrize().getName() + " uuid: " + event.getUuid() + " getPaymentId(): " + event.getPaymentId() + " getPrice():" + event.getPrice());
                if (event.getPaymentId() == null && event.getPrice() != null && event.getPrice() > 0)
                    btnEdit.setVisibility(View.GONE);
                else
                    btnEdit.setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.linear_params).setVisibility(View.VISIBLE);

                if (event.getEnd() != null && event.getEnd() > 0) {
                    Date end = new Date(event.getEnd() * 1000);
                    Date curTime = TimeUtils.getCurrent();
                    long time = end.getTime() - curTime.getTime();
                    icoTime.setVisibility(time > 0 ? View.VISIBLE : View.GONE);
                    txtTime.setVisibility(time > 0 ? View.VISIBLE : View.GONE);
                    icoTime.setVisibility(View.VISIBLE);
                    txtTime.setVisibility(View.VISIBLE);
                    int days = TimeUtils.differenceDays(end, curTime);
                    int hours = TimeUtils.differenceHours(end, curTime);
                    int minutes = TimeUtils.differenceMin(end, curTime);

                    String sDays = String.valueOf(days);
                    String sHours = String.valueOf(hours);
                    String sMinutes = String.valueOf(minutes);

                    if (days <= 0 && hours < 1 && minutes > 60) {
                        txtTime.setText("");
                    }

                    if (days > 0) {
                        txtTime.setText(sDays + " д");
                    }
                    if (days == 0 && hours >= 1 && hours < 24) {
                        txtTime.setText(sHours + " ч");
                    }
                    if (days == 0 && hours < 1 && minutes > 0) {
                        txtTime.setText(sMinutes + " м");
                    }
                } else {
                    icoTime.setVisibility(View.GONE);
                    txtTime.setVisibility(View.GONE);
                    txtTime.setText("");
                }

                if (event.getPrizes() != null) {
                    int prizeCounter = 0;
                    for (int i = 0; i < event.getPrizes().size(); i++) {
                        if (event.getPrizes().get(i).getIsRandom()) prizeCounter++;
                    }
                    txtPresent.setText(String.valueOf(prizeCounter));
                }


                if (event.getMembersCount() != null && event.getMembersCount() > 0) {
                    imgMembers.setVisibility(View.VISIBLE);
                    String member = String.valueOf(event.getNumSuccMembers());
                    String countMemder = String.valueOf(event.getMembersCount());
                    txtMembers.setText(member + " / " + countMemder);
                } else {
                    imgMembers.setVisibility(View.GONE);
                }
            }
        }

//        imgRest.setOnClickListener(view -> {
//            if (cardListener != null) cardListener.onRestBtn(this);
//        });

        imgRestGar.setOnClickListener(view -> {
            if (cardListener != null) cardListener.onRestBtn(this);
        });

        if (event != null && event.getIsDone() && delegateMode) {
            btnPick.setVisibility(View.VISIBLE);
            btnPick.setText(R.string.raffles_btn_retry);
            btnPick.setOnClickListener(view -> {
                if (cardListener != null) cardListener.onEditBtn(this);
            });
        } else {
            checkTwoWeeks();
            btnPick.setOnClickListener(view -> {
                if (cardListener != null) cardListener.onPickBtn(this);
            });
            btnPickGar.setOnClickListener(view -> {
                if (cardListener != null) cardListener.onPickBtn(this);
            });
        }

        btnEdit.setOnClickListener(view -> {
            if (cardListener != null) cardListener.onEditBtn(this);
        });

        btnResult.setOnClickListener(view -> {
            if (cardListener != null) cardListener.onResultBtn(this);
        });

        if (!checkTwoWeeks()) {
            if (FULL_MODE) {
                bg.setOnClickListener(view -> {
                    if (cardListener != null) cardListener.onCardClick(this);
                });

                cardView.setOnClickListener(view -> {
                    if (cardListener != null) cardListener.onCardClick(this);
                });
            }
        }

        bg.getViewTreeObserver().addOnGlobalLayoutListener(this);
        bg.setVisibility(View.INVISIBLE);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        mPrizeImageSlideAdapter = new PrizeImageSlideAdapter(getContext());
        mPrizeImageSlideAdapter.setImages(getImages());
        mSlideViewPager.setAdapter(mPrizeImageSlideAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        GestureDetector tapGestureDetector = new GestureDetector(getContext(), new TapGestureListener());

        mSlideViewPager.setOnTouchListener((v, event) -> {
            if (sliderListener != null)
                sliderListener.onSliderTouchEvent(this, event);
            tapGestureDetector.onTouchEvent(event);
            return false;
        });
    }

    public List<String> getImages() {
        ArrayList<String> images = new ArrayList<>();

        if (lockPrize != null && lockPrize.getPhotos() != null && lockPrize.getPhotos().get(0) != null) {
            images.add(lockPrize.getPhotos().get(0));
            prizeList.add(lockPrize.getName());
            return images;
        }

        if (event != null) {
            if (event.getPrizes() == null) return images;

            for (int i = 0; i < event.getPrizes().size(); i++) {
                Prize prize = event.getPrizes().get(i);
                if (prize.getIsRandom() && prize.getPhotos() != null && prize.getPhotos().get(0) != null)
                    images.add(prize.getPhotos().get(0));
                prizeList.add(prize.getName());
            }
        }
        return images;
    }

    public void addDotsIndicator(int position) {
        mDotLayout.removeAllViews();
        mDots = new TextView[getImages().size()];

        if (mDots.length > 1) {
            for (int i = 0; i < mDots.length; i++) {
                mDots[i] = new TextView(getContext());
                mDots[i].setText(Html.fromHtml("&#8226;"));
                mDots[i].setTextSize(40);
                mDots[i].setTextColor(getResources().getColor(R.color.colorWhite));
                mDotLayout.addView(mDots[i]);
            }

            if (mDots.length > 0) {
                mDots[position].setTextColor(getResources().getColor(R.color.green));
            }
        }
    }

    public void setPrizeName(int position) {
        TransitionManager.beginDelayedTransition(transitionsContainer,
                new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN));
        txtPrizeName.setText(firstUpperCase(prizeList.get(position)));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            setPrizeName(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };

    class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (cardListener != null && !checkTwoWeeks())
                if (FULL_MODE)
                    cardListener.onCardClick(RaffleCardFragment.this);
            return true;
        }
    }

    public void setCardListener(CardListener cardListener) {
        this.cardListener = cardListener;
    }

    public void setSliderListener(SliderListener sliderListener) {
        this.sliderListener = sliderListener;
    }

    @Override
    public void onGlobalLayout() {
        bg.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        if (lockPrize != null)
            if (!lockPrize.getIsRandom()) {
                String finalUrlPrizeGar = lockPrize.getPhotos().get(0);
                loadGarImage(finalUrlPrizeGar);
            }

        Place place = (event == null ? null : event.getPlace());
        if (place != null && place.getLogo() != null) {
            String url = place.getLogo();
            Picasso.get().load(url).transform(new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    if (source == null) return null;

                    Bitmap bitmap = source;
                    bitmap = ImageUtils.getCropedBitmap(bitmap, 200, 200);
                    bitmap = ImageUtils.getRoundedCornerBitmap(bitmap, 100);

                    if (bitmap != source) source.recycle();

                    return bitmap;
                }

                @Override
                public String key() {
                    return "RaffleCardFragmentLogo";
                }
            }).into(imgRest);

            Picasso.get().load(url).transform(new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    if (source == null) return null;

                    Bitmap bitmap = source;
                    bitmap = ImageUtils.getCropedBitmap(bitmap, 200, 200);
                    bitmap = ImageUtils.getRoundedCornerBitmap(bitmap, 100);

                    if (bitmap != source) source.recycle();

                    return bitmap;
                }

                @Override
                public String key() {
                    return "RaffleCardFragmentLogo";
                }
            }).into(imgRestGar);
        }

        if (cardListener != null) cardListener.onCardIinited();

    }

    public Event getEvent() {
        return event;
    }

    public Bitmap getScreenshot() {
        return ImageUtils.getScreenshot(cont);
    }

    public interface CardListener {
        void onCardIinited();

        void onRestBtn(RaffleCardFragment card);

        void onPickBtn(RaffleCardFragment card);

        void onEditBtn(RaffleCardFragment card);

        void onResultBtn(RaffleCardFragment card);

        void onCardClick(RaffleCardFragment card);
    }

    public interface SliderListener {
        void onSliderTouchEvent(RaffleCardFragment card, MotionEvent event);
    }

    public String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return ""; //или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public void loadGarImage(String url) {

        String urlMin = PicUrl.getMinPicUrl(url);

        cont.post(new Runnable() {
            @Override
            public void run() {
                Picasso.get()
                        .load(urlMin)
                        .transform(new Transformation() {
                            @Override
                            public Bitmap transform(Bitmap source) {
                                if (source == null) {
                                    return null;
                                }

                                Bitmap bitmap = source;
                                if (imgGar.getWidth() > 0) {
                                    bitmap = ImageUtils.getCropedBitmap(source, imgGar.getWidth(), imgGar.getHeight());
                                }

                                bitmap = ImageUtils.getRoundedCornerBitmap(bitmap, imgGar.getWidth() / 2);

                                if (bitmap != source) {
                                    source.recycle();
                                }
                                return bitmap;
                            }

                            @Override
                            public String key() {
                                return "RaffleCardFragmentGar";
                            }
                        }).into(imgGar, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        loadBigImage(url);
                    }

                    @Override
                    public void onError(Exception e) {
                        loadBigImage(url);
                    }
                });
            }
        });
    }

    public void loadBigImage(String url) {
        cont.post(new Runnable() {
            @Override
            public void run() {
                Picasso.get()
                        .load(url)
                        .transform(new Transformation() {
                            @Override
                            public Bitmap transform(Bitmap source) {
                                if (source == null) {
                                    return null;
                                }

                                Bitmap bitmap = source;
                                if (imgGar.getWidth() > 0) {
                                    bitmap = ImageUtils.getCropedBitmap(source, imgGar.getWidth(), imgGar.getHeight());
                                }

                                bitmap = ImageUtils.getRoundedCornerBitmap(bitmap, imgGar.getWidth() / 2);

                                if (bitmap != source) {
                                    source.recycle();
                                }
                                return bitmap;
                            }

                            @Override
                            public String key() {
                                return "RaffleCardFragmentGar";
                            }
                        }).into(imgGar);
            }
        });
    }


    private boolean checkTwoWeeks() {
        Date date = new Date(System.currentTimeMillis());
        if (event != null && event.getEnded() != null) {
            Log.d(TAG, "checkTwoWeeks Player: " + (date.getTime() / 1000 - event.getEnded()) / 86400);
            if ((date.getTime() / 1000 - event.getEnded()) / 86400 > 14) {
                if (event.getIsDone() && !User.getUser().isDelegateMode()) {
                    btnPick.setVisibility(View.GONE);
                    txtNotPicked.setVisibility(View.VISIBLE);
                    btnPickGar.setVisibility(View.GONE);
                    txtNotPickedGar.setVisibility(View.VISIBLE);
                }
                return true;
            }
        }
        return false;
    }
}
