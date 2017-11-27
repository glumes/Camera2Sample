package com.glumes.camerasample.eventhandler;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.glumes.camerasample.camerav1.CameraActivity;
import com.glumes.camerasample.metadata.RetrieverDataActivity;
import com.glumes.camerasample.surfaceview.SurfaceViewActivity;
import com.glumes.camerasample.textureview.TextureViewActivity;
import com.glumes.camerasample.textureview.VideoRecorderActivity;
import com.glumes.camerasample.utils.Constants;
import com.glumes.camerasample.viewholderitem.TextModel;

/**
 * @Author glumes
 */

public class EventHandler {

    public void onClick(View v, TextModel model) {
        switch (model.type) {
            case Constants.SURFACE_VIEW:
                startActivity(v.getContext(), SurfaceViewActivity.class);
                break;
            case Constants.TEXTURE_VIEW:
                startActivity(v.getContext(), TextureViewActivity.class);
                break;
            case Constants.CAMERA_1:
                startActivity(v.getContext(), CameraActivity.class);
                break;
            case Constants.VIDEO_RECORDER:
                startActivity(v.getContext(), VideoRecorderActivity.class);
                break;
            case Constants.MEDIA_METADATA_RETIREVER:
                startActivity(v.getContext(), RetrieverDataActivity.class);
                break;
            default:
                break;
        }
    }


    private void startActivity(Context context, Class<?> clazz) {
        context.startActivity(new Intent(context, clazz));
    }
}
