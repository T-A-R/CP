package irongate.checkpot.view.screens.player.event;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class DetailBgTop extends RelativeLayout {
    private Paint paintWhite;
    private Paint paintErase;
    private Paint paintLine;

    public DetailBgTop(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_HARDWARE, null);

        paintWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintWhite.setColor(Color.WHITE);

        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine.setColor(Color.GRAY);

        paintErase = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintErase.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        float d = getContext().getResources().getDisplayMetrics().density;
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.drawRoundRect(new RectF(0, 0, width, height), d * 5, d * 5, paintWhite);

        canvas.drawCircle(0, height, 8 * d, paintErase);
        canvas.drawCircle(width, height, 8 * d, paintErase);
        canvas.drawLine(15 * d, height, width - 15 * d, height, paintLine);
    }
}
