package irongate.checkpot.view.screens.delegate.greeting;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import irongate.checkpot.App;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.model.User;
import irongate.checkpot.utils.StringUtils;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.SmartFragment;

/**
 * Created by Iron on 07.09.2017.
 */

public class DelegateLinkFragment extends SmartFragment implements View.OnClickListener {
    static final public String INTENT_PROFILE = "INTENT_DELEGATE";
    static final public String INTENT_BACK = "INTENT_BACK";

    private RelativeLayout cont;
    private Button btnSend;
    private ImageButton btnClose;
    private TextView descLink;
    private TextView titleEmail;
    private TextView descEmail;
    private TextView titleLink;
    private Button btnProfile;
    private EditText input;

    boolean isLink = false;
    boolean sended = false;
    boolean isClosed = false;

    public DelegateLinkFragment() {
        super(R.layout.fragment_delegate_link);
    }

    @Override
    protected void onReady() {
        cont = (RelativeLayout) findViewById(R.id.cont);
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        titleEmail = (TextView) findViewById(R.id.title_email);
        descEmail = (TextView) findViewById(R.id.desc_email);
        input = (EditText) findViewById(R.id.input);
        btnSend = (Button) findViewById(R.id.btn_send);
        titleLink = (TextView) findViewById(R.id.title_link);
        descLink = (TextView) findViewById(R.id.desc_link);
        btnProfile = (Button) findViewById(R.id.btn_profile);

        titleEmail.setTypeface(Fonts.getFuturaPtMedium());
        descEmail.setTypeface(Fonts.getFuturaPtBook());
        input.setTypeface(Fonts.getFuturaPtBook());
        btnSend.setTypeface(Fonts.getFuturaPtBook());
        btnSend.setTransformationMethod(null);
        titleLink.setTypeface(Fonts.getFuturaPtMedium());
        descLink.setTypeface(Fonts.getFuturaPtBook());
        btnProfile.setTypeface(Fonts.getFuturaPtBook());
        btnProfile.setTransformationMethod(null);

        MainFragment.disableSideMenu();

        cont.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        cont.startAnimation(Anim.getAppear(getContext()));

        showMail();
    }

    private void showMail() {
        titleLink.startAnimation(Anim.getDisappear(getContext()));
        descLink.startAnimation(Anim.getDisappear(getContext()));
        btnProfile.startAnimation(Anim.getDisappear(getContext()));

        btnProfile.setEnabled(false);
        input.setEnabled(true);

        titleEmail.startAnimation(Anim.getAppearSlide(getContext(), 200));
        descEmail.startAnimation(Anim.getAppearSlide(getContext(), 400));
        input.startAnimation(Anim.getAppearSlide(getContext(), 600));
        btnSend.startAnimation(Anim.getAppearSlide(getContext(), 800));

        isLink = false;
    }

    private  void showLink() {
        Anim.disappearAndInvisible(getContext(), titleEmail);
        Anim.disappearAndInvisible(getContext(), descEmail);
        Anim.disappearAndInvisible(getContext(), input);
        Anim.disappearAndInvisible(getContext(), btnSend);

        hideKeyboard();
        input.setEnabled(false);

        titleLink.setVisibility(View.VISIBLE);
        descLink.setVisibility(View.VISIBLE);
        btnProfile.setVisibility(View.VISIBLE);

        titleLink.startAnimation(Anim.getAppearSlide(getContext(), 200));
        descLink.startAnimation(Anim.getAppearSlide(getContext(), 400));
        btnProfile.startAnimation(Anim.getAppearSlide(getContext(), 600));

        btnProfile.setEnabled(true);

        isLink = true;
    }

    private void send() {
        if (!StringUtils.validEmail(input.getText().toString())) {
            Toast.makeText(getContext(), R.string.delegate2_mail_toast, Toast.LENGTH_SHORT).show();
            return;
        }

        btnSend.setEnabled(false);
        CheckpotAPI.getUiLinkForFillRestaurant(input.getText().toString(), ok -> {
            if (ok) {
                User.getUser().updateUser(ok1 -> {
                    btnSend.post(() -> {
                        sended = true;
                        showLink();
                    });
                });
                App.getMetricaLogger().log( "Регистрация ресторана через веб-форму");
            } else {
                btnSend.post(() -> {
                    btnSend.setEnabled(true);
                    Toast.makeText(getContext(), R.string.error_submit, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void hide() {
        if (isClosed)
            return;

        isClosed = true;
        cont.startAnimation(Anim.getDisappear(getContext(), new Runnable() {
            @Override
            public void run() {
                dispatchIntent(sended ? INTENT_PROFILE : INTENT_BACK);
            }
        }));
    }

    @Override
    public void onClick(View view) {
        if (isClosed)
            return;

        if (view == btnSend) {
            send();
        } else if (view == btnProfile) {
            hide();
        } else if (view == btnClose) {
            onBackPressed();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (!isClosed)
            hide();

        return true;
    }
}
