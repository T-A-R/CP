package irongate.checkpot.view.screens.delegate.information;

import android.view.View;
import android.widget.Button;
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

public class InfoDoneFragment extends SmartFragment implements View.OnClickListener {
    static final public String INTENT_BTN = "INTENT_BTN";
    static final public String INTENT_BACK = "INTENT_BACK";

    private RelativeLayout cont;
    private ImageButton btnClose;
    private Button btn;

    boolean isClosed = false;

    public InfoDoneFragment() {
        super(R.layout.fragment_info_done);
    }

    @Override
    protected void onReady() {
        cont = (RelativeLayout) findViewById(R.id.cont);
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        TextView title = (TextView) findViewById(R.id.title);
        TextView desc = (TextView) findViewById(R.id.desc);
        btn = (Button) findViewById(R.id.btn);

        title.setTypeface(Fonts.getFuturaPtMedium());
        desc.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);

        cont.startAnimation(Anim.getAppear(getContext()));
        title.startAnimation(Anim.getAppearSlide(getContext(), 200));
        desc.startAnimation(Anim.getAppearSlide(getContext(), 400));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 600));

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
}
