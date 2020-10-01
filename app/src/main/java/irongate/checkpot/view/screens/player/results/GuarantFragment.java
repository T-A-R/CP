package irongate.checkpot.view.screens.player.results;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Prize;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.ConfettiView;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class GuarantFragment extends ScreenFragment {
    private ConfettiView confetti;
    private ImageView img;

    private Prize prize;

    public GuarantFragment() {
        super(R.layout.fragment_guarant);
    }

    public GuarantFragment setPrize(Prize prize) {
        this.prize = prize;
        return this;
    }

    @Override
    protected void onReady() {
        View card = findViewById(R.id.card);
        View cont = findViewById(R.id.cont_guarant);
        View contCard = findViewById(R.id.cont_card);
        TextView title = (TextView) findViewById(R.id.txt_guarant_title);
        Button btn = (Button) findViewById(R.id.btn_guarant);
        TextView txtPrizes = (TextView) findViewById(R.id.txt_guarant_prizes);
        RelativeLayout contConf = (RelativeLayout) findViewById(R.id.cont_guarant_confetti);
        ImageView imgDog = (ImageView) findViewById(R.id.img_dog);
        TextView txtName = (TextView) findViewById(R.id.txt_name);
        img = (ImageView) findViewById(R.id.img_photo);

        try {
            imgDog.setImageResource(R.drawable.anim_dog_win);
            ((AnimationDrawable) imgDog.getDrawable()).start();
        } catch (OutOfMemoryError e) {
            imgDog.setImageResource(R.drawable.anim_dog_win1);
        }

        title.setTypeface(Fonts.getFuturaPtMedium());
        txtName.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);
        txtPrizes.setTypeface(Fonts.getFuturaPtBook());

        cont.startAnimation(Anim.getAppear(getContext()));
        title.startAnimation(Anim.getAppearSlide(getContext(), 500));
        card.startAnimation(Anim.getAppearSlide(getContext(), 700));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        txtPrizes.startAnimation(Anim.getAppearSlide(getContext(), 1200));

        confetti = new ConfettiView(getContext());
        contConf.addView(confetti);

        txtName.setText(prize.getName());
        ImageUtils.getBitmap(prize.getPhotos().get(0), bitmap -> {
            Context context = getContext();
            if (context == null) {
                return;
            }

            bitmap = ImageUtils.getCropedBitmap(bitmap, img.getWidth(), img.getHeight());
            bitmap = ImageUtils.getRoundedCornerBitmap(bitmap, img.getWidth());
            img.setImageBitmap(bitmap);
            img.setAnimation(Anim.getAppear(context));
        });

        btn.setOnClickListener(v -> shareInstagram(ImageUtils.getScreenshot(contCard)));
        txtPrizes.setOnClickListener(v -> replaceFragment(new RafflesFragment().setWins(true)));
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
