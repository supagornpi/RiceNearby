package com.warunya.ricenearby.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class DialogAlert {

    public static void show(Activity activity, int title, int message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(activity.getResources().getString(title));
        dialog.setMessage(activity.getResources().getString(message));
        dialog.setPositiveButton(activity.getResources().getString(android.R.string.ok), listener);
        dialog.setNegativeButton(activity.getString(android.R.string.cancel), null);
        dialog.show();
    }

    public static void show(Activity activity, int message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage(activity.getResources().getString(message));
        dialog.setPositiveButton(activity.getResources().getString(android.R.string.ok), listener);
        dialog.setNegativeButton(activity.getString(android.R.string.cancel), null);
        dialog.show();
    }

    public static void show(Activity activity, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage(message);
        dialog.setPositiveButton(activity.getResources().getString(android.R.string.ok), listener);
        dialog.setNegativeButton(activity.getString(android.R.string.cancel), null);
        dialog.show();
    }

    public static void show(Activity activity, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage(message);
        dialog.setPositiveButton(activity.getResources().getString(android.R.string.ok), null);
        dialog.show();
    }

    public static void show(Activity activity, int message) {
        show(activity, activity.getResources().getString(message));
    }

    public static void showOnlyPossitive(Activity activity, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage(message);
        dialog.setPositiveButton(activity.getResources().getString(android.R.string.ok), listener);
        dialog.show();
    }
}
