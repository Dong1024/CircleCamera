package com.dong.circlecamera.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


import com.dong.circlecamera.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dong on 2018/5/23.
 */

public class CircleCameraLayout extends RelativeLayout {

    public CircleCameraLayout(Context context) {
        super(context);
        init(context, null, -1, -1);
    }

    public CircleCameraLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1, -1);
    }

    public CircleCameraLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, -1);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleCameraLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }


    private Timer timer;
    private TimerTask pressTask;
    private Context mContext;
    private int circleWidth = 0;//指定半径
    private int borderWidth = 0;//指定边框
    private CameraPreview cameraPreview;//摄像预览

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;
        timer = new Timer();
        if (attrs != null && defStyleAttr == -1 && defStyleRes == -1) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleCameraLayout, defStyleAttr, defStyleRes);
            circleWidth = (int) typedArray.getDimension(R.styleable.CircleCameraLayout_circle_camera_width, ViewGroup.LayoutParams.WRAP_CONTENT);
            borderWidth = (int) typedArray.getDimension(R.styleable.CircleCameraLayout_border_width, 5);
            typedArray.recycle();
        }
        startView();
    }

    /**
     * 设置照相预览
     *
     * @param cameraPreview
     */
    public void setCameraPreview(CameraPreview cameraPreview) {
        this.cameraPreview = cameraPreview;
    }

    /**
     * 释放回收
     */
    public void release() {
        if (null != pressTask) {
            pressTask.cancel();
            pressTask = null;
        }
        if (null != timer) {
            timer.cancel();
            timer = null;
        }
    }

    //延时启动摄像头
    public void startView() {
        pressTask = new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        pressTask.cancel();
                        pressTask = null;
                        if (null != cameraPreview) {
                            show();
                        } else {
                            startView();
                        }
                    }
                });
            }
        };
        timer.schedule(pressTask, 50);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void show() {
        //cmaera根view--layout
        RelativeLayout cameraRoot = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams rootParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootParams.addRule(CENTER_IN_PARENT, TRUE);
        cameraRoot.setBackgroundColor(Color.TRANSPARENT);
        cameraRoot.setClipChildren(false);


        //camera--layout
        FrameLayout cameraLayout = new FrameLayout(mContext);
        Camera.Size preSize = cameraPreview.getCameraSize();
        int cameraHeight = (int) ((float) preSize.width / (float) preSize.height * circleWidth);
        RelativeLayout.LayoutParams cameraParams = new RelativeLayout.LayoutParams(circleWidth, cameraHeight);
        cameraParams.addRule(CENTER_IN_PARENT, TRUE);
        cameraLayout.setLayoutParams(cameraParams);
        cameraLayout.addView(cameraPreview);

        cameraLayout.setOutlineProvider(viewOutlineProvider);//把自定义的轮廓提供者设置给imageView
        cameraLayout.setClipToOutline(true);//开启裁剪

        //circleView--layout
//        CircleView circleView = new CircleView(mContext);
        CircleView2 circleView = new CircleView2(mContext);
        circleView.setBorderWidth(circleWidth, borderWidth);

        //设置margin值---隐藏超出部分布局
        int margin = (cameraHeight - circleWidth) / 2 - borderWidth / 2;
        rootParams.setMargins(0, -margin, 0, -margin);
        cameraRoot.setLayoutParams(rootParams);

        //添加camera
        cameraRoot.addView(cameraLayout);
        //添加circle
        cameraRoot.addView(circleView);
        //添加根布局
        this.addView(cameraRoot);
    }

    //自定义一个轮廓提供者
    public ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void getOutline(View view, Outline outline) {
            //裁剪成一个圆形
            int left0 = 0;
            int top0 = (view.getHeight() - view.getWidth()) / 2;
            int right0 = view.getWidth();
            int bottom0 = (view.getHeight() - view.getWidth()) / 2 + view.getWidth();
            outline.setOval(left0, top0, right0, bottom0);
        }
    };

}
