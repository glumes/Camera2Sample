package com.glumes.camerasample.metadata;

import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.glumes.camerasample.R;
import com.glumes.camerasample.databinding.ActivityRetrieverDataBinding;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;

public class RetrieverDataActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnPreparedListener {


    public static final String AUDIO_FILE = Environment.getExternalStorageDirectory() + File.separator + "audio.mp3";
    public static final String VIDEO_FILE = Environment.getExternalStorageDirectory() + File.separator + "video.mp4";

    private ActivityRetrieverDataBinding mBinding;

    MediaMetadataRetriever metadataRetriever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_retriever_data);
        mBinding.getAudioInfo.setOnClickListener(this);

        mBinding.getVideoInfo.setOnClickListener(this);

        metadataRetriever = new MediaMetadataRetriever();

        mBinding.frameSeekbar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getAudioInfo:
                showAudioInfo();
                break;
            case R.id.getVideoInfo:
                showVideoInfo();
                break;
            default:
                break;
        }
    }

    private void showVideoInfo() {
        if (!isFileExist(AUDIO_FILE)) {
            Toast.makeText(this, "视频文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        metadataRetriever.setDataSource(VIDEO_FILE);

        mBinding.videoFrame.setImageBitmap(metadataRetriever.getFrameAtTime(30315, MediaMetadataRetriever.OPTION_NEXT_SYNC));


        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(VIDEO_FILE);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showAudioInfo() {
        if (!isFileExist(AUDIO_FILE)) {
            Toast.makeText(this, "音频文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        mBinding.showAudioInfo.setText(getAudioInfo());
    }

    private boolean isFileExist(String path) {
        return new File(path).exists();
    }


    private String getAudioInfo() {
        StringBuilder sb = new StringBuilder();

        metadataRetriever.setDataSource(AUDIO_FILE);

        sb.append("KEY_TITLE : \n");
        sb.append(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        sb.append("\n");

        sb.append("METADATA_KEY_ALBUM : \n");
        sb.append(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        sb.append("\n");

        sb.append("METADATA_KEY_ARTIST : \n");
        sb.append(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        sb.append("\n");

        sb.append("METADATA_KEY_COMPOSER : \n");
        sb.append(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER));
        sb.append("\n");

        sb.append("METADATA_KEY_DATE : \n");
        sb.append(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE));
        sb.append("\n");

        sb.append("METADATA_KEY_YEAR : \n");
        sb.append(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR));
        sb.append("\n");

        sb.append("METADATA_KEY_DURATION : \n");
        sb.append(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        sb.append("\n");

        sb.append("METADATA_KEY_NUM_TRACKS : \n");
        sb.append(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS));
        sb.append("\n");

        sb.append("METADATA_KEY_WRITER : \n");
        sb.append(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER));
        sb.append("\n");

        sb.append("METADATA_KEY_MIMETYPE : \n");
        sb.append(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE));
        sb.append("\n");

        sb.append("METADATA_KEY_BITRATE : \n");
        sb.append(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
        sb.append("\n");


        byte[] imageData = metadataRetriever.getEmbeddedPicture();

        try {
            mBinding.audioCover.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
        } catch (Exception e) {
        }

        return sb.toString();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 要乘以 1000 才行
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mBinding.videoFrame.setImageBitmap(metadataRetriever.getFrameAtTime(seekBar.getProgress() * 1000, MediaMetadataRetriever.OPTION_NEXT_SYNC));
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mBinding.frameSeekbar.setMax(mp.getDuration());
    }
}
