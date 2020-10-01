package irongate.checkpot.view.screens.delegate.aboutWinner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;

import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.utils.PicUrl;

public class CardWinnersView extends LinearLayout {

    ImageView fonRaffleGradient;
    TextView raffleName;
    TextView idRaffle;
    TextView idRaffleDetails;
    ImageView ivDate;
    TextView tvDate;
    WinnersAdapter winnerAdapter;
    RecyclerView winnerRecycler;
    int type;
    private WinnersAdapter.ItemSwipeListener swipeListener;
    private int averageColor = -1;
    private Bitmap bmpImg;

    public CardWinnersView(Context context, int type, WinnersAdapter.ItemSwipeListener swipeListener) {
        super(context);
        this.type = type;
        initComponent(type, swipeListener);

    }

    public CardWinnersView setImage(Bitmap bmpImg) {
        this.bmpImg = bmpImg;
        return this;
    }

    public void initComponent(int type, WinnersAdapter.ItemSwipeListener swipeListener) {
        this.swipeListener = swipeListener;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.card_winners_fragment, this);
        fonRaffleGradient = findViewById(R.id.fon_raffle_gradient);
        raffleName = findViewById(R.id.raffle_name);
        idRaffle = findViewById(R.id.id_raffle);
        idRaffleDetails = findViewById(R.id.id_raffle_detail);
        ivDate = findViewById(R.id.iv_date);
        tvDate = findViewById(R.id.tv_date);
        winnerRecycler = findViewById(R.id.recycler_view_winner);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        winnerAdapter = new WinnersAdapter(0);
        winnerAdapter.setItemSwipeListener(swipeListener);
        winnerRecycler.setLayoutManager(linearLayoutManager);
        winnerRecycler.setAdapter(winnerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                linearLayoutManager.getOrientation());
        winnerRecycler.addItemDecoration(dividerItemDecoration);
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public void setData(int type, SimpleCardRiffle cardItem) {
        winnerAdapter = new WinnersAdapter(type);
        winnerAdapter.setItemSwipeListener(swipeListener);
        winnerRecycler.setAdapter(winnerAdapter);
        String date = cardItem.getRaffleDate();

        if (type == 0) {
            winnerAdapter.update(type, cardItem.getUserProfileList());
        } else {
            winnerAdapter.update(type, cardItem.getUserProfilePrizedList());
        }
        if (!MainActivity.EVOTOR_MODE && cardItem.getRaffleFon() != null) {
                setSmallImage(cardItem.getRaffleFon());
        } else {
            ImageView fonRaffle = findViewById(R.id.fon_raffle);
            fonRaffle.setBackgroundResource(R.color.green);
        }
        raffleName.setText(cardItem.getRaffleName());
        idRaffle.setText(cardItem.getRaffleId());
        tvDate.setText(date);

    }

    private void setSmallImage(String url) {
        if (url != null) {
            final int[] counter = {0};
            ImageView fonRaffle = findViewById(R.id.fon_raffle);
            String urlMin = PicUrl.getMinPicUrl(url);

            Picasso.get()
                    .load(urlMin)
                    .into(fonRaffle, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            setBigImage(fonRaffle, url);
                        }

                        @Override
                        public void onError(Exception e) {
                            counter[0]++;
                            if (counter[0] < 6)
                                setSmallImage(url);
                        }
                    });
        }
    }

    public void setBigImage(ImageView image, String url) {

        final int[] counter = {0};

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                image.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                counter[0]++;
                if (counter[0] < 6)
                    setBigImage(image, url);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        Picasso.get().load(url).into(target);
    }
}
