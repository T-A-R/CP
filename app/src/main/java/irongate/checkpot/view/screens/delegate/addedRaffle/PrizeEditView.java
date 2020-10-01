package irongate.checkpot.view.screens.delegate.addedRaffle;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.view.Fonts;

public class PrizeEditView extends RelativeLayout {

    private TextView textWinningPlace;
    public EditText editText;
    private PrizeUploadView prizeUploadView;

    public PrizeEditView(Context context) {
        super(context);
        initComponent();
    }

    public PrizeEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public PrizeEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_prize_edit, this);

        TextView textView = findViewById(R.id.txt_prize);
        textWinningPlace = findViewById(R.id.txt_winning_place);
        editText = findViewById(R.id.edit_prize);
        prizeUploadView = findViewById(R.id.prize_load_view);

        textWinningPlace.setTypeface(Fonts.getFuturaPtBook());
        textView.setTypeface(Fonts.getFuturaPtBook());
        editText.setTypeface(Fonts.getFuturaPtBook());
    }

    public PrizeUploadView getPrizeUploadView() {
        return prizeUploadView;
    }

    public TextView getTextWinningPlace() {
        return textWinningPlace;
    }

    public void setBitmap(Bitmap bitmap) {
        prizeUploadView.setBitmap(bitmap);
    }

    public void setEditText(String editText) {
        this.editText.setText(editText);
    }

    public EditText getNameView(){
        return editText;
    }

    public String getName(){
        return editText.getText().toString();
    }
}
