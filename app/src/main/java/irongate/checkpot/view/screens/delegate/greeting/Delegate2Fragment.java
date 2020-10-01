package irongate.checkpot.view.screens.delegate.greeting;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.TextView;

import irongate.checkpot.App;
import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.screens.delegate.companyRegistration.CompanyReg1Fragment;
import irongate.checkpot.view.screens.delegate.editPlace.EditPlaceFragment;
import irongate.checkpot.view.screens.delegate.information.InfoFragment;
import irongate.checkpot.view.screens.player.advantage.AdvantageFragment;

/**
 * Created by Iron on 07.09.2017.
 */

public class Delegate2Fragment extends ScreenFragment implements SmartFragment.Listener {
    static final String INTENT_PROFILE = "INTENT_DELEGATE";

    private Button btn;
    private DelegateLinkFragment fragmentLink;

    public Delegate2Fragment() {
        super(R.layout.fragment_delegate2);
    }

    @Override
    protected void onReady() {
        TextView title = (TextView) findViewById(R.id.title);
        TextView desc = (TextView) findViewById(R.id.desc);
        btn = (Button) findViewById(R.id.btn);
        TextView txtLink = (TextView) findViewById(R.id.txt_link);

        title.setTypeface(Fonts.getFuturaPtMedium());
        desc.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);
        txtLink.setTypeface(Fonts.getFuturaPtBook());

        MainFragment.disableSideMenu();

        //  btn.setOnClickListener(v -> replaceFragment(new InfoFragment()));
        btn.setOnClickListener(v -> {
            replaceFragment(new CompanyReg1Fragment());
            App.getMetricaLogger().log( "Регистрация ресторана через телефон");
        });
        txtLink.setOnClickListener(v -> showLinkFragment());

        title.startAnimation(Anim.getAppearSlide(getContext()));
        desc.startAnimation(Anim.getAppearSlide(getContext(), 200));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 500));
        txtLink.startAnimation(Anim.getAppearSlide(getContext(), 700));
    }

    private void showLinkFragment() {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        btn.setEnabled(false);
        fragmentLink = new DelegateLinkFragment();
        activity.getSupportFragmentManager().beginTransaction().add(R.id.cont_panel, fragmentLink).commit();
        fragmentLink.setListener(this);
    }

    @Override
    public void fragmentIntent(SmartFragment fragment, String intent) {
        Activity activity = getActivity();
        if (activity == null || fragmentLink == null)
            return;

        fragmentLink.setListener(null);
        getActivity().getSupportFragmentManager().beginTransaction().remove(fragmentLink).commit();
        fragmentLink = null;

        if (intent.equals(INTENT_PROFILE))
            replaceFragment(new EditPlaceFragment());

        btn.setEnabled(true);
    }

    @Override
    public boolean onBackPressed() {
        if (fragmentLink != null) {
            fragmentLink.onBackPressed();
            return true;
        }

        getUser().setDelegateMode(false);
        replaceFragment(new AdvantageFragment());
        return true;
    }
}
