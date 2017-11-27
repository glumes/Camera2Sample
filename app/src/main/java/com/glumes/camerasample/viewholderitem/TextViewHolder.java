package com.glumes.camerasample.viewholderitem;

import android.graphics.Color;

import com.glumes.camerasample.databinding.TextLayoutBinding;
import com.glumes.camerasample.eventhandler.EventHandler;
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
        mBinding.setHandler(new EventHandler());
        mBinding.executePendingBindings();

        if (textModel.type == Constants.TITLE) {
            mBinding.text.setTextColor(Color.BLUE);
        }

    }


}
