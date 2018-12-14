package com.warunya.ricenearby.utils;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public class DismissKeyboardListener implements View.OnTouchListener {

    Activity mAct;

    /**
     * example
     * root.setOnClickListener(new DismissKeyboardListener(getActivity()));
     **/
    public DismissKeyboardListener(Activity activity) {
        this.mAct = activity;
    }

//    @Override
//    public void onClick(View v) {
//        if (v instanceof ViewGroup) {
//            hideSoftKeyboard(this.mAct);
//        }
//    }

    public void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view instanceof ViewGroup) {
            hideSoftKeyboard(this.mAct);
        }
        return false;
    }
}
