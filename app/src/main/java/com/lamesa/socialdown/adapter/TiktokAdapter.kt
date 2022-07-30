package com.lamesa.socialdown.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lamesa.socialdown.R
import com.lamesa.socialdown.TiktokViewHolder

class TiktokAdapter(private val videos:List<String>) : RecyclerView.Adapter<TiktokViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TiktokViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TiktokViewHolder(layoutInflater.inflate(R.layout.item_tiktok, parent,false))
    }

    override fun getItemCount(): Int {
       return videos.size
    }

    override fun onBindViewHolder(holder: TiktokViewHolder, position: Int) {
        val item: String = videos[position]
        holder.bind(item)
    }


}