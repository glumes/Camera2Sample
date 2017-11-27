package com.glumes.camerasample;

import android.os.Bundle;

import com.glumes.camerasample.base.BaseListActivity;
import com.glumes.camerasample.utils.Constants;
import com.glumes.camerasample.viewholderitem.TextModel;

public class SampleListActivity extends BaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mItems.add(new TextModel("安卓相机 1.0 API 接口", Constants.TITLE));
        mItems.add(new TextModel("相机 1.0 预览拍摄", Constants.CAMERA_1));
        mItems.add(new TextModel("", Constants.PLACEHOLDER));

        mItems.add(new TextModel("安卓相机 2.0 API 接口", Constants.TITLE));
        mItems.add(new TextModel("拍摄预览之 SurfaceView", Constants.SURFACE_VIEW));
        mItems.add(new TextModel("拍摄预览之 TextureView", Constants.TEXTURE_VIEW));
        mItems.add(new TextModel("视频录制", Constants.VIDEO_RECORDER));
        mItems.add(new TextModel("", Constants.PLACEHOLDER));

        mItems.add(new TextModel("滤镜篇", Constants.TITLE));

        mItems.add(new TextModel("", Constants.PLACEHOLDER));

        mItems.add(new TextModel("音视频信息", Constants.TITLE));
        mItems.add(new TextModel("MediaMetaDataRetriever 使用", Constants.MEDIA_METADATA_RETIREVER));


    }
}
