package irongate.checkpot.view.screens.delegate.companyRegistration;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import irongate.checkpot.R;

public class PopupViewSmall extends LinearLayout {
    private ImageView imgShape;
    private TextView txtDescription;
    private ImageView circleWhite;
    private ImageView imgAnimale;

    public PopupViewSmall(Context context) {
        super(context);
        initComponent();
    }

    public PopupViewSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public PopupViewSmall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    public void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.popup_small_view, this);


        imgShape = findViewById(R.id.img_shape_small);
        txtDescription = findViewById(R.id.description);
        imgAnimale = findViewById(R.id.img_animal_small);

    }

    public TextView getTxtDescription() {
        return txtDescription;
    }

    public void setTxtDescription(TextView txtDescription) {
        this.txtDescription = txtDescription;
    }

    public ImageView getImgShape() {
        return imgShape;
    }

    public void setImgShape(ImageView imgShape) {
        this.imgShape = imgShape;
    }
}
