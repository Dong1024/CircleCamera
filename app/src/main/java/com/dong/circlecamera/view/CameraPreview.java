package com.dong.circlecamera.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by dong on 2018/5/23.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";

    private Camera mCamera;
    private SurfaceHolder mHolder;
    private Activity mContext;
    private CameraListener listener;
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private int displayDegree = 90;

    public CameraPreview(Activity context) {
        super(context);
        mContext = context;
        mCamera = Camera.open(cameraId);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCameraListener(CameraListener listener) {
        this.listener = listener;
    }

    /**
     * 拍照获取bitmap
     */
    public void captureImage() {
        try {
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    if (null != listener) {
                        Bitmap bitmap = rotateBitmap(BitmapFactory.decodeByteArray(data, 0, data.length),
                                displayDegree);
                        listener.onCaptured(bitmap);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (null != listener) {
                listener.onCaptured(null);
            }
        }
    }

    /**
     * 预览拍照
     */
    public void startPreview() {
        mCamera.startPreview();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != mCamera) {
            mCamera.autoFocus(null);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            startCamera(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            startCamera(mHolder);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void startCamera(SurfaceHolder holder) throws IOException {
        mCamera.setPreviewDisplay(holder);
        setCameraDisplayOrientation(mContext, cameraId, mCamera);

        Camera.Size preSize = getCameraSize();

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(preSize.width, preSize.height);
        parameters.setPictureSize(preSize.width, preSize.height);
        parameters.setJpegQuality(100);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    public Camera.Size getCameraSize() {
        if (null != mCamera) {
            Camera.Parameters parameters = mCamera.getParameters();
            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
            Camera.Size preSize = Util.getCloselyPreSize(true, metrics.widthPixels, metrics.heightPixels,
                    parameters.getSupportedPreviewSizes());
            return preSize;
        }
        return null;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }


    /**
     * Android API: Display Orientation Setting
     * Just change screen display orientation,
     * the rawFrame data never be changed.
     */
    private void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            displayDegree = (info.orientation + degrees) % 360;
            displayDegree = (360 - displayDegree) % 360;  // compensate the mirror
        } else {
            displayDegree = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(displayDegree);
    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    private Bitmap rotateBitmap(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 释放资源
     */
    public synchronized void releaseCamera() {
        try {
            if (null != mCamera) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();//停止预览
                mCamera.release(); // 释放相机资源
                mCamera = null;
            }
            if (null != mHolder) {
                mHolder.removeCallback(this);
                mHolder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}