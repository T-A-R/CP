package irongate.checkpot.view.screens.delegate.companyRegistration;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import irongate.checkpot.App;
import irongate.checkpot.R;
import irongate.checkpot.utils.StringUtils;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.ScreenFragment;

public class CompanyRegEmailFragment extends ScreenFragment implements View.OnClickListener {
    private PopupViewSmall popupViewSmall;
    private LinearLayout llEmail;
    private ImageButton btnBack;
    private Button btnStepTwo;
    private EditText etEmail;
    private TextView tvEmail;
    private boolean directionBack = false;

    public CompanyRegEmailFragment() {
        super(R.layout.fragment_company_reg_email);
    }

    @Override
    protected void onReady() {

        tvEmail = (TextView) findViewById(R.id.tv_email_tittle);
        popupViewSmall = (PopupViewSmall) findViewById(R.id.popup_view_small);
        llEmail = (LinearLayout) findViewById(R.id.ll_container_email);
        popupViewSmall.getTxtDescription().setText(R.string.description_email);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnStepTwo = (Button) findViewById(R.id.btn_step_two);
        etEmail = (EditText) findViewById(R.id.edit_mail);

        //slideDown(popupViewSmall);
        popupViewSmall.startAnimation(Anim.getPopUp(getContext()));
        popupViewSmall.getTxtDescription().startAnimation(Anim.getAppearSlide(getContext(), 300));
        tvEmail.startAnimation(Anim.getAppearSlide(getContext()));
        etEmail.startAnimation(Anim.getAppearSlide(getContext()));
        btnStepTwo.startAnimation(Anim.getAppearSlide(getContext()));

        llEmail.post(() -> {
            int width = llEmail.getWidth();
            LinearLayout.LayoutParams paramsSimple = (LinearLayout.LayoutParams) popupViewSmall.getLayoutParams();
            paramsSimple.width = width;
            popupViewSmall.setLayoutParams(paramsSimple);

        });

        btnBack.setOnClickListener(this);
        btnStepTwo.setOnClickListener(this);

        App.getMetricaLogger().log("Оунер на экране ввода почты");

        if (CompanyRegRepository.INSTANCE.getCompanyData().getEmail() != null) {
            Log.d("TARLOGS", "CompanyRegRepository.INSTANCE.getCompanyData().getEmail() = " + CompanyRegRepository.INSTANCE.getCompanyData().getEmail());
            etEmail.setText(CompanyRegRepository.INSTANCE.getCompanyData().getEmail());
        }

    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            onBackPressed();
        }
        if (view == btnStepTwo) {
            if (etEmail.length() != 0 && StringUtils.validEmail(etEmail.getText().toString())) {
//                CompanyRegRepository.INSTANCE.getCompanyData().setEmail(etEmail.getText().toString());
//                replaceFragment(new CompanyRegINNFragment());
                SliderUp slider = new SliderUp();
                slider.execute();
            } else {
                Toast.makeText(getContext(), "Введите email", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        directionBack = true;
        SliderUp slider = new SliderUp();
        slider.execute();
        return true;
    }


    public void slideDown(View view) {

        view.startAnimation(Anim.getPopUp(getContext()));

    }

    class SliderUp extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvEmail.startAnimation(Anim.getDisappear(getContext()));
            etEmail.startAnimation(Anim.getDisappear(getContext()));
            btnStepTwo.startAnimation(Anim.getDisappear(getContext()));
            popupViewSmall.startAnimation(Anim.getPopUpTwo(getContext()));
            popupViewSmall.getTxtDescription().startAnimation(Anim.getDisappear(getContext()));
            tvEmail.setVisibility(View.INVISIBLE);
            etEmail.setVisibility(View.INVISIBLE);
            btnStepTwo.setVisibility(View.INVISIBLE);
            popupViewSmall.getTxtDescription().setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!directionBack) {
                CompanyRegRepository.INSTANCE.getCompanyData().setEmail(etEmail.getText().toString());
                replaceFragment(new CompanyRegINNFragment());
            } else {
                replaceFragment(new CompanyReg1Fragment());
            }
        }
    }


}
