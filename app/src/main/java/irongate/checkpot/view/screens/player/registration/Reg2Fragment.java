package irongate.checkpot.view.screens.player.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;

import irongate.checkpot.App;
import irongate.checkpot.BuildConfig;
import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.model.User;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.delegate.NotActivatedFragment;
import irongate.checkpot.view.screens.player.ProfileFragment;
import irongate.checkpot.view.screens.player.map.MapFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

import static irongate.checkpot.MainActivity.TAG;

public class Reg2Fragment extends ScreenFragment {
    private EditText inputSms;
    private Button btn;
    private TextView txtCant;
    private RelativeLayout contTop;

    private boolean isEnter;

    private int code;

    public Reg2Fragment() {
        super(R.layout.fragment_reg2);
    }

    @Override
    protected void onReady() {
        contTop = (RelativeLayout) findViewById(R.id.cont_top);
        TextView txtTitleEnter = (TextView) findViewById(R.id.txt_register2_title_enter);
        TextView txtTitleReg = (TextView) findViewById(R.id.txt_register2_title_reg);
        TextView txtTitleSms  = (TextView) findViewById(R.id.txt_register2_title_sms);
        inputSms = (EditText) findViewById(R.id.input_register2);
        btn = (Button) findViewById(R.id.btn_register2);
        txtCant = (TextView) findViewById(R.id.txt_register2_cant);

        btn.setTransformationMethod(null);

        if (isEnter) {
            txtTitleEnter.setVisibility(View.VISIBLE);
            txtTitleEnter.startAnimation(Anim.getAppearSlide(getContext()));
        } else {
            txtTitleReg.setVisibility(View.VISIBLE);
            txtTitleReg.startAnimation(Anim.getAppearSlide(getContext()));
        }

        txtTitleSms.startAnimation(Anim.getAppearSlide(getContext(), 500));
        inputSms.startAnimation(Anim.getAppearSlide(getContext(), 500));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 800));
        txtCant.startAnimation(Anim.getAppearSlide(getContext(), 1000));

        btn.setOnClickListener(v -> onBtn());
        txtCant.setOnClickListener(v -> replaceFragment(new RegCantFragment()));

        if (MainActivity.DEBUG_AUTO_ENTER_PHONE != null && code != 0 && BuildConfig.DEBUG)
        {
            CheckpotAPI.submitSmsCode(String.valueOf(code), getUtm(), this::onSubmitSmsCode);
        }

        App.getMetricaLogger().log( "Ввод кода из смс");
    }

    public Reg2Fragment setEnter(boolean enter) {
        this.isEnter = enter;
        return this;
    }

    public Reg2Fragment setCode(int code) {
        this.code = code;
        return this;
    }

    public void onBtn() {
        hideKeyboard();

        if (inputSms.length() < 4) {
            Toast.makeText(getContext(), R.string.register2_nocode, Toast.LENGTH_SHORT).show();
            return;
        }

        showScreensaver(R.string.register2_txt_wait, true);
        CheckpotAPI.submitSmsCode(inputSms.getText().toString(), getUtm(), this::onSubmitSmsCode);

    }

    @Override
    protected void onKeyboardVisible(boolean isOpen) {
        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) contTop.getLayoutParams();
        p.weight = isOpen ? 1 : 0.2f;
        contTop.setLayoutParams(p);
    }

    public void onSubmitSmsCode(final boolean ok, final boolean wrong) {
        final Activity activity = getActivity();
        if (activity == null)
            return;

        if (ok) {
            getUser().updateUser(updateOk -> {
                if (!updateOk) {
                    Toast.makeText(getContext(), R.string.error_auth, Toast.LENGTH_SHORT).show();
                    replaceFragment(new WelcomeFragment());
                    return;
                }
                MainFragment.enableSideMenu();
                OneSignal.idsAvailable((userId, registrationId) -> CheckpotAPI.pushTouch(userId));

                if(!MainActivity.EVOTOR_MODE)
                {
                    getUser().setDelegateMode(true);
                    replaceFragment(getUser().getCurrentUser().getName() != null ? new RafflesFragment().setDelegateScreen(true) : new ProfileFragment());
//                    replaceFragment(getUser().getCurrentUser().getName() != null ? new MapFragment() : new ProfileFragment());
                } else {
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
                }
            });
            return;
        }

        activity.runOnUiThread(() -> {
            hideScreensaver();
            Toast.makeText(activity, wrong ? R.string.register2_wrong_code : R.string.register2_send_error, Toast.LENGTH_SHORT).show();
        });
    }

    private CheckpotAPI.UtmBody getUtm() {
        Context context = getContext();
        if (context == null)
            return null;

        try {
            SharedPreferences preferences = context.getSharedPreferences("utm_campaign", Context.MODE_PRIVATE);
            return new CheckpotAPI.UtmBody(
                    preferences.getString("utm_source", "null"),
                    preferences.getString("utm_medium", "null"),
                    preferences.getString("utm_campaign", "null"),
                    preferences.getString("utm_term", "null"),
                    preferences.getString("utm_content", "null"));
        } catch (Exception e) {
            Log.d("IRON", "Reg2Fragment.getUtm() " + e);
        }
        return null;
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new Reg1Fragment().setEnter(true));
        return true;
    }

    private void showMessage(int type) {
        // type == 1: no place.
        // type == 2: not company.

        String title = "";
        String message = "";
        if(type == 1) { title = getString(R.string.app_name); message = getString(R.string.dialog_place_reg_evotor); }
        if(type == 2) { title = getString(R.string.app_name); message = getString(R.string.dialog_company_reg_evotor); }

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("message", message);
        NotActivatedFragment fragment = new NotActivatedFragment();
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }
}
