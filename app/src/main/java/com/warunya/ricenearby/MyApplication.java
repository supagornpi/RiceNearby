package com.warunya.ricenearby;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.onesignal.OneSignal;

public class MyApplication extends Application {

    public static Context applicationContext;
//    private static MyApplication instance;
//
//    public static MyApplication getInstance() {
//        if (instance == null) {
//            instance = new MyApplication();
//        }
//        return instance;
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        FacebookSdk.sdkInitialize(this);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}
