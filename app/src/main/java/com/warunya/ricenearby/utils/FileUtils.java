package com.warunya.ricenearby.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.warunya.ricenearby.BuildConfig;
import com.warunya.ricenearby.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class FileUtils {

    private static final int targetW = 500;
    private static final int targetH = 500;

    public static boolean canUseImagePath(Intent data) {
        if (data.getData() == null) {
            return false;
        }
        String filePath = data.getData().toString();
        if (BuildConfig.DEBUG) {
            Log.e("file", "is " + filePath);
        }
        if (filePath.contains("gallery3d")) {
            return false;
        } else {
            return true;
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public static File getResizedBitmap(Activity activity, File file) {
        try {
            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            //inJustDecodeBounds = true <-- will not load the bitmap into memory
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getPath(), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            ExifInterface exif = null;
            exif = new ExifInterface(file.getPath());

            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);

            Matrix matrix = new Matrix();
            if (rotation != 0f) {
                matrix.preRotate(rotationInDegrees);
            }

            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), bmOptions);
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            // here i override the original image file
            File fileOutput = createNewFile(activity);
            FileOutputStream outputStream = new FileOutputStream(fileOutput);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            //refresh gallery
            scanFile(activity.getApplicationContext(), fileOutput.getPath());

            //delete old file
//            deleteFile(activity.getApplicationContext(), file);

            return fileOutput;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private static File createNewFile(Activity activity) {
        String imageFileName = "JPEG_" + new Date().getTime(); //make a better file name
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                activity.getResources().getString(R.string.app_name));

        File fileImage = null;
        try {
            if (!storageDir.exists()) {
                storageDir.mkdirs();
            }
            fileImage = File.createTempFile(imageFileName,
                    ".jpg",
                    storageDir
            );
            return fileImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void deleteFile(Context context, Uri uri) {
//        deleteFile(context, uri.getPath());
    }

    public static void deleteFile(Context context, String path) {
//        try {
//            File fdelete = new File(path);
//            if (fdelete.exists()) {
//                if (fdelete.delete()) {
//                    System.out.println("file Deleted :" + path);
//                    scanFile(context, path);
//                } else {
//                    System.out.println("file not Deleted :" + path);
//                }
//            }
//
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
    }

    public static void deleteFile(Context context, File file) {
//        deleteFile(context, file.getPath());
    }

    private static void scanFile(Context context, String file) {
        MediaScannerConnection.scanFile(context,
                new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }
}
