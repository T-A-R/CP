package irongate.checkpot.view.screens.player.complain;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.FontAdapter;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.common.place.PlaceFragment;
import irongate.checkpot.view.screens.player.event.EventFragment;

public class ComplainFragment extends ScreenFragment implements AdapterView.OnItemClickListener {

    private Event event;
    private Place place;

    public ComplainFragment() {
        super(R.layout.fragment_complain);
    }

    @Override
    protected void onReady() {
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_complain_back);
        TextView title = (TextView) findViewById(R.id.txt_complain_title);
        TextView txtChoose = (TextView) findViewById(R.id.txt_complain_choose);
        RelativeLayout shadow = (RelativeLayout) findViewById(R.id.shadow_complain);

        title.setTypeface(Fonts.getFuturaPtMedium());
        txtChoose.setTypeface(Fonts.getFuturaPtBook());

        ListView list = (ListView) findViewById(R.id.list_complain);
        List<String> causes = Arrays.asList(getResources().getStringArray(R.array.complain_causes));
        FontAdapter adapter = new FontAdapter(getContext(), R.layout.list_complain, causes);
        list.setAdapter(adapter);

        btnBack.startAnimation(Anim.getAppearSlide(getContext()));
        title.startAnimation(Anim.getAppearSlide(getContext(), 200));
        txtChoose.startAnimation(Anim.getAppearSlide(getContext(), 400));
        shadow.startAnimation(Anim.getAppearSlide(getContext(), 1000));

        btnBack.setOnClickListener(v -> onBackPressed());
        list.setOnItemClickListener(this);

        MainFragment.disableSideMenu();
    }

    public Event getEvent() {
        return event;
    }

    public ComplainFragment setEvent(Event event) {
        this.event = event;
        return this;
    }

    public Place getPlace() {
        return place;
    }

    public ComplainFragment setPlace(Place place) {
        this.place = place;
        return this;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        replaceFragment(new ComplainMessageFragment().setEvent(event).setPlace(place));
    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return true;
    }
}
