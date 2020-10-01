package irongate.checkpot.view.screens.delegate.addedRaffle;

import android.graphics.Bitmap;

public class PrizeUploadModel {
    private int position;
    private int galleryRequestCode;
    private int imageRequestCode;
    private String prizeName;
    private Bitmap prizeImage;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return position + 1;
    }

    int getGalleryRequestCode() {
        return galleryRequestCode;
    }

    void setGalleryRequestCode(int galleryRequestCode) {
        this.galleryRequestCode = galleryRequestCode;
    }

    int getImageRequestCode() {
        return imageRequestCode;
    }

    void setImageRequestCode(int imageRequestCode) {
        this.imageRequestCode = imageRequestCode;
    }

    String getPrizeName() {
        return prizeName;
    }

    void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    Bitmap getPrizeImage() {
        return prizeImage;
    }

    void setPrizeImage(Bitmap prizeImage) {
        this.prizeImage = prizeImage;
    }
}
