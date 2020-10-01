package irongate.checkpot.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;

import irongate.checkpot.R;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ConfettiView extends RelativeLayout {
    static private int DELAY = 500;

    private Timer timer;

    boolean starsEnabled = true;
    boolean itemsEnabled = true;

    public ConfettiView(Context context) {
        super(context);
        init();
    }

    public ConfettiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConfettiView setStarsEnabled(boolean starsEnabled) {
        this.starsEnabled = starsEnabled;
        return this;
    }

    public ConfettiView setItemsEnabled(boolean itemsEnabled) {
        this.itemsEnabled = itemsEnabled;
        return this;
    }

    private void init() {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setClipChildren(false);
        startTimer();
    }

    public void startTimer() {
        if (timer != null)
            return;

        timer = new Timer();
        timer.schedule(new Task(), 0, DELAY);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private class Task extends TimerTask {
        @Override
        public void run() {
            post(() -> {
                try {
                    if (itemsEnabled) {
                        createItem();
                    }
                    if (starsEnabled) {
                        createStar();
                    }
                } catch (OutOfMemoryError e) {
                    onOutOfMemory();
                }
            });
        }
    }

    private void createStar() {
        int color;
        double rColor = Math.random();
        if (rColor < 0.33f) {
            color = Color.rgb(255, 128, 82);
        }
        else if (rColor > 0.66f) {
            color = Color.rgb(255, 208, 62);
        }
        else {
            color = Color.rgb(255, 255, 255);
        }

        float d = getContext().getResources().getDisplayMetrics().density;
        int size = (int) (70 * d);
        ImageView img = new ImageView(getContext());
        img.setImageResource(R.drawable.anim_star);
        ((AnimationDrawable)img.getDrawable()).start();
        img.setLayoutParams(new LayoutParams(size, size));
        img.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        img.setX((float) (Math.random() * (getWidth() - size)));
        img.setY((float) (Math.random() * (getHeight() - size)));
        addView(img);

        img.postDelayed(new RemoveStarTask(img), 2000);
    }

    private class RemoveStarTask implements Runnable {
        private ImageView star;

        private RemoveStarTask(ImageView star) {
            this.star = star;
        }

        @Override
        public void run() {
            try {
                ViewGroup parent = (ViewGroup) star.getParent();
                if (parent != null)
                    parent.removeView(star);
            } catch (Exception e) {
                Log.d("IRON", "RemoveStarTask.run() " + e);
            }
        }
    }

    private void createItem() {
        float d = getContext().getResources().getDisplayMetrics().density;
        int color;
        double rColor = Math.random();
        if (rColor < 0.33f) {
            color = Color.rgb(255, 128, 82);
        }
        else if (rColor > 0.66f) {
            color = Color.rgb(255, 208, 62);
        }
        else {
            color = Color.rgb(255, 255, 255);
        }

        int src = R.drawable.bg_circle_yellow;
        int type = (int) (Math.random() * 5);
        switch (type) {
            case 0: src = R.drawable.bg_circle_yellow; break;
            case 1: src = R.drawable.ring; break;
            case 2: src = R.drawable.square; break;
            case 3: src = R.drawable.conf_triang; break;
            case 4: src = R.drawable.conf; break;
        }
        int size = type == 4 ? (int) (60 * d) : (int) ((15 + Math.random() * 5) * d);

        RelativeLayout item = new RelativeLayout(getContext());
        item.setClipChildren(false);
        item.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        item.setX((float) (Math.random() * getWidth()));
        addView(item);

        ImageView img = new ImageView(getContext());
        img.setImageResource(src);
        img.setLayoutParams(new LayoutParams(size, size));
        img.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        item.addView(img);

        Animation animTrans = AnimationUtils.loadAnimation(getContext(), R.anim.drop);
        animTrans.setDuration((long) (3000 + Math.random() * 5000));

        animTrans.setAnimationListener(new AnimListener(item));
        item.startAnimation(animTrans);

        if (type != 4) {
            Animation animRot = AnimationUtils.loadAnimation(getContext(), R.anim.rotation);
            animRot.setDuration((long) (5000 + Math.random() * 10000));
            img.startAnimation(animRot);
        }
        else
            img.setScaleX(Math.random() < 0.5f ? 1 : -1);
    }

    private class AnimListener implements Animation.AnimationListener {
        View view;

        private AnimListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            view.post(() -> {
                try {
                    ((ViewGroup) view.getParent()).removeView(view);
                } catch (Exception e) {
                    Log.d("IRON", "AnimListener.run() " + e);
                }
            });
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private void onOutOfMemory() {
        stopTimer();
    }

    public void destroy() {
        stopTimer();
    }
}
