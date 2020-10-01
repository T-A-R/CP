package irongate.checkpot.view.screens.delegate.addedRaffle;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import irongate.checkpot.R;

class PrizeViewHolder extends RecyclerView.ViewHolder implements TextWatcher {
    private PrizeEditView prizeEditView;
    private Listener listener;
    private PrizeUploadModel prizeUploadData;

    PrizeViewHolder(@NonNull View itemView) {
        super(itemView);
        prizeEditView = (PrizeEditView) itemView;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    void onBind(PrizeUploadModel prizeUploadData) {
        this.prizeUploadData = prizeUploadData;

        String numPlace = itemView.getContext().getString(R.string.add_rafffe_num_place);
        prizeEditView.getTextWinningPlace().setText(String.format(numPlace, prizeUploadData.getId()));

        if (prizeUploadData.getPrizeImage() != null) {
            prizeEditView.setBitmap(prizeUploadData.getPrizeImage());
        }

        if (prizeUploadData.getPrizeName() != null) {
            prizeEditView.getNameView().setText(prizeUploadData.getPrizeName());
        }

        prizeEditView.getPrizeUploadView().setLoadClickListener(v -> onLoadClickListener());
        prizeEditView.getNameView().addTextChangedListener(this);
    }

    private void onLoadClickListener() {
        listener.onClick(prizeUploadData);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        prizeUploadData.setPrizeName(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    interface Listener {
        void onClick(PrizeUploadModel prizeUploadData);
    }
}
