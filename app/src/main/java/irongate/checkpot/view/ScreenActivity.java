package irongate.checkpot.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import irongate.checkpot.MainActivity;
import irongate.checkpot.R;

public class ScreenActivity extends MainActivity {

    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_screen);


        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.screen);

        View view = mainFragment.getView();
        if (view == null)
            Log.d("IRON", "MainActivity.onCreate() WTF? view == null");
        else
            view.getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    @Override
    public void onGlobalLayout() {
        View view = mainFragment.getView();
        if (view == null)
            return;

        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        view.post(() -> mainFragment.openNewAcivityScreen());
    }

    @Override
    public void onBackPressed() {
        if (mainFragment.onBackPressed())
            return;

        super.onBackPressed();
    }
}
