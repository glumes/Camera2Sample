package com.glumes.camerasample.utils;

import android.os.Environment;

import java.io.File;

/**
 * @author glumes
 */

public class Constants {


    public static final int TITLE = 0;

    public static final int PLACEHOLDER = 1;
    public static final int SURFACE_VIEW = 2;
    public static final int TEXTURE_VIEW = 3;
    public static final int CAMERA_1 = 4;

    public static final int VIDEO_RECORDER = 5;

    public static final String TEMP_FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "CameraSample";

    public static final String TEMP_PIC_PREFIX = "IMG";

    public static final String TEMP_PIC_EXTENSION = ".jpg";

    public static final String TEMP_VIDEO_PREFIX = "VID";

    public static final String TEMP_VIDEO_EXTENSION = ".mp4";


    public static final String OUTPUT_PIC_URI = "output_pic_uri";
}
