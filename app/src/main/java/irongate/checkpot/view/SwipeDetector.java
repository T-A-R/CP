package irongate.checkpot.view;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SwipeDetector implements OnTouchListener {

    private Listener listener;
    private float treshold;
    private PointF down;

    public SwipeDetector (Listener listener, float treshold) {
        this.listener = listener;
        this.treshold = treshold;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                down = new PointF(event.getX(), event.getY());
                return true;

            case MotionEvent.ACTION_MOVE:
                if (down == null)
                    break;

                if (Math.abs(event.getX() - down.x) >= treshold) {
                    if (event.getX() > down.x)
                        listener.onSwipeRight();
                    else
                        listener.onSwipeLeft();
                    down = null;
                    break;
                }
                if (Math.abs(event.getY() - down.y) >= treshold) {
                    if (event.getY() > down.y)
                        listener.onSwipeUp();
                    else
                        listener.onSwipeDown();
                    down = null;
                    break;
                }
                break;
        }
        return false;
    }

    public interface Listener {
        void onSwipeRight();
        void onSwipeLeft();
        void onSwipeUp();
        void onSwipeDown();
    }
}