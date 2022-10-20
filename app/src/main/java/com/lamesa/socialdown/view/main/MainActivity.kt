package com.lamesa.socialdown.view.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.webkit.URLUtil.isNetworkUrl
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.kongzue.dialogx.dialogs.PopTip
import com.lamesa.socialdown.R
import com.lamesa.socialdown.adapter.DownloadsAdapter
import com.lamesa.socialdown.data.remote.APIHelper
import com.lamesa.socialdown.data.remote.APIHelper.AppApi.*
import com.lamesa.socialdown.databinding.ActivityMainBinding
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.utils.DialogXUtils
import com.lamesa.socialdown.utils.SDAnimation
import com.lamesa.socialdown.utils.SocialHelper
import com.lamesa.socialdown.utils.SocialHelper.checkAppTypeUrl
import com.lamesa.socialdown.viewmodel.MainViewModel
import com.lamesa.socialdown.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    private lateinit var adapter: DownloadsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDownloadsRV()
        animateViews()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViewModel() {
        // ViewModel y Factory
        val factory = MainViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        // Observer
        viewModel.lstMediaDownloadedDB.observe(this) {
            if (it.isNotEmpty()) {
                binding.cnEmpty.visibility = View.GONE
                it.let {
                    adapter.updateList(it as ArrayList<ModelMediaDownloaded>)
                    adapter.notifyDataSetChanged()
                    binding.tvDownloadCount.text = "${it.size} files"
                }
            } else {
                binding.cnEmpty.visibility = View.VISIBLE
                SDAnimation().setSDAnimation(this@MainActivity, binding.cnEmpty, R.anim.scale_up)
            }
        }

        viewModel.tvTitleDownloader.observe(this) {
            animateTypeDownloader(it)
        }
    }

    private fun initDownloadsRV() {
        adapter = DownloadsAdapter(this@MainActivity)
        binding.rvMediaDownloaded.layoutManager = GridLayoutManager(this, 2)
        binding.rvMediaDownloaded.adapter = adapter
    }

    /**
     * Alternativa de animaciones (en un futuro implementar MotionLayout)
     */
    private fun animateViews() {
        SDAnimation().setSDAnimation(this, binding.main, R.anim.anim_slide_top_in)
        SDAnimation().setSDAnimation(this, binding.rvMediaDownloaded, R.anim.anim_slide_bottom_in)
        SDAnimation().setSDAnimation(
            this,
            arrayOf(binding.tvTitleDownloader, binding.svGetMedia, binding.svLinearLayout),
            R.anim.slide_up_fade_in
        )
    }

    private fun clickListener() {
        binding.ivDownload.setOnClickListener {
            SDAnimation().setSDAnimation(this, binding.ivDownload, R.anim.scale_up)
            searchByLink(binding.etLink.text.toString())
        }

        SDAnimation().setSDAnimation(this, binding.mainLayout, R.anim.anim_slide_top_in)
        SDAnimation().setSDAnimation(this, binding.tvTitleDownloader, R.anim.slide_up_fade_in)
        SDAnimation().setSDAnimation(this, binding.svGetMedia, R.anim.slide_up_fade_in)
        SDAnimation().setSDAnimation(this, binding.svLinearLayout, R.anim.slide_up_fade_in)
    }

    private fun animateTypeDownloader(type: String) {
        binding.tvWelcome.visibility = View.GONE
        if (type.lowercase().contains(AppDownloader.TIKTOK.app.lowercase())) {
            binding.tvTitleDownloader.text = "Tiktok\nDownloader"
            binding.tvSubTitleDownloader.text = AppDownloader.TIKTOK.mediaSupported
            binding.mainLayout.setBackgroundResource(R.drawable.gradiant_tiktok)
            binding.ivTiktok.setBackgroundResource(AppDownloader.TIKTOK.iconOn)
            SDAnimation().setSDAnimation(
                this,
                arrayOf(binding.mainLayout, binding.ivTiktok),
                R.anim.fade_in_main
            )
        } else if (type.lowercase().contains(AppDownloader.INSTAGRAM.app.lowercase())) {
            binding.tvTitleDownloader.text = "Instagram\nDownloader"
            binding.tvSubTitleDownloader.text = AppDownloader.INSTAGRAM.mediaSupported
            binding.mainLayout.setBackgroundResource(R.drawable.gradiant_instagram)
            binding.ivInstagram.setBackgroundResource(AppDownloader.INSTAGRAM.iconOn)
            SDAnimation().setSDAnimation(
                this,
                arrayOf(binding.mainLayout, binding.ivInstagram),
                R.anim.fade_in_main
            )
        } else if (type.lowercase().contains(AppDownloader.FACEBOOK.app.lowercase())) {
            binding.tvTitleDownloader.text = "Facebook\nDownloader"
            binding.tvSubTitleDownloader.text = AppDownloader.FACEBOOK.mediaSupported
            binding.mainLayout.setBackgroundResource(R.drawable.gradiant_facebook)
            binding.ivFacebook.setBackgroundResource(AppDownloader.FACEBOOK.iconOn)
            SDAnimation().setSDAnimation(
                this,
                arrayOf(binding.mainLayout, binding.ivFacebook),
                R.anim.fade_in_main
            )
        } else {
            binding.tvWelcome.visibility = View.VISIBLE
            binding.tvTitleDownloader.text = "SocialDown"
            binding.tvSubTitleDownloader.text = AppDownloader.INSTAGRAM.mediaSupported
            binding.mainLayout.setBackgroundResource(R.drawable.gradiant_main)
            binding.ivInstagram.setBackgroundResource(AppDownloader.INSTAGRAM.iconOff)
            binding.ivFacebook.setBackgroundResource(AppDownloader.FACEBOOK.iconOff)
            binding.ivTiktok.setBackgroundResource(AppDownloader.TIKTOK.iconOff)
            SDAnimation().setSDAnimation(this, binding.mainLayout, R.anim.fade_in_main)
        }
    }

    private fun searchByLink(queryLink: String) {
        if (SocialHelper.isOnline(this)) {
            if (isNetworkUrl(queryLink)) {
                when (checkAppTypeUrl(queryLink)) {
                    TIKTOK -> APIHelper.executeApi(this, TIKTOK, queryLink)
                    INSTAGRAM -> APIHelper.executeApi(this, INSTAGRAM, queryLink)
                    FACEBOOK -> APIHelper.executeApi(this, FACEBOOK, queryLink)
                    else -> DialogXUtils.Notification.showNotification("Link no compatible")
                }
            } else {
                PopTip.build().setTintIcon(true).setMessage("Link no valido").show()
            }
        } else {
            PopTip.show("Sin conexión a internet").iconError()
        }
    }
}
