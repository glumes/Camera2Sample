package com.glumes.camerasample.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.glumes.camerasample.R;
import com.glumes.camerasample.databinding.ActivityBaseListBinding;
import com.glumes.camerasample.viewholderitem.TextItemBinder;
import com.glumes.camerasample.viewholderitem.TextModel;
import com.glumes.databindingadapter.DataBindingAdapter;
import com.glumes.databindingadapter.Items;

public class BaseListActivity extends AppCompatActivity {

    protected ActivityBaseListBinding mBinding;
    protected DataBindingAdapter mBindingAdapter;
    protected Items mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_list);
        mBinding.typeRecyclerView.setHasFixedSize(true);
        mBinding.typeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBindingAdapter = new DataBindingAdapter();

        mItems = new Items();
        mBindingAdapter
                .map(TextModel.class, new TextItemBinder())
                .setItems(mItems);

        mBinding.typeRecyclerView.setAdapter(mBindingAdapter);
    }
}
