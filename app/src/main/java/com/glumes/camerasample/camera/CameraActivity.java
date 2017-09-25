package com.glumes.camerasample.camera;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;

import com.glumes.camerasample.R;
import com.glumes.camerasample.databinding.ActivityCameraBinding;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private ActivityCameraBinding mBinding;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private int mWidth;
    private int mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera);
        mSurfaceHolder = mBinding.surfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                }
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mBinding.surfaceView != null) {
            mWidth = mBinding.surfaceView.getWidth();
            mHeight = mBinding.surfaceView.getHeight();
        }
    }

    private void initCamera() {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPreviewSize(mWidth, mHeight);
                parameters.setPreviewFpsRange(4, 10);
                parameters.setPictureFormat(ImageFormat.JPEG);
                parameters.set("jpeg-quality", 90);

                parameters.setPictureSize(mWidth, mHeight);
                mCamera.setPreviewDisplay(mSurfaceHolder);

                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void takePicture() {
        mCamera.autoFocus(mAutoFocusCallback);
    }

    Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                camera.takePicture(new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {

                    }
                }, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {

                    }
                }, mPictureCallback);
            }
        }
    };

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            final Bitmap resource = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (resource != null) {
                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                final Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);
                if (bitmap != null) {
                    mCamera.stopPreview();
                    // TODO: 2017/9/26 display this bitmap into imageview
                }
            }
        }
    };
}
