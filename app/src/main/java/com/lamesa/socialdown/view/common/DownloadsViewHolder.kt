package com.lamesa.socialdown.view.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kongzue.dialogx.dialogs.BottomDialog
import com.kongzue.dialogx.dialogs.BottomMenu
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener
import com.lamesa.socialdown.BuildConfig
import com.lamesa.socialdown.R.*
import com.lamesa.socialdown.data.remote.APIHelper.AppApi.*
import com.lamesa.socialdown.databinding.ItemMediaDownloadedBinding
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.usecase.DeleteMediaUseCase
import com.lamesa.socialdown.utils.Constansts.SHARE_ITEM_REQUEST_CODE
import com.lamesa.socialdown.utils.DialogXUtils
import com.lamesa.socialdown.utils.SDAnimation
import com.lamesa.socialdown.utils.SocialHelper.AppDownloader
import com.lamesa.socialdown.utils.SocialHelper.MediaType.*
import com.lamesa.socialdown.utils.SocialHelper.MediaType.NONE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class DownloadsViewHolder(private val context: Context, view: View) :
    RecyclerView.ViewHolder(view) {

    private val binding = ItemMediaDownloadedBinding.bind(view)

    fun bind(media: ModelMediaDownloaded) {

        when (media.fromApp) {
            TIKTOK.id -> binding.ivTypeApp.setImageResource(AppDownloader.TIKTOK.iconOn)
            INSTAGRAM.id -> {
                binding.ivTypeApp.setImageResource(AppDownloader.INSTAGRAM.iconOn)
            }
            FACEBOOK.id -> binding.ivTypeApp.setImageResource(AppDownloader.FACEBOOK.iconOn)
            else -> {}
        }

        when (media.fromApp) {
            TIKTOK.id -> binding.ivGradiant.setImageResource(drawable.gradiant_media_tiktok)
            INSTAGRAM.id -> binding.ivGradiant.setImageResource(drawable.gradiant_media_insta)
            FACEBOOK.id -> binding.ivGradiant.setImageResource(drawable.gradiant_media_face)
            else -> binding.ivGradiant.setImageResource(drawable.gradiant_media_grey)
        }

        when (media.mediaType) {
            VIDEO.type -> binding.ivTypePost.setImageResource(VIDEO.icon)
            REEL.type -> binding.ivTypePost.setImageResource(REEL.icon)
            STORY.type -> binding.ivTypePost.setImageResource(STORY.icon)
            POST.type -> binding.ivTypePost.setImageResource(POST.icon)
            IGTV.type -> binding.ivTypePost.setImageResource(IGTV.icon)
            else -> binding.ivTypeApp.setImageResource(NONE.icon)
        }

        Glide.with(context)
            .asBitmap()
            .load(Uri.fromFile(File(media.filePatch)))
            .placeholder(drawable.ic_download)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(binding.ivMedia)

        SDAnimation(context).setSDAnimation(
            binding.content,
            anim.anim_alpha_in
        )

        binding.content.setOnClickListener {
            dialogOptions(media)
        }
        binding.content.setOnLongClickListener {
            deleteItem(media)
            true
        }
    }

    private fun openFile(media: ModelMediaDownloaded) {
        var mimeType = ""
        val intent = Intent(Intent.ACTION_VIEW)
        // val uri = Uri.fromFile(File(media.filePatch))
        val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                File(media.filePatch)
            )
        } else {
            Uri.fromFile(File(media.filePatch))
        }

        if (File(media.filePatch).exists() && uri != null) {
            if (media.filePatch.contains(".jpg")) mimeType = "image/jpg"
            if (media.filePatch.contains(".mp4")) mimeType = "video/mp4"

            if (mimeType.isNotEmpty()) {
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                intent.setDataAndType(uri, mimeType)
                context.startActivity(intent)
            }
        } else {
            DialogXUtils.ToastX.showError(context.getString(string.error_openFile))
        }
    }

    private fun dialogOptions(media: ModelMediaDownloaded) {
        BottomMenu.show(
            arrayOf(
                context.getString(string.option_open),
                context.getString(string.option_share),
                context.getString(string.option_delete)
            )
        )
            .setOnIconChangeCallBack(object : OnIconChangeCallBack<BottomMenu?>(true) {
                override fun getIcon(dialog: BottomMenu?, index: Int, menuText: String?): Int {
                    when (menuText) {
                        context.getString(string.option_open) -> return drawable.ic_open
                        context.getString(string.option_share) -> return drawable.ic_new_post
                        context.getString(string.option_delete) -> return drawable.ic_delete
                    }
                    dialog!!.hide()
                    return 0
                }
            }).setOnMenuItemClickListener(object : OnMenuItemClickListener<BottomMenu?> {
                override fun onClick(
                    dialog: BottomMenu?,
                    text: CharSequence?,
                    index: Int
                ): Boolean {
                    when (index) {
                        0 -> openFile(media)
                        1 -> shareItem(media)
                        2 -> deleteItem(media)
                    }
                    dialog!!.hide()
                    return true
                }
            })
    }

    private fun deleteItem(media: ModelMediaDownloaded) {
        BottomDialog.show(context.getString(string.dialog_deleteItem), "")
            .setCancelButton(
                context.getString(string.text_delete),
                OnDialogButtonClickListener { dialog, v ->
                    CoroutineScope(Dispatchers.IO).launch {
                        DeleteMediaUseCase().invoke(media)
                        if (File(media.filePatch).exists()) File(media.filePatch).delete()
                    }
                    dialog.hide()
                    DialogXUtils.ToastX.showSuccess(context.getString(string.text_itemDelete))
                        .showShort()
                    true
                })
    }

    private fun shareItem(media: ModelMediaDownloaded) {
        val activity = context as Activity
        var mimeType = ""
        val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                File(media.filePatch)
            )
        } else {
            Uri.fromFile(File(media.filePatch))
        }

        if (File(media.filePatch).exists() && uri != null) {
            if (media.filePatch.contains("jpg")) mimeType = "image/jpg"
            if (media.filePatch.contains("mp4")) mimeType = "video/mp4"
            if (mimeType.isNotEmpty()) {
                val share = Intent()
                share.action = Intent.ACTION_SEND
                share.type = mimeType
                share.putExtra(Intent.EXTRA_STREAM, uri)
                activity.startActivityForResult(
                    Intent.createChooser(
                        share,
                        context.getString(string.text_shareFile)
                    ), SHARE_ITEM_REQUEST_CODE
                )
            }
        } else {
            DialogXUtils.ToastX.showError(context.getString(string.error_openFile))
        }
    }

}