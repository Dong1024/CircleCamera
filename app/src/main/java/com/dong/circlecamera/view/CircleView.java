package com.dong.circlecamera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.dong.circlecamera.R;


/**
 * Created by dong on 2018/5/23.
 */

public class CircleView extends View {

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint borderPaint;
    private int borderWidth;

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
        int borderColor = getResources().getColor(R.color.colorAccent);
        if (null == borderPaint){
            borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setAntiAlias(true);//抗锯齿
            borderPaint.setDither(true);//防抖动
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画边框
        if (borderWidth != 0) {
            borderPaint.setStrokeWidth(borderWidth);
            float x = getWidth() / 2.0F;
            float y = getHeight() / 2.0F;
            float radius = getWidth() / 2.0F - borderWidth / 2.0F + 1;
            canvas.drawCircle(x, y, radius, borderPaint);
        }
    }

}
