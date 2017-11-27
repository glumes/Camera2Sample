package com.glumes.camerasample.utils;

import android.os.Environment;

import java.io.File;

/**
 * @author glumes
 */

public class Constants {

    //通用
    public static final int TITLE = 0x00;
    public static final int PLACEHOLDER = 0x01;

    // 相机 1.0 API 接口
    public static final int CAMERA_1 = 0x10;

    // 相机 2.0 API 接口
    public static final int SURFACE_VIEW = 0x20;
    public static final int TEXTURE_VIEW = 0x21;
    public static final int VIDEO_RECORDER = 0x22;


    // 音视频信息获取
    public static final int MEDIA_METADATA_RETIREVER = 0x30;

    // 滤镜
    public static final int FILTER_BLACK_WHITE = 0x40;



    // 生成的图片、音频、视频临时文件
    public static final String TEMP_FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "CameraSample";

    public static final String TEMP_PIC_PREFIX = "IMG";

    public static final String TEMP_PIC_EXTENSION = ".jpg";

    public static final String TEMP_VIDEO_PREFIX = "VID";

    public static final String TEMP_VIDEO_EXTENSION = ".mp4";

    public static final String OUTPUT_PIC_URI = "output_pic_uri";
}
