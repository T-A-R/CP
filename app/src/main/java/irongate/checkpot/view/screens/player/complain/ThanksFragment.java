package irongate.checkpot.view.screens.player.complain;

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
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class ThanksFragment extends ScreenFragment {

    public ThanksFragment() {
        super(R.layout.fragment_thanks);
    }

    @Override
    protected void onReady() {
        View cont = findViewById(R.id.cont_thanks);
        TextView title = (TextView) findViewById(R.id.title);
        TextView desc = (TextView) findViewById(R.id.desc);
        Button btn = (Button) findViewById(R.id.btn_thanks);
        TextView txtAnother = (TextView) findViewById(R.id.txt_thanks_another);
        ImageView imgBoy = (ImageView) findViewById(R.id.img_boy);
        ImageView imgDog = (ImageView) findViewById(R.id.img_dog);

        MainFragment.disableSideMenu();

        try {
            imgDog.setImageResource(R.drawable.anim_dog_side);
            imgBoy.setImageResource(R.drawable.anim_boy);
            ((AnimationDrawable) imgDog.getDrawable()).start();
            ((AnimationDrawable) imgBoy.getDrawable()).start();
        } catch (OutOfMemoryError e) {
            imgDog.setImageResource(R.drawable.anim_dog_side1);
            imgBoy.setImageResource(R.drawable.anim_boy1);
        }

        title.setTypeface(Fonts.getFuturaPtMedium());
        desc.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);
        txtAnother.setTypeface(Fonts.getFuturaPtBook());

        cont.startAnimation(Anim.getAppear(getContext()));
        title.startAnimation(Anim.getAppearSlide(getContext(), 500));
        desc.startAnimation(Anim.getAppearSlide(getContext(), 700));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        txtAnother.startAnimation(Anim.getAppearSlide(getContext(), 1200));

        btn.setOnClickListener(view -> onBackPressed());
        txtAnother.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new RafflesFragment());
        return true;
    }
}
