package irongate.checkpot.view.screens.delegate.aboutWinner;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.model.User;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.utils.ScreenUtils;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.common.WebFragment;
import irongate.checkpot.view.screens.player.complain.ParticipFragment;

import static irongate.checkpot.model.User.getUser;

public class WinnersFragment extends ScreenFragment {
    private EditText etIdWinner;
    private Button btnNotPrized;
    private Button btnPrized;
    private EditText editText;
    private TextView titleDialog;
    private Button btnSend;
    private Button btnCancel;
    private LinearLayout view;
    private RecyclerView rvWinners;
    private WinnersViewAdapter winnersViewAdapter;
    private WinnersRepository winnersRepository = WinnersRepository.INSTANCE;
    private int type = 0;
    private Bitmap bmpImg;
    private int averageColor = -1;
    private boolean wasBackPressed = false;
    private ImageView sideBtn;
    private ImageView arrow;
    private RelativeLayout cont;

    public WinnersFragment() {
        super(R.layout.fragment_winners);
    }

    public int getType() {
        return type;
    }

    public WinnersFragment setImage(Bitmap bmpImg) {
        this.bmpImg = bmpImg;
        return this;
    }

    @Override
    public boolean isMenuShown() {
        return true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onReady() {
        cont = (RelativeLayout) findViewById(R.id.cont);
        btnNotPrized = (Button) findViewById(R.id.subtitle_not_prized);
        btnPrized = (Button) findViewById(R.id.subtitle_prized);
        etIdWinner = (EditText) findViewById(R.id.et_id);
        rvWinners = (RecyclerView) findViewById(R.id.rv_winners);
        sideBtn = (ImageView) findViewById(R.id.side_btn);
        arrow = (ImageView) findViewById(R.id.btn_arrow);
        btnNotPrized.setBackgroundResource(R.drawable.bg_btn_blue_22);
        btnPrized.setBackgroundResource(R.drawable.bg_btn_trans);
        btnNotPrized.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        btnPrized.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));

        MainFragment.enableSideMenu();

        if (MainActivity.EVOTOR_MODE) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rvWinners.getLayoutParams();
            layoutParams.bottomMargin = 0;
            rvWinners.setLayoutParams(layoutParams);
            sideBtn.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.VISIBLE);
            sideBtn.setOnClickListener(v -> MainFragment.openDrawer());
        }

        etIdWinner.post(() -> {
            int width = etIdWinner.getWidth();
            RelativeLayout.LayoutParams paramsSimple = (RelativeLayout.LayoutParams) btnNotPrized.getLayoutParams();
            paramsSimple.width = (int) (width / 1.8 - 20);
            btnNotPrized.setLayoutParams(paramsSimple);
            RelativeLayout.LayoutParams paramsSimple1 = (RelativeLayout.LayoutParams) btnPrized.getLayoutParams();
            paramsSimple1.width = width / 2 - 20;
            btnPrized.setLayoutParams(paramsSimple1);
        });

        winnersViewAdapter = new WinnersViewAdapter(type, (direction, userProfile) -> {

            if (direction == 0) {
                createAlertDialogCause(userProfile, 0);

            } else if(direction == 1){
                createAlertDialogCause(userProfile, 1);
                btnNotPrized.setBackgroundResource(R.drawable.bg_btn_blue_22);
                btnPrized.setBackgroundResource(R.drawable.bg_btn_trans);
                btnNotPrized.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                btnPrized.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                fillListView(0, winnersRepository.loadDataRaffle(type));

            } else if(direction == 2) {
                String url = "https://vk.com/id" + userProfile.getVk_user_id();
                replaceFragment(new WebFragment().setData(url));
            }
        });
        rvWinners.setLayoutManager(new LinearLayoutManager(getContext()));
        rvWinners.setAdapter(winnersViewAdapter);

        editText = (EditText) findViewById(R.id.description);
        titleDialog = (TextView) findViewById(R.id.title_prize_confirm);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        btnNotPrized.setOnClickListener(v -> onBtnNotPrized());
        btnPrized.setOnClickListener(view -> onBtnPrized());
        listener();

        showScreensaver(false);
        getUser().updateUser(ok -> {
            hideScreensaver();
            fillListView(type, winnersRepository.loadDataRaffle(type));
        });
    }

    public void listener() {
        etIdWinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String winnerId = editable.toString();
                List<SimpleCardRiffle> cardRiffleList = winnersRepository.searchWinnerId(type, winnerId);
                fillListView(type, cardRiffleList);
            }
        });
    }

    private void onBtnNotPrized() {
        showScreensaver(false);

        Context context = getContext();
        if (context == null)
            return;

        btnNotPrized.setBackgroundResource(R.drawable.bg_btn_blue_22);
        btnPrized.setBackgroundResource(R.drawable.bg_btn_trans);
        btnNotPrized.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        btnPrized.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        type = 0;
        fillListView(type, winnersRepository.loadDataRaffle(type));

        getUser().updateUser(ok -> {
            if (context == null)
                return;

            hideScreensaver();

            if (type == 0)
                fillListView(type, winnersRepository.loadDataRaffle(type));
        });
    }

    private void onBtnPrized() {

        Context context = getContext();
        btnPrized.setBackgroundResource(R.drawable.bg_btn_blue_22);
        btnNotPrized.setBackgroundResource(R.drawable.bg_btn_trans);
        btnPrized.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        btnNotPrized.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        type = 1;
        fillListView(type, winnersRepository.loadDataRaffle(type));

    }

    public void fillListView(int type, List<SimpleCardRiffle> cardList) {

        winnersViewAdapter.clear();
        List<SimpleCardRiffle> simpleCardRiffles = new ArrayList<>();
        for (int i = cardList.size() - 1; i >= 0; i--) {
            if (type == 0 && !cardList.get(i).getUserProfileList().isEmpty()) {
                simpleCardRiffles.add(cardList.get(i));
            } else if (type == 1 && !cardList.get(i).getUserProfilePrizedList().isEmpty()) {
                simpleCardRiffles.add(cardList.get(i));
            }
        }
        winnersViewAdapter.addItems(type, simpleCardRiffles);
    }

    public void createAlertDialogCause(SimpleUserProfile userProfile, Integer type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        view = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.fragment_alert_dialog, null);


        builder.setView(view)
                .setCancelable(false);

        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawableResource(R.drawable.bg_panel_15);

        alert.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(alert.getWindow().getAttributes());
//        lp.width = ScreenUtils.convertDpToPixel(330, getContext());
//        lp.height = ScreenUtils.convertDpToPixel(400, getContext());
        lp.y = -50;
        alert.getWindow().setAttributes(lp);
        btnCancel = alert.findViewById(R.id.btn_cancel);
        btnSend = alert.findViewById(R.id.btn_send);
        editText = alert.findViewById(R.id.description);
        titleDialog = alert.findViewById(R.id.title_prize_confirm);
        btnCancel.setOnClickListener(view -> alert.dismiss());

        editText.post(() -> {
            int width = editText.getWidth();
            RelativeLayout.LayoutParams paramsSimple = (RelativeLayout.LayoutParams) btnCancel.getLayoutParams();
            paramsSimple.width = (width / 2 - 20);
            btnCancel.setLayoutParams(paramsSimple);
            RelativeLayout.LayoutParams paramsSimple1 = (RelativeLayout.LayoutParams) btnSend.getLayoutParams();
            paramsSimple1.width = width / 2 - 20;
            btnSend.setLayoutParams(paramsSimple1);
            if (type == 1) {
                editText.setVisibility(View.VISIBLE);
                titleDialog.setVisibility(View.GONE);
            } else {
                editText.setVisibility(View.GONE);
                titleDialog.setVisibility(View.VISIBLE);
            }
        });

        btnSend.setOnClickListener(view -> {
            if (type == 1) {
                String text = editText.getText().toString();
                if (text.isEmpty()) {
                    editText.setError("Пожалуйста укажите причину");
                } else {
                    alert.dismiss();
                    showScreensaver(true);
                    CheckpotAPI.patchEventDecline(userProfile.getRaffleUuid(), userProfile.getWinnerUuid(), text, () -> getUser().updateUser(ok -> {
                        hideScreensaver();
                        if (ok) {
//                            fillListView(type, winnersRepository.loadDataRaffle(type));
                            onBtnNotPrized();
                        }
                    }));
                }
            } else {
                alert.dismiss();
                showScreensaver(false);
                CheckpotAPI.patchEventAccept(userProfile.getRaffleUuid(), userProfile.getWinnerUuid(), () -> getUser().updateUser(ok -> {
                    hideScreensaver();
                    if (ok) {
                        fillListView(type, winnersRepository.loadDataRaffle(type));
                    } else {

                    }
                }));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        MainFragment.enableSideMenu();
    }

    @Override
    public boolean onBackPressed() {
        if (MainActivity.EVOTOR_MODE) {
            if (wasBackPressed) {
                return super.onBackPressed();
            }

            wasBackPressed = true;
            Toast.makeText(getContext(), R.string.map_doubleclick_exit, Toast.LENGTH_SHORT).show();
            return true;
        } else
            return super.onBackPressed();
    }
}
