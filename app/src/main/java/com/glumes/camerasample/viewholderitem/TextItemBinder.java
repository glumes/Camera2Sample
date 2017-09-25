package com.glumes.camerasample.viewholderitem;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.glumes.camerasample.R;
import com.glumes.camerasample.databinding.TextLayoutBinding;
import com.glumes.databindingadapter.ViewHolderBinder;

/**
 * @author glumes
 */

public class TextItemBinder extends ViewHolderBinder<TextModel, TextViewHolder> {


    @Override
    public TextViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {

        TextLayoutBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.text_layout, viewGroup, false);

        return new TextViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TextViewHolder textViewHolder, TextModel textModel) {
        textViewHolder.onBind(textModel);
    }
}
