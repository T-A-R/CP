package irongate.checkpot.view.screens.player.complain;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import irongate.checkpot.App;
import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.utils.CompareCheck;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.ScreenFragment;

public class HandmadeBillFragment extends ScreenFragment implements ViewTreeObserver.OnGlobalLayoutListener, CheckpotAPI.SendCheckListener {

    private View cont;
    private ImageButton btnClose;
    private Button btnSend;
    private TextView tvHead;
    private TextView billFN;
    private EditText editFN;
    private TextView helpFN;
    private TextView billFPD;
    private EditText editFPD;
    private TextView helpFPD;
    private TextView billFD;
    private EditText editFD;
    private TextView helpFD;
    private TextView billSum;
    private EditText editSum;
    private TextView helpSum;
    private TextView billDate;
    private TextView editDate;
    private TextView helpDate;

    private String dataFN;
    private String dataFPD;
    private String dataFD;
    private String dataSum;
    private String dataDate;
    private String correctDate;


    private int scrollY;
    private Event event;
    private boolean isDataCorrect;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private Calendar calendar;
    private DatePickerDialog dialogDate;
    private int year, month, day, hour, minute;
    private String monthS, dayS, hourS, minuteS;

    public HandmadeBillFragment() {
        super(R.layout.fragment_handmade_bill);
    }

    public HandmadeBillFragment setScrollY(int scrollY) {
        this.scrollY = scrollY;
        return this;
    }

    public HandmadeBillFragment setEvent(Event event) {
        this.event = event;
        return this;
    }

    @Override
    protected void onReady() {

        cont = findViewById(R.id.cont_scroll);
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnSend = (Button) findViewById(R.id.btn_send);
        tvHead = (TextView) findViewById(R.id.bill_head);
        billFN = (TextView) findViewById(R.id.bill_fn);
        editFN = (EditText) findViewById(R.id.edit_fn);
        helpFN = (TextView) findViewById(R.id.fn_help);
        billFPD = (TextView) findViewById(R.id.bill_fpd);
        editFPD = (EditText) findViewById(R.id.edit_fpd);
        helpFPD = (TextView) findViewById(R.id.fpd_help);
        billFD = (TextView) findViewById(R.id.bill_fd);
        editFD = (EditText) findViewById(R.id.edit_fd);
        helpFD = (TextView) findViewById(R.id.fd_help);
        billSum = (TextView) findViewById(R.id.bill_sum);
        editSum = (EditText) findViewById(R.id.edit_sum);
        helpSum = (TextView) findViewById(R.id.sum_help);
        billDate = (TextView) findViewById(R.id.bill_date);
        editDate = (TextView) findViewById(R.id.edit_date);
        helpDate = (TextView) findViewById(R.id.date_help);

        tvHead.setTypeface(Fonts.getFuturaPtBook());

        editDate.setOnClickListener(view -> {

            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);

            dialogDate = new DatePickerDialog(
                    Objects.requireNonNull(getContext()),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day
            );
            Objects.requireNonNull(dialogDate.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogDate.show();
        });

        mDateSetListener = (view, year, month, day) -> {
            this.year = year;
            this.month = month;
            this.day = day;
            showTimeDialog();
        };

        mTimeSetListener = (view1, hour, minute) -> {
            this.hour = hour;
            this.minute = minute;
            setDate();
        };

        btnSend.setOnClickListener(v -> sendBill());

        btnClose.setOnClickListener(v -> onBackPressed());

    }

    @Override
    public boolean onBackPressed() {
        ParticipFragment part = new ParticipFragment();
        part.setEvent(event);
        replaceFragment(part);
        Objects.requireNonNull(getActivity()).finish();
        return true;
    }

    @Override
    public void onGlobalLayout() {
        cont.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    public void sendBill() {
        collectData();
        if (isDataCorrect) {
            if (checkSum()) {
                CompareCheck newCheck = new CompareCheck(event,String.valueOf(year),
                        monthS,
                        dayS,
                        hourS,
                        minuteS,
                        dataSum,
                        dataFN,
                        dataFD,
                        dataFPD);

                if (newCheck.isNewCheck()) {
                    Log.d(MainActivity.TAG, "sendBill: OK");
                    sendDataToServer();
                    //getQRCode();
                    //showDialog(R.string.app_name, R.string.add_raffles_title);
                } else {
                    showDialog(R.string.app_name, R.string.bill_not_new);
                }

            } else {
                showDialog(R.string.app_name, R.string.details_sum);
            }
        } else {
            Toast.makeText(getContext(), getString(R.string.bill_lines), Toast.LENGTH_SHORT).show();
        }

    }

    public void collectData() {

        isDataCorrect = true;

        if (!editFN.getText().toString().equals("") && editFN.getText() != null) {
            dataFN = editFN.getText().toString();
            helpFN.setVisibility(View.INVISIBLE);
            editFN.setBackgroundResource(R.drawable.bg_selector_blue);
        } else {
            isDataCorrect = false;
            helpFN.setVisibility(View.VISIBLE);
            editFN.setBackgroundResource(R.drawable.bg_frame_orange_input);
        }

        if (!editFPD.getText().toString().equals("") && editFPD.getText() != null) {
            dataFPD = editFPD.getText().toString();
            helpFPD.setVisibility(View.INVISIBLE);
            editFPD.setBackgroundResource(R.drawable.bg_selector_blue);
        } else {
            isDataCorrect = false;
            helpFPD.setVisibility(View.VISIBLE);
            editFPD.setBackgroundResource(R.drawable.bg_frame_orange_input);
        }

        if (!editFD.getText().toString().equals("") && editFD.getText() != null) {
            dataFD = editFD.getText().toString();
            helpFD.setVisibility(View.INVISIBLE);
            editFD.setBackgroundResource(R.drawable.bg_selector_blue);
        } else {
            isDataCorrect = false;
            helpFD.setVisibility(View.VISIBLE);
            editFD.setBackgroundResource(R.drawable.bg_frame_orange_input);
        }

        if (!editSum.getText().toString().equals("") && editSum.getText() != null) {
            dataSum = editSum.getText().toString();
            helpSum.setVisibility(View.INVISIBLE);
            editSum.setBackgroundResource(R.drawable.bg_selector_blue);
        } else {
            isDataCorrect = false;
            helpSum.setVisibility(View.VISIBLE);
            editSum.setBackgroundResource(R.drawable.bg_frame_orange_input);
        }

        if (!editDate.getText().toString().equals("") && editDate.getText() != null) {
            dataDate = editDate.getText().toString();
            helpDate.setVisibility(View.INVISIBLE);
            editDate.setBackgroundResource(R.drawable.bg_selector_blue);
        } else {
            isDataCorrect = false;
            helpDate.setVisibility(View.VISIBLE);
            editDate.setBackgroundResource(R.drawable.bg_frame_orange_input);
        }
    }

    private void sendDataToServer() {
        showScreensaver(R.string.submiting, true);
        new Thread(() -> CheckpotAPI.sendCheck(event.getUuid(), getQRCode(), this)).start();
        Log.d(MainActivity.TAG, "sendDataToServer: " + getQRCode());
    }

    @Override
    public void onSendCheckEvent(boolean ok) {
        if (!ok) {
            hideScreensaver();
            Toast.makeText(getContext(), R.string.error_submit, Toast.LENGTH_SHORT).show();
            return;
        }

        App.getMetricaLogger().log("Принял участие в розыгрыше");
        try {
            getUser().updateUser(this::onUserUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onUserUpdate(boolean ok) {
        replaceFragment(new ThanksFragment());
    }

    private String getQRCode() {
        Log.d(MainActivity.TAG, "getQRCode: " + correctDate);
        String QRCode = correctDate
                + "&s=" + dataSum
                + "&fn=" + dataFN
                + "&i=" + dataFD
                + "&fp=" + dataFPD
                + "&n=1";


        return QRCode;
    }

    private boolean checkSum() {
        Log.d(MainActivity.TAG, "checkSum: " + Double.parseDouble(dataSum));
        if (Double.parseDouble(dataSum) >= event.getMainPrize().getMinReceipt())
            return true;
        else return false;
    }

    private void showTimeDialog() {

        TimePickerDialog dialogTime = new TimePickerDialog(getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true);
        Objects.requireNonNull(dialogTime.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogTime.show();
    }

    @SuppressLint("SetTextI18n")
    private void setDate() {
        Log.d(MainActivity.TAG, "onSetDate: " + day + "." + (month + 1) + "." + year + " " + hour + ":" + minute);

        if(month < 9) monthS = "0" + String.valueOf(month + 1);
        else monthS = String.valueOf(month + 1);
        if(day < 10) dayS = "0" + String.valueOf(day);
        else dayS = String.valueOf(day);
        if(hour < 10) hourS = "0" + String.valueOf(hour);
        else hourS = String.valueOf(hour);
        if(minute < 10) minuteS = "0" + String.valueOf(minute);
        else minuteS = String.valueOf(minute);
        correctDate = "t=" + year + monthS + dayS + "T" + hourS + "" + minuteS;
        editDate.setText(dayS + "." + monthS + "." + year + " " + hourS + ":" + minuteS);
    }

    private void showDialog(int title, int message) {
        AlertDialog dialogCheck = new AlertDialog.Builder(getContext()).setMessage(message).setTitle(title)
                .setPositiveButton("OK", (dialog, which) -> {}).create();
        dialogCheck.setCanceledOnTouchOutside(false);
        dialogCheck.show();
    }
}
