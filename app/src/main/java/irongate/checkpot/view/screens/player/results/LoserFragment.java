package irongate.checkpot.view.screens.player.results;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.player.map.MapFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class LoserFragment extends ScreenFragment {

    public LoserFragment() {
        super(R.layout.fragment_loser);
    }

    @Override
    protected void onReady() {
        View cont = findViewById(R.id.cont_loser);
        TextView title = (TextView) findViewById(R.id.txt_loser_title);
        Button btn = (Button) findViewById(R.id.btn_loser);
        ImageView imgBox = (ImageView) findViewById(R.id.img_box);
        ImageView imgPresent = (ImageView) findViewById(R.id.img_present);
        final ImageView imgAlien = (ImageView) findViewById(R.id.img_alien);

        MainFragment.disableSideMenu();

        imgAlien.startAnimation(Anim.getAnimation(getContext(), R.anim.alien_in));
        imgAlien.postDelayed(() -> {
            Context context = getContext();
            if (context == null)
                return;

            imgAlien.startAnimation(Anim.getAnimation(context, R.anim.alien_out));
        }, 5000);

        try {
            imgBox.setImageResource(R.drawable.anim_box);
            ((AnimationDrawable) imgBox.getDrawable()).start();

            imgPresent.setImageResource(R.drawable.anim_present_lost);
            ((AnimationDrawable) imgPresent.getDrawable()).start();

            imgAlien.setImageResource(R.drawable.anim_alien);
            ((AnimationDrawable) imgAlien.getDrawable()).start();
        } catch (OutOfMemoryError ignored) {
        }

        title.setTypeface(Fonts.getFuturaPtMedium());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);

        cont.startAnimation(Anim.getAppear(getContext()));
        title.startAnimation(Anim.getAppearSlide(getContext(), 500));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 1000));

        btn.setOnClickListener(v -> replaceFragment(new MapFragment()));
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new RafflesFragment());
        return true;
    }
}
