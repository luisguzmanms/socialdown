package com.lamesa.socialdown.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.kongzue.dialogx.dialogs.BottomDialog
import com.lamesa.socialdown.app.SDApp.Context.tinyDB
import com.lamesa.socialdown.databinding.ActivityMainBinding
import com.lamesa.socialdown.utils.Constansts.Ad.rewardedInterstitialAd
import com.lamesa.socialdown.utils.Constansts.Ad.rewardedVideoAd
import com.lamesa.socialdown.utils.Constansts.Ad.userReward
import com.lamesa.socialdown.utils.Constansts.Analytics.HasRated


class SDAd {

    private var TAG = "SDAd"
    private var mInterstitialAd: InterstitialAd? = null

    // for testing
    /*
    val idNativeAdLoading = "ca-app-pub-3940256099942544/1044960115"
    val idNativeAdDownloading = "ca-app-pub-3940256099942544/1044960115"
    val idInterAd = "ca-app-pub-3940256099942544/1033173712"
    val idVideoAd = "ca-app-pub-3940256099942544/5224354917"

    va idNativeAdLoading = "ca-app-pub-1553194436365145/1299673649"
    val idNativeAdDownloading = "ca-app-pub-1553194436365145/2203908811"
    val idInterAd = "ca-app-pub-1553194436365145/9410097958"
    val idVideoAd = "ca-app-pub-1553194436365145/1196566017"
     */


    internal fun initBannerAd(binding: ActivityMainBinding) {
        val typeAd = "Banner"
        val adRequest: AdRequest = AdRequest.Builder().build()
        binding.adMainBanner.loadAd(adRequest)
        binding.adMainBanner.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                SDAnalytics().eventAdClicked(typeAd)
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                SDAnalytics().eventAdClosed(typeAd)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                SDAnalytics().eventAdError(typeAd, adError.message)
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                SDAnalytics().eventAdOpened(typeAd)
            }
        }
    }

    internal fun initBannerAdLoading(v: View?) {
        val typeAd = "Banner"
        val adRequest: AdRequest = AdRequest.Builder().build()
        val banner: AdView = v!!.findViewById(com.lamesa.socialdown.R.id.adBannerLoading)
        banner.loadAd(adRequest)
        banner.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                SDAnalytics().eventAdClicked(typeAd)
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                SDAnalytics().eventAdClosed(typeAd)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                SDAnalytics().eventAdError(typeAd, adError.message)
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                SDAnalytics().eventAdOpened(typeAd)
            }
        }
    }

    internal fun initBannerAdDownloading(v: View?) {
        val typeAd = "Banner"
        val adRequest: AdRequest = AdRequest.Builder().build()
        val banner: AdView = v!!.findViewById(com.lamesa.socialdown.R.id.adBannerDownloading)
        banner.loadAd(adRequest)
        banner.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                SDAnalytics().eventAdClicked(typeAd)
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                SDAnalytics().eventAdClosed(typeAd)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                SDAnalytics().eventAdError(typeAd, adError.message)
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                SDAnalytics().eventAdOpened(typeAd)
            }
        }
    }

    internal fun showInterAd(context: Context) {
        val typeAd = "InterAd"
        val adRequest: AdRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, "ca-app-pub-1553194436365145/9410097958", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(@NonNull interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.c
                    mInterstitialAd = interstitialAd
                    if (mInterstitialAd != null) {
                        if (Build.VERSION.SDK_INT != 23) {
                            if ((1..2).random() == 2) {
                                mInterstitialAd?.show(context as Activity)
                            }
                        }
                    } else {
                        Log.d(TAG, "The interstitial ad wasn't ready yet.")
                    }
                }

                override fun onAdFailedToLoad(@NonNull loadAdError: LoadAdError) {
                    // Handle the error
                    Log.d(TAG, loadAdError.toString())
                    mInterstitialAd = null
                    SDAnalytics().eventAdError(typeAd, loadAdError.message)
                }
            })

        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
                SDAnalytics().eventAdClicked(typeAd)

            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mInterstitialAd = null
                SDAnalytics().eventAdClosed(typeAd)
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
                SDAnalytics().eventAdOpened(typeAd)
            }
        }

    }

    internal fun loadInterBonAd(context: Context) {
        val typeAd = "InterBonAd"
        RewardedInterstitialAd.load(context,
            "ca-app-pub-1553194436365145/4756588375", // ca-app-pub-1553194436365145/4756588375 --- // ca-app-pub-1553194436365145/9410097958
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    rewardedInterstitialAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    rewardedInterstitialAd = null
                    SDAnalytics().eventAdError(typeAd, adError.message)
                }
            })
    }

    private fun showInterBonAd(context: Context, dialog: BottomDialog? = null) {
        val typeAd = "InterBonAd"
        userReward = false
        if (rewardedInterstitialAd != null) {
            if (Build.VERSION.SDK_INT != 23) {
                //region rewardedInterstitialAd listener
                rewardedInterstitialAd!!.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d(TAG, "Ad was clicked.")
                            SDAnalytics().eventAdClicked(typeAd)
                        }

                        override fun onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            Log.d(TAG, "Ad dismissed fullscreen content.")
                            SDAnalytics().eventAdClosed(typeAd)
                            loadInterBonAd(context)
                            // Si se cierra el anuncio y se reclamo la recompensa se inica la descarga.
                            if (userReward && dialog != null) {
                                dialog.dismiss()
                            }
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            // Called when ad fails to show.
                            Log.e(TAG, "Ad failed to show fullscreen content.")
                            rewardedInterstitialAd = null
                            SDAnalytics().eventAdError(typeAd, adError.message)
                        }

                        override fun onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d(TAG, "Ad recorded an impression.")
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(TAG, "Ad showed fullscreen content.")
                            SDAnalytics().eventAdOpened(typeAd)
                        }
                    }
                //endregion
                //region show rewardedInterstitialAd
                rewardedInterstitialAd!!.show(context as Activity, OnUserEarnedRewardListener {
                    fun onUserEarnedReward(rewardItem: RewardItem) {
                        Log.d(TAG, "User earned reward.")
                        SDAnalytics().eventUserReward(typeAd)
                        userReward = true
                    }
                    onUserEarnedReward(it)
                })
                //endregion
            }
        } else {
            loadInterBonAd(context)
        }
    }

    internal fun loadVideoAd(context: Context) {
        val typeAd = "VideoAd"
        RequestConfiguration.Builder().setTestDeviceIds(listOf("A17F472B7E1F5BD692CD53DBE3100647"))
        RewardedAd.load(
            context,
            "ca-app-pub-1553194436365145/1196566017",
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedVideoAd = ad
                }
            })
    }

    private fun showVideoAd(context: Context, dialog: BottomDialog? = null) {
        val typeAd = "VideoAd"
        userReward = false
        if (rewardedVideoAd != null) {
            if (Build.VERSION.SDK_INT != 23) {
                // listener de anuncio
                rewardedVideoAd!!.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdShowedFullScreenContent() {
                            // Code to be invoked when the ad showed full screen content.
                            SDAnalytics().eventAdOpened(typeAd)
                        }

                        override fun onAdDismissedFullScreenContent() {
                            // Code to be invoked when the ad dismissed full screen content.
                            SDAnalytics().eventAdClosed(typeAd)
                            loadVideoAd(context)
                            if (userReward && dialog != null) {
                                dialog.dismiss()
                            }
                        }
                    }
                // mostrar anuncio
                rewardedVideoAd!!.show(
                    context as Activity,
                    OnUserEarnedRewardListener { rewardItem ->
                        SDAnalytics().eventUserReward(typeAd)
                        userReward = true
                    })
            }
        } else {
            loadVideoAd(context)
        }
    }

    internal fun showInterOrVideo(context: Context) {
        // Generamos un número aleatorio entre 1 y 8,
        // numInter aumenta la posibilidad de que sal un InterAd por encima de un VideoAd
        when ((1..8).random()) {
            2 -> showVideoAd(context)
            6 -> showInterAd(context)
            // Si el número aleatorio es cualquier otro valor
            else -> {}
        }
    }

    internal fun showAdToDownload(context: Context, dialog: BottomDialog?, typeAd: Int) {
        when (typeAd) {
            1 -> showInterBonAd(context, dialog)
            2 -> showVideoAd(context, dialog)
            // Si el número aleatorio es cualquier otro valor
            else -> {}
        }
    }

    internal fun adToDownloadIsLoaded(context: Context): Int {
        if (tinyDB.getBoolean(HasRated)) {
            return if (rewardedInterstitialAd != null) {
                1
            } else if (rewardedVideoAd != null) {
                2
            } else {
                loadInterBonAd(context)
                loadVideoAd(context)
                0
            }
        }
        return 0
    }

}