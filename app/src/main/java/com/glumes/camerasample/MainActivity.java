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

        items.add(new TextModel("Camera Display On SurfaceView", Constants.SURFACEVIEW));
        items.add(new TextModel("Camera Display On TextureView", Constants.SURFACEVIEW));
        items.add(new TextModel("Use Camera", Constants.CAMERA_1));
    }
}
