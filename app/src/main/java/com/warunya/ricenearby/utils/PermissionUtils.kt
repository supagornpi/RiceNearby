package com.warunya.ricenearby.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import com.warunya.ricenearby.MyApplication

class PermissionUtils {

    companion object {
        val PERMISSION_READ_EXTERNAL_STORAGE_AND_CAMERA = 44
        val PERMISSION_READ_EXTERNAL_STORAGE = 33

        /** work in activity only **/
        fun isRequestPermissionReadExternalStorage(activity: Activity): Boolean {
            isRequestPermissionReadExternalStorage(activity, PERMISSION_READ_EXTERNAL_STORAGE)
            return true
        }

        /** work in activity only **/
        fun isRequestPermissionReadExternalStorage(activity: Activity, requestCode: Int): Boolean {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_READ_EXTERNAL_STORAGE)
                }
                return false
            }
            return true
        }

        /** work in activity only **/
        fun isRequestPermissionReadExternalStorageAndCamera(activity: Activity): Boolean {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA), PERMISSION_READ_EXTERNAL_STORAGE_AND_CAMERA)
                }
                return false
            }
            return true
        }

        fun isGrantAll(permissions: Array<String>): Boolean {
            var isGrantAll = true
            permissions.forEach {
                if (ActivityCompat.checkSelfPermission(MyApplication.applicationContext, it) != PackageManager.PERMISSION_GRANTED) {
                    isGrantAll = false
                }
            }
            return isGrantAll
        }
    }

}