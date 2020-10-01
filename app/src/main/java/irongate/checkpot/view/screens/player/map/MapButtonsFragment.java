package irongate.checkpot.view.screens.player.map;

import android.annotation.SuppressLint;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxSeekBar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.SmartFragment;

public class MapButtonsFragment extends SmartFragment implements View.OnClickListener {
    private View cont;
    private ImageButton btnSearch;
    private ImageButton btnLevel;
    private ImageButton btnCurrent;
    private TextView mTextCurBill;
    private RelativeLayout mPanelButton;
    private RelativeLayout mPanelSeek;
    private RecyclerView searchRV;
    private SearchView mSearchView;
    private SeekBar mSeekBar;

    private ButtonsListener btnListener;
    private boolean needShow = false;

    public void setPlaceList(List<Place> placeList, SearchAdapter.ItemClickListener listener) {
        initSearch(placeList, listener);
    }

    public MapButtonsFragment() {
        super(R.layout.fragment_map_buttons);
    }

    @Override
    protected void onReady() {
        cont = findViewById(R.id.cont);
        TextView mTextBill = (TextView) findViewById(R.id.txt_your_bill);
        TextView mTextMinBill = (TextView) findViewById(R.id.txt_zero);
        mTextCurBill = (TextView) findViewById(R.id.txt_current_bill);
        TextView mTextMaxBill = (TextView) findViewById(R.id.txt_max_bill);
        btnSearch = (ImageButton) findViewById(R.id.btn_search);
        btnLevel = (ImageButton) findViewById(R.id.btn_level);
        btnCurrent = (ImageButton) findViewById(R.id.btn_aim);
        Button mClose = (Button) findViewById(R.id.close);
        mPanelButton = (RelativeLayout) findViewById(R.id.three_btn);
        mPanelSeek = (RelativeLayout) findViewById(R.id.panel_seek);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);

        mTextBill.setTypeface(Fonts.getFuturaPtMedium());
        mTextMinBill.setTypeface(Fonts.getFuturaPtMedium());
        mTextCurBill.setTypeface(Fonts.getFuturaPtMedium());
        mTextMaxBill.setTypeface(Fonts.getFuturaPtMedium());

        mClose.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnLevel.setOnClickListener(this);
        btnCurrent.setOnClickListener(this);
        mSearchView = (SearchView) findViewById(R.id.search_view);

        searchRV = (RecyclerView) findViewById(R.id.rvSearch);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity());
        searchRV.setLayoutManager(llm);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
        linearLayoutManager.getOrientation());
        searchRV.addItemDecoration(dividerItemDecoration);

        initSeekBar();

        if (needShow)
            show();
    }

    public void setBtnListener(ButtonsListener btnListener) {
        this.btnListener = btnListener;
    }

    @SuppressLint("CheckResult")
    private void initSeekBar() {
        RxSeekBar.changes(mSeekBar)
            .map(progress -> {
                int receipt = progressToReceipt(progress);
                String minCheckProgress;

                if (progress == 0) {
                    minCheckProgress = "0";
                } else if (progress == 5000) {
                    minCheckProgress = "более " + String.valueOf(receipt);
                } else {
                    minCheckProgress = "до " + String.valueOf(receipt);
                }

                mTextCurBill.setText(minCheckProgress);
                return receipt;
            })
            .subscribeOn(AndroidSchedulers.mainThread())
            .throttleLast(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(receipt -> btnListener.onFilterCheck(),
                    error -> Log.d("IRON", "MapButtonsFragment.initSeekBar() " + error));
    }

    private int progressToReceipt(int progress) {
        float q1 = 0.363f;
        float q2 = 0.908f;
        float q3 = 1.40f;

        float period1 = 1300;
        float period2 = 2500;

        if (progress == 0)
            return 0;

        if (progress < period1)
            return (int) (((progress - 100) * q1) + 100);

        if (progress < period2)
            return (int) (((progress - period1) * q2) + 500);

        if (progress == 5000)
            return 5000;

        return (int) (((progress - period2) * q3) + 1500);
    }

    public int getFilterReceipt() {
        if (mSeekBar.getProgress() == 5000)
            return -1;  // Значит не фильтрует

        return progressToReceipt(mSeekBar.getProgress());
    }

    private void initSearch(List<Place> placeList, SearchAdapter.ItemClickListener itemClickListener) {
        SearchAdapter searchAdapter = new SearchAdapter(getContext(), placeList);
        searchAdapter.setItemClickListener(itemClickListener);
        searchRV.setAdapter(searchAdapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRV.setVisibility(View.VISIBLE);
                btnListener.onFilteredList();
                return true;
            }
        });

        mSearchView.setOnCloseListener(() -> {
            btnSearch.setVisibility(View.VISIBLE);
            btnSearch.setAnimation(Anim.getAppearSlide(getContext()));
            mSearchView.setVisibility(View.GONE);
            searchRV.setVisibility(View.GONE);
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                btnSearch.setVisibility(View.GONE);
                mSearchView.setVisibility(View.VISIBLE);
                mSearchView.setIconified(false);
                break;
            case R.id.btn_level:
                mPanelSeek.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.GONE);
                btnLevel.setVisibility(View.GONE);
                btnCurrent.setVisibility(View.GONE);
                mSearchView.setVisibility(View.GONE);
                searchRV.setVisibility(View.GONE);
                btnListener.onFilterClicked();
                break;
            case R.id.btn_aim:
                btnSearch.setVisibility(View.VISIBLE);
                btnSearch.setAnimation(Anim.getAppearSlide(getContext()));
                mSearchView.setVisibility(View.GONE);
                searchRV.setVisibility(View.GONE);
                btnListener.onBtnCurrent();
                break;
            case R.id.close:
                mPanelSeek.setVisibility(View.GONE);
                mPanelButton.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.VISIBLE);
                btnLevel.setVisibility(View.VISIBLE);
                btnCurrent.setVisibility(View.VISIBLE);
                mPanelButton.setAnimation(Anim.getAppearSlide(getContext()));
                btnListener.onMapButtonsClose();

        }
    }

    public void show() {
        if (cont == null) {
            needShow = true;
            return;
        }
        cont.setVisibility(View.VISIBLE);
        cont.setAnimation(Anim.getAppearSlide(getContext()));
    }

    public void hidePanel() {
        if (cont == null) {
            needShow = false;
            return;
        }
        mSearchView.setVisibility(View.GONE);
        searchRV.setVisibility(View.GONE);
        mPanelButton.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);
        btnLevel.setVisibility(View.VISIBLE);
        btnCurrent.setVisibility(View.VISIBLE);
        mPanelButton.setAnimation(Anim.getAppearSlide(getContext()));
    }

    public void showAim() {
        Anim.animateAppear(btnCurrent);
        btnCurrent.setEnabled(true);
    }

    public void hideAim() {
        btnCurrent.setEnabled(false);
        btnCurrent.startAnimation(Anim.getDisappear(getContext(), () -> {
            btnCurrent.clearAnimation();
            btnCurrent.setVisibility(View.GONE);
        }));
    }

    interface ButtonsListener {
        void onBtnCurrent();
        void onFilteredList();
        void onFilterCheck();
        void onMapButtonsClose();
        void onFilterClicked();
    }
}