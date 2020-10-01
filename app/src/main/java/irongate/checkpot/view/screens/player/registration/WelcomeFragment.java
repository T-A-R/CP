package irongate.checkpot.view.screens.player.registration;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.ConfettiView;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.player.map.MapFragment;

public class WelcomeFragment extends ScreenFragment implements View.OnClickListener {
    private Button btnEnter;
    private Button btnReg;
    private TextView txtSkip;
    private ConfettiView confetti;

    private boolean enterBtn;

    public WelcomeFragment() {
        super(R.layout.fragment_welcome);
    }

    @Override
    protected void onReady() {
        RelativeLayout contBg = (RelativeLayout) findViewById(R.id.cont_welcome_bg);
        ImageView imgMap = (ImageView) findViewById(R.id.img_map);
        ImageView imgMarker = (ImageView) findViewById(R.id.img_marker);
        ImageView imgGirl = (ImageView) findViewById(R.id.img_girl);
        ImageView imgBoy = (ImageView) findViewById(R.id.img_boy);
        ImageView imgDog = (ImageView) findViewById(R.id.img_dog);
        btnEnter = (Button) findViewById(R.id.btn_welcome_enter);
        btnReg = (Button) findViewById(R.id.btn_register);
        txtSkip = (TextView) findViewById(R.id.txt_register_skip);
        RelativeLayout cont = (RelativeLayout) findViewById(R.id.cont_welcome);
        TextView txtTitle = (TextView) findViewById(R.id.txt_register_title);

        MainFragment.disableSideMenu();

        try {
            imgMap.setImageResource(R.drawable.anim_map);
            imgMarker.setImageResource(R.drawable.anim_marker);
            imgGirl.setImageResource(R.drawable.anim_girl_welcome);
            imgDog.setImageResource(R.drawable.anim_dog_side);
            imgBoy.setImageResource(R.drawable.anim_boy);
            ((AnimationDrawable) imgMap.getDrawable()).start();
            ((AnimationDrawable) imgMarker.getDrawable()).start();
            ((AnimationDrawable) imgGirl.getDrawable()).start();
            ((AnimationDrawable) imgDog.getDrawable()).start();
            ((AnimationDrawable) imgBoy.getDrawable()).start();
        } catch (OutOfMemoryError e) {
            imgMap.setImageResource(R.drawable.anim_map1);
            imgMarker.setImageResource(R.drawable.anim_marker1);
            imgGirl.setImageResource(R.drawable.anim_girl_welcome1);
            imgDog.setImageResource(R.drawable.anim_dog_side1);
            imgBoy.setImageResource(R.drawable.anim_boy1);
        }

        txtTitle.setTypeface(Fonts.getFuturaPtMedium());
        btnEnter.setTypeface(Fonts.getFuturaPtBook());
        btnEnter.setTransformationMethod(null);
        btnReg.setTypeface(Fonts.getFuturaPtBook());
        btnReg.setTransformationMethod(null);
        txtSkip.setTypeface(Fonts.getFuturaPtBook());

        btnEnter.setOnClickListener(this);
        btnReg.setOnClickListener(this);
        txtSkip.setOnClickListener(this);

        try {
            confetti = new ConfettiView(getContext());
            contBg.addView(confetti);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }

        cont.startAnimation(Anim.getAppear(getContext()));

        if (enterBtn) {
            btnEnter.setVisibility(View.VISIBLE);
            btnEnter.startAnimation(Anim.getAppearSlide(getContext(), 500));
        } else {
            btnReg.setVisibility(View.VISIBLE);
//            txtSkip.setVisibility(View.VISIBLE);
            btnReg.startAnimation(Anim.getAppearSlide(getContext(), 500));
//            txtSkip.startAnimation(Anim.getAppearSlide(getContext(), 700));
        }

        getUser().setFirstStart(false);
        getUser().setDelegateMode(false);
    }

    public WelcomeFragment setEnter(boolean enter) {
        this.enterBtn = enter;
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (confetti != null)
            confetti.startTimer();
    }

    @Override
    public void onPause() {
        if (confetti != null)
            confetti.stopTimer();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (confetti != null)
            confetti.stopTimer();

        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view == btnReg) {
            replaceFragment(new Reg1Fragment().setEnter(false));
        } else if (view == btnEnter) {
            replaceFragment(new Reg1Fragment().setEnter(true));
        } else if (view == txtSkip) {
//            replaceFragment(new MapFragment());
        }
    }
}
