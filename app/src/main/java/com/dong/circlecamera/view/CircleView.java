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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int borderColor = getResources().getColor(R.color.colorAccent);
            setBackgroundColor(Color.TRANSPARENT);

            paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setAntiAlias(true);//抗锯齿
            paint.setDither(true);//防抖动

            borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setAntiAlias(true);//抗锯齿
            borderPaint.setDither(true);//防抖动

        } else {
            int borderColor = getResources().getColor(R.color.colorAccent);
            setBackgroundColor(Color.WHITE);
            paint = new Paint();
            paint.setColor(Color.TRANSPARENT);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setAntiAlias(true);//抗锯齿
            paint.setDither(true);//防抖动
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Paint.Style.FILL);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //画圆形
            Path pathRect = new Path();
            pathRect.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CCW);
            Path pathCircle = new Path();
            pathCircle.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - borderWidth, Path.Direction.CCW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pathRect.op(pathCircle, Path.Op.DIFFERENCE);
            }
            canvas.drawPath(pathRect, paint);
            //画边框
            if (borderWidth != 0) {
                borderPaint.setStrokeWidth(borderWidth);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - borderWidth / 2, borderPaint);
            }
        } else {
            if (borderWidth != 0) {
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, borderPaint);
            }
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - borderWidth, paint);
        }
    }

}
