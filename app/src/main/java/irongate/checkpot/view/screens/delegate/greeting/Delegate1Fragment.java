package irongate.checkpot.view.screens.delegate.greeting;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;

/**
 * Created by Iron on 07.09.2017.
 */

public class Delegate1Fragment extends ScreenFragment implements View.OnClickListener {
    static final public String INTENT_ENTER = "INTENT_ENTER";
    static final public String INTENT_BACK = "INTENT_BACK";

    private Button btn;

    public Delegate1Fragment() {
        super(R.layout.fragment_delegate1);
    }

    @Override
    protected void onReady() {
        TextView txtTitle = (TextView) findViewById(R.id.txt_delegate1_title);
        TextView txtDesc = (TextView) findViewById(R.id.txt_delegate1_desc);
        btn = (Button) findViewById(R.id.btn_delegate1);

        txtTitle.setTypeface(Fonts.getFuturaPtMedium());
        txtDesc.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);

        MainFragment.disableSideMenu();

        btn.setOnClickListener(this);

        txtTitle.startAnimation(Anim.getAppearSlide(getContext()));
        txtTitle.startAnimation(Anim.getAppearSlide(getContext(), 200));
        txtDesc.startAnimation(Anim.getAppearSlide(getContext(), 400));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 700));
    }

    @Override
    public void onClick(View view) {
        if (view == btn) {
            dispatchIntent(INTENT_ENTER);
        }
    }

    @Override
    public boolean onBackPressed() {
        dispatchIntent(INTENT_BACK);
        return true;
    }
}
