package irongate.checkpot.view.screens.player.advantage;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import irongate.checkpot.App;
import irongate.checkpot.R;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.delegate.greeting.Delegate2Fragment;
import irongate.checkpot.view.screens.player.map.MapFragment;
import irongate.checkpot.view.screens.player.registration.Reg1Fragment;

public class AdvantageFragment extends ScreenFragment {
    private LinearLayout linearCards;

    public AdvantageFragment() {
        super(R.layout.fragment_advantage);
    }

    @Override
    protected void onReady() {
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        TextView title = (TextView) findViewById(R.id.title);
        linearCards = (LinearLayout) findViewById(R.id.linear_cards);
        Button btn = (Button) findViewById(R.id.btn);

        title.setTypeface(Fonts.getFuturaPtMedium());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);

        MainFragment.disableSideMenu();

        populate();

        btnBack.startAnimation(Anim.getAppearSlide(getContext()));
        title.startAnimation(Anim.getAppearSlide(getContext(), 200));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 400));

        btnBack.setOnClickListener(v -> onBackPressed());
        btn.setOnClickListener(v -> {
            if (getUser().isAuthorized()) {
                getUser().setDelegateMode(true);
                replaceFragment(new Delegate2Fragment());
            } else {
                replaceFragment(new Reg1Fragment().setEnter(false));
                App.getMetricaLogger().log("Регистрация ресторана через телефон");
            }
        });
    }

    private void populate() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null)
            return;

        List<AdvantageModel> advantageModels = new ArrayList<>();
        advantageModels.add(new AdvantageModel(R.string.advantage_card_title_average_check, R.string.advantage_card_average_desc_check, R.drawable.img_advantage3));
        advantageModels.add(new AdvantageModel(R.string.advantage_card_title_add_check, R.string.advantage_card_desc_add_check, R.drawable.img_advantage6));
        advantageModels.add(new AdvantageModel(R.string.advantage_card_title_digital, R.string.advantage_card_desc_digital, R.drawable.img_advantage7));
//        advantageModels.add(new AdvantageModel(R.string.advantage_card_title_loyalty,R.string.advantage_card_desc_loyalty,R.drawable.img_advantage4));
//        advantageModels.add(new AdvantageModel(R.string.advantage_card_title_law,R.string.advantage_card_desc_law,R.drawable.img_advantage2));
//        advantageModels.add(new AdvantageModel(R.string.advantage_card_title_integration,R.string.advantage_card_desc_integration,R.drawable.img_advantage5));
//        advantageModels.add(new AdvantageModel(R.string.advantage_card_title_emotions,R.string.advantage_card_desc_emotions,R.drawable.img_advantage1));
        for (int i = 0; i < advantageModels.size(); i++) {
            AdvantageCardFragment card = new AdvantageCardFragment().setAdvantageModel(advantageModels.get(i));
            fragmentManager.beginTransaction().add(linearCards.getId(), card).commit();
        }
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new MapFragment());
        return true;
    }
}
