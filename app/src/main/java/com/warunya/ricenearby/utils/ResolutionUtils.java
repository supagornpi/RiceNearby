package com.warunya.ricenearby.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.warunya.ricenearby.R;

public class ResolutionUtils {

    public static float dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }

    public static float px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale + 0.5f);
    }

    /**
     * @return Array of screen size index 0 = width, 1 = height
     */
    public static int[] getScreenSize(Context context) {
        int[] screenSize = new int[2];
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowmanager != null) {
            windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
            screenSize[0] = displayMetrics.widthPixels;
            screenSize[1] = displayMetrics.heightPixels;
        }

        return screenSize;
    }

    public static int getBannerHeightFromRatio(Context context) {
        Float ratio = 7.0f / 15.0f;
        Float properWith = (float) ResolutionUtils.getScreenSize(context)[0];
        Float properHeight = properWith * ratio;
        return properHeight.intValue();
    }

    public static void setViewSize(Context context, float scaleWidth, float scaleHeight, View view) {
        //set image size
        Resources resources = context.getResources();
        int screenWidth = ResolutionUtils.getScreenSize(context)[0];
        int viewMargin = resources.getDimensionPixelOffset(R.dimen.padding_small) * 2;
        int viewWidth = ((int) Math.floor(screenWidth / scaleWidth)) - viewMargin;
        int viewHeight = ((int) Math.floor(screenWidth / scaleHeight) - viewMargin);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = viewWidth;
        layoutParams.height = viewHeight;
        view.setLayoutParams(layoutParams);
    }

    public static void setViewWidth(Context context, float scaleWidth, View view) {
        //set image size
        Resources resources = context.getResources();
        int screenWidth = ResolutionUtils.getScreenSize(context)[0];
        int viewMargin = resources.getDimensionPixelOffset(R.dimen.padding_small) * 2;
        int viewWidth = ((int) Math.floor(screenWidth / scaleWidth)) - viewMargin;

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = viewWidth;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        view.setLayoutParams(layoutParams);
    }
}