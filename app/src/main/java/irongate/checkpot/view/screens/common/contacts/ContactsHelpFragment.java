package irongate.checkpot.view.screens.common.contacts;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.SmartFragment;

/**
 * Created by Iron on 07.09.2017.
 */

public class ContactsHelpFragment extends SmartFragment implements View.OnClickListener {
    static final public String INTENT_BACK = "INTENT_BACK";

    private RelativeLayout cont;
    private Button btn;
    private ImageButton btnClose;
    private TextView title;
    private TextView desc;

    boolean isClosed = false;

    public ContactsHelpFragment() {
        super(R.layout.fragment_contacts_help);
    }

    @Override
    protected void onReady() {
        cont = (RelativeLayout) findViewById(R.id.cont);
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        title = (TextView) findViewById(R.id.title);
        desc = (TextView) findViewById(R.id.desc);
        btn = (Button) findViewById(R.id.btn);

        title.setTypeface(Fonts.getFuturaPtMedium());
        desc.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);

        cont.setOnClickListener(this);
        btn.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        cont.startAnimation(Anim.getAppear(getContext()));

        MainFragment.disableSideMenu();
    }

    private void hide() {
        if (isClosed)
            return;

        isClosed = true;
        cont.startAnimation(Anim.getDisappear(getContext(), () -> dispatchIntent(INTENT_BACK)));
        MainFragment.enableSideMenu();
    }

    @Override
    public void onClick(View view) {
        if (isClosed)
            return;

        if (view == btnClose) {
            onBackPressed();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (!isClosed)
            hide();

        return true;
    }
}
