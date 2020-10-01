package irongate.checkpot.view.screens.delegate.information;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.SmartFragment;

/**
 * Created by Iron on 07.09.2017.
 */

public class InfoSignFragment extends SmartFragment implements View.OnClickListener {
    static final public String INTENT_BTN = "INTENT_BTN";
    static final public String INTENT_BACK = "INTENT_BACK";

    private RelativeLayout cont;
    private ImageButton btnClose;
    private ImageButton btn;
    private SignView signView;

    boolean isClosed = false;

    public InfoSignFragment() {
        super(R.layout.fragment_info_sign);
    }

    @Override
    protected void onReady() {
        cont = (RelativeLayout) findViewById(R.id.cont);
        btnClose = (ImageButton) findViewById(R.id.btn_back);
        btn = (ImageButton) findViewById(R.id.btn_done);
        TextView title = (TextView) findViewById(R.id.title);
        RelativeLayout contSign = (RelativeLayout) findViewById(R.id.cont_sign);

        signView = new SignView(getContext());
        contSign.addView(signView);

        title.setTypeface(Fonts.getFuturaPtBook());

        cont.startAnimation(Anim.getAppear(getContext()));

        cont.setOnClickListener(this);
        btn.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    private void hide(final String intent) {
        if (isClosed)
            return;

        isClosed = true;
        cont.startAnimation(Anim.getDisappear(getContext(), new Runnable() {
            @Override
            public void run() {
                dispatchIntent(intent);
            }
        }));
    }

    @Override
    public void onClick(View view) {
        if (isClosed)
            return;

        if (view == btn) {
            hide(INTENT_BTN);
        } else if (view == btnClose) {
            onBackPressed();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (!isClosed)
            hide(INTENT_BACK);

        return true;
    }

    public Bitmap getBitmap() {
        return signView.getBitmap();
    }
}
