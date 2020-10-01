package irongate.checkpot.view.screens.player.registration;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import irongate.checkpot.App;
import irongate.checkpot.BuildConfig;
import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.utils.AppLogs;
import irongate.checkpot.utils.NetworkUtils;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.common.contacts.ContactsFragment;
import irongate.checkpot.view.screens.player.event.EventFragment;
import irongate.checkpot.view.screens.player.map.MapFragment;

import static irongate.checkpot.MainActivity.FULL_MODE;

public class Reg1Fragment extends ScreenFragment implements CheckpotAPI.GetSmsListener {

    private EditText inputPhone;
    private Button btn;
    private TextView txtCant;
    private TextView txtTimer;
    private RelativeLayout contTop;
    private TextView txtDesc;

    Timer timer;
    private boolean enter;
    private boolean isDesc;
    private int secLeft;
    private int code;
    private PhoneFormatter phoneFormatter = new PhoneFormatter();

    public Reg1Fragment() {
        super(R.layout.fragment_reg1);
    }

    @Override
    public boolean isMenuShown() {
        return getPrevClass() != WelcomeFragment.class && getPrevClass() != RegCantFragment.class;
    }

    @Override
    protected void onReady() {
        TextView txtTitleEnter = (TextView) findViewById(R.id.txt_register1_title_enter);
        TextView txtTitleReg = (TextView) findViewById(R.id.txt_register1_title_reg);
        txtDesc = (TextView) findViewById(R.id.txt_register1_desc);
        TextView txtTitlePhone = (TextView) findViewById(R.id.txt_register1_title_phone);
        inputPhone = (EditText) findViewById(R.id.input_register1);
        btn = (Button) findViewById(R.id.btn_register1);
        txtCant = (TextView) findViewById(R.id.txt_register1_cant);
        txtTimer = (TextView) findViewById(R.id.txt_register1_timer);
        contTop = (RelativeLayout) findViewById(R.id.cont_top);

        btn.setTransformationMethod(null);

        hideMenu();

        if (enter) {
            txtTitleEnter.setVisibility(View.VISIBLE);
            txtTitleEnter.startAnimation(Anim.getAppearSlide(getContext()));
        } else {
            txtTitleReg.setVisibility(View.VISIBLE);
            txtTitleReg.startAnimation(Anim.getAppearSlide(getContext()));
        }

        if (isDesc) {
            txtDesc.setVisibility(View.VISIBLE);
            txtDesc.startAnimation(Anim.getAppearSlide(getContext(), 200));
        }

        txtTitlePhone.startAnimation(Anim.getAppearSlide(getContext(), 500));
        inputPhone.startAnimation(Anim.getAppearSlide(getContext(), 500));
        txtDesc.startAnimation(Anim.getAppearSlide(getContext(), 600));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 800));
        txtCant.startAnimation(Anim.getAppearSlide(getContext(), 1000));

        inputPhone.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                inputPhone.setText("+7(");
            }
        });

        inputPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                phoneFormatter.beforeTextChanged(start, count);
            }

            @Override
            public void onTextChanged(CharSequence cs, int cursorPosition, int before, int count) {
                if (!cs.toString().equals(phoneFormatter.phone)) {
                    phoneFormatter.onTextChanged(cs.toString(), cursorPosition, before, count);
                    inputPhone.setText(phoneFormatter.phone);
                    inputPhone.setSelection(phoneFormatter.selection);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn.setOnClickListener(v -> sendPhoneNumber());
        txtCant.setOnClickListener(v -> replaceFragment(new RegCantFragment()));

        if (MainActivity.DEBUG_AUTO_ENTER_PHONE != null && BuildConfig.DEBUG) {
            CheckpotAPI.getSms(MainActivity.DEBUG_AUTO_ENTER_PHONE, this, true);
        }

        App.getMetricaLogger().log("Ввод номера телефона");
    }

    public Reg1Fragment setEnter(boolean enter) {
        this.enter = enter;
        return this;
    }

    public boolean isEnter() {
        return enter;
    }

    public Reg1Fragment setDesc(boolean desc) {
        this.isDesc = desc;
        return this;
    }

    String cleaned(String s) {
        String value = s.toString()
                .replace("+7(", "")
                .replace("+7", "")
                .replace("+", "")
                .replace("(", "")
                .replace(")", "")
                .replace("-", "");
        return value;
    }

    @Override
    protected void onKeyboardVisible(boolean isOpen) {
        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) contTop.getLayoutParams();
        p.weight = isOpen ? 1 : 0.2f;
        contTop.setLayoutParams(p);
        txtDesc.setVisibility(!isOpen && isDesc ? View.VISIBLE : View.GONE);
    }

    private void sendPhoneNumber() {
        Context context = getContext();
        if (context == null)
            return;

        hideKeyboard();


        String phone = "7" + cleaned(inputPhone.getText().toString());
        if (phone.length() < 11) {
            Toast.makeText(context, R.string.register1_nonumber, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!NetworkUtils.isOnline(context)) {
            Toast.makeText(context, R.string.check_connection, Toast.LENGTH_SHORT).show();
            return;
        }
        btn.setEnabled(false);
        showScreensaver(true);
        CheckpotAPI.getSms(phone, this);
    }

    @Override
    public void onGetSms(final boolean ok, final int expiredIn, int code) {
        final Activity activity = getActivity();
        if (activity == null)
            return;

        activity.runOnUiThread(() -> {
            hideScreensaver();
            if (expiredIn > 0) {
                Toast.makeText(activity, R.string.register1_send_often, Toast.LENGTH_SHORT).show();
                showTimer(expiredIn);
                return;
            }
            if (!ok) {
                Toast.makeText(activity, R.string.register1_send_error, Toast.LENGTH_SHORT).show();
//                Toast.makeText(activity, AppLogs.getOnGetSms(), Toast.LENGTH_SHORT).show();
                btn.setEnabled(true);
                return;
            }
            this.code = code;
            replaceFragment(new Reg2Fragment().setEnter(isEnter()).setCode(getCode()));
        });
    }

    private void showTimer(int expiredIn) {
        btn.setEnabled(false);

        txtTimer.setVisibility(View.VISIBLE);
        secLeft = expiredIn;

        if (timer != null)
            return;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                btn.post(() -> updateTimer());
            }
        }, 0, 1000);
    }

    private void updateTimer() {
        secLeft--;
        if (secLeft <= 0) {
            hideTimer();
            return;
        }
        int min = secLeft / 60;
        int sec = secLeft % 60;
        txtTimer.setText(R.string.register1_timer_hint);
        txtTimer.append(" " + min + ":" + (sec < 10 ? "0" : "") + sec);
    }

    private void hideTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        txtTimer.setVisibility(View.INVISIBLE);
        btn.setEnabled(true);
    }

    @Override
    public boolean onBackPressed() {
        if(FULL_MODE) {
            if (getPrevClass() == MapFragment.class || getPrevClass() == ContactsFragment.class || getPrevClass() == EventFragment.class)
                replaceFragment(new MapFragment());
            else
                replaceFragment(new WelcomeFragment().setEnter(isEnter()));
        } else {
            replaceFragment(new WelcomeFragment().setEnter(isEnter()));
        }
        return true;
    }

    @Override
    public void onDestroy() {
        if (timer != null)
            timer.cancel();

        super.onDestroy();
    }

    public int getCode() {
        return code;
    }
}
