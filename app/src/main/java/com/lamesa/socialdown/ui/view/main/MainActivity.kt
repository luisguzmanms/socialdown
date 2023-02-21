package com.lamesa.socialdown.ui.view.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.lamesa.socialdown.R
import com.lamesa.socialdown.adapter.DownloadsAdapter
import com.lamesa.socialdown.app.SDApp.Context.tinyDB
import com.lamesa.socialdown.data.remote.APIHelper.AppApi.*
import com.lamesa.socialdown.databinding.ActivityMainBinding
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.ui.viewmodel.MainViewModel
import com.lamesa.socialdown.ui.viewmodel.MainViewModelFactory
import com.lamesa.socialdown.usecase.GetCustomMsgUseCase
import com.lamesa.socialdown.usecase.GetUpdateUseCase
import com.lamesa.socialdown.utils.Constansts.Analytics.TBDownloads
import com.lamesa.socialdown.utils.Constansts.SHARE_APP_REQUEST_CODE
import com.lamesa.socialdown.utils.Constansts.SHARE_ITEM_REQUEST_CODE
import com.lamesa.socialdown.utils.DialogXUtils
import com.lamesa.socialdown.utils.DialogXUtils.NotificationX
import com.lamesa.socialdown.utils.DialogXUtils.Policies.dialogStars
import com.lamesa.socialdown.utils.SDAd
import com.lamesa.socialdown.utils.SDAnalytics
import com.lamesa.socialdown.utils.SDAnimation
import com.lamesa.socialdown.utils.SocialHelper.AppDownloader
import com.lamesa.socialdown.utils.SocialHelper.checkAppTypeUrl
import com.lamesa.socialdown.utils.SocialHelper.searchByLink

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: DownloadsAdapter
    private lateinit var sdAnimation: SDAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sdAnimation = SDAnimation(this)
        initDownloadsRV()
        initViewModel()
        animateViews()
        clickListener()
        getIntentExtra()
        initAds()
        initFirebase()
    }

    private fun initFirebase() {
        GetCustomMsgUseCase().invoke()
        GetUpdateUseCase().invoke()
    }

    private fun initAds() {
        SDAd().initBannerAd(binding)
        SDAd().loadInterBonAd(this)
        SDAd().loadVideoAd(this)
        SDAd().loadInterAd(this)
        SDAd().showInterOrVideo(this)
    }

    private fun getIntentExtra() {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            val link = bundle.getString("link") // 1
            binding.etLink.setText(link)
            searchByLink(this, link!!)
            animateTypeDownloader(checkAppTypeUrl(link).toString())
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun initViewModel() {
        // ViewModel y Factory
        val factory = MainViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        // Observer
        viewModel.lstMediaDownloadedDB.observe(this) {
            DialogXUtils().dialogCongratulations(this)
            if (it.isNotEmpty()) {
                if (tinyDB.getInt(TBDownloads) == 0) {
                    tinyDB.putInt(TBDownloads, it.size)
                }
                dialogStars(this)
                binding.cnEmpty.visibility = View.GONE
                it.let {
                    adapter.updateList(it as ArrayList<ModelMediaDownloaded>)
                    adapter.notifyDataSetChanged()
                    binding.tvDownloadCount.visibility = View.VISIBLE
                    binding.tvDownloadCount.text = "${it.size} files"
                }
            } else {
                adapter.updateList(it as ArrayList<ModelMediaDownloaded>)
                adapter.notifyDataSetChanged()
                binding.cnEmpty.visibility = View.VISIBLE
                binding.tvDownloadCount.visibility = View.INVISIBLE
                sdAnimation.setSDAnimation(binding.cnEmpty, R.anim.scale_up)
            }
        }
        viewModel.tvTitleDownloader.observe(this) {
            animateTypeDownloader(it)
        }
    }

    private fun animateTypeDownloader(type: String) {
        if (type.lowercase().contains(AppDownloader.TIKTOK.app.lowercase())) {
            binding.tvTitleDownloader.animateText(getString(R.string.text_tiktokDownloader))
            binding.tvSubTitleDownloader.animateText(AppDownloader.TIKTOK.mediaSupported)
            binding.mainLayout.setBackgroundResource(R.drawable.gradiant_tiktok)
            binding.ivTiktok.setBackgroundResource(AppDownloader.TIKTOK.iconOn)
            sdAnimation.setSDAnimation(
                arrayOf(binding.mainLayout, binding.ivTiktok),
                R.anim.fade_in_main
            )
        } else if (type.lowercase().contains(AppDownloader.INSTAGRAM.app.lowercase())) {
            binding.tvTitleDownloader.animateText(this@MainActivity.resources.getString(R.string.text_instagramDownloader))
            binding.tvSubTitleDownloader.animateText(AppDownloader.INSTAGRAM.mediaSupported)
            binding.mainLayout.setBackgroundResource(R.drawable.gradiant_instagram)
            binding.ivInstagram.setBackgroundResource(AppDownloader.INSTAGRAM.iconOn)
            sdAnimation.setSDAnimation(
                arrayOf(binding.mainLayout, binding.ivInstagram),
                R.anim.fade_in_main
            )
        } else if (type.lowercase().contains(AppDownloader.FACEBOOK.app.lowercase())) {
            binding.tvTitleDownloader.animateText(this@MainActivity.resources.getString(R.string.text_facebookDownloader))
            binding.tvSubTitleDownloader.animateText(AppDownloader.FACEBOOK.mediaSupported)
            binding.mainLayout.setBackgroundResource(R.drawable.gradiant_facebook)
            binding.ivFacebook.setBackgroundResource(AppDownloader.FACEBOOK.iconOn)
            sdAnimation.setSDAnimation(
                arrayOf(binding.mainLayout, binding.ivFacebook),
                R.anim.fade_in_main
            )
        } else {
            binding.tvTitleDownloader.animateText(this@MainActivity.resources.getString(R.string.app_name))
            binding.tvSubTitleDownloader.animateText(AppDownloader.INSTAGRAM.mediaSupported)
            binding.mainLayout.setBackgroundResource(R.drawable.gradiant_main)
            binding.ivInstagram.setBackgroundResource(AppDownloader.INSTAGRAM.iconOff)
            binding.ivFacebook.setBackgroundResource(AppDownloader.FACEBOOK.iconOff)
            binding.ivTiktok.setBackgroundResource(AppDownloader.TIKTOK.iconOff)
            sdAnimation.setSDAnimation(binding.mainLayout, R.anim.fade_in_main)
        }
    }

    /**
     * Alternativa de animaciones (en un futuro implementar MotionLayout)
     */
    private fun animateViews() {
        sdAnimation.setSDAnimation(binding.main, R.anim.anim_slide_top_in)
        sdAnimation.setSDAnimation(binding.rvMediaDownloaded, R.anim.anim_slide_bottom_in)
    }

    private fun initDownloadsRV() {
        adapter = DownloadsAdapter(this@MainActivity)
        binding.rvMediaDownloaded.layoutManager = GridLayoutManager(this, 2)
        binding.rvMediaDownloaded.adapter = adapter
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun clickListener() {
        binding.ivDownload.setOnClickListener {
            sdAnimation.setSDAnimation(binding.ivDownload, R.anim.scale_up)
            if (binding.etLink.text.toString().trim().isNotEmpty()) {
                searchByLink(this, binding.etLink.text.toString().trim())
                animateTypeDownloader(
                    checkAppTypeUrl(
                        binding.etLink.text.toString().trim()
                    ).toString()
                )
            } else {
                NotificationX.showWarning(getString(R.string.error_empty)).showLong()
                SDAd().showInterAd(this)
            }
            hideSoftKeyboard()
        }
        binding.ivClean.setOnClickListener { view ->
            binding.etLink.text.clear()
        }
        binding.etLink.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                when (checkAppTypeUrl(text.toString())) {
                    TIKTOK -> viewModel.updateTitleDownloader(getString(R.string.text_tiktokDownloader))
                    INSTAGRAM -> viewModel.updateTitleDownloader(getString(R.string.text_instagramDownloader))
                    FACEBOOK -> viewModel.updateTitleDownloader(getString(R.string.text_facebookDownloader))
                    NONE -> viewModel.updateTitleDownloader("SocialDown")
                }
                if (text.toString().isNotEmpty()) {
                    binding.ivClean.visibility = View.VISIBLE
                    sdAnimation.setSDAnimation(
                        binding.ivClean,
                        R.anim.scale_up
                    )
                } else {
                    sdAnimation.setSDAnimation(
                        binding.ivClean,
                        R.anim.anim_alpha_out
                    )
                    binding.ivClean.visibility = View.GONE
                }
                DialogXUtils.Policies.dialogPermissions(this@MainActivity)
            }
        })
        binding.ivFacebook.setOnClickListener {
            animateTypeDownloader(FACEBOOK.toString())
        }
        binding.ivTiktok.setOnClickListener {
            animateTypeDownloader(TIKTOK.toString())
        }
        binding.ivInstagram.setOnClickListener {
            animateTypeDownloader(AppDownloader.INSTAGRAM.app)
        }
        binding.etLink.setOnClickListener {
            if (checkClipboard().isNotEmpty() && binding.etLink.text.toString().isEmpty()) {
                binding.etLink.setText(checkClipboard())
                hideSoftKeyboard()
            }
        }
        binding.ivMenu.setOnClickListener {
            sdAnimation.setSDAnimation(binding.ivMenu, R.anim.scale_up)
            DialogXUtils().dialogMainMenu(this)
        }
    }

    private fun checkClipboard(): String {
        val clipBoardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val linkClipboard = clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString()
        if (!linkClipboard.isNullOrEmpty()) {
            if (checkAppTypeUrl(linkClipboard) != NONE) {
                return linkClipboard
            }
        }
        return ""
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager: InputMethodManager = this@MainActivity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                this@MainActivity.currentFocus!!.windowToken,
                0
            )
        }
    }

    // Detectar si se ha compartido contenido
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SHARE_APP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // The user shared the content successfully
                SDAnalytics().eventShareApp()
            }
        }
        if (requestCode == SHARE_ITEM_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // The user shared the content successfully
                SDAnalytics().eventShareItem()
            }
        }
    }

}