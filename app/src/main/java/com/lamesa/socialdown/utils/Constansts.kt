package com.lamesa.socialdown.utils

import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd

object Constansts {

    const val SHARE_APP_REQUEST_CODE = 1
    const val SHARE_ITEM_REQUEST_CODE = 2

    object Analytics {
        const val MediaDataExtracted = "MediaDataExtracted"
        const val ErrorApiData = "ErrorApiData"
        const val EventError = "EventError"
        const val MediaDownloaded = "MediaDownloaded"
        const val FailureDownload = "FailureDownload"
        const val EventAdClicked = "AdClicked"
        const val EventAdOpened = "AdOpened"
        const val EventAdClosed = "AdClosed"
        const val EventAdError = "AdError"
        const val EventTimingDownloadFile = "TimeDownloadedFile"
        const val EventShareApp = "ShareApp"
        const val EventShareItem = "ShareItem"
        const val EventViewDialogUpdate = "ViewDialogUpdate"
        const val EventViewDialogMessage = "ViewDialogMessage"
        const val EventAdUserReward = "AdUserReward"
        const val EventViewStars = "ViewStars"
        const val TBDownloads = "Downloads"

        // se guarda el ultimo evento enviado para evitar enviar spam
        var EventAdErrorSend = ""
        const val HasRated = "HasRated"
    }

    object Ad {
        var rewardedInterstitialAd: RewardedInterstitialAd? = null
        var rewardedVideoAd: RewardedAd? = null
        var userReward: Boolean = false
        var mInterstitialAd: InterstitialAd? = null
    }

    object Code {
        var attemptsCode = 0
    }

    object TinyDB {
        const val TB_DOWNLOADS_COUNT_KEY = "download_count"
        const val TB_LAST_DOWNLOAD_DAY_KEY = "last_download_time"
        const val TB_MAX_DOWNLOADS_PER_DAY = 4
    }

}


