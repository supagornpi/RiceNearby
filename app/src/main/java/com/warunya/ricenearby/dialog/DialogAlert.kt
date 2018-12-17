package com.warunya.ricenearby.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface

class DialogAlert {

    companion object {
        fun show(activity: Activity, title: Int, message: Int, listener: DialogInterface.OnClickListener) {
            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle(activity.resources.getString(title))
            dialog.setMessage(activity.resources.getString(message))
            dialog.setPositiveButton(activity.resources.getString(android.R.string.ok), listener)
            dialog.setNegativeButton(activity.getString(android.R.string.cancel), null)
            dialog.show()
        }

        fun show(activity: Activity, message: Int, listener: DialogInterface.OnClickListener) {
            val dialog = AlertDialog.Builder(activity)
            dialog.setMessage(activity.resources.getString(message))
            dialog.setPositiveButton(activity.resources.getString(android.R.string.ok), listener)
            dialog.setNegativeButton(activity.getString(android.R.string.cancel), null)
            dialog.show()
        }

        fun show(activity: Activity, message: String, listener: DialogInterface.OnClickListener) {
            val dialog = AlertDialog.Builder(activity)
            dialog.setMessage(message)
            dialog.setPositiveButton(activity.resources.getString(android.R.string.ok), listener)
            dialog.setNegativeButton(activity.getString(android.R.string.cancel), null)
            dialog.show()
        }

        fun show(activity: Activity, message: String) {
            val dialog = AlertDialog.Builder(activity)
            dialog.setMessage(message)
            dialog.setPositiveButton(activity.resources.getString(android.R.string.ok), null)
            dialog.show()
        }

        fun show(activity: Activity, message: Int) {
            show(activity, activity.resources.getString(message))
        }
    }
}
