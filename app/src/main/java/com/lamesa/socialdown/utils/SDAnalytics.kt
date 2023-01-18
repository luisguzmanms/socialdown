package com.lamesa.socialdown.utils

import android.os.Bundle
import com.amplitude.api.Amplitude
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.lamesa.socialdown.app.SDApp.Analytics.firebaseAnalytics
import com.lamesa.socialdown.app.SDApp.Analytics.mixpanel
import com.lamesa.socialdown.app.SDApp.Context.tinyDB
import com.lamesa.socialdown.domain.model.api.ModelMediaDataExtracted
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.utils.Constansts.Analytics.ErrorApiData
import com.lamesa.socialdown.utils.Constansts.Analytics.EventAdClicked
import com.lamesa.socialdown.utils.Constansts.Analytics.EventAdClosed
import com.lamesa.socialdown.utils.Constansts.Analytics.EventAdError
import com.lamesa.socialdown.utils.Constansts.Analytics.EventAdErrorSend
import com.lamesa.socialdown.utils.Constansts.Analytics.EventAdOpened
import com.lamesa.socialdown.utils.Constansts.Analytics.EventAdUserReward
import com.lamesa.socialdown.utils.Constansts.Analytics.EventError
import com.lamesa.socialdown.utils.Constansts.Analytics.EventShareApp
import com.lamesa.socialdown.utils.Constansts.Analytics.EventShareItem
import com.lamesa.socialdown.utils.Constansts.Analytics.EventViewDialogMessage
import com.lamesa.socialdown.utils.Constansts.Analytics.EventViewDialogUpdate
import com.lamesa.socialdown.utils.Constansts.Analytics.EventViewStars
import com.lamesa.socialdown.utils.Constansts.Analytics.FailureDownload
import com.lamesa.socialdown.utils.Constansts.Analytics.MediaDataExtracted
import com.lamesa.socialdown.utils.Constansts.Analytics.MediaDownloaded
import com.lamesa.socialdown.utils.Constansts.Analytics.TBDownloads
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

class SDAnalytics {

    fun eventMediaDataExtracted(dataExtracted: ModelMediaDataExtracted) {
        //region Analytics
        val props = JSONObject()
        props.put("App", dataExtracted.app)
        props.put("QueryLink", dataExtracted.queryLink)
        props.put("TypeMedia", dataExtracted.mediaType)
        props.put("CodeResponse", dataExtracted.codeResponse)
        props.put("Body", dataExtracted.body)
        props.put("Raw", dataExtracted.raw)
        props.put("Key", dataExtracted.key)

        mixpanel.track(MediaDataExtracted, props)
        //endregion
        //region Firebase Analytics
        firebaseAnalytics.logEvent(MediaDataExtracted) {
            param("App", dataExtracted.app!!)
            param("QueryLink", dataExtracted.queryLink!!)
            param("TypeMedia", dataExtracted.mediaType!!)
            param("CodeResponse", dataExtracted.codeResponse!!)
            param("Body", dataExtracted.body!!)
            param("Raw", dataExtracted.raw!!)
            param("Key", dataExtracted.key!!)
        }
        //endregion
        //region Amplitude Analytics
        Amplitude.getInstance().logEvent(MediaDataExtracted, props)
        //endregion
    }

    fun eventErrorApiData(dataExtracted: ModelMediaDataExtracted) {
        //region Analytics
        val props = JSONObject()
        props.put("App", dataExtracted.app)
        props.put("QueryLink", dataExtracted.queryLink)
        props.put("TypeMedia", dataExtracted.mediaType)
        props.put("CodeResponse", dataExtracted.codeResponse)
        props.put("Body", dataExtracted.body)
        props.put("Raw", dataExtracted.raw)
        props.put("Key", dataExtracted.key)

        mixpanel.track(ErrorApiData, props)
        //endregion
        //region Firebase Analytics
        firebaseAnalytics.logEvent(ErrorApiData) {
            param("App", dataExtracted.app!!)
            param("QueryLink", dataExtracted.queryLink!!)
            param("TypeMedia", dataExtracted.mediaType!!)
            param("CodeResponse", dataExtracted.codeResponse!!)
            param("Body", dataExtracted.body!!)
            param("Raw", dataExtracted.raw!!)
            param("Key", dataExtracted.key!!)
        }
        //endregion
    }

    fun eventError(msg: String) {
        //region Analytics
        val props = JSONObject()
        props.put("Error", msg)
        mixpanel.track(EventError, props)
        //endregion
        //region Firebase Analytics
        firebaseAnalytics.logEvent(EventError) {
            param("Error", msg)
        }
        //endregion
    }

    fun eventMediaDownloaded(modelMediaDownloaded: ModelMediaDownloaded) {
        //region Analytics
        val props = JSONObject()
        props.put("App", modelMediaDownloaded.fromApp)
        props.put("QueryLink", modelMediaDownloaded.queryLink)
        props.put("TypeMedia", modelMediaDownloaded.mediaType)
        mixpanel.track(MediaDownloaded, props)
        //endregion
        //region Firebase Analytics
        firebaseAnalytics.logEvent(MediaDownloaded) {
            param("App", modelMediaDownloaded.fromApp)
            param("QueryLink", modelMediaDownloaded.queryLink)
            param("TypeMedia", modelMediaDownloaded.mediaType)
        }
        //endregion
        // Aumentar contador de descargas en Mixpanel
        tinyDB.putInt(TBDownloads, tinyDB.getInt(TBDownloads) + 1)
        mixpanel.people.withIdentity(mixpanel.distinctId).increment("Downloads", 1.0)
    }

    fun eventFailureDownload(error: String, modelMediaDownloaded: ModelMediaDownloaded) {
        //region Analytics
        val props = JSONObject()
        props.put("App", modelMediaDownloaded.fromApp)
        props.put("QueryLink", modelMediaDownloaded.queryLink)
        props.put("TypeMedia", modelMediaDownloaded.mediaType)
        props.put("Error", error)
        mixpanel.track(FailureDownload, props)
        //endregion
        //region Firebase Analytics
        firebaseAnalytics.logEvent(FailureDownload) {
            param("App", modelMediaDownloaded.fromApp)
            param("QueryLink", modelMediaDownloaded.queryLink)
            param("TypeMedia", modelMediaDownloaded.mediaType)
            param("Error", error)
        }
        //endregion
    }

    fun eventTiming(): MixpanelAPI {
        return mixpanel
    }

    //region AdMob Analitycs
    fun eventAdClicked(type: String) {
        //region Analytics
        val props = JSONObject()
        props.put("Ad", type)
        mixpanel.track(EventAdClicked, props)
        //endregion
        //region Firebase Analytics
        firebaseAnalytics.logEvent(FailureDownload) {
            param("Ad", type)
        }
        //endregion
    }

    fun eventAdOpened(type: String) {
        //region Analytics
        val props = JSONObject()
        props.put("Ad", type)
        mixpanel.track(EventAdOpened, props)
        //endregion
        //region Firebase Analytics
        firebaseAnalytics.logEvent(EventAdOpened) {
            param("Ad", type)
        }
        //endregion
    }

    fun eventAdClosed(type: String) {
        //region Analytics
        val props = JSONObject()
        props.put("Ad", type)
        mixpanel.track(EventAdClosed, props)
        //endregion
        //region Firebase Analytics
        firebaseAnalytics.logEvent(EventAdClosed) {
            param("Ad", type)
        }
        //endregion
    }

    fun eventAdError(type: String, errorMessage: String) {
        if (EventAdErrorSend != errorMessage) {
            //region Analytics
            val props = JSONObject()
            props.put("Ad", type)
            props.put("Error", errorMessage)
            mixpanel.track(EventAdError, props)
            //endregion
            //region Firebase Analytics
            firebaseAnalytics.logEvent(EventAdError) {
                param("Ad", type)
                param("Error", errorMessage)
            }
            //endregion
            EventAdErrorSend = errorMessage
        }
    }
    //enregion

    fun eventShareItem() {
        //region Analytics
        val props = JSONObject()
        props.put("Event", EventShareItem)
        mixpanel.track(EventShareItem, props)
        //endregion
        //region Firebase Analytics
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, EventShareItem)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "media")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle)
        //endregion
        //region Amplitude Analytics
        Amplitude.getInstance().logEvent(EventShareItem, props)
        //endregion
        tinyDB.putInt(EventShareItem, tinyDB.getInt(EventShareItem) + 1)
        mixpanel.people.withIdentity(mixpanel.distinctId).increment(EventShareItem, 1.0)
    }

    fun eventShareApp() {
        //region Analytics
        val props = JSONObject()
        props.put("Event", EventShareApp)
        mixpanel.track(EventShareApp, props)
        //endregion
        //region Firebase Analytics
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, EventShareApp)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "app")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle)
        //endregion
        //region Amplitude Analytics
        Amplitude.getInstance().logEvent(EventShareApp, props)
        //endregion
    }

    fun eventViewUpdate() {
        //region Analytics
        val props = JSONObject()
        props.put("Event", EventViewDialogUpdate)
        mixpanel.track(EventViewDialogUpdate, props)
        //endregion
        //region Firebase Analytics
        firebaseAnalytics.logEvent(EventViewDialogUpdate) {
            param("Event", EventViewDialogUpdate)
        }
        //endregion
        //region Amplitude Analytics
        Amplitude.getInstance().logEvent(EventViewDialogUpdate, props)
        //endregion
    }

    fun eventViewMessage() {
        //region Analytics
        val props = JSONObject()
        props.put("Event", EventViewDialogMessage)
        mixpanel.track(EventViewDialogMessage, props)
        //endregion
        //region Firebase Analytics
        firebaseAnalytics.logEvent(EventViewDialogMessage) {
            param("Event", EventViewDialogMessage)
        }
        //endregion
        //region Amplitude Analytics
        Amplitude.getInstance().logEvent(EventViewDialogMessage, props)
        //endregion
    }

    fun eventViewStars() {
        //region Analytics
        val props = JSONObject()
        props.put("Event", EventViewStars)
        mixpanel.track(EventViewStars, props)
        //endregion
        //region Firebase Analytics
        firebaseAnalytics.logEvent(EventViewStars) {
            param("Event", EventViewStars)
        }
        //endregion
        //region Amplitude Analytics
        Amplitude.getInstance().logEvent(EventViewStars, props)
        //endregion
    }

}
