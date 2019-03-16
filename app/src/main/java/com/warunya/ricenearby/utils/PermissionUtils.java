package com.warunya.ricenearby.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.warunya.ricenearby.MyApplication;

public class PermissionUtils {

    public static final int PERMISSION_READ_EXTERNAL_STORAGE = 33;
    public static final int PERMISSION_LOCATION = 77;

    /**
     * work in activity only
     **/
    public static boolean isRequestPermissionReadExternalStorage(Activity activity, int requestCode) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_READ_EXTERNAL_STORAGE);
            }
            return false;
        }
        return true;
    }

    /**
     * work in activity only
     **/
    public static boolean isRequestPermissionLocation(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
            }
            return false;
        }
        return true;
    }

    public static boolean isGrantAll(String[] permissions) {
        boolean isGrantAll = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(MyApplication.applicationContext, permission) != PackageManager.PERMISSION_GRANTED) {
                isGrantAll = false;
            }
        }
        return isGrantAll;
    }
}
