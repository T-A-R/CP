package irongate.checkpot.view.screens.delegate.companyRegistration;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import irongate.checkpot.R;

public class PawEditNarrowView extends RelativeLayout {

    private EditText editText;
    private ImageView imgPaw;
    private TextView txtHelp;

    public PawEditNarrowView(Context context) {
        super(context);
        initComponent();
    }

    public PawEditNarrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public PawEditNarrowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    public void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.paw_view, this);

        editText = (EditText) findViewById(R.id.edit_help);
        imgPaw = findViewById(R.id.img_paw);
        txtHelp = findViewById(R.id.txt_help);

    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }


    public TextView getTxtHelp() {
        return txtHelp;
    }

    public void setTxtHelp(TextView txtHelp) {
        this.txtHelp = txtHelp;
    }

    public ImageView getImgPaw() {
        return imgPaw;
    }

    public void setImgPaw(ImageView imgPaw) {
        this.imgPaw = imgPaw;
    }
}
