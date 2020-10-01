package irongate.checkpot.view.screens.delegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.model.User;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.delegate.editPlace.EditPlaceFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;
import irongate.checkpot.view.screens.player.registration.WelcomeFragment;

public class NotActivatedFragment extends ScreenFragment {

    int titleText;
    int descText;

    TextView title;
    TextView desc;
    ImageView qrCode;

    public NotActivatedFragment() {
        super(R.layout.fragment_not_activated);
    }

    @Override
    protected void onReady() {
        title = (TextView) findViewById(R.id.title);
        desc = (TextView) findViewById(R.id.description);
        TextView txtMail = (TextView) findViewById(R.id.txt_mail);
        TextView txtTeleg = (TextView) findViewById(R.id.txt_teleg);
        ImageView circleMail = (ImageView) findViewById(R.id.circle_mail);
        ImageView circleTeleg = (ImageView) findViewById(R.id.circle_teleg);
        ImageView icoMail = (ImageView) findViewById(R.id.ico_mail);
        ImageView icoTeleg = (ImageView) findViewById(R.id.ico_teleg);
        qrCode = (ImageView) findViewById(R.id.qr_code);
        Button btn = (Button) findViewById(R.id.btn);
        TextView txtAgain = (TextView) findViewById(R.id.txt_again);

        title.setTypeface(Fonts.getFuturaPtMedium());
        desc.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        txtMail.setTypeface(Fonts.getFuturaPtMedium());
        txtTeleg.setTypeface(Fonts.getFuturaPtMedium());
        txtAgain.setTypeface(Fonts.getFuturaPtBook());

        MainFragment.disableSideMenu();

        if (getUser().isWaitForm()) {
            desc.setText(R.string.not_activated_desс_form);
            txtAgain.setVisibility(View.VISIBLE);
        }

        if (getUser().isActive() && getUser().getRafflesRemain() <= 0) {
            title.setText(R.string.not_activated_title_tarif);
            desc.setText(R.string.not_activated_desc_tarif);
        }

        if (MainActivity.EVOTOR_MODE) {
            btn.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) btn.getLayoutParams();
            lp.setMargins(35,0,35,35);
            btn.setLayoutParams(lp);
            txtMail.setVisibility(View.GONE);
            txtTeleg.setVisibility(View.GONE);
            circleMail.setVisibility(View.GONE);
            circleTeleg.setVisibility(View.GONE);
            icoMail.setVisibility(View.GONE);
            icoTeleg.setVisibility(View.GONE);
            txtAgain.setVisibility(View.GONE);

            Bundle bundle = getArguments();
            if (bundle != null) {
                title.setText(bundle.getString("title"));
                desc.setText(bundle.getString("message"));
                if (Objects.requireNonNull(bundle.getString("message")).contains("зарегистрировать компанию")) {
                    qrCode.setVisibility(View.VISIBLE);
                }

                btn.setVisibility(View.VISIBLE);
                btn.setText(R.string.btn_reg_evotor);
                btn.setOnClickListener(v -> renew());
            }
        }

        title.startAnimation(Anim.getAppearSlide(getContext()));
        desc.startAnimation(Anim.getAppearSlide(getContext(), 200));
        circleMail.startAnimation(Anim.getAppearSlide(getContext(), 500));
        icoMail.startAnimation(Anim.getAppearSlide(getContext(), 500));
        txtMail.startAnimation(Anim.getAppearSlide(getContext(), 500));
        circleTeleg.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        icoTeleg.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        txtTeleg.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 1500));
        txtAgain.startAnimation(Anim.getAppearSlide(getContext(), 2000));

        circleMail.setOnClickListener(v -> toMail());
        txtMail.setOnClickListener(v -> toMail());
        txtTeleg.setOnClickListener(v -> toTelegram());
        circleTeleg.setOnClickListener(v -> toTelegram());
        if (!MainActivity.EVOTOR_MODE)
            btn.setOnClickListener(v -> toProfile());
        txtAgain.setOnClickListener(v -> onAgain());
    }

    private void toMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.support_mail)});
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.support_mail_subj));
        startActivity(Intent.createChooser(intent, ""));
    }

    private void toTelegram() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.telegram_link_url)));
        startActivity(browserIntent);
    }

    private void onAgain() {
        CheckpotAPI.getUiLinkForFillRestaurant(getUser().getRestaurant().getEmail(), new CheckpotAPI.getUiLinkForFillRestaurantCallback() {
            @Override
            public void onUiLinkForFillRestaurant(boolean ok) {
                View view = getView();
                if (view == null)
                    return;

                view.post(() -> {
                    Toast.makeText(getContext(), R.string.submit_ok, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                });
            }
        });
    }

    private void toProfile() {
        replaceFragment(new EditPlaceFragment());
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(MainActivity.EVOTOR_MODE ? new WelcomeFragment().setEnter(true) : new RafflesFragment());
        return true;
    }

    private void renew() {
        showScreensaver(true);
        getUser().updateUser(ok -> {
            hideScreensaver();
            if (getUser().getRestaurant() != null) {
                User.getUser().setDelegateMode(true);
                if (getUser().getPlace() != null) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                } else {
                    showMessage(1);
                }
            } else {
                showMessage(2);
            }
        });
    }

    private void showMessage(int type) {
        // type == 1: no place.
        // type == 2: not company.

        String titleTxt = "";
        String messageTxt = "";
        if(type == 1) { titleTxt = getString(R.string.app_name); messageTxt = getString(R.string.dialog_place_reg_evotor); qrCode.setVisibility(View.INVISIBLE); }
        if(type == 2) { titleTxt = getString(R.string.app_name); messageTxt = getString(R.string.dialog_company_reg_evotor); }

        title.setText(titleTxt);
        desc.setText(messageTxt);

    }
}
