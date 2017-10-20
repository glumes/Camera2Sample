package com.glumes.camerasample.utils;

import android.os.Environment;

import java.io.File;

/**
 * @author glumes
 */

public class Constants {


    public static final int TITLE = 0;

    public static final int PLACEHOLDER = 1;
    public static final int SURFACEVIEW = 2;
    public static final int TEXTUREVIEW = 3;
    public static final int CAMERA_1 = 4;

    public static final int MEDIA_RECODER = 5;

    public static final String TEMP_FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "CameraSample";

    public static final String TMEP_PIC_PREFIX = "IMG";

    public static final String TEMP_PIC_EXTENSION = ".jpg";

    public static final String TEMP_VIDEO_PREFIX = "VID";

    public static final String TEMP_VIDEO_EXTENSION = ".mp4";


    public static final String OUTPUT_PIC_URI = "output_pic_uri";
}
