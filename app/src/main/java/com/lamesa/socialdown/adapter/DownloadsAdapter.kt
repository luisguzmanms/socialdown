package com.lamesa.socialdown.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.lamesa.socialdown.R.anim
import com.lamesa.socialdown.R.layout
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.ui.view.common.DownloadsViewHolder

/** Created by luis Mesa on 08/08/22 */
class DownloadsAdapter(private val context: Context) : RecyclerView.Adapter<DownloadsViewHolder>() {

    private var listMedia = ArrayList<ModelMediaDownloaded>()
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DownloadsViewHolder(
            context,
            layoutInflater.inflate(
                layout.item_media_downloaded,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listMedia.size
    }

    override fun onBindViewHolder(holder: DownloadsViewHolder, position: Int) {
        val media = listMedia[position]
        setAnimation(holder.itemView, position)
        holder.bind(media)
    }

    internal fun updateList(newList: List<ModelMediaDownloaded>) {
        listMedia.clear()
        listMedia = ArrayList(newList)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, anim.scale_up)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}