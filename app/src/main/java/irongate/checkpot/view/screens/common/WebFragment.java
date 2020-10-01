package irongate.checkpot.view.screens.common;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import irongate.checkpot.R;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.delegate.aboutWinner.WinnersFragment;

import static irongate.checkpot.MainActivity.TAG;

public class WebFragment extends ScreenFragment implements View.OnClickListener {
    ImageButton btnBack;
    WebView webView;
    String url;

    public WebFragment() {
        super(R.layout.fragment_web);
    }

    public WebFragment setData(String url) {
        this.url = url;
        return this;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onReady() {
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.startAnimation(Anim.getAppearSlide(getContext()));
        btnBack.setOnClickListener(this);

        webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(url);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());

        MainFragment.disableSideMenu();
        hideMenu();
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            onBackPressed();
        }
    }

    @Override
    public boolean onBackPressed() {
        Log.d(TAG, "onBackPressed: WEBFRAGMENT");
        replaceFragment(new WinnersFragment());
        return true;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }
}
