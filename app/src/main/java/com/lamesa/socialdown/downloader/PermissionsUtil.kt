package com.lamesa.socialdown.downloader

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionsUtil {

    const val REQUEST_WRITE_STORAGE = 112
    const val REQUEST_READ_MEDIA_IMAGES = 113
    const val REQUEST_READ_MEDIA_VIDEO = 114
    const val REQUEST_READ_MEDIA_AUDIO = 115

    fun checkWriteStoragePermission(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val permissionImage = ContextCompat.checkSelfPermission(activity, "android.permission.READ_MEDIA_IMAGES")
            val permissionVideo = ContextCompat.checkSelfPermission(activity, "android.permission.READ_MEDIA_VIDEO")
            val permissionAudio = ContextCompat.checkSelfPermission(activity, "android.permission.READ_MEDIA_AUDIO")
            return permissionImage == PackageManager.PERMISSION_GRANTED &&
                    permissionVideo == PackageManager.PERMISSION_GRANTED &&
                    permissionAudio == PackageManager.PERMISSION_GRANTED
        } else {
            val permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return permission == PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestWriteStoragePermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    "android.permission.READ_MEDIA_IMAGES",
                    "android.permission.READ_MEDIA_VIDEO",
                    "android.permission.READ_MEDIA_AUDIO"
                ),
                REQUEST_WRITE_STORAGE
            )
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_STORAGE
            )
        }
    }
}





