package irongate.checkpot.view.screens.player.map;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import irongate.checkpot.GPS;
import irongate.checkpot.R;
import irongate.checkpot.model.User;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.SmartFragment;

public class MapEnterCityFragment extends SmartFragment {
    static final String INTENT_OK = "INTENT_OK";
    static final String INTENT_BACK = "INTENT_BACK";

    private View panel;
    private TextView title;
    private EditText edit;
    private Button btn;

    public MapEnterCityFragment() {
        super(R.layout.fragment_map_enter_city);
    }

    @Override
    protected void onReady() {
        panel = findViewById(R.id.panel);
        title = (TextView) findViewById(R.id.title);
        edit = (EditText) findViewById(R.id.edit_name);
        btn = (Button) findViewById(R.id.btn);

        title.setTypeface(Fonts.getFuturaPtMedium());
        edit.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);

        panel.setAnimation(Anim.getAppearSlide(getContext()));
        btn.setAnimation(Anim.getAppearSlide(getContext(), 300));

        btn.setOnClickListener(v -> checkCity());
    }

    private void checkCity() {
        String name = edit.getText().toString();
        if (name.length() == 0)
            return;

        GPS.getAddress(getContext(), name, address -> {
            if (address == null) {
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(getContext(), R.string.map_enter_no, Toast.LENGTH_SHORT).show());
                return;
            }

            String cityName = address.getLocality();

            User.getUser().setCityName(cityName);
            dispatchIntent(INTENT_OK);
        });
    }

    @Override
    public boolean onBackPressed() {
        dispatchIntent(INTENT_BACK);
        return true;
    }
}