package irongate.checkpot.view.screens.player.tutorial;

import android.widget.ImageView;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.SmartFragment;

public class TutorialPageFragment extends SmartFragment {

    private int page;

    public TutorialPageFragment() {
        super(R.layout.fragment_tutorial_page);
    }

    @Override
    protected void onReady() {
        ImageView img = (ImageView) findViewById(R.id.img);

        TextView title = (TextView) findViewById(R.id.title);
        title.setTypeface(Fonts.getFuturaPtMedium());

        switch (page) {
            case 0:
                title.setText(R.string.tutorial_title1);
                img.setImageResource(R.drawable.bg_tutor1);
                break;

            case 1:
                title.setText(R.string.tutorial_title2);
                img.setImageResource(R.drawable.bg_tutor2);
                break;

            case 2:
                title.setText(R.string.tutorial_title3);
                img.setImageResource(R.drawable.bg_tutor3);
                break;
        }
    }

    public void setPage(int page) {
        this.page = page;
    }
}
