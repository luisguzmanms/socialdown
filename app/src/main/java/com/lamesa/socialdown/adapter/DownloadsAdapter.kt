package com.lamesa.socialdown.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lamesa.socialdown.R
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.viewholder.DownloadsViewHolder

/** Created by luis Mesa on 08/08/22 */
class DownloadsAdapter(private val context: Context) : RecyclerView.Adapter<DownloadsViewHolder>() {

    private var listMedia = ArrayList<ModelMediaDownloaded>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DownloadsViewHolder(
            context,
            layoutInflater.inflate(R.layout.item_media_downloaded, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listMedia.size
    }

    override fun onBindViewHolder(holder: DownloadsViewHolder, position: Int) {
        val media = listMedia[position]
        holder.bind(media)
    }

    fun updateList(newList: ArrayList<ModelMediaDownloaded>) {
        listMedia.clear()
        listMedia.addAll(newList)
    }

}