package com.glumes.camerasample.viewholderitem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.glumes.camerasample.textureview.AndroidCameraApi;
import com.glumes.camerasample.camera.CameraActivity;
import com.glumes.camerasample.surfaceview.SurfaceViewActivity;
import com.glumes.camerasample.databinding.TextLayoutBinding;
import com.glumes.camerasample.textureview.TextureViewActivity;
import com.glumes.camerasample.utils.Constants;
import com.glumes.databindingadapter.BindingViewHolder;

/**
 * @author glumes
 */

public class TextViewHolder extends BindingViewHolder<TextModel, TextLayoutBinding> {

    public TextViewHolder(TextLayoutBinding binding) {
        super(binding);
    }

    @Override
    protected void onBind(final TextModel textModel, int i) {
        mBinding.setViewmodel(textModel);
        mBinding.executePendingBindings();


        if (textModel.type == Constants.TITLE) {
            mBinding.text.setTextColor(Color.BLUE);
        }

        mBinding.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (textModel.type) {
                    case Constants.SURFACEVIEW:
                        startActivity(v.getContext(), SurfaceViewActivity.class);
                        break;
                    case Constants.TEXTUREVIEW:
                        startActivity(v.getContext(), TextureViewActivity.class);
                        break;
                    case Constants.CAMERA_1:
                        startActivity(v.getContext(), CameraActivity.class);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void startActivity(Context context, Class<?> clazz) {
        context.startActivity(new Intent(context, clazz));
    }
}
