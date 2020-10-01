package irongate.checkpot.view.screens.delegate;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.utils.StringUtils;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.delegate.aboutWinner.WinnersFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;


public class PromoFragment extends ScreenFragment {
    private RelativeLayout contTop;
    private EditText editMail;
    private EditText editVk;

    public PromoFragment() {
        super(R.layout.fragment_promo);
    }

    @Override
    public boolean isMenuShown() {
        return true;
    }

    @Override
    protected void onReady() {
        TextView title =  findViewById(R.id.title);
        TextView desc =  findViewById(R.id.desc);
        editMail =  findViewById(R.id.edit_mail);
        editVk =  findViewById(R.id.edit_vk);
        Button btn =  findViewById(R.id.btn);
        contTop =  findViewById(R.id.cont_top);

        btn.setTransformationMethod(null);

        title.startAnimation(Anim.getAppearSlide(getContext()));
        desc.startAnimation(Anim.getAppearSlide(getContext(), 200));
        editVk.startAnimation(Anim.getAppearSlide(getContext(), 400));
        editMail.startAnimation(Anim.getAppearSlide(getContext(), 600));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 1000));

        MainFragment.disableSideMenu();

        btn.setOnClickListener(v -> onBtn());
    }

    @Override
    protected void onKeyboardVisible(boolean isOpen) {
        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) contTop.getLayoutParams();
        p.weight = isOpen ? 1 : 0.2f;
        contTop.setLayoutParams(p);
    }

    private void onBtn() {
        String communityLink = editVk.getText().toString();
        if (!StringUtils.validVkLink(communityLink)) {
            Toast.makeText(getContext(), R.string.promo_mail_vk_toast, Toast.LENGTH_SHORT).show();
            return;
        }

        String email = editMail.getText().toString();
        if (!StringUtils.validEmail(email)) {
            Toast.makeText(getContext(), R.string.promo_mail_toast, Toast.LENGTH_SHORT).show();
            return;
        }

        showScreensaver(true);
        CheckpotAPI.sendPromo(email, communityLink, this::onSend);
    }

    public void onSend(boolean ok) {
        hideScreensaver();
        if (!ok) {
            Toast.makeText(getContext(), R.string.error_submit, Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getContext(), R.string.submit_ok_mail, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(MainActivity.EVOTOR_MODE ? new WinnersFragment() : new RafflesFragment());
        return true;
    }
}
