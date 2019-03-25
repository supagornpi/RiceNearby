package com.warunya.ricenearby.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.warunya.ricenearby.MyApplication;
import com.warunya.ricenearby.R;

public class GlideLoader {

    public static void loadDefaultImage(ImageView imageView) {
        imageView.setImageResource(R.drawable.logo);
    }

    public static void load(String url, ImageView imageView) {
        load(url, new RequestOptions().fitCenter(), imageView);
    }

    public static void load(String url, RequestOptions requestOptions, ImageView imageView) {
        if (url == null || url.isEmpty()) {
            loadDefaultImage(imageView);
        } else {
            Glide.with(MyApplication.applicationContext)
                    .load(url)
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    public static void load(Context context, int image, ImageView imageView) {
        Glide.with(context)
                .load(image)
                .into(imageView);
    }

    public static void load(Uri uri, ImageView imageView) {
        Glide.with(MyApplication.applicationContext)
                .load(uri)
                .apply(new RequestOptions().error(R.drawable.logo))
                .into(imageView);
    }

    public static void loadImageCircle(String url, ImageView imageView) {
        Glide.with(MyApplication.applicationContext)
                .load(url)
                .apply(new RequestOptions().fitCenter().circleCrop().error(R.drawable.logo))
                .into(imageView);
    }

    public static void loadImageCircle(Uri uri, ImageView imageView) {
        Glide.with(MyApplication.applicationContext)
                .load(uri)
                .apply(new RequestOptions().fitCenter().circleCrop().error(R.drawable.logo))
                .into(imageView);
    }

    public static void loadImageCircle(int imageId, ImageView imageView) {
        Glide.with(MyApplication.applicationContext)
                .load(imageId)
                .apply(new RequestOptions().fitCenter().circleCrop())
                .into(imageView);
    }

    public static void loadImageCircle(Context context, String url, ImageView imageView) {
        if (url == null || url.isEmpty()) {
            loadDefaultImage(imageView);
        } else {
            Glide.with(context)
                    .load(url)
                    .apply(new RequestOptions().fitCenter().circleCrop())
                    .into(imageView);
        }
    }
}
