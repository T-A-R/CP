package irongate.checkpot.view.screens.player.tutorial;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import irongate.checkpot.App;
import irongate.checkpot.R;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.PageIndicator;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.player.registration.WelcomeFragment;

/**
 * Created by Iron on 07.09.2017.
 */

public class TutorialFragment extends ScreenFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private PageIndicator indicator;
    private TextView txtNext;
    private TextView txtSkip;
    private ViewPager pager;

    private int curPage = 0;

    public TutorialFragment() {
        super(R.layout.fragment_tutorial);
    }

    @Override
    protected void onReady() {
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new FragmentStatePagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                TutorialPageFragment item = new TutorialPageFragment();
                item.setPage(position);
                return item;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        pager.addOnPageChangeListener(this);

        indicator = (PageIndicator) findViewById(R.id.indicator);
        indicator.setDotColor(Color.rgb(241, 241, 241));
        indicator.setNumPages(3);

        txtNext = (TextView) findViewById(R.id.txt_next);
        txtSkip = (TextView) findViewById(R.id.txt_skip);

        txtNext.setTypeface(Fonts.getFuturaPtBook());
        txtSkip.setTypeface(Fonts.getFuturaPtBook());

        txtNext.setOnClickListener(this);
        txtSkip.setOnClickListener(this);
        App.getMetricaLogger().log( "Туториал");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        indicator.setPositionOffset(positionOffset + position);
    }

    @Override
    public void onPageSelected(int position) {
        curPage = position;
        txtNext.setText(curPage < 2 ? R.string.tutorial_next : R.string.tutorial_begin);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        if (view == txtNext) {
            if (curPage < 2)
                pager.setCurrentItem(curPage + 1);
            else {
                toWelcome();
            }
        }
        else if (view == txtSkip) {
            toWelcome();
        }
    }

    private void toWelcome() {
        replaceFragment(new WelcomeFragment().setEnter(false));
    }

    @Override
    public boolean onBackPressed() {
        toWelcome();
        return true;
    }
}
