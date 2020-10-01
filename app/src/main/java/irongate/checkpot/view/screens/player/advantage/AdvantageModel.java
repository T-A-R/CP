package irongate.checkpot.view.screens.player.advantage;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class AdvantageModel {
    @StringRes
    int title;
    @StringRes
    int desc;
    @DrawableRes
    int image;

    public AdvantageModel(int title, int desc, int image) {
        this.title = title;
        this.desc = desc;
        this.image = image;
    }
}
