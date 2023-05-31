package com.lamesa.socialdown.app

import android.app.Application
import android.content.res.Resources
import com.amplitude.api.Amplitude
import com.google.ads.mediation.inmobi.InMobiConsent
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.inmobi.sdk.InMobiSdk
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.MIUIStyle
import com.lamesa.socialdown.app.SDApp.Analytics.mixpanel
import com.lamesa.socialdown.app.SDApp.Context.roomDatabase
import com.lamesa.socialdown.app.SDApp.Context.tinyDB
import com.lamesa.socialdown.data.db.MediaDB
import com.lamesa.socialdown.di.RoomModule
import com.lamesa.socialdown.utils.TinyDB
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.unity3d.ads.metadata.MetaData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

/** Created by luis Mesa on 08/08/22 */
class SDApp : Application() {

    object Context {
        internal lateinit var roomDatabase: MediaDB
        internal lateinit var resources: Resources
        internal lateinit var tinyDB: TinyDB
    }

    // Analytics
    private val MIXPANEL_API_TOKEN = "b2c8ac3a977a99c02d47a6099b05dc52"

    object Analytics {
        internal lateinit var mixpanel: MixpanelAPI
        internal var firebaseAnalytics = Firebase.analytics
    }

    override fun onCreate() {
        super.onCreate()
        initDialogX()
        initFirebase()
        initMixPanel()
        initAmplitude()
        initMobileAds()
        CoroutineScope(Dispatchers.IO).launch {
            roomDatabase = RoomModule.provideRoom(applicationContext)
        }
        Context.resources = resources
        tinyDB = TinyDB(this)

    }

    private fun initAmplitude() {
        Amplitude.getInstance().initialize(this, "bfc345c331841705506a208d68fd8bc7")
            .enableForegroundTracking(
                applicationContext as Application?
            )
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
        Firebase.database.setPersistenceEnabled(true)
    }

    private fun initMixPanel() {
        mixpanel = MixpanelAPI.getInstance(this, MIXPANEL_API_TOKEN, true)
    }

    private fun initDialogX() {
        DialogX.init(this)
        DialogX.globalTheme = DialogX.THEME.AUTO
        DialogX.globalStyle = MIUIStyle.style()
        DialogX.implIMPLMode = DialogX.IMPL_MODE.VIEW
    }

    private fun initMobileAds() {
        MobileAds.initialize(this)


        val configuration = RequestConfiguration.Builder()
            .setTestDeviceIds(listOf("A17F472B7E1F5BD692CD53DBE3100647")).build()
        MobileAds.setRequestConfiguration(configuration)



        //region Unity Ads
        val gdprMetaData = MetaData(this)
        gdprMetaData["gdpr.consent"] = true
        gdprMetaData.commit()
        val ccpaMetaData = MetaData(this)
        ccpaMetaData["privacy.consent"] = true
        ccpaMetaData.commit()
        //endregion

        //region inMobii
        val consentObject = JSONObject()
        try {
            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true)
            consentObject.put("gdpr", "1")
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }

        InMobiConsent.updateGDPRConsent(consentObject)
        //endregion
    }

}



