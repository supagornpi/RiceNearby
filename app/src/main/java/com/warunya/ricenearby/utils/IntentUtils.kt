package com.warunya.ricenearby.utils


import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object IntentUtils {

    fun startIntentGallery(activity: Activity, requestCode: Int) {
        if (PermissionUtils.isRequestPermissionReadExternalStorage(activity)) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            activity.startActivityForResult(Intent.createChooser(intent, "Select your image"), requestCode)
        }
    }
}
