package irongate.checkpot.view.screens.common;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.screens.player.rafles.RaffleCardFragment;

/**
 * Created by Iron on 07.09.2017.
 */

public class CardNotYetFragment extends SmartFragment {

    private boolean delegate = false;
    private CardListener listener;

    public CardNotYetFragment() {
        super(R.layout.fragment_card_not_yet);
    }

    public CardNotYetFragment setDelegate(boolean delegate) {
        this.delegate = delegate;
        return this;
    }

    public void setCardListener(CardListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onReady() {
        TextView title = (TextView) findViewById(R.id.txt_raffle_card_not_yet);
        Button btn = (Button) findViewById(R.id.btn_raffle_card_not_yet);

        title.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);

        if (delegate) {
            title.setText(R.string.card_not_yet_title);
            btn.setText(R.string.card_not_yet_btn_delegate);
            btn.setBackgroundResource(R.drawable.bg_btn_blue_36);
        }

        btn.setOnClickListener(v -> listener.onBtn());
    }

    public interface CardListener {
        void onBtn();
    }
}
