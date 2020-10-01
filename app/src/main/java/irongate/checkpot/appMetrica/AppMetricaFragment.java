package irongate.checkpot.appMetrica;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import irongate.checkpot.R;
import irongate.checkpot.view.ScreenFragment;

public class AppMetricaFragment extends ScreenFragment {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    public AppMetricaFragment() {
        super(R.layout.fragment_app_metrica);
    }

    @Override
    protected void onReady() {
            initContent();
        }

        private void initContent() {
            mPager = (ViewPager) findViewById(R.id.viewPager);
            mPagerAdapter = new SampleFragmentPagerAdapter(getActivity().getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuInfo:
                    Toast.makeText(getContext(), Stuff.formLibraryInfo(), Toast.LENGTH_LONG).show();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        private final class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

            public SampleFragmentPagerAdapter(FragmentManager fragmentManager) {
                super(fragmentManager);
            }

            @Override
            public Fragment getItem(int pagePosition) {
                return BasePageFragment.newPageInstance(pagePosition);
            }

            @Override
            public CharSequence getPageTitle(int pagePosition) {
                return BasePageFragment.getPageTitleByPosition(pagePosition);
            }

            @Override
            public int getCount() {
                return BasePageFragment.getPagesCount();
            }

        }

    }
