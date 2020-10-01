package irongate.checkpot.view.screens.delegate.information;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.model.User;
import irongate.checkpot.utils.StringUtils;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.FontAdapter;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.NoDefaultSpinner;
import irongate.checkpot.view.screens.delegate.editPlace.EditPlaceFragment;
import irongate.checkpot.view.screens.delegate.greeting.Delegate2Fragment;
import irongate.checkpot.view.screens.player.map.MapFragment;

public class InfoFragment extends ScreenFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener, SmartFragment.Listener, CheckpotAPI.PutRestaurantListener {
    private ImageButton btnBack;
    private ImageView btnCheck;
    private ImageView btnCheck2;
    private TextView txtAgree;
    private Button btn;
    private TextView titleInn;
    private MaskedEditText editInn;
    private EditText editOgrn;
    private EditText editMail;
    private EditText editName;
    private EditText editAddress;
    private EditText editBik;
    private EditText editScore;
    private EditText editFio;
    private SmartFragment openedFragment;

    private FormType formType = FormType.Nothing;
    private boolean agree = false;
    private Bitmap bmpSign;

    public InfoFragment() {
        super(R.layout.fragment_info);
    }

    @Override
    protected void onReady() {
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        TextView title = (TextView) findViewById(R.id.title);
        View shadow = findViewById(R.id.shadow);
        View panel = findViewById(R.id.panel);
        TextView titleMail = (TextView) findViewById(R.id.title_mail);
        TextView titleForm = (TextView) findViewById(R.id.title_form);
        TextView titleName = (TextView) findViewById(R.id.title_name);
        titleInn = (TextView) findViewById(R.id.title_inn);
        TextView titleOgrn = (TextView) findViewById(R.id.title_ogrn);
        TextView titleAddress = (TextView) findViewById(R.id.title_address);
        TextView titleBik = (TextView) findViewById(R.id.title_bik);
        TextView titleScore = (TextView) findViewById(R.id.title_score);
        TextView titleFio = (TextView) findViewById(R.id.title_fio);
        btnCheck = (ImageView) findViewById(R.id.btn_check);
        btnCheck2 = (ImageView) findViewById(R.id.btn_check2);
        txtAgree = (TextView) findViewById(R.id.txt_agree);
        TextView txtLink = (TextView) findViewById(R.id.txt_link);

        editMail = (EditText) findViewById(R.id.edit_mail);
        NoDefaultSpinner spinnerForm = (NoDefaultSpinner) findViewById(R.id.spinner_form);
        editName = (EditText) findViewById(R.id.edit_name);
        editInn = (MaskedEditText) findViewById(R.id.edit_inn);
        editOgrn = (EditText) findViewById(R.id.edit_ogrn);
        editAddress = (EditText) findViewById(R.id.edit_address);
        editBik = (EditText) findViewById(R.id.edit_bik);
        editScore = (EditText) findViewById(R.id.edit_score);
        editFio = (EditText) findViewById(R.id.edit_fio);

        btn = (Button) findViewById(R.id.btn);

        title.setTypeface(Fonts.getFuturaPtMedium());
        titleMail.setTypeface(Fonts.getFuturaPtBook());
        titleForm.setTypeface(Fonts.getFuturaPtBook());
        titleName.setTypeface(Fonts.getFuturaPtBook());
        titleInn.setTypeface(Fonts.getFuturaPtBook());
        titleOgrn.setTypeface(Fonts.getFuturaPtBook());
        titleAddress.setTypeface(Fonts.getFuturaPtBook());
        titleBik.setTypeface(Fonts.getFuturaPtBook());
        titleScore.setTypeface(Fonts.getFuturaPtBook());
        titleFio.setTypeface(Fonts.getFuturaPtBook());
        txtAgree.setTypeface(Fonts.getFuturaPtBook());
        txtLink.setTypeface(Fonts.getFuturaPtBook());
        editMail.setTypeface(Fonts.getFuturaPtBook());
        editName.setTypeface(Fonts.getFuturaPtBook());
        editInn.setTypeface(Fonts.getFuturaPtBook());
        editOgrn.setTypeface(Fonts.getFuturaPtBook());
        editAddress.setTypeface(Fonts.getFuturaPtBook());
        editBik.setTypeface(Fonts.getFuturaPtBook());
        editScore.setTypeface(Fonts.getFuturaPtBook());
        editFio.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);

        MainFragment.disableSideMenu();

        ArrayList<String> adapterItems = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.inf_forms)));
        FontAdapter adapter = new FontAdapter(getContext(), R.layout.spinner_item, adapterItems);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerForm.setAdapter(adapter);

        editMail.setOnFocusChangeListener(this);
        editName.setOnFocusChangeListener(this);
        editInn.setOnFocusChangeListener(this);
        editOgrn.setOnFocusChangeListener(this);
        editAddress.setOnFocusChangeListener(this);
        editBik.setOnFocusChangeListener(this);
        editScore.setOnFocusChangeListener(this);
        editFio.setOnFocusChangeListener(this);

        btnBack.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        txtAgree.setOnClickListener(this);
        btn.setOnClickListener(this);
        txtLink.setOnClickListener(v -> onPrivacyLink());

        spinnerForm.setOnItemSelectedListener(this);

        btnBack.startAnimation(Anim.getAppearSlide(getContext()));
        title.startAnimation(Anim.getAppearSlide(getContext(), 200));
        shadow.startAnimation(Anim.getAppearSlide(getContext(), 500));
        panel.startAnimation(Anim.getAppearSlide(getContext(), 500));
        btnCheck2.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        txtAgree.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        txtLink.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 1000));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                setOOO();
                break;

            case 1:
                setIp();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setOOO() {
        editInn.setMask(getResources().getString(R.string.info_mask_innkpp));
        titleInn.setText(R.string.info_title_innkpp);

        InputFilter[] ifOgrn = { new InputFilter.LengthFilter(13) };
        editOgrn.setFilters(ifOgrn);

        onFocusChange(editInn, false);
        onFocusChange(editOgrn, false);

        editInn.setEnabled(true);
        editOgrn.setEnabled(true);

        editName.setHint(R.string.info_hint_name);

        formType = FormType.OOO;
    }

    private void setIp() {
        editInn.setMask(getResources().getString(R.string.info_mask_inn));
        titleInn.setText(R.string.info_title_inn);

        InputFilter[] ifOgrn = { new InputFilter.LengthFilter(15) };
        editOgrn.setFilters(ifOgrn);

        onFocusChange(editInn, false);
        onFocusChange(editOgrn, false);

        editInn.setEnabled(true);
        editOgrn.setEnabled(true);

        editName.setHint(R.string.info_hint_fio);

        formType = FormType.IP;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus)
            view.setBackgroundResource(R.drawable.bg_selector_blue);
        else {
            boolean validated = isValidEdit((EditText) view);
            view.setBackgroundResource(validated ? R.drawable.bg_selector_blue : R.drawable.bg_frame_red_8_input);
        }
    }

    private boolean isValidEdit(EditText edit) {
        String str = edit.getText().toString();
        if (edit == editMail) {
            return StringUtils.validEmail(str);
        }
        else if (edit == editInn) {
            switch (formType) {
                case Nothing:
                    return false;

                case OOO:
                    return ((MaskedEditText)edit).getRawText().length() == 19;

                case IP:
                    return ((MaskedEditText)edit).getRawText().length() == 12;
            }
        } else if (edit == editOgrn) {
            switch (formType) {
                case Nothing:
                    return false;

                case OOO:
                    return str.length() == 13;

                case IP:
                    return str.length() == 15;
            }
        } else if (edit == editBik) {
            return str.length() == 9;
        } else if (edit == editScore) {
            return str.length() == 20;
        } else {
            return str.length() != 0;
        }
        return false;
    }

    private boolean isAllValid() {
        return isValidEdit(editMail)
            && isValidEdit(editName)
            && isValidEdit(editInn)
            && isValidEdit(editOgrn)
            && isValidEdit(editAddress)
            && isValidEdit(editBik)
            && isValidEdit(editScore)
            && isValidEdit(editFio);
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            onBackPressed();
        } else if (view == btnCheck) {
            onCheckBtn();
        } else if (view == txtAgree) {
            onCheckBtn();
        } else if (view == btn) {
            if (!isAllValid())
                Toast.makeText(getContext(), R.string.need_all_valid, Toast.LENGTH_SHORT).show();
            else if (!agree)
                Toast.makeText(getContext(), R.string.info_agree_toast, Toast.LENGTH_SHORT).show();
            else {
                btn.setEnabled(false);
                showFragment(new InfoSignFragment());
            }
        }
    }

    private void onPrivacyLink() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.info_link_url))));
    }

    private void onCheckBtn() {
        agree = !agree;
        if (agree)
            btnCheck2.setImageResource(R.drawable.ico_done);
        else
            btnCheck2.setImageDrawable(null);
    }

    private void showFragment(SmartFragment fragment) {
        FragmentActivity activity = getActivity();
        if (activity == null || openedFragment != null) {
            return;
        }
        activity.getSupportFragmentManager().beginTransaction().add(R.id.cont_panel, fragment).commit();
        fragment.setListener(this);
        openedFragment = fragment;
    }

    @Override
    public void fragmentIntent(SmartFragment fragment, String intent) {
        if (openedFragment == null)
            return;

        Activity activity = getActivity();
        if (activity == null || fragment == null)
            return;

        fragment.setListener(null);
        getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        openedFragment = null;

        if (fragment instanceof InfoSignFragment && intent.equals(InfoSignFragment.INTENT_BTN)) {
            bmpSign = ((InfoSignFragment)fragment).getBitmap();
            submitInfo();
        }
        else if (fragment instanceof InfoDoneFragment) {
            replaceFragment(new EditPlaceFragment());
        }
        else
            btn.setEnabled(true);
    }

    private void submitInfo() {
        String bankAccountNo = editScore.getText().toString();
        String bic = editBik.getText().toString();
        String email = editMail.getText().toString();
        String innkpp = editInn.getText().toString();
        String legalAddress = editAddress.getText().toString();
        String legalManager = editFio.getText().toString();
        String legalName = editName.getText().toString();
        String ogrn = editOgrn.getText().toString();
        String type = formType == FormType.OOO ? "ООО" : "ИП";
        showScreensaver(false);
        CheckpotAPI.putRestaurant(bankAccountNo, bic, email, innkpp, legalAddress, legalManager, legalName, ogrn, type, bmpSign, this);
    }

    @Override
    public void onPutRestaurant(String uuid) {
        hideScreensaver();
        if (uuid == null) {
            editName.post(() -> Toast.makeText(getContext(), R.string.error_submit, Toast.LENGTH_SHORT).show());
            getUser().updateUser(ok -> {
                getUser().setDelegateMode(false);
                replaceFragment(new MapFragment());
            });
        }
        else {
            getUser().updateUser(ok -> showFragment(new InfoDoneFragment()));
        }
    }

    @Override
    public boolean onBackPressed() {
        if (openedFragment != null) {
            openedFragment.onBackPressed();
            return true;
        }

        replaceFragment(new Delegate2Fragment());
        return true;
    }

    private enum FormType {
        Nothing, OOO, IP
    }
}
