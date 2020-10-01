package irongate.checkpot.view.screens.delegate.companyRegistration;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
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
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.ScreenFragment;

public class CompanyDataScoreFragment extends ScreenFragment implements View.OnClickListener {
    private PopupViewSmall popupViewSmall;
    private LinearLayout llPanel;
    private ImageButton btnBack;
    private Button btnStep;
    private EditText etScore;
    private TextView tvScore;

    private boolean directionBack = false;

    public CompanyDataScoreFragment() {
        super(R.layout.fragment_company_data_score);
    }

    @Override
    protected void onReady() {
        popupViewSmall = (PopupViewSmall) findViewById(R.id.popup_view_small);
        llPanel = (LinearLayout) findViewById(R.id.ll_container_score);
        popupViewSmall.getTxtDescription().setText(R.string.score);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnStep = (Button) findViewById(R.id.btn_score);
        etScore = (EditText) findViewById(R.id.edit_score);
        tvScore = (TextView) findViewById(R.id.tv_data_score);

        popupViewSmall.startAnimation(Anim.getPopUp(getContext()));
        popupViewSmall.getTxtDescription().startAnimation(Anim.getAppearSlide(getContext(), 300));
        tvScore.startAnimation(Anim.getAppearSlide(getContext()));
        etScore.startAnimation(Anim.getAppearSlide(getContext()));
        btnStep.startAnimation(Anim.getAppearSlide(getContext()));

        llPanel.post(() -> {
            int width = llPanel.getWidth();
            ConstraintLayout.LayoutParams paramsSimple = (ConstraintLayout.LayoutParams) popupViewSmall.getImgShape().getLayoutParams();
            paramsSimple.width = width;
            popupViewSmall.getImgShape().setLayoutParams(paramsSimple);

        });

        btnBack.setOnClickListener(this);
        btnStep.setOnClickListener(this);
        App.getMetricaLogger().log("Оунер на экране ввода расчетного счета");

        if (CompanyRegRepository.INSTANCE.getCompanyData().getBankAccountNo() != null) {
            Log.d("TARLOGS", "CompanyRegRepository.INSTANCE.getCompanyData().getBankAccountNo() = " + CompanyRegRepository.INSTANCE.getCompanyData().getBankAccountNo());
            etScore.setText(CompanyRegRepository.INSTANCE.getCompanyData().getBankAccountNo());
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            onBackPressed();
        }
        if (view == btnStep) {
            if (etScore.length() == 20) {
                CompanyRegRepository.INSTANCE.getCompanyData().setBankAccountNo(etScore.getText().toString());
                Slider slider = new Slider();
                slider.execute();
            } else {
                Toast.makeText(getContext(), "Количество цифр должно быть 20", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        directionBack = true;
        Slider slider = new Slider();
        slider.execute();
        return true;
    }

    class Slider extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvScore.startAnimation(Anim.getDisappear(getContext()));
            etScore.startAnimation(Anim.getDisappear(getContext()));
            btnStep.startAnimation(Anim.getDisappear(getContext()));
            popupViewSmall.startAnimation(Anim.getPopUpTwo(getContext()));
            popupViewSmall.getTxtDescription().startAnimation(Anim.getDisappear(getContext()));
            tvScore.setVisibility(View.INVISIBLE);
            etScore.setVisibility(View.INVISIBLE);
            btnStep.setVisibility(View.INVISIBLE);
            popupViewSmall.getTxtDescription().setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.MILLISECONDS.sleep(550);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!directionBack) {
                replaceFragment(new CompanyDataBIKFragment());
            } else {
                replaceFragment(new CompanyDataFragment());
            }
        }
    }
}