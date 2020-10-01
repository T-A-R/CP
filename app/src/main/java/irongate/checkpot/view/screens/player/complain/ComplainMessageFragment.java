package irongate.checkpot.view.screens.player.complain;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.common.place.PlaceFragment;
import irongate.checkpot.view.screens.player.event.EventFragment;

public class ComplainMessageFragment extends ScreenFragment {

    private Event event;
    private Place place;

    public ComplainMessageFragment() {
        super(R.layout.fragment_complain_message);
    }

    @Override
    protected void onReady() {
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_complain_message_back);
        RelativeLayout shadow = (RelativeLayout) findViewById(R.id.shadow_complain_message);
        TextView title = (TextView) findViewById(R.id.txt_complain_message_title);
        TextView txtDesc = (TextView) findViewById(R.id.txt_complain_message_desc);
        EditText input = (EditText) findViewById(R.id.input_complain_message);
        Button btn = (Button) findViewById(R.id.btn_complain_message);

        title.setTypeface(Fonts.getFuturaPtMedium());
        txtDesc.setTypeface(Fonts.getFuturaPtBook());
        input.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);

        btnBack.startAnimation(Anim.getAppearSlide(getContext()));
        title.startAnimation(Anim.getAppearSlide(getContext(), 200));
        txtDesc.startAnimation(Anim.getAppearSlide(getContext(), 400));
        shadow.startAnimation(Anim.getAppearSlide(getContext(), 1000));

        MainFragment.disableSideMenu();

        btnBack.setOnClickListener(v -> onBackPressed());
        btn.setOnClickListener(v -> {
            showScreensaver(true);
            String text = input.getText().toString();
            CheckpotAPI.sendEventAppeal(event.getUuid(),text, this::onAppeal);
        });
    }

    private void onAppeal() {
        Toast.makeText(getContext(), "Жалоба отправлена", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    public Event getEvent() {
        return event;
    }

    public ComplainMessageFragment setEvent(Event event) {
        this.event = event;
        return this;
    }

    public Place getPlace() {
        return place;
    }

    public ComplainMessageFragment setPlace(Place place) {
        this.place = place;
        return this;
    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return true;
    }
}
