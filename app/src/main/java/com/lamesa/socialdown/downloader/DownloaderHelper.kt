package com.lamesa.socialdown.downloader

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.coolerfall.download.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.kongzue.dialogx.dialogs.BottomDialog
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback
import com.kongzue.dialogx.interfaces.OnBindView
import com.lamesa.socialdown.R
import com.lamesa.socialdown.adapter.PostSlideAdapter
import com.lamesa.socialdown.data.remote.APIHelper
import com.lamesa.socialdown.domain.model.api.ModelMediaDataExtracted
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.usecase.AddMediaUseCase
import com.lamesa.socialdown.utils.Constansts.Analytics.EventTimingDownloadFile
import com.lamesa.socialdown.utils.DialogXUtils
import com.lamesa.socialdown.utils.DialogXUtils.ToastX
import com.lamesa.socialdown.utils.SDAd
import com.lamesa.socialdown.utils.SDAnalytics
import com.lamesa.socialdown.utils.SocialHelper.AppDownloader.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.relex.circleindicator.CircleIndicator
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit


/** Created by luis Mesa on 09/08/22 */
class DownloaderHelper(private val context: Context) {
    enum class ExtensionFile { MP4, JPG }

    private val TAG = "DownloaderHelper"
    private var dialogDownloading: BottomDialog? = dialogDownloading()
    private var notificationDownloading = NotificationDownloading(context)
    private lateinit var mediaDownloaded: ModelMediaDownloaded

    internal fun initSaveMedia(media: ModelMediaDownloaded) {
        mediaDownloaded = media
    }

    private fun saveMediaRoom() {
        if (::mediaDownloaded.isInitialized) {
            if (File(mediaDownloaded.filePatch).exists()) {
                CoroutineScope(Dispatchers.IO).launch {
                    AddMediaUseCase().invoke(mediaDownloaded)
                }
            }
        }
    }

    internal fun createDownloadManager(): DownloadManager? {
        val client: OkHttpClient = OkHttpClient.Builder()
            .build()

        return DownloadManager.Builder().context(context)
            .downloader(OkHttpDownloader.create(client))
            .threadPoolSize(3)
            .logger { message -> Log.d(TAG, message) }
            .build()
    }

    internal fun createDownloadRequest(
        url: String?,
        dataFileToDownload: ModelMediaDownloaded
    ): DownloadRequest? {
        return DownloadRequest.Builder()
            .url(url)
            .downloadCallback(ListernerDownloader())
            .retryTime(5)
            .retryInterval(5, TimeUnit.SECONDS)
            .progressInterval(1, TimeUnit.SECONDS)
            .destinationDirectory(dataFileToDownload.folderPatch)
            .destinationFilePath(dataFileToDownload.filePatch)
            .priority(Priority.HIGH)
            .build()
    }

    private inner class ListernerDownloader : DownloadCallback() {
        private var startTimestamp: Long = 0
        private var startSize: Long = 0
        var notiBuilder = notificationDownloading.provideNotificationBuilder()

        override fun onStart(downloadId: Int, totalBytes: Long) {
            Log.d(TAG, "start download: $downloadId")
            Log.d(TAG, "totalBytes: $totalBytes")
            startTimestamp = System.currentTimeMillis()
            notiBuilder.setContentText(context.getString(R.string.text_StarDownload))
            notificationDownloading.notify(notiBuilder)
            dialogDownloading!!.show()
            SDAnalytics().eventTiming().timeEvent(EventTimingDownloadFile)
        }

        override fun onRetry(downloadId: Int) {
            Log.d(TAG, "retry downloadId: $downloadId")
            notiBuilder.setContentText(context.getString(R.string.text_retryDownload))
            dialogDownloading!!.customView.findViewById<TextView>(R.id.tv_descDialog).text =
                context.getString(R.string.text_retryDownload)
            dialogDownloading!!.customView.findViewById<LinearProgressIndicator>(R.id.progress_bar).visibility =
                View.GONE
            notificationDownloading.notify(notiBuilder)
        }

        @SuppressLint("SetTextI18n")
        override fun onProgress(downloadId: Int, bytesWritten: Long, totalBytes: Long) {
            val progress = (bytesWritten * 100f / totalBytes).toInt()
            //  val currentTimestamp = System.currentTimeMillis()
            Log.d(TAG, "progress: $progress")
            //   val deltaTime = (currentTimestamp - startTimestamp + 1).toInt()
            //   val speed = ((bytesWritten - startSize) * 1000 / deltaTime).toInt() / 1024
            val startSizeInMb: Long = bytesWritten / (1024 * 1024)
            val totalSizeInMb: Long = totalBytes / (1024 * 1024)

            notiBuilder.setProgress(100, progress, false)
            notiBuilder.setContentText(context.getString(R.string.text_downloading) + " $progress%...")
            notificationDownloading.notify(notiBuilder)

            dialogDownloading!!.customView.findViewById<LinearProgressIndicator>(R.id.progress_bar).progress =
                progress
            dialogDownloading!!.customView.findViewById<TextView>(R.id.tv_titleDialog).text =
                context.getString(R.string.text_downloading) + "\n$progress%"
            dialogDownloading!!.customView.findViewById<TextView>(R.id.tv_descDialog).text =
                "Size: $startSizeInMb mb / $totalSizeInMb mb"
        }

        override fun onSuccess(downloadId: Int, filepath: String) {
            Log.d(TAG, "success: " + downloadId + " size: " + File(filepath).length())
            saveMediaRoom()
            SDAnalytics().eventMediaDownloaded(mediaDownloaded)

            notiBuilder.setContentText(context.getString(R.string.text_successfulDownload))
            notiBuilder.setProgress(0, 0, false)
            notificationDownloading.notify(notiBuilder)
            dialogDownloading!!.isCancelable = true
            dialogDownloading!!.dismiss()

            ToastX.showSuccess(context.getString(R.string.text_successfulDownload))
            SDAd().showInterAd(context)
            SDAnalytics().eventTiming().track(EventTimingDownloadFile)
        }

        override fun onFailure(downloadId: Int, statusCode: Int, errMsg: String?) {
            Log.d(TAG, "fail: $downloadId $statusCode $errMsg")
            val errorMsg = "Error: $downloadId - $statusCode - $errMsg"
            SDAnalytics().eventFailureDownload(errorMsg, mediaDownloaded)
            DialogXUtils.NotificationX.showError(errorMsg)

            notiBuilder.setProgress(0, 0, false)
            notiBuilder.setContentText(errorMsg)
            notificationDownloading.notify(notiBuilder)

            dialogDownloading!!.customView.findViewById<TextView>(R.id.tv_titleDialog).text =
                context.getString(R.string.text_thereWasAError)
            dialogDownloading!!.customView.findViewById<TextView>(R.id.tv_descDialog).text =
                errorMsg
            dialogDownloading!!.isCancelable = true

            SDAnalytics().eventTiming().clearTimedEvent(EventTimingDownloadFile)
            SDAd().showInterAd(context)
        }
    }

    private fun dialogDownloading(): BottomDialog? {
        return BottomDialog.build(object : OnBindView<BottomDialog?>(R.layout.dialog_downloading) {
            override fun onBind(dialog: BottomDialog?, v: View?) {
                SDAd().initBannerAdDownloading(v)
                val progressBar = v!!.findViewById<LinearProgressIndicator>(R.id.progress_bar)
                if (mediaDownloaded.fromApp.isNotEmpty()) {
                    when (mediaDownloaded.fromApp.uppercase()) {
                        FACEBOOK.app -> progressBar.setIndicatorColor(context.resources.getColor(R.color.facebook_main))
                        INSTAGRAM.app -> progressBar.setIndicatorColor(context.resources.getColor(R.color.instagram_main))
                        TIKTOK.app -> progressBar.setIndicatorColor(context.resources.getColor(R.color.tiktok_main))
                    }
                }
            }
        }).setCancelable(false)
    }

    private fun dialogPostDownload(dataExtracted: ModelMediaDataExtracted) {
        val linkPosts = dataExtracted.linksToDownload
        println("btnDownload:: ${dataExtracted.linksToDownload}}")
        BottomDialog.show(object : OnBindView<BottomDialog?>(R.layout.dialog_post_slider) {
            override fun onBind(dialog: BottomDialog?, v: View?) {
                val tvTitleDialog = v?.findViewById<TextView>(R.id.tv_titleDialog)
                val viewpager = v?.findViewById<ViewPager>(R.id.viewpager)
                val btnDownloadUnLock = v?.findViewById<MaterialButton>(R.id.btn_download_unlock)
                val btnDownloadLock = v?.findViewById<MaterialButton>(R.id.btn_download_lock)
                val viewPagerAdapter = PostSlideAdapter(context, linkPosts!!)
                viewpager?.adapter = viewPagerAdapter
                val indicator = v?.findViewById<CircleIndicator>(R.id.indicator)
                indicator?.setViewPager(viewpager)

                //region Cambiar colores de acuerdo a link
                when (dataExtracted.app!!.uppercase()) {
                    FACEBOOK.app -> (btnDownloadUnLock!!.setBackgroundResource(FACEBOOK.background))
                    INSTAGRAM.app -> btnDownloadUnLock!!.setBackgroundResource(INSTAGRAM.background)
                    TIKTOK.app -> btnDownloadUnLock!!.setBackgroundResource(TIKTOK.background)
                }
                when (dataExtracted.app!!.uppercase()) {
                    FACEBOOK.app -> tvTitleDialog!!.text =
                        context.getString(R.string.text_facebookDownloader)
                    INSTAGRAM.app -> tvTitleDialog!!.text =
                        context.getString(R.string.text_instagramDownloader)
                    TIKTOK.app -> tvTitleDialog!!.text =
                        context.getString(R.string.text_tiktokDownloader)
                }
                //endregion

                //region Decidir si mostrar anuncio para desbloquear descarga
                if (SDAd().adToDownloadIsLoad()) {
                    btnDownloadLock!!.visibility = View.VISIBLE
                    btnDownloadLock.setOnClickListener {
                        if (viewPagerAdapter.getlistToDownload().isNotEmpty()) {
                            SDAd().showAdToDodwnload(context, dialog)
                            // si se cierra el dialogo desde onAdDismissedFullScreenContent, se iniciara la descarga
                            dialog!!.dialogLifecycleCallback =
                                object : DialogLifecycleCallback<BottomDialog?>() {
                                    override fun onDismiss(dialog: BottomDialog?) {
                                        // descargar cada elemento
                                        for (linkToDownload in viewPagerAdapter.getlistToDownload()) {
                                            MediaDownloader(context).downloadMedia(
                                                linkToDownload,
                                                dataExtracted
                                            )
                                        }
                                    }
                                }
                            println("btnDownload:: {${viewPagerAdapter.getlistToDownload()}}")
                        } else {
                            ToastX.showWarning(context.getString(R.string.text_selectToDownload))
                        }
                    }
                } else {
                    btnDownloadUnLock!!.visibility = View.VISIBLE
                    btnDownloadUnLock.setOnClickListener {
                        if (viewPagerAdapter.getlistToDownload().isNotEmpty()) {
                            for (linkToDownload in viewPagerAdapter.getlistToDownload()) {
                                MediaDownloader(context).downloadMedia(
                                    linkToDownload,
                                    dataExtracted
                                )
                                dialog!!.dismiss()
                            }
                            println("btnDownload:: {${viewPagerAdapter.getlistToDownload()}}")
                        } else {
                            ToastX.showWarning(context.getString(R.string.text_selectToDownload))
                        }
                    }
                }
                //endregion
            }
        })
    }

    internal fun checkToDownload(dataExtracted: ModelMediaDataExtracted) {
        if (dataExtracted.codeResponse == APIHelper.CodeApi.C_200.code.toString()) {
            if (!dataExtracted.linksToDownload.isNullOrEmpty() && !dataExtracted.body.toString()
                    .contains("private")
            ) {
                // Dialogo de descarga
                DownloaderHelper(context).dialogPostDownload(dataExtracted)
                /** Se envia datos obtenidos a Analytics */
                SDAnalytics().eventMediaDataExtracted(dataExtracted)
            } else {
                // validar error por link privado
                if (dataExtracted.body.toString().contains("private")) {
                    DialogXUtils.NotificationX.showError(context.getString(R.string.error_linkPrivate))
                        .showLong()
                } else {
                    DialogXUtils.NotificationX.showError(context.getString(R.string.error_getDataApi))
                        .showLong()
                }
                /** Se envia datos de error a Analytics */
                SDAnalytics().eventErrorApiData(dataExtracted)
                ToastX.showSuccess("Error code: ${dataExtracted.codeResponse}")
            }
        }
    }

}


