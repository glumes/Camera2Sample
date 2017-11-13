package com.glumes.camerasample.display;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;

import com.glumes.camerasample.R;
import com.glumes.camerasample.databinding.ActivityDisplayBinding;
import com.glumes.camerasample.utils.Constants;
import com.glumes.camerasample.utils.ImageUtil;


public class DisplayActivity extends AppCompatActivity {


    ActivityDisplayBinding mBinding;

    private String mOutputUri;

    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_display);

        mOutputUri = getIntent().getStringExtra(Constants.OUTPUT_PIC_URI);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.display.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        showPic();
                        mBinding.display.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
    }


    public void showPic() {

        int width = mBinding.display.getMeasuredWidth();
        int height = mBinding.display.getMeasuredHeight();

        if (mBitmap == null) {
            mBitmap = ImageUtil.getRotatedBitmap(Uri.parse(mOutputUri).getPath(), width, height);
        }

        if (mBinding != null) {
            mBinding.display.setImageBitmap(mBitmap);
        }


    }

}
