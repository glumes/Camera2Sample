package com.glumes.camerasample.filter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.glumes.camerasample.R;

public class BaseFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_filter);
    }
}
