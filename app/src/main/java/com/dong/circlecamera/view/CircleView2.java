package com.dong.circlecamera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.dong.circlecamera.R;


/**
 * Created by dong on 2018/5/23.
 * 另一各实现圆形边框
 */

public class CircleView2 extends View {

    public CircleView2(Context context) {
        super(context);
        init();
    }

    public CircleView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint paint;
    private Paint borderPaint;
    private int borderWidth;

    /**
     * @param viewHeight  需要指定view的高度
     * @param borderWidth 边框宽度
     */
    public void setBorderWidth(int viewHeight, int borderWidth) {
        this.borderWidth = borderWidth;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.height = viewHeight + borderWidth * 2;
    }

    private void init() {
        setBackgroundColor(Color.WHITE);
        //圆开透明视图
        borderPaint = new Paint();
        borderPaint.setColor(Color.TRANSPARENT);
        borderPaint.setStyle(Paint.Style.FILL);
        borderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        //圆形边框
        int borderColor = getResources().getColor(R.color.colorAccent);
        paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);//抗锯齿
        paint.setDither(true);//防抖动
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, borderPaint);
        if (borderWidth != 0) {
            paint.setStrokeWidth(borderWidth);
            int left = borderWidth / 2;
            int top = (getHeight() - (getWidth())) / 2 + borderWidth / 2;
            int right = getWidth() - borderWidth / 2;
            int bottom = (getHeight() - getWidth()) / 2 + getWidth() - borderWidth / 2;
            RectF rectF = new RectF(left, top, right, bottom);
            canvas.drawArc(rectF, 0, 360, false, paint);
        }
    }

}
