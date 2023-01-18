package com.lamesa.socialdown.downloader

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionsUtil {

    const val REQUEST_WRITE_STORAGE = 112

    fun checkWriteStoragePermission(activity: Activity): Boolean {
        val permission =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    fun requestWriteStoragePermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_WRITE_STORAGE
        )
    }

}



