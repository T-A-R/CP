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

import java.util.List;
import java.util.concurrent.TimeUnit;

import irongate.checkpot.App;
import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Suggestions;
import irongate.checkpot.utils.DaDataUtils;
import irongate.checkpot.utils.OnSuggestionsListener;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.ScreenFragment;

public class CompanyRegINNFragment extends ScreenFragment implements View.OnClickListener {
    private PopupViewSmall popupViewSmall;
    private LinearLayout llInn;
    private ImageButton btnBack;
    private Button btnInn;
    private EditText etInn;
    private TextView tvInn;
    private final static String TAG = "TARLOGS";
    private final String inn = "7706107510";
    private boolean directionBack = false;

    public CompanyRegINNFragment() {
        super(R.layout.fragment_company_reg_inn);
    }

    @Override
    protected void onReady() {

        popupViewSmall = (PopupViewSmall) findViewById(R.id.popup_view_small);
        llInn = (LinearLayout) findViewById(R.id.ll_container_inn);
        popupViewSmall.getTxtDescription().setText(R.string.description_inn);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnInn = (Button) findViewById(R.id.btn_inn);
        etInn = (EditText) findViewById(R.id.edit_inn);
        tvInn = (TextView) findViewById(R.id.text_inn);

        popupViewSmall.startAnimation(Anim.getPopUp(getContext()));
        tvInn.startAnimation(Anim.getAppearSlide(getContext()));
        popupViewSmall.getTxtDescription().startAnimation(Anim.getAppearSlide(getContext(),300));
        etInn.startAnimation(Anim.getAppearSlide(getContext()));
        btnInn.startAnimation(Anim.getAppearSlide(getContext()));

        llInn.post(() -> {
            int width = llInn.getWidth();
            ConstraintLayout.LayoutParams paramsSimple = (ConstraintLayout.LayoutParams) popupViewSmall.getImgShape().getLayoutParams();
            paramsSimple.width = width;
            popupViewSmall.getImgShape().setLayoutParams(paramsSimple);

        });

        btnBack.setOnClickListener(this);
        btnInn.setOnClickListener(this);
        App.getMetricaLogger().log("Оунер на экране ввода ИНН");


        if (CompanyRegRepository.INSTANCE.getCompanyData().getInn() != null) {
            Log.d("TARLOGS", "CompanyRegRepository.INSTANCE.getCompanyData().getInn() = " + CompanyRegRepository.INSTANCE.getCompanyData().getInn());
            etInn.setText(CompanyRegRepository.INSTANCE.getCompanyData().getInn());
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            onBackPressed();
        }
        if (view == btnInn) {
            Slider slider = new Slider();
            slider.execute();
        }
    }

    private void bntNext() {
        DaDataUtils.query(etInn.getText().toString(), new OnSuggestionsListener() {
            @Override
            public void onSuggestionsReady(List<Suggestions> suggestions) {
                Log.d(TAG, "onSuggestionsReady: " + suggestions);
                if (!suggestions.isEmpty()) {
                    App.getMetricaLogger().log("Дадата нашла данные");
                    if(MainActivity.EVOTOR_MODE) Toast.makeText(getContext(), "Дадата нашла данные", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuggestionsReady: ИНН OK ++++++++++++++++++++++++++++++++++++++");
                    CompanyRegRepository.INSTANCE.getCompanyData().setInn(etInn.getText().toString());
                    replaceFragment(new CompanyDataFragment().setSuggestions(suggestions.get(0)));
                } else {
                    Toast.makeText(getContext(), "Не найдено", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onSuggestionsReady: ИНН не найдено ++++++++++++++++++++++++++++++++++++++");
                    onDaDataFail();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Не удалось проверить ИНН", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onSuggestionsReady: ERROR ++++++++++++++++++++++++++++++++++++++" + message);
                onDaDataFail();
            }
        });
    }

    private void onDaDataFail() {

        App.getMetricaLogger().log("Дадата не нашла данные");
        CompanyRegRepository.INSTANCE.getCompanyData().setInn(etInn.getText().toString());
        CompanyRegRepository.INSTANCE.getCompanyData().setSuggestions(null);
        replaceFragment(new CompanyDataFragment().setSuggestions(null));
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
            tvInn.startAnimation(Anim.getDisappear(getContext()));
            etInn.startAnimation(Anim.getDisappear(getContext()));
            btnInn.startAnimation(Anim.getDisappear(getContext()));
            popupViewSmall.startAnimation(Anim.getPopUpTwo(getContext()));
            popupViewSmall.getTxtDescription().startAnimation(Anim.getDisappear(getContext()));
            tvInn.setVisibility(View.INVISIBLE);
            etInn.setVisibility(View.INVISIBLE);
            btnInn.setVisibility(View.INVISIBLE);
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
            if(!directionBack)
            {
                bntNext();
            } else {
                replaceFragment(new CompanyRegEmailFragment());
            }
        }
    }

}
