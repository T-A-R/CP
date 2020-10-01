package irongate.checkpot.view.screens.delegate.companyRegistration;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import irongate.checkpot.R;

public class PopupView extends RelativeLayout {
    private ImageView imgShape;
    private TextView txtDescription;
    private ImageView imgAnimale;
    private Button btnProfile;

    public PopupView(Context context) {
        super(context);
        initComponent();
    }

    public PopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public PopupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    public void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.popup_view, this);

        imgShape = findViewById(R.id.img_shape);
        txtDescription = findViewById(R.id.description);
        imgAnimale = findViewById(R.id.img_animal);
        btnProfile = findViewById(R.id.btn_profile);
    }

    public TextView getTxtDescription() {
        return txtDescription;
    }

    public void setTxtDescription(TextView txtDescription) {
        this.txtDescription = txtDescription;
    }


    public void setDescription() {

    }

    public Button getBtnProfile() {
        return btnProfile;
    }

    public void setBtnProfile(Button btnProfile) {
        this.btnProfile = btnProfile;
    }

    public ImageView getImgShape() {
        return imgShape;
    }

    public void setImgShape(ImageView imgShape) {
        this.imgShape = imgShape;
    }
}
