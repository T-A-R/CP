package irongate.checkpot.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class PageIndicator extends View {
    Paint paintDot;
    Paint paintCursor;

    private int numPages = 1;
    private float positionOffset = 0;

    public PageIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_HARDWARE, null);

        paintDot = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDot.setColor(Color.WHITE);

        paintCursor = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCursor.setColor(Color.rgb(131, 210, 137));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radius = getHeight() / 2 ;
        for (int i = 0; i < numPages; i++) {
            float x = radius * (1 + i * 4 + (i <= positionOffset ? 0 : 1));
            canvas.drawCircle(x, radius, radius, paintDot);
        }
        float x = positionOffset * radius * 4;
        canvas.drawRoundRect(new RectF(x, 0, x + radius * 3, radius * 2), radius, radius, paintCursor);
    }

    public void setPositionOffset(float positionOffset) {
        this.positionOffset = positionOffset;
        invalidate();
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
        invalidate();
    }

    public void setDotColor(int color) {
        paintDot.setColor(color);
        invalidate();
    }

    public void setCursorColor(int color) {
        paintCursor.setColor(color);
        invalidate();
    }
}
