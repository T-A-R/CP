package irongate.checkpot.view.screens.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import irongate.checkpot.App;
import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.UserModel;
import irongate.checkpot.utils.AppLogs;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.delegate.companyRegistration.CompanyReg1Fragment;
import irongate.checkpot.view.screens.delegate.editPlace.EditPlaceFragment;
import irongate.checkpot.view.screens.player.map.MapFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class ProfileFragment extends ScreenFragment {

    private ImageButton btnCheck;
    private ImageButton btnCheck2;
    private EditText inputName;

    private boolean agree;
    private Bitmap bmpPhoto;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    protected void onReady() {

        TextView txtTitle = (TextView) findViewById(R.id.txt_profile_title);
        View panel = findViewById(R.id.panel);
        TextView txtName = (TextView) findViewById(R.id.txt_profile_name);
        inputName = (EditText) findViewById(R.id.input_profile_name);
        Button btn = (Button) findViewById(R.id.btn_profile);
        btnCheck = (ImageButton) findViewById(R.id.btn_check);
        btnCheck2 = (ImageButton) findViewById(R.id.btn_check2);
        TextView txtAgree = (TextView) findViewById(R.id.txt_agree);
        View linearCheck = findViewById(R.id.linear_check);

        txtAgree.setMovementMethod(LinkMovementMethod.getInstance());

        txtTitle.setTypeface(Fonts.getFuturaPtMedium());
        txtName.setTypeface(Fonts.getFuturaPtBook());
        inputName.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);
        txtAgree.setTypeface(Fonts.getFuturaPtBook());

        MainFragment.enableSideMenu();

        UserModel currentUser = getUser().getCurrentUser();

        if (currentUser.getName() != null)
            inputName.setText(currentUser.getName());

        if (getUser().getCurrentUser() != null)
            linearCheck.setVisibility(getUser().getCurrentUser().getName() == null ? View.VISIBLE : View.GONE);

        txtTitle.startAnimation(Anim.getAppear(getContext()));
        panel.startAnimation(Anim.getAppear(getContext()));
        txtName.startAnimation(Anim.getAppearSlide(getContext(), 700));
        inputName.startAnimation(Anim.getAppearSlide(getContext(), 700));
        txtAgree.startAnimation(Anim.getAppearSlide(getContext(), 900));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 1100));

        btn.setOnClickListener(v -> onSaveBtn());
        btnCheck.setOnClickListener(v -> onCheckBtn());
        txtAgree.setOnClickListener(v -> onCheckBtn());
    }

    @Override
    public boolean isMenuShown() {
        return getUser().getCurrentUser().getName() != null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getContext();
        if (context == null || resultCode != Activity.RESULT_OK) {
            return;
        }

    }

    private void onCheckBtn() {
        agree = !agree;
        if (agree)
            btnCheck2.setImageResource(R.drawable.ico_done);
        else
            btnCheck2.setImageDrawable(null);
    }

    private void onSaveBtn() {
        if (!agree && getUser().getCurrentUser().getName() == null) {
            Toast.makeText(getContext(), R.string.profile_toast_agree, Toast.LENGTH_SHORT).show();
        } else if (!checkFields()) {
            Toast.makeText(getContext(), R.string.need_all_valid, Toast.LENGTH_SHORT).show();
        } else {
            submit();
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean checkFields() {
        if (inputName.getText().length() == 0) {
            Toast.makeText(getContext(), "Не указано имя", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void submit() {
        showScreensaver(true);
        String name = inputName.getText().toString();
        CheckpotAPI.patchCurrentUser(name, bmpPhoto, ok -> {
            if (ok) {
                getUser().updateUser(ok1 -> {
                    Toast.makeText(getContext(), R.string.submit_ok, Toast.LENGTH_SHORT).show();
//                    replaceFragment(new MapFragment());
                    if (getUser().getRestaurant() != null)
                    {
                        replaceFragment(getUser().getPlace() != null ? new RafflesFragment() : new EditPlaceFragment());
                    } else {
                        replaceFragment(new CompanyReg1Fragment());
                    }

                });
            } else {
                Toast.makeText(getContext(), R.string.error_submit, Toast.LENGTH_SHORT).show();
                hideScreensaver();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
//        replaceFragment(new RafflesFragment());
        return true;
    }
}
