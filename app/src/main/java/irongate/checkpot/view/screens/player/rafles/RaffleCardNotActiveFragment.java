package irongate.checkpot.view.screens.player.rafles;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.view.ScreenFragment;

public class RaffleCardNotActiveFragment extends ScreenFragment implements ViewTreeObserver.OnGlobalLayoutListener {
    static public final String INTENT_PLACE = "INTENT_PLACE";

    private RelativeLayout cont;
    private ImageView bg;
    private TextView title;
    private Button btnNotify;

    private Place place;
    private float widthDp = 0;
    private float heightDp = 0;
    private int radius;

    public RaffleCardNotActiveFragment() {
        super(R.layout.fragment_card_not_raffles);
    }

    public RaffleCardNotActiveFragment setPlace(Place place) {
        this.place = place;
        return this;
    }

    public void setWidthDp(float widthDp) {
        this.widthDp = widthDp;
    }

    public void setHeightDp(float heightDp) {
        this.heightDp = heightDp;
    }

    @Override
    protected void onReady() {
        cont = (RelativeLayout) findViewById(R.id.cont_no_raffle_card);
        bg = (ImageView) findViewById(R.id.bg_raffle_card);
        title = (TextView) findViewById(R.id.title);
        btnNotify = (Button) findViewById(R.id.btn_notify);

        float density = getDensity();
        radius = (int) (density * 15);

        if (widthDp != 0 || heightDp != 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cont.getLayoutParams();
            if (widthDp != 0)
                params.width = (int) (density * widthDp);

            if (heightDp != 0)
                params.height = (int) (density * heightDp);

            cont.setLayoutParams(params);
            cont.requestLayout();
        }

        title.setText(String.format("«%s»\n%s", place.getName(), "Нет активных розыгрышей"));

        btnNotify.setOnClickListener(v -> onNotifyClick());
        bg.setOnClickListener(v -> onBgClick());

        bg.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        bg.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        if (place.getPhotos().size() != 0 && place.getPhotos().get(0) != null) {
            String url = place.getPhotos().get(0);
            Picasso.get().load(url).transform(new Transformation() {
                @Override
                public Bitmap transform(Bitmap source) {
                    if (source == null) {
                        return null;
                    }

                    Bitmap bitmap = source;
                    if (cont.getWidth() > 0) {
                        bitmap = ImageUtils.getCropedBitmap(bitmap, cont.getWidth(), cont.getHeight());
                    }
                    int averageColor = ImageUtils.getAverageColor(bitmap);
                    bitmap = ImageUtils.getGradiented(bitmap, averageColor);
                    bitmap = ImageUtils.getRoundedCornerBitmap(bitmap, radius);

                    if (bitmap != source) {
                        source.recycle();
                    }
                    return bitmap;
                }

                @Override
                public String key() {
                    return "RaffleCardNotActiveFragment";
                }
            }).into(bg);
        }
    }

    private void onBgClick() {
        dispatchIntent(INTENT_PLACE);
    }

    private void onNotifyClick() {
        btnNotify.setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(), "Оповещения временно недоступны", Toast.LENGTH_SHORT).show();
    }

    public Place getPlace() {
        return place;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
