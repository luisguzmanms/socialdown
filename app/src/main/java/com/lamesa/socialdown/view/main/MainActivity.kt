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

        // ViewModel y Factory
        val factory = MainViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        binding.etLink.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                text: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                when (checkAppTypeUrl(text.toString())) {
                    TIKTOK -> viewModel.updateTitleDownloader("Tiktok\nDownloader")
                    INSTAGRAM -> viewModel.updateTitleDownloader("Instagram\nDownloader")
                    FACEBOOK -> viewModel.updateTitleDownloader("Facebook\nDownloader")
                    NONE -> viewModel.updateTitleDownloader("SocialDown")
                }

            }
        })

        viewModel.lstMediaDownloadedDB.observe(this) {
            it.let {
                adapter.updateList(it as ArrayList<ModelMediaDownloaded>)
                adapter.notifyDataSetChanged()
            }
            if (!it.isNullOrEmpty()) {
                DialogXUtils.Toast.showSuccess("items en lstData =${it.size}")
            } else {
                DialogXUtils.Toast.showError("NO hay tiems en lstDataDB")
            }
        }

    }

    /**
     * Alternativa de animaciones (en un futuro implementar MotionLayout)
     */
    private fun animateViews() {

        binding.tvTitleDownloader.setOnClickListener {
            SDAnimation().setSDAnimation(
                this,
                binding.tvTitleDownloader,
                R.anim.learn_item_anim_fall_down
            )
            SDAnimation().setSDAnimation(this, binding.svGetMedia, R.anim.learn_item_anim_fall_down)
            SDAnimation().setSDAnimation(
                this,
                binding.svLinearLayout,
                R.anim.learn_item_anim_fall_down
            )
        }
        binding.ivDownload.setOnClickListener {
            SDAnimation().setSDAnimation(this, binding.ivDownload, R.anim.scale_up)
            searchByLink(binding.etLink.text.toString())
        }

        SDAnimation().setSDAnimation(this, binding.mainLayout, R.anim.anim_slide_top_in)
        SDAnimation().setSDAnimation(this, binding.tvTitleDownloader, R.anim.slide_up_fade_in)
        SDAnimation().setSDAnimation(this, binding.svGetMedia, R.anim.slide_up_fade_in)
        SDAnimation().setSDAnimation(this, binding.svLinearLayout, R.anim.slide_up_fade_in)
    }

    private fun initDownloadsRV() {
        adapter = DownloadsAdapter(this@MainActivity)
        binding.rvMediaDownloaded.layoutManager = GridLayoutManager(this, 2)
        binding.rvMediaDownloaded.adapter = adapter
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
