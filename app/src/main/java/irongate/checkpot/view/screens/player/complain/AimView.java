package irongate.checkpot.view.screens.player.complain;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class AimView extends View {
    private float dip;
    private Paint pBg = new Paint();
    private Paint pLine = new Paint();

    public AimView(Context context) {
        super(context);
        init();
    }

    public AimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        dip = getContext().getResources().getDisplayMetrics().density;

        pBg.setARGB(150, 0, 0, 0);

        pLine.setColor(Color.rgb(126, 208, 132));
        pLine.setStrokeWidth(6 * dip);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int margin = (int) (20 * dip);
        int lineWidth = (int) (6 * dip);
        int lineLong = (int) (26 * dip);
        int left = (int) (margin - lineWidth / 2);
        int rigth = (int) (getWidth() - margin + lineWidth / 2);
        int top = (int) (getHeight() / 2f - getWidth() / 2f + margin - lineWidth / 2) - 80;
        int bot = (int) (getHeight() / 2f + getWidth() / 2f - margin + lineWidth / 2) - 80;

        canvas.drawRect(0, 0, getWidth(), getHeight() / 2f - getWidth() / 2f - 80 + margin, pBg);
        canvas.drawRect(0, getHeight() / 2f - getWidth() / 2f + margin - 80, margin, getHeight() / 2f + getWidth() / 2f - margin - 80, pBg);
        canvas.drawRect(getWidth() - margin, getHeight() / 2f - getWidth() / 2f + margin - 80, getWidth(), getHeight() / 2f + getWidth() / 2f - margin - 80, pBg);
        canvas.drawRect(0, getHeight() / 2f + getWidth() / 2f - margin - 80, getWidth(), getHeight(), pBg);

        canvas.drawLine(left - lineWidth / 2f, top, left + lineLong, top, pLine);
        canvas.drawLine(left, top - lineWidth / 2f, left, top + lineLong, pLine);

        canvas.drawLine(left - lineWidth / 2f, bot, left + lineLong, bot, pLine);
        canvas.drawLine(left, bot + lineWidth / 2f, left, bot - lineLong, pLine);

        canvas.drawLine(rigth + lineWidth / 2f, top, rigth - lineLong, top, pLine);
        canvas.drawLine(rigth, top - lineWidth / 2f, rigth, top + lineLong, pLine);

        canvas.drawLine(rigth + lineWidth / 2f, bot, rigth - lineLong, bot, pLine);
        canvas.drawLine(rigth, bot + lineWidth / 2f, rigth, bot - lineLong, pLine);
    }
}
