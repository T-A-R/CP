package irongate.checkpot.view;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;

import java.util.HashMap;
import java.util.Set;

import irongate.checkpot.R;

public class ScrollAppearsListener implements ViewTreeObserver.OnScrollChangedListener {

    private ScrollView scroll;
    private Set<View> items;
    private HashMap<View, Boolean> states = new HashMap<>();
    private int offset;

    public ScrollAppearsListener(ScrollView scroll, Set<View> items, int offset) {
        this.scroll = scroll;
        this.items = items;
        this.offset = offset;
        if (items != null) checkAppears();
    }

    @Override
    public void onScrollChanged() {
        checkAppears();
    }

    private void checkAppears() {
        if (items == null)
            return;

        for (View item : items) {
            if (scroll.getScrollY() > item.getY() + offset) {
                if (states.get(item) != null && !states.get(item)) {
                    item.startAnimation(Anim.getAppearSlide(item.getContext()));
                    states.put(item, true);
                }
            } else {
                if (states.get(item) == null) {
                    item.startAnimation(AnimationUtils.loadAnimation(item.getContext(), R.anim.disappear_slide));
                    states.put(item, false);
                } else if (states.get(item)) {
                    item.startAnimation(Anim.getDisappearSlide(item.getContext()));
                    states.put(item, false);
                }
            }
        }
    }
}
