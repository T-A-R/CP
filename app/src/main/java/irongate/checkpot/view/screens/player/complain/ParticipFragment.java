package irongate.checkpot.view.screens.player.complain;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.Date;

import irongate.checkpot.App;
import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Prize;
import irongate.checkpot.utils.ImageUtils;
import irongate.checkpot.utils.TimeUtils;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.common.place.PlaceFragment;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class ParticipFragment extends ScreenFragment implements ViewTreeObserver.OnGlobalLayoutListener {
    static final private int GALLERY_REQUEST = 400;
    static final private int CAMERA_REQUEST = 401;
    final int RequestCameraPermissionID = 1001;

    private ImageButton btnLoad;
    private ImageView imgCheck;
    private ImageButton btnCancle;
    private TextView txtLoad;
    private TextView txtManual;
    private ImageButton btnBack;
    private ImageView imgRest;
    private ImageView img;
    private TextView txtName;
    private TextView txtCheck;
    private TextView txtDescAdress;
    private TextView txtDescDays;
    private TextView txtDescMembers;
    private Bitmap bmpImg;
    private ScrollView scroll;
    private View cont;
    private LinearLayout linear;
    private TextView txtRulesLink;

    private boolean checkDone = false;
    private int averageColor = -1;
    private int scrollY;
    private Event event;
    private Bitmap receip;
    private Prize prize;

    public ParticipFragment() {
        super(R.layout.fragment_particip);
    }

    public ParticipFragment setEvent(Event event){
        this.event = event;
        return this;
    }

       public ParticipFragment setAverageColor(int color) {
        averageColor = color;
        return this;
    }

    public ParticipFragment setImage(Bitmap bmpImg) {
        this.bmpImg = bmpImg;
        return this;
    }

    public ParticipFragment setScrollY(int scrollY) {
        this.scrollY = scrollY;
        return this;
    }

    @Override
    protected void onReady() {
        cont = findViewById(R.id.cont_part);
        RelativeLayout contImg = (RelativeLayout) findViewById(R.id.cont_part_img);
        scroll = (ScrollView) findViewById(R.id.scroll_part);
        linear = (LinearLayout) findViewById(R.id.linear);
        img = (ImageView) findViewById(R.id.img_part);
        btnBack = (ImageButton) findViewById(R.id.btn_part_back);
        imgRest = (ImageView) findViewById(R.id.img_part_rest);
        View contMain = findViewById(R.id.cont_part_main);
        txtName = (TextView) findViewById(R.id.txt_part_name);
        txtDescAdress = (TextView) findViewById(R.id.txt_part_desc_adress);
        txtDescDays = (TextView) findViewById(R.id.txt_part_desc_days);
        txtDescMembers = (TextView) findViewById(R.id.txt_part_desc_members);
        imgCheck = (ImageView) findViewById(R.id.img_part_check);
        btnLoad = (ImageButton) findViewById(R.id.btn_part_load);
        btnCancle = (ImageButton) findViewById(R.id.btn_part_cancle);
        txtCheck = (TextView) findViewById(R.id.txt_part_check);
        txtLoad = (TextView) findViewById(R.id.txt_part_load);
        txtManual = (TextView) findViewById(R.id.txt_part_manual);
        ImageButton btnLoc = (ImageButton) findViewById(R.id.btn_part_location);
        TextView txtRules = (TextView) findViewById(R.id.txt_rules);
        txtRulesLink = (TextView) findViewById(R.id.txt_rules_link);

        txtName.setTypeface(Fonts.getFuturaPtMedium());
        txtDescAdress.setTypeface(Fonts.getFuturaPtBook());
        txtDescDays.setTypeface(Fonts.getFuturaPtBook());
        txtDescMembers.setTypeface(Fonts.getFuturaPtBook());
        txtCheck.setTypeface(Fonts.getFuturaPtBook());
        txtLoad.setTypeface(Fonts.getFuturaPtBook());
        txtManual.setTypeface(Fonts.getFuturaPtBook());
        txtRules.setTypeface(Fonts.getFuturaPtBook());
        txtRulesLink.setTypeface(Fonts.getFuturaPtBook());

        MainFragment.disableSideMenu();

        // TODO: 25.10.2018 случай, когда картинка не пришла

        TranslateAnimation trans = new TranslateAnimation(0, 0, -scrollY, 0);
        trans.setDuration(500);
        trans.setInterpolator(new DecelerateInterpolator());
        contImg.startAnimation(trans);

        contMain.startAnimation(Anim.getAppearSlide(getContext()));
        btnLoc.startAnimation(Anim.getAppearSlide(getContext(), 500));
        txtName.startAnimation(Anim.getAppear(getContext(), 500));

        btnBack.setOnClickListener(v -> onBackPressed());
        imgRest.setOnClickListener(v -> {if(event != null) replaceFragment(new PlaceFragment().setPlace(event.getPlace()));});
        btnLoad.setOnClickListener(v -> startScanner());
        txtManual.setOnClickListener(v -> startHandmadeBill());
        btnCancle.setOnClickListener(v -> cancleCheck());
        txtRulesLink.setOnClickListener(v -> loadRules());

        populate();

        img.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void loadBgImage() {
        if (event == null || event.getPrizes() == null || event.getPrizes().get(0).getPhotos() == null || event.getPrizes().get(0).getPhotos().isEmpty())
            return;

        String url = event.getMainPrize().getPhotos().get(0);
        img.post(() -> Picasso.get().load(url).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                if (source == null) {
                    return null;
                }

                boolean resized = false;
                Bitmap bitmap = source;
                if (img.getWidth() > 0) {

                    bitmap = ImageUtils.getCropedBitmap(source, img.getWidth(), img.getHeight());
                    resized = true;
                }
//                int averageColor = ImageUtils.getAverageColor(bitmap);
//                bitmap = ImageUtils.getGradiented(bitmap, averageColor);
                if (!resized && img.getWidth() > 0) {

                    bitmap = ImageUtils.getCropedBitmap(source, img.getWidth(), img.getHeight());
                }
                if (bitmap != source) {
                    source.recycle();
                }

                return bitmap;
            }

            @Override
            public String key() {
                return "ParticipFragment";
            }

        }).into(img));
    }

    @Override
    public void onGlobalLayout() {
        img.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        initScroll();
    }
    
    private void populate() {
        if(event != null) {
            String placeLogoUrl = event.getPlace().getLogo();
            if (placeLogoUrl != null)
                ImageUtils.getBitmap(placeLogoUrl, this::setLogo);

            loadBgImage();

            txtName.setText(String.format("Участие в розыгрыше «%s»", event.getMainPrize().getName()));
            txtDescAdress.setText(String.format("Адрес: %s", event.getPlace().getAddress()));

            if (event.getEnd() != null && event.getEnd() > 0) {
                Date curTime = TimeUtils.getCurrent();
                Date end = new Date(event.getEnd() * 1000);
                txtDescDays.setText(String.format("Действие акции: %s дня", String.valueOf(TimeUtils.differenceDays(end, curTime))));
            } else
                txtDescDays.setVisibility(View.GONE);

            if (event.getMembersCount() != null && event.getMembersCount() > 0) {
                txtDescMembers.setText(String.format("Участники: Не более: %s", String.valueOf(event.getMembersCount())));
                txtDescDays.setVisibility(View.GONE);
            } else
                txtDescMembers.setVisibility(View.GONE);

            txtCheck.setText(String.format(getString(R.string.event_min_check), event.getMainPrize().getMinReceipt()));
        }
    }

    private void setLogo(Bitmap bitmap) {
        if (bitmap == null || bitmap.getWidth() <= 10)
            return;

        Bitmap cropedBitmap = ImageUtils.getCropedBitmap(bitmap, imgRest.getWidth(), imgRest.getHeight());
        Bitmap roundedCornerBitmap = ImageUtils.getRoundedCornerBitmap(cropedBitmap, imgRest.getWidth() / 2f);
        imgRest.post(() -> imgRest.setImageBitmap(roundedCornerBitmap));
    }

    private void initScroll() {
        // TODO: 31.10.2018
//        HashSet<View> items = new HashSet<>();
//        items.add(contBtn);
//        items.add(contDesc);
//        items.add(linearAnother);
//        items.add(contCheck);
//        items.add(contTime);
//        items.add(contGuarant);
//
//        int offset = (int) (-cont.getHeight() + linear.getY() + 20 * getDensity());
//        scroll.getViewTreeObserver().addOnScrollChangedListener(new ScrollAppearsListener(scroll, items, offset));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getContext();
        if (context == null || resultCode != Activity.RESULT_OK) {
            return;
        }

        Bitmap bitmap = null;
        if (requestCode == GALLERY_REQUEST) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
            } catch (IOException e) {
                Log.d("IRON", "EditPlaceFragment.onActivityResult() " + e);
                return;
            }
        }
        else if (requestCode == CAMERA_REQUEST) {
            bitmap = getCameraBitmap();
        }

        if (bitmap == null) {
            Log.d("IRON", "EditPlaceFragment.onActivityResult() bitmap=null request=" + requestCode);
            return;
        }
        setReceip(bitmap);
    }

    private void setReceip(Bitmap photo) {
        receip = photo;
        Bitmap cropedBitmap = ImageUtils.getCropedBitmap(photo, imgCheck.getWidth(), imgCheck.getHeight());
        Bitmap colorCovered = ImageUtils.getColorCovered(cropedBitmap, Color.argb(171, 0, 0, 0));
        Bitmap roundedCornerBitmap = ImageUtils.getRoundedCornerBitmap(colorCovered, 8 * getDensity());
        imgCheck.setImageBitmap(roundedCornerBitmap);
        btnLoad.setImageResource(R.drawable.ico_done);
        btnLoad.setEnabled(false);
        txtLoad.setText(R.string.particip_loaded);
        btnCancle.setVisibility(View.VISIBLE);
        checkDone = true;
    }

    private void cancleCheck() {
        receip = null;
        imgCheck.setImageResource(R.drawable.bg_frame_dotted_green);
        btnLoad.setImageResource(R.drawable.ico_upload);
        btnLoad.setEnabled(true);
        txtLoad.setText(R.string.particip_load);
        btnCancle.setVisibility(View.INVISIBLE);
        checkDone = false;
    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return true;
    }

    private void loadRules() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(CheckpotAPI.getEventRulesUrl(event.getUuid())));
        startActivity(browserIntent);
    }

    public void startScanner() {

        ScannerFragment part = new ScannerFragment().
                setScrollY(-2000).setEvent(event);

        replaceFragment(part);
    }

    public void startHandmadeBill() {

        HandmadeBillFragment part = new HandmadeBillFragment()
                //.setScrollY(-2000)
                .setEvent(event);

        replaceFragment(part);
    }

}
