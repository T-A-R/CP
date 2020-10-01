package irongate.checkpot.view.screens.delegate.information;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class SignView extends View implements ViewTreeObserver.OnGlobalLayoutListener, View.OnTouchListener {
    static final private int WIDTH = 354;
    static final private int HEIGHT = 236;

    private float scale;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint;
    private PointF point;
    private boolean isCreated = false;


    public SignView(Context context) {
        super(context);
        setWillNotDraw(false);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        scale = (float)getWidth() / (float)WIDTH;
        int height = (int) ((float)getWidth() * HEIGHT / WIDTH);
        setLayoutParams(new RelativeLayout.LayoutParams(getWidth(), height));
        init();
    }

    private void init() {
        bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.rgb(0, 0, 120));
        paint.setStrokeWidth(getContext().getResources().getDisplayMetrics().density * 1.5f);
        paint.setStrokeCap(Paint.Cap.ROUND);

        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, new Rect(0, 0, WIDTH, HEIGHT), new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), new Paint(Paint.ANTI_ALIAS_FLAG));
        invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case ACTION_DOWN:
                point = new PointF(x, y);
                isCreated = true;
                break;

            case ACTION_UP:
                point = null;
                break;

            case ACTION_MOVE:
                if (point == null)
                    return true;

                canvas.drawLine(point.x / scale, point.y / scale, x / scale, y / scale, paint);
                point = new PointF(x, y);
                isCreated = true;
                break;
        }
        return true;
    }

    public Bitmap getBitmap() {
        if(isCreated) return bitmap;
        else return null;
    }
}
