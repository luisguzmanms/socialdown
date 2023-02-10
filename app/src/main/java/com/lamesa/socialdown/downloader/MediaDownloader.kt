package com.lamesa.socialdown.downloader

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.webkit.URLUtil
import com.coolerfall.download.DownloadRequest
import com.lamesa.socialdown.domain.model.api.ModelMediaDataExtracted
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.downloader.DownloaderHelper.ExtensionFile
import com.lamesa.socialdown.utils.DialogXUtils
import com.lamesa.socialdown.utils.SocialHelper
import com.lamesa.socialdown.utils.SocialHelper.MediaType.*
import com.lamesa.socialdown.utils.SocialHelper.checkExtensionFile
import com.lamesa.socialdown.utils.timeNow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.*


/** Created by luis Mesa on 09/08/22 */
class MediaDownloader(private val context: Context) {

    private val TAG = "MediaDownloader"

    private var downloadRequest: DownloadRequest? = null
    private var downloadManager: com.coolerfall.download.DownloadManager? = null
    private var notificationDownloading: NotificationDownloading? = null
    private lateinit var downloaderHelper: DownloaderHelper
    private lateinit var mediaDownloaded: ModelMediaDownloaded

    internal fun downloadMedia(linkToDownload: String, dataExtracted: ModelMediaDataExtracted) {

        downloaderHelper = DownloaderHelper(context)

        val nameFile = SocialHelper.randomString()
        val extensionFile = when (dataExtracted.mediaType) {
            POST.type -> "." + checkExtensionFile(linkToDownload).toString().lowercase()
            IMAGE.type -> "." + ExtensionFile.JPG.toString().lowercase()
            STORY.type -> "." + checkExtensionFile(linkToDownload).toString().lowercase()
            else -> "." + ExtensionFile.MP4.toString().lowercase()
        }

        val mediaFile = "$nameFile$extensionFile"
        val folderPatch = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "/SocialDown"
        )

        val filePatch = "$folderPatch/$mediaFile"
        val mediaType = dataExtracted.mediaType

        // Comprobar permisos
        if (!PermissionsUtil.checkWriteStoragePermission(context as Activity)) {
            DialogXUtils.Policies.dialogPermissions(context)
        } else {
            if (!folderPatch.exists()) {
                folderPatch.mkdirs()
            }
            // Datos para guardar en Room
            val numRandom = Random().nextInt(9999)
            mediaDownloaded = ModelMediaDownloaded(
                numRandom,
                mediaType!!,
                dataExtracted.app.toString(),
                nameFile,
                folderPatch.toString(),
                filePatch,
                dataExtracted.queryLink!!,
                Date().timeNow()
            )
            if (URLUtil.isNetworkUrl(linkToDownload)) {
                try {
                    downloaderHelper.initSaveMedia(mediaDownloaded)
                    notificationDownloading = NotificationDownloading(context)
                    downloadManager = downloaderHelper.createDownloadManager()
                    downloadRequest =
                        downloaderHelper.createDownloadRequest(linkToDownload, mediaDownloaded)
                    downloadManager!!.add(downloadRequest)
                } catch (e: IOException) {
                    CoroutineScope(Dispatchers.Main).launch {
                        DialogXUtils.LoadingX.hideLoading()
                        DialogXUtils.NotificationX.showError("Please try again. ${e.message}")
                            .showLong()
                    }
                }
            }
        }
    }
}




