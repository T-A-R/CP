package irongate.checkpot.view.screens.delegate.addedRaffle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import irongate.checkpot.App;
import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Prices;
import irongate.checkpot.checkpotAPI.models.Prize;
import irongate.checkpot.checkpotAPI.models.PrizeWithBitmaps;
import irongate.checkpot.checkpotAPI.models.Tariff;
import irongate.checkpot.utils.AppLogs;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.delegate.RaffleSuccessfullyCreatedFragment;
import irongate.checkpot.view.screens.player.event.EventFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AddRaffleFragment extends ScreenFragment implements CheckpotAPI.CreateEventListener, CheckpotAPI.GetPricesListener {
    static final private int GALLERY_REQUEST = 1000;
    static final private int IMAGE_CAPTURE_REQUEST = 2000;
    static final private int GUARANTEED_GALLERY_REQUEST = 202;
    static final private int GUARANTEED_IMAGE_CAPTURE_REQUEST = 203;

    private RelativeLayout diffPrizes;
    private Switch switchExtended;
    private Switch switchDiffPrizes;
    private TextView tvNumWinner;
    private EditText editNumWinners;
    private PrizeEditView prizeEditView;
    private ImageView imgPrizeLoad;
    private ImageButton btnPrizeLoad;
    private ImageButton btnPrizeCancel;
    private TextView txtPrizeLoad;
    private TextView txtPrizeLoadTwo;
    private EditText editMinCheck;
    private EditText editTotalCost;
    private CheckBox checkGuarant;
    private TextView titleGuaranteedPrize;
    private EditText editGuaranteedPrize;
    private GarPrizeUploadView guaranteedPrizeLoad;
    private LinearLayout llSecurity;
    private ImageView dropDown;
    private ImageView dropUp;
    private TextView tvSecurityDescription;
    private Button btnPublication;
    private Button btnPreview;
    private EditText editAboutRaffles;
    private PrizeUploadAdapter prizeUploadAdapter;
    private RecyclerView recyclerView;

    private Bitmap bmpRandom;
    private Bitmap bmpGuarant;
    private boolean isSecurityVisible = false;

    private Event event;
    List<PrizeUploadModel> uploadDataListGlobal = new ArrayList<>();
    private View viewFocused;

    public AddRaffleFragment() {
        super(R.layout.fragment_add_raffles);
    }

    public AddRaffleFragment setEvent(Event event) {
        this.event = event;
        return this;
    }

    @Override
    public boolean isMenuShown() {
        return true;
    }

    @Override
    protected void onReady() {
        TextView title = (TextView) findViewById(R.id.title);
        View contPanel = findViewById(R.id.cont_panel);
        TextView tvSettings = (TextView) findViewById(R.id.txt_settings);
        diffPrizes = (RelativeLayout) findViewById(R.id.relative_diff_prizes);
        switchExtended = (Switch) findViewById(R.id.switch_settings);
        TextView tvDiffPrizes = (TextView) findViewById(R.id.txt_diff_prize);
        switchDiffPrizes = (Switch) findViewById(R.id.switch_prizes);
        tvNumWinner = (TextView) findViewById(R.id.txt_num_prize);
        editNumWinners = (EditText) findViewById(R.id.edit_num_prizes);
        prizeEditView = (PrizeEditView) findViewById(R.id.prize_edit_view);
        TextView tvPrize = (TextView) findViewById(R.id.txt_prize);
        EditText etPrize = (EditText) findViewById(R.id.edit_prize);
        imgPrizeLoad = (ImageView) findViewById(R.id.img_part_prize);
        btnPrizeLoad = (ImageButton) findViewById(R.id.btn_prize_load);
        btnPrizeCancel = (ImageButton) findViewById(R.id.btn_prize_cancel);
        txtPrizeLoad = (TextView) findViewById(R.id.txt_prize_photo);
        txtPrizeLoadTwo = (TextView) findViewById(R.id.txt_prize_photo_two);
        TextView tvTypeRaffles = (TextView) findViewById(R.id.txt_type_raffles);
        TextView amtWinner = (TextView) findViewById(R.id.txt_amt_winner);
        TextView tvMinCheck = (TextView) findViewById(R.id.txt_min_check);
        editMinCheck = (EditText) findViewById(R.id.edit_min_cheсk);
        TextView tvTotalPrize = (TextView) findViewById(R.id.txt_total_prizes);
        editTotalCost = (EditText) findViewById(R.id.edit_total_prizes);
        checkGuarant = (CheckBox) findViewById(R.id.check_guarant);
        titleGuaranteedPrize = (TextView) findViewById(R.id.guaranteed_prize_name);
        editGuaranteedPrize = (EditText) findViewById(R.id.edit_guaranteed_prize);
        guaranteedPrizeLoad = (GarPrizeUploadView) findViewById(R.id.guaranteed_prize_load);
        llSecurity = (LinearLayout) findViewById(R.id.ll_security);
        dropDown = (ImageView) findViewById(R.id.img_drop);
        dropUp = (ImageView) findViewById(R.id.img_up);
        TextView tvSecurity = (TextView) findViewById(R.id.tv_security);
        tvSecurityDescription = (TextView) findViewById(R.id.tv_security_description);
        btnPublication = (Button) findViewById(R.id.btn_publication);
        btnPreview = (Button) findViewById(R.id.btn_preview);
        editAboutRaffles = (EditText) findViewById(R.id.edit_about_raffles);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_prize_edit);

        title.setTypeface(Fonts.getFuturaPtMedium());
        tvSettings.setTypeface(Fonts.getFuturaPtBook());
        tvDiffPrizes.setTypeface(Fonts.getFuturaPtBook());
        tvNumWinner.setTypeface(Fonts.getFuturaPtBook());
        editNumWinners.setTypeface(Fonts.getFuturaPtBook());
        tvPrize.setTypeface(Fonts.getFuturaPtBook());
        etPrize.setTypeface(Fonts.getFuturaPtBook());
        txtPrizeLoad.setTypeface(Fonts.getFuturaPtBook());
        txtPrizeLoadTwo.setTypeface(Fonts.getFuturaPtBook());
        tvTypeRaffles.setTypeface(Fonts.getFuturaPtBook());
        amtWinner.setTypeface(Fonts.getFuturaPtBook());
        tvMinCheck.setTypeface(Fonts.getFuturaPtBook());
        editMinCheck.setTypeface(Fonts.getFuturaPtBook());
        tvTotalPrize.setTypeface(Fonts.getFuturaPtBook());
        editTotalCost.setTypeface(Fonts.getFuturaPtBook());
        checkGuarant.setTypeface(Fonts.getFuturaPtBook());
        titleGuaranteedPrize.setTypeface(Fonts.getFuturaPtBook());
        editGuaranteedPrize.setTypeface(Fonts.getFuturaPtBook());
        tvSecurity.setTypeface(Fonts.getFuturaPtBook());
        tvSecurityDescription.setTypeface(Fonts.getFuturaPtBook());
        btnPublication.setTypeface(Fonts.getFuturaPtBook());
        btnPreview.setTypeface(Fonts.getFuturaPtBook());

        MainFragment.disableSideMenu();
        btnPreview.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        prizeUploadAdapter = new PrizeUploadAdapter();
        recyclerView.setAdapter(prizeUploadAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        if (getUser().getRestaurant() != null && getUser().getRestaurant().getTariff() != null) {
            Tariff tariff = getUser().getRestaurant().getTariff();
            switchExtended.setEnabled(tariff.getIsExtended() != null && tariff.getIsExtended());
            if (tariff.getTitle().equals("Простой") || tariff.getTitle().equals("Простой х5")) {
                switchExtended.setVisibility(View.GONE);
                tvSettings.setVisibility(View.GONE);
            }
        }

        initListeners();

        if (event != null) {
            copyEvent();
        }

        title.startAnimation(Anim.getAppearSlide(getContext()));
        contPanel.startAnimation(Anim.getAppearSlide(getContext(), 300));


    }

    public void toggleSetting(boolean isVisible) {
        diffPrizes.setVisibility(isVisible ? VISIBLE : GONE);
        tvNumWinner.setVisibility(isVisible ? VISIBLE : GONE);
        editNumWinners.setVisibility(isVisible ? VISIBLE : GONE);
        prizeEditView.getTextWinningPlace().setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void toggleDiffPrize(boolean isVisible) {
        prizeEditView.setVisibility(isVisible ? GONE : VISIBLE);
        recyclerView.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void toggleCheckGuaranted(boolean isVisible) {
        titleGuaranteedPrize.setVisibility(isVisible ? VISIBLE : GONE);
        editGuaranteedPrize.setVisibility(isVisible ? VISIBLE : GONE);
        guaranteedPrizeLoad.setVisibility(isVisible ? VISIBLE : GONE);
    }

    public void toggleSecurity(boolean isVisible) {
        dropUp.setVisibility(isVisible ? GONE : VISIBLE);
        dropDown.setVisibility(isVisible ? VISIBLE : GONE);
        tvSecurityDescription.setVisibility(isVisible ? VISIBLE : GONE);
    }

    @Override
    protected void onKeyboardVisible(boolean isOpen) {
        if (isOpen) {
            hideMenu();
            viewFocused = recyclerView.getFocusedChild();
            recyclerView.setY(400);

        } else {
            if (viewFocused != null) {
                viewFocused.requestFocus();
            }
            showMenu();
        }
    }

    public void initListeners() {
        switchExtended.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (!isChecked) {
                switchDiffPrizes.setChecked(false);
            }
            toggleSetting(isChecked);
        });

        switchDiffPrizes.setOnCheckedChangeListener((compoundButton, b) -> toggleDiffPrize(b));

        editNumWinners.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.isEmpty()) {
                    return;
                }

                int winnerCount = Integer.valueOf(text);
                if (winnerCount <= 0) {
                    editMinCheck.setError("Введите положительное число");
                    return;
                }
                countPrize(winnerCount);
            }
        });

        editMinCheck.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.isEmpty()) {
                    return;
                }
                int number;
                try {
                    number = Integer.valueOf(text);
                } catch (Exception ignored) {
                    number = -1;
                }
                if (number < 0) {
                    editMinCheck.setError("Введите положительное число либо 0 ");
                }
            }
        });

        editTotalCost.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if (text.isEmpty()) {
                    return;
                }
                int number = Integer.valueOf(text);
                if (number <= 0) {
                    editMinCheck.setError("Введите положительное число");
                }
            }
        });

        checkGuarant.setOnCheckedChangeListener((compoundButton, b) -> toggleCheckGuaranted(b));
        btnPrizeLoad.setOnClickListener(view -> showLoadImageAlert(IMAGE_CAPTURE_REQUEST, GALLERY_REQUEST));
        btnPrizeCancel.setOnClickListener(view -> cancelCheck());
        guaranteedPrizeLoad.setLoadClickListener(view -> showLoadImageAlert(GUARANTEED_IMAGE_CAPTURE_REQUEST, GUARANTEED_GALLERY_REQUEST));
        btnPreview.setOnClickListener(v -> onPreviewBtn());
        btnPublication.setOnClickListener(v -> onBuplicationBtn());
        prizeEditView.getPrizeUploadView().setLoadClickListener(view -> showLoadImageAlert(IMAGE_CAPTURE_REQUEST, GALLERY_REQUEST));

        llSecurity.setOnClickListener(view -> {
            isSecurityVisible = !isSecurityVisible;
            toggleSecurity(isSecurityVisible);
        });

        prizeUploadAdapter.setListener(prizeUploadData -> {
            showLoadImageAlert(prizeUploadData.getImageRequestCode(), prizeUploadData.getGalleryRequestCode());
            recyclerView.clearFocus();
        });
    }

    public void countPrize(int prizeCount) {
        prizeUploadAdapter.clear();
        List<PrizeUploadModel> uploadDataList = new ArrayList<>();
        for (int i = 0; i < prizeCount; i++) {
            PrizeUploadModel prizeUploadData = new PrizeUploadModel();
            prizeUploadData.setPosition(i);

            final int imageRequestCode = IMAGE_CAPTURE_REQUEST + prizeUploadData.getId();
            final int galleryRequestCode = GALLERY_REQUEST + prizeUploadData.getId();
            prizeUploadData.setImageRequestCode(imageRequestCode);
            prizeUploadData.setGalleryRequestCode(galleryRequestCode);
            uploadDataList.add(prizeUploadData);
        }

        prizeUploadAdapter.setUploadDataList(uploadDataList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getContext();
        if (context == null || resultCode != Activity.RESULT_OK)
            return;

        try {
            if (requestCode > 1000 && requestCode < 2000) {
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                } catch (IOException e) {
                    return;
                }
                prizeUploadAdapter.setBitmap(bitmap, requestCode);
            } else if (requestCode > 2000 && requestCode <= 3000) {
                requestCode = requestCode - 1000;
                Bitmap cameraBitmap = getCameraBitmap();
                if (cameraBitmap != null) {
                    prizeUploadAdapter.setBitmap(cameraBitmap, requestCode);
                }
            } else if (requestCode == GALLERY_REQUEST) {
                Uri selectedImage = data.getData();
                try {
                    bmpRandom = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    return;
                }
                setPrizePhoto(bmpRandom);
            } else if (requestCode == IMAGE_CAPTURE_REQUEST) {
                bmpRandom = getCameraBitmap();
                if (bmpRandom != null) {
                    bmpRandom = CheckpotAPI.cropBmpToServer(bmpRandom);
                    setPrizePhoto(bmpRandom);
                }
            } else if (requestCode == GUARANTEED_GALLERY_REQUEST) {
                Uri selectedImage = data.getData();
                try {
                    bmpGuarant = MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    return;
                }
                bmpGuarant = CheckpotAPI.cropBmpToServer(bmpGuarant);
                guaranteedPrizeLoad.setBitmap(bmpGuarant);
            } else if (requestCode == GUARANTEED_IMAGE_CAPTURE_REQUEST) {
                bmpGuarant = getCameraBitmap();
                if (bmpGuarant != null) {
                    bmpGuarant = CheckpotAPI.cropBmpToServer(bmpGuarant);
                    guaranteedPrizeLoad.setBitmap(bmpGuarant);
                }
            }
        } catch (OutOfMemoryError e) {
            Toast.makeText(getContext(), R.string.no_memory, Toast.LENGTH_SHORT).show();
        }
    }

    private void setPrizePhoto(Bitmap photo) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int height = (int) ((174 * scale + 0.5f));
        int width = (int) ((370 * scale + 0.5f));
        Bitmap cropedBitmap = ImageUtils.getCropedBitmap(photo, width, height);
        Bitmap colorCovered = ImageUtils.getColorCovered(cropedBitmap, Color.argb(171, 0, 0, 0));
        Bitmap roundedCornerBitmap = ImageUtils.getRoundedCornerBitmap(colorCovered, 8 * getDensity());
        imgPrizeLoad.setImageBitmap(roundedCornerBitmap);
        btnPrizeLoad.setImageResource(R.drawable.ico_done);
        btnPrizeLoad.setEnabled(false);
        txtPrizeLoad.setText(R.string.particip_loaded);
        txtPrizeLoadTwo.setVisibility(View.GONE);
        btnPrizeCancel.setVisibility(View.VISIBLE);
        bmpRandom = photo;
    }

    private void setGuarantPrizePhoto(Bitmap photo) {
        Bitmap cropedBitmap = ImageUtils.getCropedBitmap(photo, guaranteedPrizeLoad.getWidth(), guaranteedPrizeLoad.getHeight());
        Bitmap colorCovered = ImageUtils.getColorCovered(cropedBitmap, Color.argb(171, 0, 0, 0));
        Bitmap roundedCornerBitmap = ImageUtils.getRoundedCornerBitmap(colorCovered, 8 * getDensity());
        guaranteedPrizeLoad.setBitmap(roundedCornerBitmap);
        btnPrizeLoad.setImageResource(R.drawable.ico_done);
        btnPrizeLoad.setEnabled(false);
        txtPrizeLoad.setText(R.string.particip_loaded);
        txtPrizeLoadTwo.setVisibility(View.GONE);
        btnPrizeCancel.setVisibility(View.VISIBLE);
        bmpGuarant = roundedCornerBitmap;
    }

    private void cancelCheck() {
        imgPrizeLoad.setImageResource(R.drawable.bg_frame_dotted_blue);
        btnPrizeLoad.setImageResource(R.drawable.ico_upload);
        btnPrizeLoad.setEnabled(true);
        txtPrizeLoad.setText(R.string.particip_load);
        txtPrizeLoadTwo.setVisibility(View.VISIBLE);
        btnPrizeCancel.setVisibility(View.INVISIBLE);
    }

    private void onPreviewBtn() {
        if (!checkFields()) {
            return;
        }
        openScreenInNewActivity(new EventFragment().setEvent(getFormEvent()).setDelegateScreen(true));
    }

    private void onBuplicationBtn() {
        if (!checkFields()) {
            return;
        }

        showScreensaver(R.string.submiting, true);
        new Thread(this::submit).start();
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean checkFields() {
        if (editMinCheck.getText().length() == 0) {
            Toast.makeText(getContext(), "Не указан минимальный чек", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!switchDiffPrizes.isChecked()) {
            if (prizeEditView.getName().length() == 0) {
                Toast.makeText(getContext(), "Не указано название приза", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (bmpRandom == null) {
                Toast.makeText(getContext(), "Не загружена картинка приза", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (switchExtended.isChecked() && editNumWinners.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Не указано количество победителей", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            int emptyName = prizeUploadAdapter.checkEmptyName();
            if (emptyName > 0) {
                Toast.makeText(getContext(), "Не указано название приза " + emptyName, Toast.LENGTH_SHORT).show();
                return false;
            }

            int imageNull = prizeUploadAdapter.checkImageIsNull();
            if (imageNull > 0) {
                Toast.makeText(getContext(), "Не загружена картинка приза " + imageNull, Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (checkGuarant.isChecked()) {
            if (editGuaranteedPrize.getText().length() == 0) {
                Toast.makeText(getContext(), "Не указано название гарантированного приза", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (bmpGuarant == null) {
                Toast.makeText(getContext(), "Не загружена картинка гарантированного приза", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (editAboutRaffles.getText().length() == 0) {
            Toast.makeText(getContext(), "Не указано описание розыгрыша", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (editTotalCost.getText().length() == 0) {
            Toast.makeText(getContext(), "Не указана общая стоимость всех призов", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private Event getFormEvent() {
        int minReceipt = Integer.parseInt(editMinCheck.getText().toString());

        List<Prize> prizes = new ArrayList<>();
        if (!switchExtended.isChecked()) {
            PrizeWithBitmaps randomPrize = new PrizeWithBitmaps(true, prizeEditView.getName(), minReceipt, bmpRandom);
            prizes.add(randomPrize);
        } else {
            if (!switchDiffPrizes.isChecked()) {
                PrizeWithBitmaps randomPrize = new PrizeWithBitmaps(true, prizeEditView.getName(), minReceipt, bmpRandom);
                int numPrizes = Integer.parseInt(editNumWinners.getText().toString());
                for (int i = 0; i < numPrizes; i++) {
                    prizes.add(randomPrize);
                }
            } else {
                List<PrizeUploadModel> uploadDataList = prizeUploadAdapter.getUploadDataList();
                for (int i = 0; i < uploadDataList.size(); i++) {
                    PrizeUploadModel prizeUploadData = uploadDataList.get(i);
                    PrizeWithBitmaps prizeBody = new PrizeWithBitmaps(true, prizeUploadData.getPrizeName(), minReceipt, prizeUploadData.getPrizeImage());
                    prizes.add(prizeBody);
                }
            }
        }
        if (checkGuarant.isChecked()) {
            PrizeWithBitmaps prizeBody = new PrizeWithBitmaps(false, editGuaranteedPrize.getText().toString(), minReceipt, bmpGuarant);
            prizes.add(prizeBody);
        }

        String desc = editAboutRaffles.getText().toString();

        long totalCost = Long.parseLong(editTotalCost.getText().toString());

        Event event = new Event();
        event.setPrizes(prizes);
        event.setTotalCost(totalCost);
        event.setDescription(desc);
        event.setPlace(getUser().getPlace());
        return event;
    }

    private void submit() {
        CheckpotAPI.getPrices(this);
    }

    @Override
    public void onCreateEvent(Event event) {
        final Activity activity = getActivity();
        if (activity == null)
            return;

        if (event == null) {
            hideScreensaver();
            activity.runOnUiThread(() -> Toast.makeText(getContext(), R.string.add_raffles_fail, Toast.LENGTH_SHORT).show());
            return;
        }

        App.getMetricaLogger().log("Оунер опубликовал первый розыгрыш");
        getUser().updateUser(ok -> {
            event.setPlace(getUser().getPlace());
            replaceFragment(new RaffleSuccessfullyCreatedFragment().setEvent(event));
        });
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new RafflesFragment());
        return true;
    }

    public void copyEvent() {

        int randomPrizes = 0;
        for (int i = 0; i < event.getPrizes().size(); i++) {
            if (event.getPrizes().get(i).getIsRandom()) {
                randomPrizes++;
            }
        }
        int guaranteedPrizes = event.getPrizes().size() - randomPrizes;

        if (event.getPrizes() != null && randomPrizes > 1) {
            switchExtended.setChecked(true);
            toggleSetting(true);
            editNumWinners.setText(String.valueOf(randomPrizes));
            switchDiffPrizes.setChecked(true);

            for (int i = 0; i < event.getPrizes().size(); i++) {
                if (event.getPrizes().get(i).getIsRandom())
                    if (event.getMainPrize() instanceof PrizeWithBitmaps) {
                        PrizeWithBitmaps prizeBmp = (PrizeWithBitmaps) event.getPrizes().get(i);
                        if (prizeBmp != null && prizeBmp.getPhotosBmp() != null && prizeBmp.getPhotosBmp().get(0) != null) {
                            Bitmap bitmap = prizeBmp.getPhotosBmp().get(0);
                            addPrizeFromEvent(bitmap);
                        }
                    } else
                        ImageUtils.getBitmap(event.getPrizes().get(i).getPhotos().get(0), this::addPrizeFromEvent);
            }

        } else {

            if (event.getMainPrize() != null && event.getMainPrize().getPhotos() != null
                    && !event.getMainPrize().getPhotos().isEmpty() && event.getMainPrize().getPhotos().get(0) != null) {
                ImageUtils.getBitmap(event.getMainPrize().getPhotos().get(0), this::setPrizePhoto);
            } else if (event.getMainPrize() instanceof PrizeWithBitmaps) {
                PrizeWithBitmaps mainPrizeBmp = (PrizeWithBitmaps) event.getMainPrize();
                if (mainPrizeBmp != null && mainPrizeBmp.getPhotosBmp() != null &&
                        !mainPrizeBmp.getPhotosBmp().isEmpty() && mainPrizeBmp.getPhotosBmp().get(0) != null) {
                    Bitmap bitmap = mainPrizeBmp.getPhotosBmp().get(0);
                    bmpRandom = bitmap;
                    setPrizePhoto(bitmap);
                }
            }


        }

        if (event.getMainPrize() != null && event.getMainPrize().getName() != null) {
            prizeEditView.setEditText(event.getMainPrize().getName());
        }

        if (event.getDescription() != null) {
            editAboutRaffles.setText(event.getDescription());
        }

        if (event.getMainPrize() != null) {
            editMinCheck.setText(event.getMainPrize().getMinReceipt().toString());
        }

        editTotalCost.setText(event.getTotalCost().toString());

        if (guaranteedPrizes > 0) {
            checkGuarant.setChecked(true);
            toggleCheckGuaranted(true);
            editGuaranteedPrize.setText(event.getPrizes().get(event.getPrizes().size() - 1).getName());

            if (event.getPrizes().get(event.getPrizes().size() - 1).getPhotos() != null
                    && !event.getPrizes().get(event.getPrizes().size() - 1).getPhotos().isEmpty() && event.getPrizes().get(event.getPrizes().size() - 1).getPhotos().get(0) != null) {
                ImageUtils.getBitmap(event.getPrizes().get(event.getPrizes().size() - 1).getPhotos().get(0), this::setGuarantPrizePhoto);
            } else if (event.getPrizes().get(event.getPrizes().size() - 1) instanceof PrizeWithBitmaps) {
                PrizeWithBitmaps guarPrizeBmp = (PrizeWithBitmaps) event.getPrizes().get(event.getPrizes().size() - 1);
                if (guarPrizeBmp != null && guarPrizeBmp.getPhotosBmp() != null &&
                        !guarPrizeBmp.getPhotosBmp().isEmpty() && guarPrizeBmp.getPhotosBmp().get(0) != null) {
                    Bitmap bitmap = guarPrizeBmp.getPhotosBmp().get(0);
                    setGuarantPrizePhoto(bitmap);
                }
            }
        }
    }

    public void addPrizeFromEvent(Bitmap image) {
        prizeUploadAdapter.clear();
        int position = uploadDataListGlobal.size();
        PrizeUploadModel prizeUploadData = new PrizeUploadModel();
        prizeUploadData.setPosition(position);

        final int imageRequestCode = IMAGE_CAPTURE_REQUEST + prizeUploadData.getId();
        final int galleryRequestCode = GALLERY_REQUEST + prizeUploadData.getId();
        prizeUploadData.setGalleryRequestCode(galleryRequestCode);
        prizeUploadData.setImageRequestCode(imageRequestCode);
        prizeUploadData.setPrizeName(event.getPrizes().get(position).getName());
        prizeUploadData.setPrizeImage(image);

        uploadDataListGlobal.add(prizeUploadData);
        prizeUploadAdapter.setUploadDataList(uploadDataListGlobal);
    }

    @Override
    public void onPrices(Prices prices) {
        hideScreensaver();
        if (prices == null || prices.getTariffs().isEmpty()) {
            Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            return;
        }

        replaceFragment(new PaymentFragment().setData(getFormEvent(), prices));
    }
}
