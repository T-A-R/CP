package irongate.checkpot.view.screens.delegate.companyRegistration;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Suggestions;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.FontAdapter;
import irongate.checkpot.view.NoDefaultSpinner;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.delegate.addedRaffle.CustomTextWatcher;

public class CompanyDataFragment extends ScreenFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {
    private ImageButton btnBack;
    private ImageView btnCheck;
    private TextView txtAgree;
    private TextView title;
    private TextView titleName;
    private Button btn;
    private LinearLayout llPanel;
    private MaskedEditText editInn;
    private PawEditNarrowView editOgrn;
    private PawEditNarrowView editName;
    private PawEditNarrowView editAddress;
    private PawEditNarrowView editFio;
    private PopupViewSmall popupViewSmall;
    private TextView titleInn;
    private NoDefaultSpinner spinnerFormOrgType;
    private CompanyDataFragment.FormType formType = FormType.Nothing;
    private boolean agree = false;
    private static final String TAG = "TARLOGS";
    private boolean directionBack = false;

    private Suggestions suggestions;

    public CompanyDataFragment() {
        super(R.layout.fragment_company_data);
    }

    public CompanyDataFragment setSuggestions(Suggestions suggestions) {
        this.suggestions = suggestions;
        return this;
    }

    @Override
    protected void onReady() {
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        title = (TextView) findViewById(R.id.title);
        titleName = (TextView) findViewById(R.id.title_name);
        llPanel = (LinearLayout) findViewById(R.id.panel);
        btnCheck = (ImageView) findViewById(R.id.btn_check);
        txtAgree = (TextView) findViewById(R.id.txt_agree);
        popupViewSmall = (PopupViewSmall) findViewById(R.id.popup_view_small);
        titleInn = (TextView) findViewById(R.id.title_inn);


        spinnerFormOrgType = (NoDefaultSpinner) findViewById(R.id.spinner_form);
        editName = (PawEditNarrowView) findViewById(R.id.edit_name);
        editInn = (MaskedEditText) findViewById(R.id.edit_inn);
        editOgrn = (PawEditNarrowView) findViewById(R.id.edit_ogrn);
        editAddress = (PawEditNarrowView) findViewById(R.id.edit_address);
        editFio = (PawEditNarrowView) findViewById(R.id.edit_fio);

        btn = (Button) findViewById(R.id.btn);

        popupViewSmall.getTxtDescription().setText(R.string.description_data);

        title.startAnimation(Anim.getAppearSlide(getContext()));
        titleName.startAnimation(Anim.getAppearSlide(getContext()));
        llPanel.startAnimation(Anim.getAppearSlide(getContext()));
        titleInn.startAnimation(Anim.getAppearSlide(getContext()));
        spinnerFormOrgType.startAnimation(Anim.getAppearSlide(getContext()));
        btn.startAnimation(Anim.getAppearSlide(getContext()));
        popupViewSmall.startAnimation(Anim.getPopUpFour(getContext()));
        popupViewSmall.getTxtDescription().startAnimation(Anim.getAppearSlide(getContext()));


        ArrayList<String> adapterItems = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.inf_forms)));
        FontAdapter adapter = new FontAdapter(getContext(), R.layout.spinner_item, adapterItems);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerFormOrgType.setAdapter(adapter);
        llPanel.post(() -> {
            int width = llPanel.getWidth();
            ConstraintLayout.LayoutParams paramsSimple = (ConstraintLayout.LayoutParams) popupViewSmall.getImgShape().getLayoutParams();
            paramsSimple.width = width;
            popupViewSmall.getImgShape().setLayoutParams(paramsSimple);
        });

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) editAddress.getEditText().getLayoutParams();
        params.height = 400;
        editAddress.getEditText().setLayoutParams(params);

        editOgrn.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        editAddress.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editFio.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
        btnBack.setOnClickListener(this);
        btn.setOnClickListener(this);
        spinnerFormOrgType.setOnItemSelectedListener(this);

//        if (suggestions == null) {
//            if (CompanyRegRepository.INSTANCE.getCompanyData().getSuggestions() != null) {
//                suggestions = CompanyRegRepository.INSTANCE.getCompanyData().getSuggestions();
//                Log.d(TAG, "suggestions loaded! <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//            } else {
//
//            }
//        }

//        editInn.setText(CompanyRegRepository.INSTANCE.getCompanyData().getInnkpp());
//        CompanyRegRepository.INSTANCE.getCompanyData().setLegalAddress(editAddress.getEditText().getText().toString());
//        CompanyRegRepository.INSTANCE.getCompanyData().setLegalManager(editAddress.getEditText().getText().toString());
//        CompanyRegRepository.INSTANCE.getCompanyData().setLegalName(editName.getEditText().getText().toString());
//        CompanyRegRepository.INSTANCE.getCompanyData().setOgrn(editOgrn.getEditText().getText().toString());
//        CompanyRegRepository.INSTANCE.getCompanyData().setType(formType == FormType.OOO ? "ООО" : "ИП");

        if (suggestions != null) {

            if (suggestions.getValue() != null) {
                editName.getEditText().setText(suggestions.getValue());
            }
            if (suggestions.getData().getOgrn() != null) {
                editOgrn.getEditText().setText(suggestions.getData().getOgrn());
            }
            editAddress.getEditText().setText(suggestions.getData().getAddress().getUnrestricted_value());
            if (suggestions.getData().getManagement() != null) {
                editFio.getEditText().setText(suggestions.getData().getManagement().getName());
            }

            if (suggestions.getData().getType().equals("INDIVIDUAL")) {
                setIp();
            } else {
                setOOO();
            }
        } else {
            if (CompanyRegRepository.INSTANCE.getCompanyData().getInnkpp() == null) {
                if (CompanyRegRepository.INSTANCE.getCompanyData().getInn() != null)
                    editInn.setText(CompanyRegRepository.INSTANCE.getCompanyData().getInn());
                editInn.setEnabled(true);
                editName.setEnabled(true);
                editName.getEditText().setHint(R.string.info_hint_name);
                title.setText(R.string.title_text_data_setting_company);
                popupViewSmall.getTxtDescription().setText(R.string.description_data);
                //setOOO();
            } else {
                Log.d(TAG, "GET DATA MANAGER: >>>>>>>>>>>>>>>>>>> " + CompanyRegRepository.INSTANCE.getCompanyData().getLegalManager());
                editName.getEditText().setText(CompanyRegRepository.INSTANCE.getCompanyData().getLegalName());
                editOgrn.getEditText().setText(CompanyRegRepository.INSTANCE.getCompanyData().getOgrn());
                editAddress.getEditText().setText(CompanyRegRepository.INSTANCE.getCompanyData().getLegalAddress());
                editFio.getEditText().setText(CompanyRegRepository.INSTANCE.getCompanyData().getLegalManager());


                if (CompanyRegRepository.INSTANCE.getCompanyData().getType().equals("ИП")) {
                    setIp();
                } else {
                    setOOO();
                }
            }
        }

        if (suggestions != null || CompanyRegRepository.INSTANCE.getCompanyData().getInnkpp() != null)
            popupViewSmall.getTxtDescription().setText(R.string.description_data);
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            onBackPressed();
        } else if (view == btn) {
            if (!isAllValid()) {
                Toast.makeText(getContext(), R.string.need_all_valid, Toast.LENGTH_SHORT).show();
            } else {
                CompanyRegRepository.INSTANCE.getCompanyData().setInnkpp(editInn.getRawText());
                CompanyRegRepository.INSTANCE.getCompanyData().setLegalAddress(editAddress.getEditText().getText().toString());
                CompanyRegRepository.INSTANCE.getCompanyData().setLegalManager(editFio.getEditText().getText().toString());
                CompanyRegRepository.INSTANCE.getCompanyData().setLegalName(editName.getEditText().getText().toString());
                CompanyRegRepository.INSTANCE.getCompanyData().setOgrn(editOgrn.getEditText().getText().toString());
                CompanyRegRepository.INSTANCE.getCompanyData().setType(formType == FormType.OOO ? "ООО" : "ИП");
                CompanyRegRepository.INSTANCE.getCompanyData().setSuggestions(suggestions);
                Slider slider = new Slider();
                slider.execute();
            }
        }

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

    private void setOOO() {
        editInn.setMask(getResources().getString(R.string.info_mask_innkpp));
        if (suggestions != null)
            editInn.setText(suggestions.getData().getInn() + suggestions.getData().getKpp());

        if (CompanyRegRepository.INSTANCE.getCompanyData().getInnkpp() != null)
            editInn.setText(CompanyRegRepository.INSTANCE.getCompanyData().getInnkpp());

        titleInn.setText(R.string.info_title_innkpp);

        InputFilter[] ifOgrn = {new InputFilter.LengthFilter(13)};
        editOgrn.getEditText().setFilters(ifOgrn);

        onFocusChange(editInn, false);
        onFocusChange(editOgrn.getEditText(), false);
        onFocusChange(editAddress.getEditText(), false);
        onFocusChange(editFio.getEditText(), false);
        onFocusChange(editName.getEditText(), false);

        editInn.setEnabled(true);
        editOgrn.getEditText().setEnabled(true);
        editAddress.getEditText().setEnabled(true);
        editFio.getEditText().setEnabled(true);
        editName.getEditText().setEnabled(true);

        editOgrn.getEditText().setHint(R.string.info_hint_ogrn);
        editAddress.getEditText().setHint(R.string.info_hint_address);
        editName.getEditText().setHint(R.string.info_hint_name);
        editFio.getEditText().setHint(R.string.info_hint_fio);

        if (CompanyRegRepository.INSTANCE.getCompanyData().getInnkpp() != null)
            editInn.setText(CompanyRegRepository.INSTANCE.getCompanyData().getInnkpp());
        else if ((suggestions == null || suggestions.getData().getInn() == null) && CompanyRegRepository.INSTANCE.getCompanyData().getInn() != null)
            editInn.setText(CompanyRegRepository.INSTANCE.getCompanyData().getInn());

        spinnerFormOrgType.setSelection(0);
        formType = CompanyDataFragment.FormType.OOO;
    }

    private void setIp() {
        editInn.setMask(getResources().getString(R.string.info_mask_inn));
        if (suggestions != null) editInn.setText(suggestions.getData().getInn());
        if (CompanyRegRepository.INSTANCE.getCompanyData().getInnkpp() != null)
            editInn.setText(CompanyRegRepository.INSTANCE.getCompanyData().getInnkpp());
        titleInn.setText(R.string.info_title_inn);

        InputFilter[] ifOgrn = {new InputFilter.LengthFilter(15)};
        editOgrn.getEditText().setFilters(ifOgrn);

        onFocusChange(editInn, false);
        onFocusChange(editOgrn.getEditText(), false);
        onFocusChange(editAddress.getEditText(), false);
        onFocusChange(editFio.getEditText(), false);

        editAddress.getEditText().setEnabled(true);
        editFio.getEditText().setEnabled(true);
        editName.getEditText().setEnabled(true);
        editInn.setEnabled(true);
        editOgrn.getEditText().setEnabled(true);

        editOgrn.getEditText().setHint(R.string.info_hint_ogrn);
        editAddress.getEditText().setHint(R.string.info_hint_address);
        editName.getEditText().setHint(R.string.info_hint_ip);
        editFio.getEditText().setHint(R.string.info_hint_fio);

        if (CompanyRegRepository.INSTANCE.getCompanyData().getInn() != null)
            editInn.setText(CompanyRegRepository.INSTANCE.getCompanyData().getInn());

        spinnerFormOrgType.setSelection(1);
        formType = CompanyDataFragment.FormType.IP;
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus)
            view.setBackgroundResource(R.drawable.bg_selector_blue);
        else {
            if (suggestions != null) {
                fieldChange(view);
                ((EditText) view).addTextChangedListener(new CustomTextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        fieldChange(view);
                    }
                });
            }

        }
    }

    private void fieldChange(View view) {
        boolean validated = isValidEdit((EditText) view);
        if (suggestions != null) {
            title.setText(validated ? R.string.title_text_data_company : R.string.title_text_data_setting_company);
            popupViewSmall.getTxtDescription().setText(validated ? R.string.description_data : R.string.description_help);
            setTextHelp((EditText) view, validated);
            view.setBackgroundResource(validated ? R.drawable.bg_selector_blue : R.drawable.bg_frame_orange_input);
        }
    }

    private void setTextHelp(EditText edit, boolean validate) {
        if (edit == editName.getEditText()) {
            editName.getTxtHelp().setText(validate ? "" : "Заполните поле");
            editName.getImgPaw().setVisibility(validate ? View.GONE : View.VISIBLE);
        }

        if (edit == editAddress.getEditText()) {
            editAddress.getTxtHelp().setText(validate ? "" : "Заполните поле");
            editAddress.getImgPaw().setVisibility(validate ? View.GONE : View.VISIBLE);
        }

        if (edit == editFio.getEditText()) {
            editFio.getTxtHelp().setText(validate ? "" : "Заполните поле");
            editFio.getImgPaw().setVisibility(validate ? View.GONE : View.VISIBLE);
        }
        if (edit == editOgrn.getEditText()) {
            switch (formType) {
                case Nothing:
                    editOgrn.getTxtHelp().setText(validate ? "" : "Выберите форму");
                    return;

                case OOO:
                    editOgrn.getTxtHelp().setText(validate ? "" : "Кол-во цифр должно быть 13");

                case IP:
                    editOgrn.getTxtHelp().setText(validate ? "" : "Кол-во цифр должно быть 15");
            }

            editOgrn.getImgPaw().setVisibility(validate ? View.GONE : View.VISIBLE);
        }

    }

    private boolean isValidEdit(EditText edit) {
        String str = edit.getText().toString();
        if (edit == editInn) {
            switch (formType) {
                case Nothing:
                    setTextHelp(editInn, false);
                    return false;

                case OOO:
                    if (((MaskedEditText) edit).getText().length() == 20) {
                        Log.d(TAG, "======== OOO INN OK!" + ((MaskedEditText) edit).getText());
                        setTextHelp(editInn, true);
                        return true;
                    } else {
                        setTextHelp(editInn, false);
                    }

                case IP:
                    if (((MaskedEditText) edit).getText().length() == 12) {
                        Log.d(TAG, "======== IP INN OK!");
                        setTextHelp(editInn, true);
                        return true;
                    } else {
                        setTextHelp(editInn, false);
                    }
            }
        }
        if (edit == editOgrn.getEditText()) {
            switch (formType) {
                case Nothing:
                    return false;

                case OOO:
                    if (str.length() == 13) {
                        Log.d(TAG, "======== OOO OGRN OK!");
                        setTextHelp(editOgrn.getEditText(), true);
                        return true;
                    } else {
                        setTextHelp(editOgrn.getEditText(), false);
                    }

                case IP:
                    if (str.length() == 15) {
                        Log.d(TAG, "======== IP OGRN OK!");
                        setTextHelp(editOgrn.getEditText(), true);
                        return true;
                    } else {
                        setTextHelp(editOgrn.getEditText(), false);
                    }
            }
        }

        if (edit == editAddress.getEditText()) {
            if (str.length() > 0) {
                Log.d(TAG, "======== ADRESS OK!");
                setTextHelp(editAddress.getEditText(), true);
                return true;
            } else {
                setTextHelp(editAddress.getEditText(), false);
            }

        }
        if (edit == editName.getEditText()) {
            if (str.length() > 0) {
                Log.d(TAG, "======== NAME OK!");
                setTextHelp(editName.getEditText(), true);
                return true;
            } else {
                setTextHelp(editName.getEditText(), false);
            }

        }
        if (edit == editFio.getEditText()) {
            if (str.length() > 0) {
                Log.d(TAG, "======== FIO OK!");
                setTextHelp(editFio.getEditText(), true);
                return true;
            } else {
                setTextHelp(editFio.getEditText(), false);
            }
        }

        return false;
    }

    private boolean isAllValid() {
        return isValidEdit(editName.getEditText())
                && isValidEdit(editInn)
                && isValidEdit(editOgrn.getEditText())
                && isValidEdit(editAddress.getEditText())
                && isValidEdit(editFio.getEditText());
    }

    private enum FormType {
        Nothing, OOO, IP
    }

    @Override
    public boolean onBackPressed() {
        directionBack = true;
        Slider slider = new Slider();
        slider.execute();
        //replaceFragment(new CompanyRegINNFragment());
        return true;
    }

    class Slider extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            title.startAnimation(Anim.getDisappear(getContext()));
            titleName.startAnimation(Anim.getDisappear(getContext()));
            llPanel.startAnimation(Anim.getDisappear(getContext()));
            titleInn.startAnimation(Anim.getDisappear(getContext()));
            spinnerFormOrgType.startAnimation(Anim.getDisappear(getContext()));
            btn.startAnimation(Anim.getDisappear(getContext()));

            popupViewSmall.startAnimation(Anim.getPopUpThree(getContext()));
            popupViewSmall.getTxtDescription().startAnimation(Anim.getDisappear(getContext()));

            title.setVisibility(View.INVISIBLE);
            titleName.setVisibility(View.INVISIBLE);
            llPanel.setVisibility(View.INVISIBLE);
            titleInn.setVisibility(View.INVISIBLE);
            spinnerFormOrgType.setVisibility(View.INVISIBLE);
            btn.setVisibility(View.INVISIBLE);
            popupViewSmall.getTxtDescription().setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.MILLISECONDS.sleep(450);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!directionBack) {
                replaceFragment(new CompanyDataScoreFragment());
            } else {
                replaceFragment(new CompanyRegINNFragment());
            }
        }
    }
}
