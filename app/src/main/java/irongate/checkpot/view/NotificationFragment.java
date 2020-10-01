package irongate.checkpot.view;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.view.screens.delegate.information.EventInformationFragment;
import irongate.checkpot.view.screens.player.event.EventFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class NotificationFragment extends ScreenFragment {

    private RelativeLayout cont;
    private TextView text;
    private ImageView closeRing;
    private ImageView closeCross;
    private boolean positive = true;
    private boolean isClosed = true;
    private boolean isBill;
    private boolean isEventEnd;
    private boolean isEventStart;
    private Event event;
    private String eventID;

    public NotificationFragment() {
        super(R.layout.notification);
    }

    @Override
    protected void onReady() {

        cont = (RelativeLayout) findViewById(R.id.cont);
        text = (TextView) findViewById(R.id.tv_notification);
        closeRing = (ImageView) findViewById(R.id.img_close_ring);
        closeCross = (ImageView) findViewById(R.id.img_close_crest);

        text.setSelected(true);

        cont.setOnClickListener(v -> textPress());
        closeRing.setOnClickListener(v -> closePress());
    }

    public void setData(String txtNotification, String action, String eventID) {

        if (action.equals("success")) this.positive = true;
        if (action.equals("incorrect")) this.positive = false;
        if (action.equals("account_activated")) {
            MainFragment.wasNotify = MainFragment.NotifyType.Activated;
            this.positive = true;
        }
        this.eventID = eventID;
        isClosed = false;

        if (action.equals("incorrect") || action.equals("success")) isBill = true;
        else isBill = false;
        if (action.equals("event_end")) isEventEnd = true;
        else isEventEnd = false;
        if (action.equals("confirmed")) isEventStart = true;
        else isEventStart = false;

        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(() -> {
                            text.setText(txtNotification);

                            if (positive) {
                                Log.d(MainActivity.TAG, "Notification positive: " + txtNotification);
                                cont.setVisibility(View.VISIBLE);
                                cont.startAnimation(Anim.getAnimation(getContext(), R.anim.show_notification));
                                cont.setBackgroundResource(R.drawable.notification_green);
                                closeRing.setBackgroundResource(R.drawable.circle_yellow);
                                closeCross.setBackgroundResource(R.drawable.ico_x);
                            } else {
                                Log.d(MainActivity.TAG, "Notification negative: " + txtNotification);
                                cont.setVisibility(View.VISIBLE);
                                cont.startAnimation(Anim.getAnimation(getContext(), R.anim.show_notification));
                                cont.setBackgroundResource(R.drawable.notification_orange);
                                closeRing.setBackgroundResource(R.drawable.circle_green);
                                closeCross.setBackgroundResource(R.drawable.ico_close_white);
                            }

                            text.setSelected(true);

                            Slider slider = new Slider();
                            slider.execute();
                        });

                }
            }
        };
        thread.start();

    }

    public void hide() {
        cont.setVisibility(View.GONE);
        isClosed = true;
    }

    class Slider extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!isClosed) {
                cont.startAnimation(Anim.getAnimation(getContext(), R.anim.hide_notification));
                cont.setVisibility(View.GONE);
                isClosed = true;
//                if (!isBill) restartActivity();
            }
        }
    }

    private void textPress() {

        isClosed = true;

        Thread thread = new Thread() {
            @Override
            public void run() {
                synchronized (this) {
                    getActivity().runOnUiThread(() -> {
                        hide();
                        if (isBill || isEventEnd || isEventStart) {
                            showScreensaver(true);
                            getUser().updateUser(ok -> {
                                hideScreensaver();
                                Context context = getContext();
                                if (!ok && context != null) {

                                    Toast.makeText(context, R.string.raffles_fail, Toast.LENGTH_SHORT).show();
                                }
                                if (isBill || isEventStart || isEventEnd)
                                    openEvent();

                            });

                        } else restartActivity();
                    });
                }
            }
        };
        thread.start();
    }

    private void closePress() {
        if (!isClosed) {
            cont.startAnimation(Anim.getAnimation(getContext(), R.anim.hide_notification));
            cont.setVisibility(View.GONE);
            isClosed = true;
            if (!isBill) restartActivity();
        }
    }

    private void restartActivity() {
//        Intent intent = new Intent(getContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        getContext().startActivity(intent);
        openRaffles();
    }

    private void openEvent() {
        if (getUser().getCurrentUser() != null) {
            for (int i = 0; i < getUser().getCurrentUser().getEvents().size(); i++) {
                if (getUser().getCurrentUser().getEvents().get(i).getUuid().equals(eventID)) {
                    event = getUser().getCurrentUser().getEvents().get(i);
                    break;
                }
            }
        }
        if (event != null) {
            if (isEventEnd)
                openScreenInNewActivity(new EventInformationFragment().setEvent(event));
            else
                openScreenInNewActivity(new EventFragment().setEvent(event));
        } else Log.d(MainActivity.TAG, "Event from notification not found! ");
    }

    private void openRaffles() {
        RafflesFragment fragment = new RafflesFragment();
        fragment.setWins(true);

        openScreenInNewActivity(fragment);
    }
}
