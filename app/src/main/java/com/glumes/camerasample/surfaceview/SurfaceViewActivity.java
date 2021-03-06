package com.glumes.camerasample.surfaceview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.glumes.camerasample.R;
import com.glumes.camerasample.display.DisplayActivity;
import com.glumes.camerasample.utils.Constants;
import com.glumes.camerasample.utils.FileUtil;
import com.glumes.camerasample.views.CircleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import timber.log.Timber;

public class SurfaceViewActivity extends AppCompatActivity implements View.OnClickListener {


    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    ///为了使照片竖直显示
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    private CircleButton mCircleButton;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private CameraManager cameraManager;

    private String mCameraId;
    private ImageReader imageReader;

    private CameraCaptureSession cameraCaptureSession;
    private CameraDevice cameraDevice;

    private Handler mainHandler;
    private Handler childHandler;
    private Context mContext;

    public static final String TAG = "SurfaceViewActivity";

    public String mOutputUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.android_camera_surface_view);
        mContext = this;
        initView();
    }

    private void initView() {
        mCircleButton = (CircleButton) findViewById(R.id.image_view);
        mCircleButton.setOnClickListener(this);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);

        surfaceView.setOnClickListener(this);
        surfaceView.setKeepScreenOn(true);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                initCamera2();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
    }

    private void initCamera2() {

        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());

        mainHandler = new Handler(getMainLooper());

        mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT;
        imageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1);

        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {

                Log.d(TAG, "onImageAvailable");

                Image image = reader.acquireNextImage();

                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);

                File tempOutputPic = FileUtil.makeTempFile(mContext, Constants.TEMP_FILE_DIR, Constants.TEMP_PIC_PREFIX, Constants.TEMP_PIC_EXTENSION);

                FileOutputStream output = null;

                try {
                    output = new FileOutputStream(tempOutputPic);
                    output.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    image.close();
                    if (null != output) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Timber.d("save image");
                }

                mOutputUri = Uri.fromFile(tempOutputPic).toString();

                Intent intent = new Intent(mContext, DisplayActivity.class);
                intent.putExtra(Constants.OUTPUT_PIC_URI, mOutputUri);

                startActivity(intent);

                Timber.d(mOutputUri);

            }
        }, mainHandler);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            cameraManager.openCamera(mCameraId, stateCallback, mainHandler);
        } catch (CameraAccessException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice device) {
            cameraDevice = device;

            takePreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice device) {
            if (cameraDevice != null) {
                cameraDevice.close();
                cameraDevice = null;
            }
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            Log.d(TAG, "open camera failed");
        }
    };


    private CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {

        @Override
        public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);

            Log.d(TAG, "onCaptureStarted");
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);

            Log.d(TAG, "onCaptureCompleted");

        }


        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            super.onCaptureProgressed(session, request, partialResult);

            Log.d(TAG, "onCaptureProgressed");

        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);

            Log.d(TAG, "onCaptureFailed");

        }

        @Override
        public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, int sequenceId, long frameNumber) {
            super.onCaptureSequenceCompleted(session, sequenceId, frameNumber);

            Log.d(TAG, "onCaptureSequenceCompleted");

        }
    };

    private void takePreview() {
        try {
            final CaptureRequest.Builder previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(surfaceHolder.getSurface());

            cameraDevice.createCaptureSession(Arrays.asList(surfaceHolder.getSurface(), imageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            if (cameraDevice == null)
                                return;
                            cameraCaptureSession = session;
                            try {
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                                CaptureRequest previewRequest = previewRequestBuilder.build();

                                cameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandler);

                            } catch (CameraAccessException e) {
                                Log.d(TAG, "open camera failed");
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                        }
                    }, childHandler);

        } catch (CameraAccessException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick");
        takePicture();
    }

    private void takePicture() {
        if (cameraDevice == null)
            return;
        CaptureRequest.Builder captureRequestBuilder;
        try {
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.addTarget(imageReader.getSurface());

            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

            int rotation = getWindowManager().getDefaultDisplay().getRotation();

            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            CaptureRequest captureRequest = captureRequestBuilder.build();

            cameraCaptureSession.capture(captureRequest, captureCallback, childHandler);
        } catch (CameraAccessException e) {
            Log.d(TAG, "open camera failed");
        }
    }
}
