package irongate.checkpot.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.model.User;
import irongate.checkpot.utils.FileUtils;
import irongate.checkpot.view.screens.IMainFragment;
import irongate.checkpot.view.screens.common.place.PlaceFragment;
import irongate.checkpot.view.screens.player.complain.ComplainFragment;
import irongate.checkpot.view.screens.player.complain.ComplainMessageFragment;
import irongate.checkpot.view.screens.player.complain.ParticipFragment;
import irongate.checkpot.view.screens.player.event.EventFragment;

import static irongate.checkpot.MainActivity.TAG;

@SuppressWarnings("unused")
public abstract class ScreenFragment extends SmartFragment {
    static protected int numActivities = 0;   // max 3
    private IMainFragment main;
    private ScreenListener screenListener;
    private boolean delegateScreen;
    private Class<? extends ScreenFragment> prevClass;
    private String cameraPhotoPath;
    private int requestCodeFragment;

    final int RequestCameraPermissionID = 1001;

    public ScreenFragment(int layoutSrc) {
        super(layoutSrc);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = getActivity();
        if (activity != null)
            KeyboardVisibilityEvent.setEventListener(activity, this::onKeyboardVisible);
    }

    protected void onKeyboardVisible(boolean isOpen) {

    }

    public IMainFragment getMain() {
        return main;
    }

    public void setMain(IMainFragment main) {
        this.main = main;
    }


    public void setScreenListener(ScreenListener listener) {
        this.screenListener = listener;
    }

    public void showScreensaver(int titleId, boolean full) {
        String title = getResources().getString(titleId);
        showScreensaver(title, full);
    }

    /**
     * Показывать ли меню при открытии этого экрана
     */
    public boolean isMenuShown() {
        return false;
    }

    /**
     * Является ли экраном представителя
     */
    public boolean isDelegateScreen() {
        return delegateScreen;
    }

    public ScreenFragment setDelegateScreen(boolean delegateScreen) {
        this.delegateScreen = delegateScreen;
        return this;
    }

    public void showLoadImageAlert(int cameraRequestCode, int galleryRequestCode) {
        Context context = getContext();
        if (context == null)
            return;

        new AlertDialog.Builder(context)
                .setMessage(R.string.load_image)
                .setPositiveButton(R.string.camera, (dialogInterface, i) -> openCamera(cameraRequestCode))
                .setNegativeButton(R.string.gallery, (dialogInterface, i) -> openGallery(galleryRequestCode))
                .create()
                .show();
    }

    public void openGallery(int requestCode) {
        System.gc();

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, requestCode);
    }

    public void openCamera(int requestCode) {
        requestCodeFragment = requestCode;
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
            return;
        }

        Context context = getContext();
        FragmentActivity activity = getActivity();
        if (activity == null || context == null) {
            return;
        }

        System.gc();

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) == null) {
            return;
        }

        cameraPhotoPath = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile;
        try {
            photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("IRON", "ScreenFragment.openCamera() " + e);
            return;
        }

        cameraPhotoPath = photoFile.getAbsolutePath();

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.getUriForFile(context, photoFile));
        startActivityForResult(takePictureIntent, requestCode);
    }

    public Bitmap getCameraBitmap() {
        if (cameraPhotoPath == null) {
            Log.d("IRON", "ScreenFragment.getCameraBitmap() cameraPhotoPath == null");
            return null;
        }

        System.gc();
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;

            Bitmap bitmap = BitmapFactory.decodeFile(cameraPhotoPath, options);
            bitmap = CheckpotAPI.cropBmpToServer(bitmap);
            System.gc();
            return bitmap;
        } catch (OutOfMemoryError e) {
            if (getContext() != null)
                Toast.makeText(getContext(), R.string.no_memory, Toast.LENGTH_SHORT).show();

            return null;
        }
    }

    public void showScreensaver(boolean full) {
        showScreensaver("", full);
    }

    public void showScreensaver(String title, boolean full) {
        hideKeyboard();
        if (main != null)
            main.showScreensaver(title, full);
    }

    public void hideScreensaver() {
        if (main != null)
            main.hideScreensaver();
    }

    public void showMenu() {
        if (main != null)
            main.showMenu();
    }

    public void showDrawer() {
        if (main != null)
            main.showDrawer();
    }

    public void hideMenu() {
        if (main != null)
            main.hideMenu();
    }

    public void setMenuCursor(int index) {
        if (main != null)
            main.setMenuCursor(index);
    }

    public User getUser() {
        return User.getUser();
    }

    public void openScreenInNewActivity(ScreenFragment newScreen) {
        openScreenInNewActivity(newScreen, this);
    }

    static public void openScreenInNewActivity(ScreenFragment newScreen, Fragment fragment) {
        if (fragment == null || fragment.getContext() == null)
            return;

        numActivities = Math.min(numActivities + 1, 3);
        MainFragment.newActivityScreen = newScreen;
        Intent intent = new Intent(fragment.getContext(), ScreenActivity.class);
        fragment.startActivity(intent);
    }

    protected boolean needCloseActivity() {
        if (getPrevClass() != null)
            return false;

        numActivities = Math.max(numActivities - 1, -1);
        return numActivities >= 0;
    }

    protected void replaceFragment(ScreenFragment newScreen) {
        if (newScreen instanceof EventFragment ||
                newScreen instanceof PlaceFragment ||
                newScreen instanceof ParticipFragment ||
                newScreen instanceof ComplainFragment ||
                newScreen instanceof ComplainMessageFragment) {

            openScreenInNewActivity(newScreen);
            return;
        }

        View view = getView();
        if (screenListener != null && view != null)
            view.post(() -> screenListener.fragmentReplace(ScreenFragment.this, newScreen));
    }


    public void shareInstagram(Bitmap bitmap) {
        Context context = getContext();
        if (context == null)
            return;

        showScreensaver(true);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "IMG_" + timeStamp + ".jpg";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File file;
        try {
            file = File.createTempFile(fileName, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("IRON", "WinnerFragment.shareInstagram() " + e);
            hideScreensaver();
            return;
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // ставить 85 бесполезно, PNG - это формат сжатия без потерь
            out.close();
        } catch (Exception e) {
            Log.d("IRON", "ScreenFragment.shareInstagram() " + e);
            hideScreensaver();
            return;
        }

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        File media = new File(file.getAbsolutePath());

        share.putExtra(Intent.EXTRA_STREAM, FileUtils.getUriForFile(context, media));
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(share, "Share to"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        hideScreensaver();
    }

    public Class<? extends ScreenFragment> getPrevClass() {
        return prevClass;
    }

    public void setPrevClass(Class<? extends ScreenFragment> prevClass) {
        this.prevClass = prevClass;
    }

    public interface ScreenListener {
        void fragmentReplace(ScreenFragment curScreen, ScreenFragment newScreen);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        openCamera(requestCodeFragment);
                        return;
                    }
                }
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    onBackPressed();
                }
            }
            break;
        }
    }
}
