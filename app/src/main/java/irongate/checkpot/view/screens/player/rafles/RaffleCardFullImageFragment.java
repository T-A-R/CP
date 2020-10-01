package irongate.checkpot.view.screens.player.rafles;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Date;

import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.model.User;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.utils.TimeUtils;
import irongate.checkpot.view.SmartFragment;

public class RaffleCardFullImageFragment extends SmartFragment implements ViewTreeObserver.OnGlobalLayoutListener {
    private RelativeLayout cont;
    private ImageView bg;
    private ImageView bgSolid;
    private Button btnPick;
    private Button btnEdit;
    private ImageView imgRest;
    private CardView imageCard;

    private Event event;
    private CardListener cardListener;
    private boolean showPickBtn = true;
    private float widthDp = 0;
    private float heightDp = 0;
    private int widthCardImageDp = 0;
    private int heighCardImagetDp = 0;
    private int radius;
    private int fontSize;
    private TextView txtMembers;
    private TextView txtPrizeName;
    private ImageView imgMembers;

    static private User mUser;
    private int mode;

    public RaffleCardFullImageFragment() {
        super(R.layout.fragment_raffle_card);
    }

    public RaffleCardFullImageFragment setEvent(Event event) {
        this.event = event;
        return this;
    }

    public RaffleCardFullImageFragment setShowPickBtn(boolean showPickBtn) {
        this.showPickBtn = showPickBtn;
        return this;
    }

    public RaffleCardFullImageFragment setMode(int mode) {
        this.mode = mode;
        return this;
    }

    @Override
    protected void onReady() {
        cont = (RelativeLayout) findViewById(R.id.cont_raffle_card);
        imageCard = (CardView) findViewById(R.id.image_card);
        bgSolid = (ImageView) findViewById(R.id.bg_solid);
        bg = (ImageView) findViewById(R.id.bg_raffle_card);
        imgRest = (ImageView) findViewById(R.id.img_raffle_card_rest);
        View icoTime = findViewById(R.id.ico_raffle_card_time);
        TextView txtRestName = (TextView) findViewById(R.id.txt_raffle_card_rest_name);
        txtPrizeName = (TextView) findViewById(R.id.txt_raffle_card_prize_name);
        TextView txtReceipt = (TextView) findViewById(R.id.txt_min_receipt);
        TextView txtTime = (TextView) findViewById(R.id.txt_raffle_card_time);
        TextView txtPresent = (TextView) findViewById(R.id.txt_raffle_card_present);
        ImageView icoPresent = (ImageView) findViewById(R.id.ico_raffle_card_present);
        txtMembers = (TextView) findViewById(R.id.txt_raffle_card_members);
        imgMembers = (ImageView) findViewById(R.id.ico_raffle_card_members);
        btnPick = (Button) findViewById(R.id.btn_raffle_card_pick);
        btnEdit = (Button) findViewById(R.id.btn_edit);

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

        btnPick.setTransformationMethod(null);
        btnPick.setTransformationMethod(null);

        if (event != null) {
            if (event.getPlace() != null)
                try {
                    txtRestName.setText(firstUpperCase(event.getPlace().getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        if (event != null) {
            if (event.getMainPrize() != null) {
                try {
                    txtPrizeName.setText(firstUpperCase(event.getMainPrize().getName()));
                    txtReceipt.setText(getString(R.string.raffles_card_min_receipt, event.getMainPrize().getMinReceipt()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (event != null) {
            if (event.getIsDone()) {
                findViewById(R.id.cont_finished).setVisibility(View.VISIBLE);
                if ((event.getMyRandomPrize() != null || event.getMyGuarantedPrize() != null) && showPickBtn) {
                    btnPick.setVisibility(View.VISIBLE);
                }

                txtMembers.setVisibility(View.GONE);
                imgMembers.setVisibility(View.GONE);
                txtTime.setVisibility(View.GONE);
                icoTime.setVisibility(View.GONE);
                txtPresent.setVisibility(View.GONE);
                icoPresent.setVisibility(View.GONE);
            } else if (event.getBanned()) {
                findViewById(R.id.cont_banned).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.linear_params).setVisibility(View.VISIBLE);
                if (event.getEnd() != null && event.getEnd() > 0) {
                    Date end = new Date(event.getEnd() * 1000);
                    Date curTime = TimeUtils.getCurrent();
                    long time = end.getTime() - curTime.getTime();
                    icoTime.setVisibility(time > 0 ? View.VISIBLE : View.GONE);
                    txtTime.setVisibility(time > 0 ? View.VISIBLE : View.GONE);
                    int days = TimeUtils.differenceDays(end, curTime);
                    int hours = TimeUtils.differenceHours(end, curTime);
                    int minutes = TimeUtils.differenceMin(end, curTime);

                    String sDays = String.valueOf(days);
                    String sHours = String.valueOf(hours);
                    String sMinutes = String.valueOf(minutes);

                    if (days <= 0 && hours < 1 && minutes > 60)
                        txtTime.setText("");
                    if (days > 0)
                        txtTime.setText(sDays + " д");
                    if (days == 0 && hours >= 1 && hours < 24)
                        txtTime.setText(sHours + " ч");
                    if (days == 0 && hours < 1 && minutes > 0)
                        txtTime.setText(sMinutes + " м");

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

        if (event.getIsDone() && delegateMode) {
            btnPick.setVisibility(View.VISIBLE);
            btnPick.setText(R.string.raffles_btn_retry);
            btnPick.setOnClickListener(view -> {
                if (cardListener != null) cardListener.onEditBtn(this);
            });
        } else {
            btnPick.setOnClickListener(view -> {
                if (cardListener != null) cardListener.onPickBtn(this);
            });
        }

        btnEdit.setOnClickListener(view -> {
            if (cardListener != null) cardListener.onEditBtn(this);
        });
        if (MainActivity.FULL_MODE)
            bg.setOnClickListener(view -> {
                if (cardListener != null) cardListener.onCardClick(this);
            });

        bg.getViewTreeObserver().addOnGlobalLayoutListener(this);

        if (mode == 1) {
            txtReceipt.setVisibility(View.GONE);
            findViewById(R.id.cont_finished).setVisibility(View.GONE);
        }
    }

    @Override
    public void onGlobalLayout() {
        bg.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        if (event.getPrizes() != null && !event.getPrizes().isEmpty() &&
                event.getPrizes().get(0).getPhotos() != null &&
                !event.getPrizes().get(0).getPhotos().isEmpty() &&
                event.getPrizes().get(0).getPhotos().get(0) != null) {
            String url = event.getPrizes().get(0).getPhotos().get(0);
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

                                    boolean resized = false;
                                    Bitmap bitmap = source;
                                    if (cont.getWidth() > 0) {
                                        bitmap = ImageUtils.getCropedBitmap(source, cont.getWidth(), cont.getHeight());
                                        resized = true;
                                    }
                                    int averageColor = ImageUtils.getAverageColor(bitmap);
                                    bitmap = ImageUtils.getGradiented(bitmap, averageColor);
                                    if (!resized && cont.getWidth() > 0) {
                                        bitmap = ImageUtils.getCropedBitmap(source, cont.getWidth(), cont.getHeight());
                                    }
                                    bitmap = ImageUtils.getRoundedCornerBitmap(bitmap, radius);

                                    if (bitmap != source) {
                                        source.recycle();
                                    }
                                    return bitmap;
                                }

                                @Override
                                public String key() {
                                    return "RaffleCardFragment";
                                }
                            }).into(bg);
                }
            });


        }

        Place place = event.getPlace();
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
        }

        if (cardListener != null) {
            cardListener.onCardIinited();
        }
    }

    public Event getEvent() {
        return event;
    }

    public Bitmap getScreenshot() {
        return ImageUtils.getScreenshot(cont);
    }

    public interface CardListener {
        void onCardIinited();

        void onRestBtn(RaffleCardFullImageFragment card);

        void onPickBtn(RaffleCardFullImageFragment card);

        void onEditBtn(RaffleCardFullImageFragment card);

        void onCardClick(RaffleCardFullImageFragment card);
    }

    public String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return ""; //или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}
