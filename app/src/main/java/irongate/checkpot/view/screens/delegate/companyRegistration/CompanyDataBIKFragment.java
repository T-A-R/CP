package irongate.checkpot.view.screens.delegate.companyRegistration;

import android.app.Activity;
import android.app.Fragment;
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
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import irongate.checkpot.App;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.screens.delegate.information.SignView;

public class CompanyDataBIKFragment extends ScreenFragment implements View.OnClickListener, SmartFragment.Listener {
    private PopupViewSmall popupViewSmall;
    private LinearLayout llPanel;
    private TextView tvBik;
    private ImageButton btnBack;
    private Button btnModeration;
    private EditText etBik;
    private ImageButton btnCheck;
    private ImageButton btnCheck2;
    private TextView txtLink;
    private TextView txtAgree;
    private boolean agree = true;
    private SmartFragment openedFragment;
    private Bitmap bmpSign;
    private RelativeLayout view;
    private SignView signView;
    private RelativeLayout contPanel;
    private RelativeLayout contFragment;
    private CompanySingFragment companySingFragment = new CompanySingFragment();

    private FragmentTransaction mTrans;

    private boolean directionBack = false;


    public CompanyDataBIKFragment() {
        super(R.layout.fragment_company_data_bik);
    }

    @Override
    protected void onReady() {
        popupViewSmall = (PopupViewSmall) findViewById(R.id.popup_view_small);
        llPanel = (LinearLayout) findViewById(R.id.ll_container_bik);
        popupViewSmall.getTxtDescription().setTextSize(22);
        popupViewSmall.getTxtDescription().setText(R.string.description_bik_help);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnModeration = (Button) findViewById(R.id.btn_moderation);
        tvBik = (TextView) findViewById(R.id.tv_bik);
        etBik = (EditText) findViewById(R.id.edit_bik);
        btnCheck = (ImageButton) findViewById(R.id.btn_check);
        btnCheck2 = (ImageButton) findViewById(R.id.btn_check2);
        txtLink = (TextView) findViewById(R.id.txt_link);
        txtAgree = (TextView) findViewById(R.id.txt_agree);
        contPanel = (RelativeLayout) findViewById(R.id.cont_panel);
        contFragment = (RelativeLayout) findViewById(R.id.cont_bik);

        Bundle bundle = this.getArguments();
        if (bundle == null) {
            //if (bundle.getInt("back", 0) != 1) {
            Log.d("TARLOGS", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> START ANIM BIK: ");
            popupViewSmall.startAnimation(Anim.getPopUp(getContext()));
            popupViewSmall.getTxtDescription().startAnimation(Anim.getAppearSlide(getContext(), 300));
            tvBik.startAnimation(Anim.getAppearSlide(getContext()));
            etBik.startAnimation(Anim.getAppearSlide(getContext()));
            btnModeration.startAnimation(Anim.getAppearSlide(getContext()));
            btnCheck.startAnimation(Anim.getAppearSlide(getContext()));
            txtLink.startAnimation(Anim.getAppearSlide(getContext()));
            txtAgree.startAnimation(Anim.getAppearSlide(getContext()));
            //}
        }

        mTrans = getFragmentManager().beginTransaction();


        llPanel.post(() -> {
            int width = llPanel.getWidth();
            ConstraintLayout.LayoutParams paramsSimple = (ConstraintLayout.LayoutParams) popupViewSmall.getImgShape().getLayoutParams();
            paramsSimple.width = width;
            popupViewSmall.getImgShape().setLayoutParams(paramsSimple);

        });

        btnBack.setOnClickListener(this);
        btnModeration.setOnClickListener(this);
        txtAgree.setOnClickListener(this);
        txtLink.setOnClickListener(v -> onPrivacyLink());
        btnCheck.setOnClickListener(this);
        App.getMetricaLogger().log("Оунер на экране ввода БИК");

        if (CompanyRegRepository.INSTANCE.getCompanyData().getBic() != null) {
            Log.d("TARLOGS", "CompanyRegRepository.INSTANCE.getCompanyData().getBic() = " + CompanyRegRepository.INSTANCE.getCompanyData().getBic());
            etBik.setText(CompanyRegRepository.INSTANCE.getCompanyData().getBic());
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            onBackPressed();
        }
        if (view == btnModeration) {
            if (!agree)
                Toast.makeText(getContext(), R.string.info_agree_toast, Toast.LENGTH_SHORT).show();
            else {
                if (etBik.length() == 9) {
                    //btnModeration.setEnabled(false);
                    CompanyRegRepository.INSTANCE.getCompanyData().setBic(etBik.getText().toString());
                    Slider slider = new Slider();
                    slider.execute();
                } else {
                    Toast.makeText(getContext(), "Количество цифр должно быть 9", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (view == btnCheck) {
            onCheckBtn();
        }
        if (view == txtAgree) {
            onCheckBtn();
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

    private void showFragment(SmartFragment fragment) {
        Log.d("TARLOGS", "showFragment: <<<<<<<<<<<<<<<<<<<0000000000000>>>>>>>>>>>>>>>>>>>>");

        FragmentActivity activity = getActivity();
        if (activity == null || openedFragment != null) {
            Log.d("TARLOGS", "onBackPressed: NULL! ");
            return;
        }
        activity.getSupportFragmentManager().beginTransaction().add(R.id.cont_bik, fragment, "123").commit();
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

    }

    @Override
    public boolean onBackPressed() {
        if (getActivity().getSupportFragmentManager().findFragmentByTag("123") != null) {
            Log.d("TARLOGS", "onBackPressed: SING OPENED! ");

            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_up, R.anim.exit_to_down, R.anim.enter_from_down, R.anim.exit_to_up)
                    .remove(getActivity().getSupportFragmentManager().findFragmentByTag("123")).commit();
            openedFragment = null;
        } else {



            directionBack = true;
            Slider slider = new Slider();
            slider.execute();
        }
        return true;
    }

    public void replaceFragmentWithAnimation(android.support.v4.app.Fragment fragment, String tag) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_up, R.anim.enter_from_up, R.anim.exit_to_down);
        transaction.replace(R.id.cont_panel, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }


    class Slider extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (directionBack) {
                tvBik.startAnimation(Anim.getDisappear(getContext()));
                etBik.startAnimation(Anim.getDisappear(getContext()));
                btnModeration.startAnimation(Anim.getDisappear(getContext()));
                btnCheck.startAnimation(Anim.getDisappear(getContext()));
                txtLink.startAnimation(Anim.getDisappear(getContext()));
                txtAgree.startAnimation(Anim.getDisappear(getContext()));
                popupViewSmall.startAnimation(Anim.getPopUpTwo(getContext()));
                popupViewSmall.getTxtDescription().startAnimation(Anim.getDisappear(getContext()));

                tvBik.setVisibility(View.INVISIBLE);
                etBik.setVisibility(View.INVISIBLE);
                btnModeration.setVisibility(View.INVISIBLE);
                btnCheck.setVisibility(View.INVISIBLE);
                txtLink.setVisibility(View.INVISIBLE);
                txtAgree.setVisibility(View.INVISIBLE);
                popupViewSmall.getTxtDescription().setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (directionBack) {
                try {
                    TimeUnit.MILLISECONDS.sleep(550);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!directionBack) {

                showFragment(new CompanySingFragment());
            } else {
                replaceFragment(new CompanyDataScoreFragment());
            }
        }
    }

    class FragmentSlider extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            contFragment.startAnimation(Anim.getFragmentSlideDown(getContext()));
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.MILLISECONDS.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
