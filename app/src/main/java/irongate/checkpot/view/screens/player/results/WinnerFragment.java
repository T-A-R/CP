package irongate.checkpot.view.screens.player.results;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.ConfettiView;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.common.WebFragment;
import irongate.checkpot.view.screens.player.rafles.RaffleCardFullImageFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class WinnerFragment extends ScreenFragment {
    private ConfettiView confetti;
    private RelativeLayout contConf;
    private RelativeLayout contCard;
    private Event event;
    private RaffleCardFullImageFragment card;

    public WinnerFragment() {
        super(R.layout.fragment_winner);
    }

    public WinnerFragment setEvent(Event event) {
        this.event = event;
        return this;
    }

    @Override
    protected void onReady() {
        View cont = findViewById(R.id.cont_winner);
        TextView title = (TextView) findViewById(R.id.txt_winner_title);
        contCard = (RelativeLayout) findViewById(R.id.cont_winner_card);
        Button btn = (Button) findViewById(R.id.btn_winner);
        TextView txtPrizes = (TextView) findViewById(R.id.txt_winner_prizes);
        contConf = (RelativeLayout) findViewById(R.id.cont_winner_confetti);
        ImageView ballLeft = (ImageView) findViewById(R.id.img_winner_balloon_left);
        ImageView ballLeft2 = (ImageView) findViewById(R.id.img_winner_balloon_left2);
        ImageView ballRight = (ImageView) findViewById(R.id.img_winner_balloon_right);
        ImageView ballRight2 = (ImageView) findViewById(R.id.img_winner_balloon_right2);

        title.setTypeface(Fonts.getFuturaPtMedium());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);
        txtPrizes.setTypeface(Fonts.getFuturaPtBook());

        MainFragment.disableSideMenu();

        card = new RaffleCardFullImageFragment().setEvent(event).setShowPickBtn(false).setMode(1);
        getFragmentManager().beginTransaction().add(contCard.getId(), card).commit();

        cont.startAnimation(Anim.getAppear(getContext()));
        title.startAnimation(Anim.getAppearSlide(getContext(), 500));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        txtPrizes.startAnimation(Anim.getAppearSlide(getContext(), 1200));

        confetti = new ConfettiView(getContext());
        contConf.addView(confetti);

        ((AnimationDrawable) ballLeft.getDrawable()).start();
        ((AnimationDrawable) ballRight.getDrawable()).start();
        ((AnimationDrawable) ballLeft2.getDrawable()).start();
        ((AnimationDrawable) ballRight2.getDrawable()).start();

        new AnimBaloonTask(ballLeft).run();
        ballRight.postDelayed(new AnimBaloonTask(ballRight), 2000);
        ballLeft2.postDelayed(new AnimBaloonTask(ballLeft2), 4000);
        ballRight2.postDelayed(new AnimBaloonTask(ballRight2), 6000);

        btn.setOnClickListener(v -> shareInstagram(card.getScreenshot()));
        txtPrizes.setOnClickListener(v -> replaceFragment(new RafflesFragment().setWins(true)));
    }

    private class AnimBaloonTask implements Runnable {
        private ImageView img;

        AnimBaloonTask(ImageView img) {
            this.img = img;
        }

        @Override
        public void run() {
            Context context = getContext();
            if (context == null)
                return;

            img.setVisibility(View.VISIBLE);
            img.startAnimation(Anim.getBalloon(context));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (confetti != null)
            confetti.startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (confetti != null)
            confetti.stopTimer();
    }

    @Override
    public void onDestroy() {
        if (confetti != null)
            confetti.stopTimer();

        super.onDestroy();
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new RafflesFragment());
        return true;
    }
}
