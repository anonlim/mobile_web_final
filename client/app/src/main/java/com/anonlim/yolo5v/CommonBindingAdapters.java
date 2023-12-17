package com.anonlim.yolo5v;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class CommonBindingAdapters {

    @BindingAdapter("app:imageUrl")
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.ic_wait)
                .error(R.drawable.ic_wait)
                .into(imageView);
    }
}