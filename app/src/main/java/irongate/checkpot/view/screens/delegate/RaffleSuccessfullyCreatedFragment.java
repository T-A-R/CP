package irongate.checkpot.view.screens.delegate;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
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
import irongate.checkpot.view.screens.common.place.PlaceFragment;
import irongate.checkpot.view.screens.player.rafles.RaffleCardFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class RaffleSuccessfullyCreatedFragment extends ScreenFragment implements RaffleCardFragment.CardListener {
    private Event event;

    public RaffleSuccessfullyCreatedFragment() {
        super(R.layout.fragment_raffles_successfull_created);
    }

    public ScreenFragment setEvent(Event event) {
        this.event = event;
        return this;
    }

    @Override
    protected void onReady() {
        View cont = getView();
        TextView title = (TextView) findViewById(R.id.title);
        TextView desc = (TextView) findViewById(R.id.desc);
        Button btnInstagram = (Button) findViewById(R.id.btn_instagram);
        TextView txtRaffles = (TextView) findViewById(R.id.txt_raffles);
        View contCard = findViewById(R.id.cont_card);
        RelativeLayout contConf = (RelativeLayout) findViewById(R.id.cont_confetti);

        title.setTypeface(Fonts.getFuturaPtMedium());
        btnInstagram.setTypeface(Fonts.getFuturaPtBook());
        txtRaffles.setTypeface(Fonts.getFuturaPtBook());

        RaffleCardFragment card = new RaffleCardFragment().setEvent(event).setFromCreated(true);
        card.setCardListener(this);
        //card.setWidthDp(290);
        //card.setHeightDp(310);
        //card.setWidthCardImageDp(380);
        //card.setHeighCardImageDp(180);
        card.setFontSize(28);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.cont_card, card).commit();

        cont.startAnimation(Anim.getAppearSlide(getContext()));
        title.startAnimation(Anim.getAppearSlide(getContext(), 200));
        desc.startAnimation(Anim.getAppearSlide(getContext(), 400));
        contCard.startAnimation(Anim.getAppearSlide(getContext(), 700));
        btnInstagram.startAnimation(Anim.getAppearSlide(getContext(), 1200));
        txtRaffles.startAnimation(Anim.getAppearSlide(getContext(), 1500));

        MainFragment.disableSideMenu();

//        ConfettiView confetti = new ConfettiView(getContext()).setItemsEnabled(false);
//        contConf.addView(confetti);

        ImageView imgBg = (ImageView) findViewById(R.id.ic_bg);
        RotateAnimation anim = new RotateAnimation(360.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(6000);
        imgBg.startAnimation(anim);


        btnInstagram.setOnClickListener(view -> shareInstagram(card.getScreenshot()));
        txtRaffles.setOnClickListener(view -> {
            RafflesFragment fragment = new RafflesFragment();
            fragment.setWins(true);
            replaceFragment(fragment);

        });
    }

    @Override
    public void onCardIinited() {

    }

    @Override
    public void onRestBtn(RaffleCardFragment card) {
        replaceFragment(new PlaceFragment().setPlace(event.getPlace()));
    }

    @Override
    public void onPickBtn(RaffleCardFragment card) {

    }

    @Override
    public void onEditBtn(RaffleCardFragment card) {

    }

    @Override
    public void onResultBtn(RaffleCardFragment card) {

    }

    @Override
    public void onCardClick(RaffleCardFragment card) {

    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new RafflesFragment());
        return true;
    }
}
