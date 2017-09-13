package com.glumes.camerasample;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by glumes on 2017/9/13.
 */

public class CameraApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
