package irongate.checkpot.view.screens.player.map;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import irongate.checkpot.App;
import irongate.checkpot.GPS;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.model.User;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.SmartFragment;

public class MapFirstStartFragment extends SmartFragment implements CheckpotAPI.GetPlacesListViaGeoListener {
    static final String INTENT_START = "INTENT_START";
    static final String INTENT_NOT_MINE = "INTENT_NOT_MINE";
    static final String INTENT_BUSINESS = "INTENT_BUSINESS";

    private View panel;
    private TextView title;
    private Button btnStart;
    private Button btnBusiness;

    public MapFirstStartFragment() {
        super(R.layout.fragment_map_first_start);
    }

    @Override
    protected void onReady() {
        panel = findViewById(R.id.panel);
        title = (TextView) findViewById(R.id.title);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnBusiness = (Button) findViewById(R.id.btn_business);

        title.setTypeface(Fonts.getFuturaPtMedium());
        btnStart.setTypeface(Fonts.getFuturaPtBook());
        btnBusiness.setTypeface(Fonts.getFuturaPtBook());
        btnStart.setTransformationMethod(null);
        btnBusiness.setTransformationMethod(null);

        MainFragment.disableSideMenu();

        GPS.getAddress(getContext(), User.getUser().getCityName(), address -> {
            if (address == null) {
                updateTitle(0);
                getActivity().runOnUiThread(() -> showButtons());
                return;
            }

            CheckpotAPI.getPlacesListViaGeo(address.getLatitude(), address.getLongitude(), this);
        });
    }

    @Override
    public void onGetPlacesListViaGeo(List<Place> list) {
        title.post(() -> {
            updateTitle(list.size());
            showButtons();
        });
    }

    private void updateTitle(int numPlaces) {
        String name = User.getUser().getCityName();
        if (name != null) {
            SpannableString ss;
            ss = new SpannableString(getString(R.string.map_city_selected_title, name, numPlaces));
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View textView) {
                    dispatchIntent(INTENT_NOT_MINE);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };
            ss.setSpan(clickableSpan, 5, 5 + name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            title.setText(ss);
            title.setMovementMethod(LinkMovementMethod.getInstance());
            title.setLinkTextColor(getResources().getColor(R.color.green));
            title.setHighlightColor(Color.TRANSPARENT);
        }
    }

    private void showButtons() {
        panel.setVisibility(View.VISIBLE);
        btnStart.setVisibility(View.VISIBLE);
        btnBusiness.setVisibility(View.VISIBLE);
        panel.setAnimation(Anim.getAppearSlide(getContext()));
        btnStart.setAnimation(Anim.getAppearSlide(getContext(), 300));
        btnBusiness.setAnimation(Anim.getAppearSlide(getContext(), 500));

        btnStart.setOnClickListener(v -> {
            MainFragment.enableSideMenu();
            dispatchIntent(INTENT_START);
        });
        btnBusiness.setOnClickListener(v -> {
            dispatchIntent(INTENT_BUSINESS);
            App.getMetricaLogger().log( "Для бизнеса");
        });
    }
}