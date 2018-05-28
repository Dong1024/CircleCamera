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
 * kl
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
    private int borderWidth;
    private RectF rectF;

    /**
     * @param circleWidth 指定view宽高
     * @param borderWidth 边框宽度
     */
    public void setBorderWidth(int circleWidth, int borderWidth) {
        this.borderWidth = borderWidth;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(circleWidth, circleWidth + borderWidth * 2);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        setLayoutParams(params);
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
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
        if (borderWidth != 0) {
            paint.setStrokeWidth(borderWidth);
            int left = borderWidth / 2;
            int top = (getHeight() - (getWidth())) / 2 + borderWidth / 2;
            int right = getWidth() - borderWidth / 2;
            int bottom = (getHeight() - getWidth()) / 2 + getWidth() - borderWidth / 2;

            if (null == rectF) rectF = new RectF(left, top, right, bottom);
            canvas.drawArc(rectF, 0, 360, false, paint);
        }
    }

}
