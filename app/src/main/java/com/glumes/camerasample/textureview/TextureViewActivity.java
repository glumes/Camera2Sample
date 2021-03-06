package com.glumes.camerasample.textureview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.glumes.camerasample.R;
import com.glumes.camerasample.display.DisplayActivity;
import com.glumes.camerasample.utils.Constants;
import com.glumes.camerasample.utils.FileUtil;
import com.glumes.camerasample.views.AutoFitTextureView;
import com.glumes.camerasample.views.CircleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class TextureViewActivity extends AppCompatActivity implements View.OnClickListener {

    //    @BindView(R.id.textureView)
    AutoFitTextureView textureView;
    //    @BindView(R.id.image_view)
    CircleButton takePicture;


    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    ///为了使照片竖直显示
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private static final int REQUEST_CAMERA_PERMISSION = 200;


    private CameraDevice mCameraDevice;
    private String cameraId;

    private Size imageDimension;

    protected CaptureRequest.Builder captureRequestBuilder;
    protected CameraCaptureSession cameraCaptureSessions;

    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;

    private Handler mMainHandler;

    private Context mContext;
    public String mOutputUri;

    private SurfaceTexture mSurfaceTexture;

    private ImageReader mImageReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_texture_view);
        mContext = this;

        textureView = (AutoFitTextureView) findViewById(R.id.textureView);

        textureView.setVisibility(View.GONE);
        textureView.setVisibility(View.VISIBLE);

        takePicture = (CircleButton) findViewById(R.id.image_view);
        takePicture.setOnClickListener(this);


    }



    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("onResume");


        startBackgroundThread();

        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeCamera();

        stopBackgroundThread();
    }


    private void closeCamera() {

        if (cameraCaptureSessions != null) {
            cameraCaptureSessions.close();
        }
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (null != mImageReader) {
            mImageReader.close();
            mImageReader = null;
        }
    }

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * TextureView 的回调，当可用时，打开摄像头开始预览
     */
    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            Timber.d("width:" + width + "  height:" + height);
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            if (mCameraDevice != null) {
                mCameraDevice.close();
            }
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    private void openCamera() {


        mMainHandler = new Handler(getMainLooper());
        initImageReader();

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = manager.getCameraIdList()[0];

            // 初始化 imageDimension 也就是图片的尺寸，在预览时用到

            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TextureViewActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, mStateCallback, mMainHandler);
        } catch (CameraAccessException e) {
            Timber.e(e.getMessage(), e);
        }
    }

    /**
     * 当相机打开时回调，响应相机的一系列状态事件
     */
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            if (mCameraDevice != null) {
                mCameraDevice.close();
            }
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {

        }
    };


    private void createCameraPreview() {
        try {

            mSurfaceTexture = textureView.getSurfaceTexture();
            mSurfaceTexture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(mSurfaceTexture);

            // 创建一个 CaptureRequest 还有不同的类型可选
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (mCameraDevice == null) {
                        return;
                    }
                    cameraCaptureSessions = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Timber.d("onConfigureFailed failed");
                }
            }, mBackgroundHandler);

        } catch (CameraAccessException e) {
            Timber.e(e.getMessage(), e);
        }
    }

    private void updatePreview() {
        if (mCameraDevice == null) {
            return;
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            Timber.e(e.getMessage(), e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Timber.d("permissions denied");
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        takePicture();
    }


    private void takePicture() {


        try {
            final CaptureRequest.Builder builder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            builder.addTarget(mImageReader.getSurface());

//            builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();

            builder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

//            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
//            outputSurfaces.add(mImageReader.getSurface());

//            mCameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
//
//                @Override
//                public void onConfigured(@NonNull CameraCaptureSession session) {
//                    try {
//                        session.capture(builder.build(), null, mBackgroundHandler);
//                    } catch (CameraAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
//
//                }
//            }, mBackgroundHandler);

            cameraCaptureSessions.capture(builder.build(), null, mBackgroundHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private void initImageReader() {
        mImageReader = ImageReader.newInstance(1920, 1080, ImageFormat.JPEG, 1);

        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                Timber.d("onImageAvailable do Action");
                Image image = imageReader.acquireNextImage();

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
        }, mMainHandler);
    }
}
