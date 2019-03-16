package com.warunya.ricenearby.utils;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

public class IntentUtils {

    public static void startIntentGallery(Activity activity, int requestCode) {
        if (PermissionUtils.isRequestPermissionReadExternalStorage(activity, requestCode)) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            activity.startActivityForResult(Intent.createChooser(intent, "Select your image"), requestCode);
        }
    }
}
