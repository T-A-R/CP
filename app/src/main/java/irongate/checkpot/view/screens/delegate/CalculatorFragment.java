package irongate.checkpot.view.screens.delegate;

import android.webkit.WebView;

import irongate.checkpot.R;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.player.map.MapFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class CalculatorFragment extends ScreenFragment {

    private WebView webView;

    public CalculatorFragment() {
        super(R.layout.fragment_calculator);
    }

    @Override
    protected void onReady() {
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("https://checkpot.fun/profit/");
        webView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new RafflesFragment());
        return true;
    }
}
