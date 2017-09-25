package com.glumes.camerasample.camera;

import android.databinding.DataBindingUtil;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;

import com.glumes.camerasample.R;
import com.glumes.camerasample.databinding.ActivityCameraBinding;

public class CameraActivity extends AppCompatActivity {

    ActivityCameraBinding mBinding;
    SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.camera_layout);
        mSurfaceHolder = mBinding.surfaceView.getHolder();
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

            }
        });
    }

    private void initCamera() {
        mCamera = Camera.open();
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();

        }
    }
}
