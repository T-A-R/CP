package irongate.checkpot.view.screens.delegate.companyRegistration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.model.User;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.screens.delegate.editPlace.EditPlaceFragment;
import irongate.checkpot.view.screens.delegate.information.SignView;
import irongate.checkpot.view.screens.player.ProfileFragment;
import irongate.checkpot.view.screens.player.map.MapFragment;

public class CompanySingFragment extends ScreenFragment implements View.OnClickListener, SmartFragment.Listener, CheckpotAPI.PutRestaurantListener, CheckpotAPI.PostUserSignListener {
    static final public String INTENT_BTN = "INTENT_BTN";
    static final public String INTENT_BACK = "INTENT_BACK";

    private RelativeLayout cont;
    private ImageButton btnClose;
    private ImageButton btn;
    private SignView signView;
    private SmartFragment openedFragment;
    private RelativeLayout view;
    private AlertDialog alert;
    private ScreenFragment nextFragment;

    boolean isClosed = false;
    boolean isSent = false;


    public CompanySingFragment() {
        super(R.layout.fragment_info_sign);
    }

    @Override
    protected void onReady() {

        cont = (RelativeLayout) findViewById(R.id.cont);
        btnClose = (ImageButton) findViewById(R.id.btn_back);
        btn = (ImageButton) findViewById(R.id.btn_done);
        TextView title = (TextView) findViewById(R.id.title);
        RelativeLayout contSign = (RelativeLayout) findViewById(R.id.cont_sign);

        signView = new SignView(getContext());
        contSign.addView(signView);

        title.setTypeface(Fonts.getFuturaPtBook());

        cont.startAnimation(Anim.getFragmentSlideUp(getContext()));

        cont.setOnClickListener(this);
        btn.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    private void hide(final String intent) {
        if (isClosed) {
            Log.d("TARLOGS", "hide: isClosed = true");
            return;
        }


        isClosed = true;
        Log.d("TARLOGS", "hide: isClosed = false > true");
        cont.startAnimation(Anim.getDisappear(getContext(), new Runnable() {
            @Override
            public void run() {
                dispatchIntent(intent);
            }
        }));
    }

    @Override
    public void onClick(View view) {
        if (isClosed)
            return;

        if (view == btn) {
            if(signView.getBitmap() != null) {
                
                if (!isSent) sendCompanyData();
                else {
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    startActivity(intent);
                }
                isSent = true;
                nextFragment = null;
                FragmentSlider fragmentSlider = new FragmentSlider();
                fragmentSlider.execute();
            } else {
                Toast.makeText(getContext(), getString(R.string.please_sign), Toast.LENGTH_SHORT).show();
            }

        } else if (view == btnClose) {
            Log.d("TARLOGS", "onClick: btnClose");
            onBackPressed();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (!isClosed && !isSent) {
            nextFragment = new CompanyDataBIKFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("back", 1);
            nextFragment.setArguments(bundle);
            FragmentSlider fragmentSlider = new FragmentSlider();
            fragmentSlider.execute();
            //hide(INTENT_BACK);

            Log.d("TARLOGS", "onBackPressed: isClose = false");
        } else {
            Log.d("TARLOGS", "onBackPressed: isClose = true");
        }
        return true;
    }

    class FragmentSlider extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cont.startAnimation(Anim.getFragmentSlideDown(getContext()));
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
            cont.setVisibility(View.INVISIBLE);
            if (nextFragment != null) replaceFragment(nextFragment);
            else {
                cont.setVisibility(View.INVISIBLE);
                createAlertDialog();
            }
        }
    }


    public Bitmap getBitmap() {
        return signView.getBitmap();
    }

    public void createAlertDialog() {
        if (alert == null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            view = (RelativeLayout) getLayoutInflater().inflate(R.layout.popup_success, null);

            builder.setView(view).setCancelable(false);

            alert = builder.show();
            alert.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            alert.getWindow().setGravity(Gravity.BOTTOM);
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            ImageView imgShape = alert.findViewById(R.id.img_shape);
            TextView txtDescription = alert.findViewById(R.id.description);
            TextView txtDescription2 = alert.findViewById(R.id.description_bold);
            ImageView imgAnimale = alert.findViewById(R.id.img_animal);
            Button btnProfile = alert.findViewById(R.id.btn_profile);
            TextView textView = alert.findViewById(R.id.title_pop);
            ImageView imgAnimaleDone = alert.findViewById(R.id.img_animal_done);
            textView.setText(R.string.info_done_title);

            btnProfile.setVisibility(View.VISIBLE);
            imgAnimale.setVisibility(View.GONE);
            imgAnimaleDone.setVisibility(View.VISIBLE);
            textView.setTypeface(Fonts.getFuturaPtMedium());
            txtDescription.setText(R.string.description_reg_final);
            txtDescription2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            txtDescription2.setText(R.string.description_reg_final2);
            imgShape.post(() -> {
                int width = imgShape.getWidth();
                RelativeLayout.LayoutParams paramsSimple = (RelativeLayout.LayoutParams) btnProfile.getLayoutParams();
                paramsSimple.width = (int) (width / 1.2);
                btnProfile.setLayoutParams(paramsSimple);
            });

            btnProfile.setOnClickListener(view -> {
                alert.dismiss();
                User.getUser().setDelegateMode(true);
                MainFragment.enableSideMenu();
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
            });
        }
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

    private void showFragment(SmartFragment fragment) {
        FragmentActivity activity = getActivity();
        if (activity == null || openedFragment != null) {
            return;
        }
        activity.getSupportFragmentManager().beginTransaction().add(R.id.cont_panel, fragment).commit();
        fragment.setListener(this);
        openedFragment = fragment;
    }

    private void sendCompanyData() {
        CompanyData companyData = CompanyRegRepository.INSTANCE.getCompanyData();
        String innKppFinal = "";
        if(companyData.getType().equals("ИП")) {
            innKppFinal = companyData.getInnkpp();
        }
        else {
            String newString = new String();
            for (int i = 0; i < companyData.getInnkpp().length(); i++) {
                newString += companyData.getInnkpp().charAt(i);
                if (i == 9) {
                    newString += "/";
                }
            }
            innKppFinal = newString;
        }

        CheckpotAPI.putRestaurant(
                companyData.getBankAccountNo(),
                companyData.getBic(),
                companyData.getEmail(),
                innKppFinal,
                companyData.getLegalAddress(),
                companyData.getLegalManager(),
                companyData.getLegalName(),
                companyData.getOgrn(),
                companyData.getType(),
                getBitmap(),
                this);
    }


    @Override
    public void onPutRestaurant(String uuid) {
        hideScreensaver();
        if (uuid == null) {
            cont.post(() -> Toast.makeText(getContext(), R.string.error_submit, Toast.LENGTH_SHORT).show());
//            getUser().updateUser(ok -> {
//                getUser().setDelegateMode(false);
//                replaceFragment(new MapFragment());
//            });
        } else {
            CheckpotAPI.postUserSign(getBitmap(), this);
        }
    }



    @Override
    public void onPostUserSign(boolean okk) {
        if(okk) {
            getUser().updateUser(ok -> createAlertDialog());
        } else {
            cont.post(() -> Toast.makeText(getContext(), R.string.error_submit, Toast.LENGTH_SHORT).show());
        }
    }

}