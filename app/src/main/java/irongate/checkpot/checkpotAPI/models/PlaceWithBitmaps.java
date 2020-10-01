package irongate.checkpot.checkpotAPI.models;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class PlaceWithBitmaps extends Place {
    private Bitmap logoBmp;
    private List<Bitmap> photosBmp;

    public Bitmap getLogoBmp() {
        return logoBmp;
    }

    public void setLogoBmp(Bitmap logoBmp) {
        this.logoBmp = logoBmp;
    }

    public List<Bitmap> getPhotosBmp() {
        return photosBmp;
    }

    public void setPhotosBmp(List<Bitmap> photosBmp) {
        this.photosBmp = photosBmp;
    }
}
