package irongate.checkpot.view.screens.delegate.addedRaffle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.view.Fonts;

public class PrizeUploadView extends RelativeLayout {
    private ImageView imageView;
    private ImageButton btnLoad;
    private ImageButton btnCancle;
    private TextView textImage;
    private TextView textImageTwo;

    public PrizeUploadView(Context context) {
        super(context);
        initComponent();
    }

    public PrizeUploadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public PrizeUploadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.prize_upload_view, this);

        imageView = findViewById(R.id.img_part_prize);
        btnLoad = findViewById(R.id.btn_prize_load);
        btnCancle = findViewById(R.id.btn_prize_cancel);
        textImage= findViewById(R.id.txt_prize_photo);
        textImageTwo= findViewById(R.id.txt_prize_photo_two);
        textImage.setTypeface(Fonts.getFuturaPtBook());
        textImageTwo.setTypeface(Fonts.getFuturaPtBook());
        btnCancle.setOnClickListener(view -> cancelCheck());
    }

    public void setLoadClickListener(View.OnClickListener onClickListener) {
        btnLoad.setOnClickListener(onClickListener);
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap == null)
            return;

        imageView.post(() -> {
            int width = imageView.getMeasuredWidth();
            int height = imageView.getMeasuredHeight();
            Bitmap cropedBitmap = ImageUtils.getCropedBitmap(bitmap, width, height);
            Bitmap colorCovered = ImageUtils.getColorCovered(cropedBitmap, Color.argb(171, 0, 0, 0));
            Bitmap roundedCornerBitmap = ImageUtils.getRoundedCornerBitmap(colorCovered, 8 * getContext().getResources().getDisplayMetrics().density);
            imageView.setImageBitmap(roundedCornerBitmap);
            btnLoad.setImageResource(R.drawable.ico_done);
            btnLoad.setEnabled(false);
            textImage.setText(R.string.particip_loaded);
            textImageTwo.setVisibility(View.GONE);
            btnCancle.setVisibility(View.VISIBLE);
        });
    }

    private void cancelCheck() {
        imageView.setImageResource(R.drawable.bg_frame_dotted_blue);
        btnLoad.setImageResource(R.drawable.ico_upload);
        btnLoad.setEnabled(true);
        textImage.setText(R.string.particip_load);
        textImageTwo.setVisibility(View.VISIBLE);
        btnCancle.setVisibility(View.INVISIBLE);
    }
}
