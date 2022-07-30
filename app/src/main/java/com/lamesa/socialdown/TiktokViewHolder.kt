package com.lamesa.socialdown

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lamesa.socialdown.databinding.ItemTiktokBinding

class TiktokViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemTiktokBinding.bind(view)

    fun bind(video:String){
        binding.tvTiktokLink.text = video
    }
}