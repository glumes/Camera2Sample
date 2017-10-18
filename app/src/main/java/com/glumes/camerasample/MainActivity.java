package com.glumes.camerasample;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.glumes.camerasample.databinding.ActivitySampleBinding;
import com.glumes.camerasample.utils.Constants;
import com.glumes.camerasample.viewholderitem.TextItemBinder;
import com.glumes.camerasample.viewholderitem.TextModel;
import com.glumes.databindingadapter.DataBindingAdapter;
import com.glumes.databindingadapter.Items;

public class MainActivity extends AppCompatActivity {

    ActivitySampleBinding mSampleBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSampleBinding = DataBindingUtil.setContentView(this, R.layout.activity_sample);
        mSampleBinding.recyclerView.setHasFixedSize(true);
        mSampleBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Items items = new Items();


        DataBindingAdapter mAdapter = new DataBindingAdapter();

        mAdapter.map(TextModel.class, new TextItemBinder()).setItems(items);

        mSampleBinding.recyclerView.setAdapter(mAdapter);

        items.add(new TextModel("安卓相机 1.0 API 接口", Constants.TITLE));
        items.add(new TextModel("相机 1.0 预览拍摄", Constants.CAMERA_1));
        items.add(new TextModel("", Constants.PLACEHOLDER));

        items.add(new TextModel("安卓相机 2.0 API 接口", Constants.TITLE));
        items.add(new TextModel("相机 2.0 预览拍摄之 SurfaceView", Constants.SURFACEVIEW));
        items.add(new TextModel("相机 2.0 预览拍摄之 TextureView", Constants.TEXTUREVIEW));
    }
}
