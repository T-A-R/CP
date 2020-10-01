package irongate.checkpot.view.screens.delegate.editPlace;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.SmartFragment;

/**
 * Created by Iron on 07.09.2017.
 */

public class EditPlaceMiniFragment extends SmartFragment implements View.OnClickListener {
    static final public String INTENT_DELETE = "INTENT_DELETE";

    private ImageView img;
    private ImageButton btn;

    private Bitmap bitmap;

    public EditPlaceMiniFragment() {
        super(R.layout.fragment_edit_place_mini);
    }

    @Override
    protected void onReady() {
        View cont = findViewById(R.id.cont);
        img = (ImageView) findViewById(R.id.img);
        btn = (ImageButton) findViewById(R.id.btn);

        if (bitmap != null) {
            Bitmap cropedBitmap = ImageUtils.getCropedBitmap(bitmap, (int) (80 * getDensity()), (int) (60 * getDensity()));
            Bitmap roundedCornerBitmap = ImageUtils.getRoundedCornerBitmap(cropedBitmap, getDensity() * 5);
            img.setImageBitmap(roundedCornerBitmap);
        }

        cont.startAnimation(Anim.getAppear(getContext()));

        btn.setOnClickListener(this);
    }

    public void setImage(Bitmap bitmap) {
        if (bitmap != null) {
            int width = Math.min(bitmap.getWidth(), MainActivity.MAX_PHOTO_WIDTH);
            int height = (int) (bitmap.getHeight() * ((float) width / (float) bitmap.getWidth()));
            this.bitmap = ImageUtils.getCropedBitmap(bitmap, width, height);
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onClick(View view) {
        dispatchIntent(INTENT_DELETE);
    }
}
