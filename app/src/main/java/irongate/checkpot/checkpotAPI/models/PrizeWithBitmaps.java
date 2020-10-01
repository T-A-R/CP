package irongate.checkpot.checkpotAPI.models;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PrizeWithBitmaps extends Prize {

    private List<Bitmap> photosBmp;

    public PrizeWithBitmaps(boolean isRandom, String name, int minReceipt, Bitmap photo) {
        setIsRandom(isRandom);
        setName(name);
        setMinReceipt(minReceipt);
        photosBmp = new ArrayList<>();
        photosBmp.add(photo);
    }

    public List<Bitmap> getPhotosBmp() {
        return photosBmp;
    }

    public void setPhotosBmp(List<Bitmap> photosBmp) {
        this.photosBmp = photosBmp;
    }
}
