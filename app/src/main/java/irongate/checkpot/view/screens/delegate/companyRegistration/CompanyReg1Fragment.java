package irongate.checkpot.view.screens.delegate.companyRegistration;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.delegate.PromoFragment;
import irongate.checkpot.view.screens.delegate.greeting.Delegate2Fragment;
import irongate.checkpot.view.screens.player.ProfileFragment;
import irongate.checkpot.view.screens.player.registration.WelcomeFragment;

import static irongate.checkpot.MainActivity.FULL_MODE;

public class CompanyReg1Fragment extends ScreenFragment implements View.OnClickListener {
    ImageButton btnBack;
    Button btnUnderstand;
    private PopupView popupView;
    private TextView textView;

    public CompanyReg1Fragment() {
        super(R.layout.fragment_company_reg1);
    }

    @Override
    protected void onReady() {
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnUnderstand = (Button) findViewById(R.id.btn_understand);
        popupView = (PopupView) findViewById(R.id.popup_view);
        textView = (TextView) findViewById(R.id.title);

        popupView.startAnimation(Anim.getAppearSlide(getContext(), 500));
        popupView.getTxtDescription().startAnimation(Anim.getAppearSlide(getContext(), 1000));
        btnBack.startAnimation(Anim.getAppearSlide(getContext()));
        textView.startAnimation(Anim.getAppearSlide(getContext()));
        btnUnderstand.startAnimation(Anim.getAppearSlide(getContext(), 500));

        MainFragment.disableSideMenu();

        btnBack.setOnClickListener(this);
        btnUnderstand.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            onBackPressed();
        }
        if (view == btnUnderstand) {
            CompanyRegRepository.INSTANCE.reset();
            replaceFragment(new CompanyRegEmailFragment());
        }
    }


    @Override
    public boolean onBackPressed() {
        if (FULL_MODE)
            replaceFragment(new Delegate2Fragment());
        else
            replaceFragment(new WelcomeFragment().setEnter(true));
            //replaceFragment(new ProfileFragment());
        return true;
    }
}
