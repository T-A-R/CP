package irongate.checkpot.view.screens.delegate.editPlace;

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

public class DetailBgSmall extends RelativeLayout {
    private Paint paintWhite;
    private Paint paintErase;

    public DetailBgSmall(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_HARDWARE, null);

        paintWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintWhite.setColor(Color.WHITE);

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
        float mid = height / 2;
        canvas.drawCircle(0, mid, 8 * d, paintErase);
        canvas.drawCircle(width, mid, 8 * d, paintErase);
    }
}
