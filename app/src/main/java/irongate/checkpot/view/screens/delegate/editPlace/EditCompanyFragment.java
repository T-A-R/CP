package irongate.checkpot.view.screens.delegate.editPlace;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import irongate.checkpot.App;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.Restaurant;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.FontAdapter;
import irongate.checkpot.view.NoDefaultSpinner;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.screens.delegate.companyRegistration.CompanyDataScoreFragment;
import irongate.checkpot.view.screens.delegate.companyRegistration.CompanyRegRepository;
import irongate.checkpot.view.screens.delegate.companyRegistration.CompanySingFragment;
import irongate.checkpot.view.screens.delegate.companyRegistration.PawEditNarrowView;
import irongate.checkpot.view.screens.delegate.companyRegistration.PopupViewSmall;
import irongate.checkpot.view.screens.delegate.information.SignView;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class EditCompanyFragment extends ScreenFragment implements View.OnClickListener, SmartFragment.Listener, AdapterView.OnItemSelectedListener, CheckpotAPI.PatchRestaurantListener {

    private ImageButton btnBack;
    private TextView txtTitle;
    private TextView txtEmail;
    private PawEditNarrowView etEmail;
    private TextView txtType;
    private NoDefaultSpinner spinnerFormOrgType;
    private TextView txtName;
    private PawEditNarrowView etName;
    private TextView txtInnKpp;
    private PawEditNarrowView etInnKpp;
    private TextView txtOgrn;
    private PawEditNarrowView etOgrn;
    private TextView txtAdress;
    private PawEditNarrowView etAdress;
    private TextView txtBik;
    private PawEditNarrowView etBik;
    private TextView txtScore;
    private PawEditNarrowView etScore;
    private TextView txtFio;
    private PawEditNarrowView etFio;
    private Button btnSend;
    private ImageButton btnCheck;
    private ImageButton btnCheck2;
    private TextView txtLink;
    private TextView txtAgree;

    private boolean agree = false;
    private boolean isOOO = true;
    private SmartFragment openedFragment;

    public EditCompanyFragment() {
        super(R.layout.fragment_edit_company);
    }

    @Override
    protected void onReady() {
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        txtTitle = (TextView) findViewById(R.id.title);
        txtEmail = (TextView) findViewById(R.id.title_email);
        etEmail = (PawEditNarrowView) findViewById(R.id.edit_email);
        txtType = (TextView) findViewById(R.id.title_type);
        spinnerFormOrgType = (NoDefaultSpinner) findViewById(R.id.spinner_form);
        txtName = (TextView) findViewById(R.id.title_name);
        etName = (PawEditNarrowView) findViewById(R.id.edit_name);
        txtInnKpp = (TextView) findViewById(R.id.title_inn_kpp);
        etInnKpp = (PawEditNarrowView) findViewById(R.id.edit_inn);
        txtOgrn = (TextView) findViewById(R.id.title_ogrn);
        etOgrn = (PawEditNarrowView) findViewById(R.id.edit_ogrn);
        txtAdress = (TextView) findViewById(R.id.title_adress);
        etAdress = (PawEditNarrowView) findViewById(R.id.edit_address);
        txtBik = (TextView) findViewById(R.id.title_bik);
        etBik = (PawEditNarrowView) findViewById(R.id.edit_bik);
        txtScore = (TextView) findViewById(R.id.title_score);
        etScore = (PawEditNarrowView) findViewById(R.id.edit_score);
        txtFio = (TextView) findViewById(R.id.title_fio);
        etFio = (PawEditNarrowView) findViewById(R.id.edit_fio);
        btnSend = (Button) findViewById(R.id.btn_publication);
        btnCheck = (ImageButton) findViewById(R.id.btn_check);
        btnCheck2 = (ImageButton) findViewById(R.id.btn_check2);
        txtLink = (TextView) findViewById(R.id.txt_link);
        txtAgree = (TextView) findViewById(R.id.txt_agree);

        btnBack.startAnimation(Anim.getAppearSlide(getContext()));
        txtTitle.startAnimation(Anim.getAppearSlide(getContext()));
        txtEmail.startAnimation(Anim.getAppearSlide(getContext()));
        etEmail.startAnimation(Anim.getAppearSlide(getContext()));
        txtType.startAnimation(Anim.getAppearSlide(getContext()));
        spinnerFormOrgType.startAnimation(Anim.getAppearSlide(getContext()));
        txtName.startAnimation(Anim.getAppearSlide(getContext()));
        etName.startAnimation(Anim.getAppearSlide(getContext()));
        txtInnKpp.startAnimation(Anim.getAppearSlide(getContext()));
        etInnKpp.startAnimation(Anim.getAppearSlide(getContext()));
        txtOgrn.startAnimation(Anim.getAppearSlide(getContext()));
        etOgrn.startAnimation(Anim.getAppearSlide(getContext()));
        txtAdress.startAnimation(Anim.getAppearSlide(getContext()));
        etAdress.startAnimation(Anim.getAppearSlide(getContext()));
        txtBik.startAnimation(Anim.getAppearSlide(getContext()));
        etBik.startAnimation(Anim.getAppearSlide(getContext()));
        txtScore.startAnimation(Anim.getAppearSlide(getContext()));
        etScore.startAnimation(Anim.getAppearSlide(getContext()));
        txtFio.startAnimation(Anim.getAppearSlide(getContext()));
        etFio.startAnimation(Anim.getAppearSlide(getContext()));
        btnSend.startAnimation(Anim.getAppearSlide(getContext()));
        btnCheck.startAnimation(Anim.getAppearSlide(getContext()));
        btnCheck2.startAnimation(Anim.getAppearSlide(getContext()));
        txtLink.startAnimation(Anim.getAppearSlide(getContext()));
        txtAgree.startAnimation(Anim.getAppearSlide(getContext()));

        ArrayList<String> adapterItems = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.inf_forms)));
        FontAdapter adapter = new FontAdapter(getContext(), R.layout.spinner_item, adapterItems);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerFormOrgType.setAdapter(adapter);

        Bundle bundle = this.getArguments();
        if (bundle == null) {
        }

        btnBack.setOnClickListener(this);
        txtAgree.setOnClickListener(this);
        txtLink.setOnClickListener(v -> onPrivacyLink());
        btnCheck.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        spinnerFormOrgType.setOnItemSelectedListener(this);

        initData();
    }

    private void initData() {
        Restaurant restaurant = getUser().getRestaurant();
        if (restaurant == null) {
            Toast.makeText(getContext(), "У вас еще нет ресторана", Toast.LENGTH_SHORT).show();
            Log.d("IRON", "EditPlaceFragment.submit() restaurant == null");
            return;
        }

        etEmail.getEditText().setText(restaurant.getEmail());
        if (restaurant.getType().equals("ООО")) {
            spinnerFormOrgType.setSelection(0);
        } else {
            spinnerFormOrgType.setSelection(1);
        }
        etName.getEditText().setText(restaurant.getLegalName());
        etInnKpp.getEditText().setText(restaurant.getInnkpp());
        etOgrn.getEditText().setText(restaurant.getOgrn());
        etAdress.getEditText().setText(restaurant.getLegalAddress());
        etBik.getEditText().setText(restaurant.getBic());
        etScore.getEditText().setText(restaurant.getBankAccountNo());
        etFio.getEditText().setText(restaurant.getLegalManager());
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            onBackPressed();
        }

        if (view == btnCheck) {
            onCheckBtn();
        }
        if (view == txtAgree) {
            onCheckBtn();
        }
        if (view == btnSend) {
            onSendBtn();
        }
    }

    private void onSendBtn() {
        if (isFieldsNotEmpty()) {
            if (agree)
                updateRestaurant();
            else
                Toast.makeText(getContext(), R.string.info_agree_toast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), R.string.need_all_valid, Toast.LENGTH_SHORT).show();
        }
    }

    private void onCheckBtn() {
        agree = !agree;
        if (agree)
            btnCheck2.setImageResource(R.drawable.ico_done);
        else
            btnCheck2.setImageDrawable(null);
    }

    private void onPrivacyLink() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.info_link_url))));
    }

//    private void showFragment(SmartFragment fragment) {
//
//        FragmentActivity activity = getActivity();
//        if (activity == null || openedFragment != null) {
//            return;
//        }
//        activity.getSupportFragmentManager().beginTransaction().add(R.id.cont_bik, fragment, "123").commit();
//        fragment.setListener(this);
//        openedFragment = fragment;
//
//    }

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

    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new RafflesFragment());
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                isOOO = true;
                break;

            case 1:
                isOOO = false;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private boolean isFieldsNotEmpty() {
        if (etEmail.getEditText().getText().toString().length() > 0 &&
                etName.getEditText().getText().toString().length() > 0 &&
                etInnKpp.getEditText().getText().toString().length() > 0 &&
                etOgrn.getEditText().getText().toString().length() > 0 &&
                etAdress.getEditText().getText().toString().length() > 0 &&
                etBik.getEditText().getText().toString().length() > 0 &&
                etScore.getEditText().getText().toString().length() > 0 &&
                etFio.getEditText().getText().toString().length() > 0) {
            return true;
        } else
            return false;
    }

    private void updateRestaurant() {
        showScreensaver(false);
        Restaurant restaurantNewData = new Restaurant();
        restaurantNewData.setUuid(getUser().getRestaurant().getUuid());
        restaurantNewData.setEmail(etEmail.getEditText().getText().toString());
        restaurantNewData.setLegalName(etName.getEditText().getText().toString());
        restaurantNewData.setInnkpp(etInnKpp.getEditText().getText().toString());
        restaurantNewData.setOgrn(etOgrn.getEditText().getText().toString());
        restaurantNewData.setLegalAddress(etAdress.getEditText().getText().toString());
        restaurantNewData.setBic(etBik.getEditText().getText().toString());
        restaurantNewData.setBankAccountNo(etScore.getEditText().getText().toString());
        restaurantNewData.setLegalManager(etFio.getEditText().getText().toString());
        restaurantNewData.setType(isOOO ? "ООО" : "ИП");

        CheckpotAPI.patchRestaurant(restaurantNewData, this);
    }

    @Override
    public void onPatchRestaurant(boolean ok) {
        hideScreensaver();
        if (ok) {
            Toast.makeText(getContext(), R.string.submit_ok, Toast.LENGTH_SHORT).show();
            showDrawer();
            replaceFragment(new RafflesFragment());
        } else {
            Toast.makeText(getContext(), R.string.error_submit, Toast.LENGTH_SHORT).show();
        }
    }
}
