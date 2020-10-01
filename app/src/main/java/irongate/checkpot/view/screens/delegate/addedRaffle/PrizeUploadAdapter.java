package irongate.checkpot.view.screens.delegate.addedRaffle;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class PrizeUploadAdapter extends RecyclerView.Adapter<PrizeViewHolder> {
    private List<PrizeUploadModel> uploadDataList = new ArrayList<>();
    private PrizeViewHolder.Listener listener;

    public void setListener(PrizeViewHolder.Listener listener) {
        this.listener = listener;
    }

    List<PrizeUploadModel> getUploadDataList() {
        return uploadDataList;
    }

    @NonNull
    @Override
    public PrizeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        PrizeEditView prizeEditView = new PrizeEditView(viewGroup.getContext());
        PrizeViewHolder prizeViewHolder = new PrizeViewHolder(prizeEditView);
        prizeViewHolder.setListener(listener);
        return prizeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PrizeViewHolder holder, int i) {
        holder.onBind(uploadDataList.get(i));
    }

    @Override
    public int getItemCount() {
        return uploadDataList.size();
    }

    public void clear() {
        uploadDataList.clear();
        notifyDataSetChanged();
    }

    void setUploadDataList(List<PrizeUploadModel> uploadDataList) {
        this.uploadDataList.clear();
        this.uploadDataList.addAll(uploadDataList);
        notifyDataSetChanged();
    }

    void setBitmap(Bitmap bitmap, int requestCode) {
        for (int i = 0; i < uploadDataList.size(); i++) {
            PrizeUploadModel prizeUploadData = uploadDataList.get(i);
            if (prizeUploadData.getGalleryRequestCode() == requestCode || prizeUploadData.getImageRequestCode() == requestCode) {
                prizeUploadData.setPrizeImage(bitmap);
                notifyItemChanged(i);
            }
        }
    }

    int checkEmptyName() {
        for (int i = 0; i < uploadDataList.size(); i++) {
            PrizeUploadModel prizeUploadData = uploadDataList.get(i);
            if (prizeUploadData.getPrizeName() == null || prizeUploadData.getPrizeName().isEmpty()) {
                return prizeUploadData.getId();
            }
        }
        return -1;
    }

    int checkImageIsNull() {
        for (int i = 0; i < uploadDataList.size(); i++) {
            PrizeUploadModel prizeUploadData = uploadDataList.get(i);
            if (prizeUploadData.getPrizeImage() == null) {
                return prizeUploadData.getId();
            }
        }
        return -1;
    }
}
