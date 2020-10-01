package irongate.checkpot.view.screens.player.map;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.checkpotAPI.models.Prize;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.utils.TimeUtils;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.screens.player.rafles.PrizeImageSlideAdapter;

class RaffleCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    static private int radius;

    private final ImageView bg;
    private final ImageView imgRest;
    private final View icoTime;
    private final TextView txtRestName;
    private final TextView txtPrizeName;
    private final TextView txtMinReceipt;
    private final TextView txtTime;
    private final TextView txtPresent;
    private final TextView txtMemders;
    private final ImageView imgMembers;
    private final CardView cardView;

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private int index;
    private Listener listener;
    private Event event;

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private PrizeImageSlideAdapter mPrizeImageSlideAdapter;
    private TextView[] mDots;

    RaffleCardViewHolder(View itemView, Listener listener) {
        super(itemView);

        this.listener = listener;

        bg = itemView.findViewById(R.id.bg_raffle_card);
        cardView = itemView.findViewById(R.id.cardview);
        imgRest = itemView.findViewById(R.id.img_raffle_card_rest);
        icoTime = itemView.findViewById(R.id.ico_raffle_card_time);
        txtRestName = itemView.findViewById(R.id.txt_raffle_card_rest_name);
        txtPrizeName = itemView.findViewById(R.id.txt_raffle_card_prize_name);
        txtMinReceipt = itemView.findViewById(R.id.txt_min_receipt);
        txtTime = itemView.findViewById(R.id.txt_raffle_card_time);
        txtPresent = itemView.findViewById(R.id.txt_raffle_card_present);
        txtMemders = itemView.findViewById(R.id.txt_raffle_card_members);
        imgMembers = itemView.findViewById(R.id.ico_raffle_card_members);
        txtRestName.setTypeface(Fonts.getFuturaPtBook());
        txtPrizeName.setTypeface(Fonts.getFuturaPtMedium());
        txtTime.setTypeface(Fonts.getFuturaPtBook());
        txtPresent.setTypeface(Fonts.getFuturaPtBook());
        txtMemders.setTypeface(Fonts.getFuturaPtBook());

        imgRest.setOnClickListener(this);
        bg.setOnClickListener(this);
        cardView.setOnClickListener(this);

        if (radius == 0)
            radius = (int) (itemView.getContext().getResources().getDisplayMetrics().density * 15);

        mSlideViewPager = (ViewPager) itemView.findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) itemView.findViewById(R.id.dotsLayout);
        mPrizeImageSlideAdapter = new PrizeImageSlideAdapter(itemView.getContext());


    }

    public List<String> getImages() {
        ArrayList<String> images = new ArrayList<>();
        if (event.getPrizes() == null)
            return images;

        for (int i = 0; i < event.getPrizes().size(); i++) {
            Prize prize = event.getPrizes().get(i);
            if (prize.getIsRandom() && prize.getPhotos() != null && prize.getPhotos().get(0) != null)
                images.add(prize.getPhotos().get(0));
        }
        return images;
    }

    public void addDotsIndicator(int position) {
        mDotLayout.removeAllViews();
        mDots = new TextView[getImages().size()];

        if (mDots.length > 1) {
            for (int i = 0; i < mDots.length; i++) {
                mDots[i] = new TextView(itemView.getContext());
                mDots[i].setText(Html.fromHtml("&#8226;"));
                mDots[i].setTextSize(40);
                mDots[i].setTextColor(itemView.getContext().getResources().getColor(R.color.colorWhite));

                mDotLayout.addView(mDots[i]);
            }

            if (mDots.length > 0) {
                mDots[position].setTextColor(itemView.getContext().getResources().getColor(R.color.green));
            }
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };

    class TapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            onClick(cardView);
            return true;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    void setEvent(Event event, int index) {
        this.event = event;
        this.index = index;


        mPrizeImageSlideAdapter.setImages(getImages());
        mSlideViewPager.setAdapter(mPrizeImageSlideAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        GestureDetector tapGestureDetector = new GestureDetector(itemView.getContext(), new TapGestureListener());

        mSlideViewPager.setOnTouchListener((v, event1) -> {
            tapGestureDetector.onTouchEvent(event1);
            return false;
        });

        txtRestName.setText(firstUpperCase(event.getPlace().getName()));

        if (event.getPrizes() != null && !event.getPrizes().isEmpty())
            txtPrizeName.setText(firstUpperCase(event.getPrizes().get(0).getName()));

        txtMinReceipt.setText("от " + event.getMainPrize().getMinReceipt() + " руб");

        if (event.getEnd() != null && event.getEnd() > 0) {
            Date end = new Date(event.getEnd() * 1000);
            Date curTime = TimeUtils.getCurrent();
            long time = end.getTime() - curTime.getTime();
            icoTime.setVisibility(time > 0 ? View.VISIBLE : View.GONE);
            txtTime.setVisibility(time > 0 ? View.VISIBLE : View.GONE);
            int days = TimeUtils.differenceDays(end, curTime);
            int hours = TimeUtils.differenceHours(end, curTime);
            int minutes = TimeUtils.differenceMin(end, curTime);

            if (days <= 0 && hours < 1 && minutes > 60)
                txtTime.setText("");

            String sDays = String.valueOf(days);
            String sHours = String.valueOf(hours);
            String sMinutes = String.valueOf(minutes);
            if (days > 0)
                txtTime.setText(sDays + " д");

            if (days <= 0 && hours >= 1 && hours < 24)
                txtTime.setText(sHours + " ч");

            if (days <= 0 && hours < 1 && minutes > 0)
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
            txtMemders.setVisibility(View.VISIBLE);
            imgMembers.setVisibility(View.VISIBLE);
            String member = String.valueOf(event.getNumSuccMembers());
            String countMemder = String.valueOf(event.getMembersCount());
            txtMemders.setText(member + " / " + countMemder);
        } else {
            txtMemders.setVisibility(View.GONE);
            imgMembers.setVisibility(View.GONE);
        }
        bg.setImageDrawable(null);
        imgRest.setImageResource(R.drawable.home);

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
    }

    void onViewAttachedToWindow() {
    }

    @Override
    public void onClick(View v) {
        if (v == imgRest)
            listener.onRestBtn(event.getPlace());
        else if (v == bg) {
            listener.onCardClick(event);
        } else if (v == cardView) {
            listener.onCardClick(event);
        }
    }

    public interface Listener {
        void onRestBtn(Place place);

        void onCardClick(Event event);
    }

    public String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return ""; //или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}
