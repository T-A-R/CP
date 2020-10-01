package irongate.checkpot.view.screens.delegate.companyRegistration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.screens.IMainFragment;
import irongate.checkpot.view.screens.delegate.information.SignView;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class AddSignFragment extends ScreenFragment implements View.OnClickListener, SmartFragment.Listener, CheckpotAPI.PostUserSignListener{

    private RelativeLayout cont;
    private ImageButton btnClose;
    private ImageButton btn;
    private SignView signView;
    private SmartFragment openedFragment;
    private RelativeLayout view;
    private ScreenFragment nextFragment;
    private IMainFragment iMainFragment;

    public void setiMainFragment(IMainFragment iMainFragment) {
        this.iMainFragment = iMainFragment;
    }

    boolean isClosed = false;
    boolean isSent = false;

    public AddSignFragment() {
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

    @Override
    public void onClick(View view) {
        if (isClosed)
            return;

        if (view == btn) {
            if(signView.getBitmap() != null) {

                if (!isSent) {
                    sendCompanyData();
                    Toast.makeText(getContext(), getString(R.string.sign_added), Toast.LENGTH_SHORT).show();
                }
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
            super.onBackPressed();
        }
    }

    @Override
    public boolean onBackPressed() {
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
                loadNextFragment();
            }
        }
    }

    public void loadNextFragment() {
        MainFragment.enableSideMenu();
        replaceFragment(new RafflesFragment());
        setMain(iMainFragment);
        showMenu();
    }

    public Bitmap getBitmap() {
        return signView.getBitmap();
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

    private void sendCompanyData() {
        CheckpotAPI.postUserSign(getBitmap(), this);
    }

    @Override
    public void onPostUserSign(boolean okk) {
        hideScreensaver();
        if (!okk) {
            cont.post(() -> Toast.makeText(getContext(), R.string.error_submit, Toast.LENGTH_SHORT).show());

        } else {
            getUser().updateUser(ok -> loadNextFragment());
        }
    }

}