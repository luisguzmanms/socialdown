package com.lamesa.socialdown.viewholder

import android.content.Context
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lamesa.socialdown.R
import com.lamesa.socialdown.data.remote.APIHelper
import com.lamesa.socialdown.databinding.ItemMediaDownloadedBinding
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.utils.SDAnimation
import com.lamesa.socialdown.utils.SocialHelper
import java.io.File


class DownloadsViewHolder(private val context: Context, view: View) :
    RecyclerView.ViewHolder(view) {

    private val binding = ItemMediaDownloadedBinding.bind(view)

    fun bind(video: ModelMediaDownloaded) {

        when (video.fromApp) {
            APIHelper.AppApi.TIKTOK.toString() -> binding.ivTypeApp.setImageResource(SocialHelper.AppTypeIconOn.TIKTOK.icon)
            APIHelper.AppApi.INSTAGRAM.toString() -> binding.ivTypeApp.setImageResource(SocialHelper.AppTypeIconOn.INSTAGRAM.icon)
            APIHelper.AppApi.FACEBOOK.toString() -> binding.ivTypeApp.setImageResource(SocialHelper.AppTypeIconOn.FACEBOOK.icon)
            else -> binding.ivTypeApp.setImageResource(SocialHelper.AppTypeIconOn.NONE.icon)
        }

        when (video.fromApp) {
            APIHelper.AppApi.TIKTOK.toString() -> binding.ivGradiant.setImageResource(R.drawable.gradiant_media_tiktok)
            APIHelper.AppApi.INSTAGRAM.toString() -> binding.ivGradiant.setImageResource(R.drawable.gradiant_media_insta)
            APIHelper.AppApi.FACEBOOK.toString() -> binding.ivGradiant.setImageResource(R.drawable.gradiant_media_face)
            else -> binding.ivGradiant.setImageResource(R.drawable.gradiant_media_grey)
        }

        when (video.mediaType) {
            SocialHelper.MediaType.VIDEO.type -> binding.ivTypePost.setImageResource(SocialHelper.MediaType.VIDEO.icon)
            SocialHelper.MediaType.REEL.type -> binding.ivTypePost.setImageResource(SocialHelper.MediaType.REEL.icon)
            SocialHelper.MediaType.STORY.type -> binding.ivTypePost.setImageResource(SocialHelper.MediaType.STORY.icon)
            SocialHelper.MediaType.POST.type -> binding.ivTypePost.setImageResource(SocialHelper.MediaType.POST.icon)
            SocialHelper.MediaType.IGTV.type -> binding.ivTypePost.setImageResource(SocialHelper.MediaType.IGTV.icon)
            else -> binding.ivTypeApp.setImageResource(SocialHelper.MediaType.NONE.icon)
        }


        /*
        Glide.with(context)
            .asBitmap()
            .load(Uri.fromFile(File(video.filePatch)))
            .placeholder(R.drawable.ic_descargar)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(binding.ivMedia)
         */

        if (File(video.filePatch).exists()) {
            val bMap = ThumbnailUtils.createVideoThumbnail(
                video.filePatch,
                MediaStore.Video.Thumbnails.MINI_KIND
            )
            binding.ivMedia.setImageBitmap(bMap)
        }

        SDAnimation().setSDAnimation(context, binding.content, R.anim.anim_alpha_in)
    }


}