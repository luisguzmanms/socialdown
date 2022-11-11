package com.lamesa.socialdown.utils

import com.google.firebase.analytics.ktx.logEvent
import com.lamesa.socialdown.app.SDApp.Analytics.firebaseAnalytics
import com.lamesa.socialdown.app.SDApp.Analytics.mixpanel
import com.lamesa.socialdown.domain.model.api.ModelMediaDataExtracted
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.utils.Constansts.Analytics.ErrorApiData
import com.lamesa.socialdown.utils.Constansts.Analytics.EventError
import com.lamesa.socialdown.utils.Constansts.Analytics.FailureDownload
import com.lamesa.socialdown.utils.Constansts.Analytics.MediaDataExtracted
import com.lamesa.socialdown.utils.Constansts.Analytics.MediaDownloaded
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

        mixpanel.track(MediaDataExtracted, props)
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
    }

    fun evenetFailureDownload(error: String, modelMediaDownloaded: ModelMediaDownloaded) {
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

}
