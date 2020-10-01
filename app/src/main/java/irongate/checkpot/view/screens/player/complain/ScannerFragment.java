package irongate.checkpot.view.screens.player.complain;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import irongate.checkpot.App;
import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Token;
import irongate.checkpot.utils.CompareCheck;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.ScrollAppearsListener;


public class ScannerFragment extends ScreenFragment implements ViewTreeObserver.OnGlobalLayoutListener, CheckpotAPI.SendCheckListener {

    private View cont;
    private TextView tvHead;
    private TextView tvResult;
    private Button btnManual;
    private ImageButton btnBack;
    SurfaceView cameraPreview;
    private String txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    private int scrollY;
    private Event event;
    private boolean isCodeCorrect = false;
    private boolean stopScanner = false;
    private List<String> dataQR;

    public ScannerFragment() {
        super(R.layout.fragment_scanner);
    }

    public ScannerFragment setScrollY(int scrollY) {
        this.scrollY = scrollY;
        return this;
    }

    public ScannerFragment setEvent(Event event) {
        this.event = event;
        return this;
    }

    @Override
    protected void onReady() {

        cont = findViewById(R.id.cont_scanner);
        tvHead = (TextView) findViewById(R.id.scanner_head);
        tvResult = (TextView) findViewById(R.id.result);
        btnManual = (Button) findViewById(R.id.btn_manual);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);

        tvHead.setTypeface(Fonts.getFuturaPtBook());

        cont.getViewTreeObserver().addOnGlobalLayoutListener(this);

        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource
                .Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .build();

        btnManual.setOnClickListener(v -> startHandmadeBill());

        btnBack.setOnClickListener(v -> onBackPressed());

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if (qrcodes.size() != 0 && !stopScanner) {
                    stopScanner = true;
                    txtResult = (qrcodes.valueAt(0).displayValue);
                    tvResult.post(() -> {
                        Vibrator vibrator = (Vibrator) Objects.requireNonNull(getContext()).getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(500);
                        sendDataToServer();
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(MainActivity.TAG, "onRequestPermissionsResult: " + e.getMessage());
                    }
                }
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    onBackPressed();
                }
            }
            break;
        }
    }

    @Override
    public boolean onBackPressed() {
        ParticipFragment part = new ParticipFragment();
        part.setEvent(event);
        replaceFragment(part);
        getActivity().finish();
        return true;
    }

    @Override
    public void onGlobalLayout() {
        cont.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    public void startHandmadeBill() {

        HandmadeBillFragment part = new HandmadeBillFragment()
                .setEvent(event);

        replaceFragment(part);
    }

    private void sendDataToServer() {

        decodeQR();

        if (isCodeCorrect) {

            CompareCheck newCheck = new CompareCheck(event, dataQR.get(0).substring(0,4),
                    dataQR.get(0).substring(4,6),
                    dataQR.get(0).substring(6),
                    dataQR.get(1).substring(0,2),
                    dataQR.get(1).substring(2,4),
                    dataQR.get(2),
                    dataQR.get(3),
                    dataQR.get(4),
                    dataQR.get(5));

            if (newCheck.isNewCheck()) {
                Log.d(MainActivity.TAG, "!!!!!!!!!!!!!!! NEW CHECK: !!!!!!!!!!!!!!!!!! " + txtResult);
                //showDialog(R.string.app_name, R.string.add_raffles_title);
                showScreensaver(R.string.submiting, true);
                new Thread(() -> CheckpotAPI.sendCheck(event.getUuid(), txtResult, this)).start();

            } else {
                showDialog(R.string.app_name, R.string.bill_not_new);
            }
        }
    }

    private List<String> decodeQR() {
        dataQR = new ArrayList<>();
        //int end = 0;
        int date = 0, time = 0, sum = 0, fn = 0, fd = 0, fpd = 0;
        int dateEnd = 0, timeEnd = 0, sumEnd = 0, fnEnd = 0, fdEnd = 0, fpdEnd = 0;
        for (int i = 0; i < txtResult.length(); i++) {
            if (txtResult.charAt(i) == 't' && txtResult.charAt(i + 1) == '=') {
                date = i + 2;
                for (int end = i; end < txtResult.length(); end++) {
                    if (txtResult.charAt(end) == 'T') {
                        dateEnd = end;
                        break;
                    }
                }
            }
            if (txtResult.charAt(i) == 'T') {
                time = i + 1;
                for (int end = i; end < txtResult.length(); end++) {
                    if (txtResult.charAt(end) == '&') {
                        timeEnd = end;
                        break;
                    }
                }
            }
            if (txtResult.charAt(i) == 's' && txtResult.charAt(i + 1) == '=') {
                sum = i + 2;
                for (int end = i; end < txtResult.length(); end++) {
                    if (txtResult.charAt(end) == '&') {
                        sumEnd = end;
                        break;
                    }
                }
            }
            if (txtResult.charAt(i) == 'f' && txtResult.charAt(i + 1) == 'n' && txtResult.charAt(i + 2) == '=') {
                fn = i + 3;
                for (int end = i; end < txtResult.length(); end++) {
                    if (txtResult.charAt(end) == '&') {
                        fnEnd = end;
                        break;
                    }
                }
            }
            if (txtResult.charAt(i) == 'i' && txtResult.charAt(i + 1) == '=') {
                fd = i + 2;
                for (int end = i; end < txtResult.length(); end++) {
                    if (txtResult.charAt(end) == '&') {
                        fdEnd = end;
                        break;
                    }
                }
            }
            if (txtResult.charAt(i) == 'f' && txtResult.charAt(i + 1) == 'p' && txtResult.charAt(i + 2) == '=') {
                fpd = i + 3;
                for (int end = i; end < txtResult.length(); end++) {
                    if (txtResult.charAt(end) == '&') {
                        fpdEnd = end;
                        break;
                    }
                }
            }

        }

        Log.d(MainActivity.TAG, "bill QR code: " + txtResult);


        if (date != 0 && time != 0 && sum != 0 && fn != 0 && fd != 0 && fpd != 0
                && dateEnd != 0 && timeEnd != 0 && sumEnd != 0 && fnEnd != 0 && fdEnd != 0 && fpdEnd != 0) {
            dataQR.add(txtResult.substring(date, dateEnd));
            dataQR.add(txtResult.substring(time, timeEnd));
            dataQR.add(txtResult.substring(sum, sumEnd));
            dataQR.add(txtResult.substring(fn, fnEnd));
            dataQR.add(txtResult.substring(fd, fdEnd));
            dataQR.add(txtResult.substring(fpd, fpdEnd));

            Log.d(MainActivity.TAG, "dateEnd: " + dateEnd + " timeEnd: " + timeEnd + " sumEnd: " + sumEnd);
            Log.d(MainActivity.TAG, "date: " + dataQR.get(0) + " time: " + dataQR.get(1) + " sum: " + dataQR.get(2) + " fn: " + dataQR.get(3) +
                    " fd: " + dataQR.get(4) + " fpd: " + dataQR.get(5));

            Log.d(MainActivity.TAG, "decodeQR: " + Double.parseDouble(dataQR.get(2)));
            if (Double.parseDouble(dataQR.get(2)) >= event.getMainPrize().getMinReceipt()) {
                isCodeCorrect = true;
            } else {
                isCodeCorrect = false;
                showDialog(R.string.app_name, R.string.details_sum);
            }

        } else {
            Log.d(MainActivity.TAG, "decodeQR: " + txtResult);
            isCodeCorrect = false;
            showDialog(R.string.app_name, R.string.bad_qr);
        }

        return dataQR;
    }

    private void showDialog(int title, int message) {
        AlertDialog dialogCheck = new AlertDialog.Builder(getContext()).setMessage(message).setTitle(title)
                .setPositiveButton("OK", (dialog, which) -> stopScanner = false).create();
        dialogCheck.setCanceledOnTouchOutside(false);
        dialogCheck.show();
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
}
