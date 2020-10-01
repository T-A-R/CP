package irongate.checkpot.view.screens.player.advantage;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.SmartFragment;

public class AdvantageCardFragment extends SmartFragment {
    private AdvantageModel advantageModel;

    public AdvantageCardFragment() {
        super(R.layout.fragment_advantage_card);
    }

    @Override
    protected void onReady() {
        ImageView img = (ImageView) findViewById(R.id.img);
        TextView title = (TextView) findViewById(R.id.title);
        TextView desc = (TextView) findViewById(R.id.desc);

        title.setTypeface(Fonts.getFuturaPtMedium());
        desc.setTypeface(Fonts.getFuturaPtBook());

        if (advantageModel == null)
            return;

        try {
            img.setImageResource(advantageModel.image);
        } catch (OutOfMemoryError e) {
            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromResource(getResources(), advantageModel.image, 100, 100);
            img.setImageBitmap(bitmap);
        }
        title.setText(advantageModel.title);
        desc.setText(advantageModel.desc);
    }

    public AdvantageCardFragment setAdvantageModel(AdvantageModel advantageModel) {
        this.advantageModel = advantageModel;
        return this;
    }
}
