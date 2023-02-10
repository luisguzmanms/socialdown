package com.lamesa.socialdown.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.checkbox.MaterialCheckBox
import com.lamesa.socialdown.R
import com.lamesa.socialdown.utils.SocialHelper

/** Created by luis Mesa on 1/10/22 */
class PostSliderAdapter(private val context: Context, private var mediaList: List<String>) :
    PagerAdapter() {
    private var listToDownload = mutableListOf<String>()
    private var listPositionSelected = mutableListOf<Int>()

    override fun getCount(): Int {
        return mediaList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.item_image_slider,
                null
            )
        val ivPost = view.findViewById<ImageView>(R.id.iv_images)
        val ivTypePost = view.findViewById<ImageView>(R.id.iv_typePost)
        val ivPostSelected = view.findViewById<ImageView>(R.id.iv_imageSelected)
        val cbPostCheck = view.findViewById<MaterialCheckBox>(R.id.cb_check)
        val tvCountCarouselPost = view.findViewById<TextView>(R.id.tv_countCarousel)
        val counterCarouselPost = (position + 1).toString() + "/$count"

        //region Si se trata de mas de un post (carousel/stories)
        if (count > 1) {

            cbPostCheck.visibility = View.VISIBLE
            tvCountCarouselPost.visibility = View.VISIBLE
            tvCountCarouselPost.text = counterCarouselPost

            // Se identifican las imagenes seleccionadas
            listPositionSelected.forEach {
                if (position == it) {
                    ivPostSelected.visibility = View.VISIBLE
                    cbPostCheck.isChecked = true
                }
            }

            // Seleccionar images a descargar
            ivPost.setOnClickListener {
                if (ivPostSelected.visibility != View.VISIBLE) {
                    cbPostCheck.isChecked = true
                    ivPostSelected.visibility = View.VISIBLE
                    listToDownload.add(mediaList[position])
                    listPositionSelected.add(position)
                } else {
                    cbPostCheck.isChecked = false
                    ivPostSelected.visibility = View.GONE
                    listToDownload.remove(mediaList[position])
                    listPositionSelected.remove(position)
                }
            }
        } else {
            cbPostCheck.visibility = View.GONE
            tvCountCarouselPost.visibility = View.GONE
            listToDownload.add(mediaList[position])
        }
        //endregion

        // Cargar imagenes
        Glide.with(context)
            .load(mediaList[position])
            .centerCrop()
            .error(me.relex.circleindicator.R.drawable.mtrl_ic_error)
            .into(ivPost)

        // Determinar tipo de post
        if (mediaList[position].contains("jpg")) ivTypePost.setImageResource(SocialHelper.MediaType.IMAGE.icon)
        else if (mediaList[position].contains("mp4")) ivTypePost.setImageResource(SocialHelper.MediaType.VIDEO.icon)

        val vp = container as ViewPager
        vp.addView(view)
        return view

    }

    internal fun getlistToDownload(): List<String> {
        return listToDownload.toSet().toList()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}

