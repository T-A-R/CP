package irongate.checkpot.view.screens.delegate.information;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.delegate.aboutWinner.WinnersFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class EventInformationFragment extends ScreenFragment {

    private LinearLayout cont;
    private TextView title;
    private TextView txtEndTime;
    private TextView txtEventPresent;
    private TextView txtEventId;
    private TextView txtId;
    private TextView prize;
    private TextView txt1;
    private TextView txt2;
    private TextView txt3;
    private TextView txt4;
    private TextView data1;
    private TextView data2;
    private TextView data3;
    private TextView data4;
    private Button btnDownload;
    private Button btnToEvents;

    private Event event;

    public EventInformationFragment() {
        super(R.layout.fragment_event_information);
    }

    public EventInformationFragment setEvent(Event event) {
        this.event = event;
        return this;
    }

    @Override
    protected void onReady() {

        cont = (LinearLayout) findViewById(R.id.cont);
        title = (TextView) findViewById(R.id.title);
        prize = (TextView) findViewById(R.id.prize);
        txtEndTime = (TextView) findViewById(R.id.txt_end_time);
        txtEventPresent = (TextView) findViewById(R.id.txt_event_present);
        txtEventId = (TextView) findViewById(R.id.txt_event_id);
        txtId = (TextView) findViewById(R.id.id_title);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        txt4 = (TextView) findViewById(R.id.txt4);
        data1 = (TextView) findViewById(R.id.data1);
        data2 = (TextView) findViewById(R.id.data2);
        data3 = (TextView) findViewById(R.id.data3);
        data4 = (TextView) findViewById(R.id.data4);
        btnDownload = (Button) findViewById(R.id.btn_download);
        btnToEvents = (Button) findViewById(R.id.btn_to_events);

        title.setTypeface(Fonts.getFuturaPtMedium());
        prize.setTypeface(Fonts.getFuturaPtBook());
        txtEndTime.setTypeface(Fonts.getFuturaPtBook());
        txtEventPresent.setTypeface(Fonts.getFuturaPtBook());
        txtEventId.setTypeface(Fonts.getFuturaPtBook());
        txtId.setTypeface(Fonts.getFuturaPtBook());
        txt1.setTypeface(Fonts.getFuturaPtBook());
        txt2.setTypeface(Fonts.getFuturaPtBook());
        txt3.setTypeface(Fonts.getFuturaPtBook());
        txt4.setTypeface(Fonts.getFuturaPtBook());
        data1.setTypeface(Fonts.getFuturaPtBook());
        data2.setTypeface(Fonts.getFuturaPtBook());
        data3.setTypeface(Fonts.getFuturaPtBook());
        data4.setTypeface(Fonts.getFuturaPtBook());
        btnDownload.setTypeface(Fonts.getFuturaPtBook());
        btnToEvents.setTypeface(Fonts.getFuturaPtBook());

        cont.startAnimation(Anim.getAppearSlide(getContext(), 1000));

        //TODO unhide later
        btnDownload.setVisibility(View.GONE);
        txt4.setVisibility(View.GONE);
        data4.setVisibility(View.GONE);

        showMenu();
        setData();
        setButtons();
    }

    private void setData() {
        if (event != null) {
            if (event.getMainPrize() != null && event.getMainPrize().getName() != null && !event.getMainPrize().getName().isEmpty()) {
                prize.setText(firstUpperCase(event.getMainPrize().getName()));
            }

            if(event.getEnded() != null) {
                Date date = new Date(event.getEnded() * 1000);
                @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                txtEndTime.setText(df.format(date));
            }

            if (event.getPrizes() != null) {
                int prizeCounter = 0;
                for (int i = 0; i < event.getPrizes().size(); i++) {
                    if (event.getPrizes().get(i).getIsRandom()) prizeCounter++;
                }
                txtEventPresent.setText(String.valueOf(prizeCounter));
            }

            if (event.getDigitalId() != null) {
                txtEventId.setText(event.getDigitalId());
            }

            data1.setText(String.valueOf(event.getNumSuccMembers()));

            if (event.getMembers() != null) {
                int counter = 0;
                float sum = 0;
                for (int i = 0; i < event.getMembers().size(); i++) {
                    if (event.getMembers().get(i).getCheck() != null && event.getMembers().get(i).getCheck().getStatus().equals("SUCCESS")) {
                        sum += Float.parseFloat(Objects.requireNonNull(event.getMembers().get(i).getCheck().getData().get("sum")));
                        counter++;
                    }
                }
                String stringSum = new DecimalFormat("#0").format(sum);

                data2.setText(stringSum + " \u20BD ");
                if (counter > 0) {
                    String stringAve = new DecimalFormat("#0").format(sum / counter);
                    data3.setText(stringAve + " \u20BD ");
                }
            }


        }
    }

    private void setButtons() {
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO later.
            }
        });

        btnToEvents.setOnClickListener(v -> winnersButton());
    }

    public String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) return ""; //или return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    @Override
    public boolean onBackPressed() {
        RafflesFragment fragment = new RafflesFragment();
        fragment.setDone(true);
        replaceFragment(fragment);
        return true;
    }

    public void winnersButton() {
        replaceFragment(new WinnersFragment());
    }
}
