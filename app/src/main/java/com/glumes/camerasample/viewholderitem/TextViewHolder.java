package com.glumes.camerasample.viewholderitem;

import android.content.Intent;
import android.view.View;

import com.glumes.camerasample.camera.CameraActivity;
import com.glumes.camerasample.surfaceview.SurfaceViewActivity;
import com.glumes.camerasample.textureview.TextureViewActivity;
import com.glumes.camerasample.databinding.TextLayoutBinding;
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
    protected void onBind(final TextModel textModel) {
        mBinding.setViewmodel(textModel);
        mBinding.executePendingBindings();

        mBinding.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                switch (textModel.type) {
                    case Constants.SURFACEVIEW:
                        intent.setClass(v.getContext(), SurfaceViewActivity.class);
                        break;
                    case Constants.TEXTUREVIEW:
                        intent.setClass(v.getContext(), TextureViewActivity.class);
                        break;
                    case Constants.CAMERA_1:
                        intent.setClass(v.getContext(), CameraActivity.class);
                        break;
                    default:
                        break;
                }
                v.getContext().startActivity(intent);
            }
        });
    }
}
