package irongate.checkpot.view.screens.player.event;

import android.graphics.Bitmap;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Prize;
import irongate.checkpot.checkpotAPI.models.PrizeWithBitmaps;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.SmartFragment;

public class DetailsAnotherFragment extends SmartFragment implements ViewTreeObserver.OnGlobalLayoutListener {

    private ImageView img;

    private Prize prize;
    private int position;

    public DetailsAnotherFragment() {
        super(R.layout.fragment_details_another);
    }

    public void setPrize(Prize prize) {
        this.prize = prize;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    protected void onReady() {
        TextView title = (TextView) findViewById(R.id.title);
        TextView desc = (TextView) findViewById(R.id.desc);
        img = (ImageView) findViewById(R.id.img);

        title.setTypeface(Fonts.getFuturaPtBook());
        desc.setTypeface(Fonts.getFuturaPtBook());

        title.setText(position + " место");
        desc.setText(prize.getName());

        title.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (prize.getPhotos() != null && !prize.getPhotos().isEmpty() ) {
            String placeLogoUrl = prize.getPhotos().get(0);
            ImageUtils.getBitmap(placeLogoUrl, this::onBitmap);
        } else if (prize instanceof PrizeWithBitmaps) {
            PrizeWithBitmaps prizeBmp = (PrizeWithBitmaps) prize;
            if (prizeBmp.getPhotosBmp() != null && !prizeBmp.getPhotosBmp().isEmpty()) {
                onBitmap(prizeBmp.getPhotosBmp().get(0));
            }
        }
    }

    private void onBitmap(Bitmap bitmap) {
        if (getContext() == null)
            return;
        if(bitmap != null) {
            Bitmap cropedBitmap = ImageUtils.getCropedBitmap(bitmap, img.getWidth(), img.getHeight());
            Bitmap roundedCornerBitmap = ImageUtils.getRoundedCornerBitmap(cropedBitmap, img.getWidth() / 2f);
            img.post(() -> img.setImageBitmap(roundedCornerBitmap));
        }
    }
}
