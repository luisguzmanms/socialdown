package com.lamesa.socialdown.app

import android.app.Application
import android.content.res.Resources
import com.amplitude.api.Amplitude
import com.google.ads.mediation.inmobi.InMobiConsent
import com.google.android.gms.ads.MobileAds
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
    }

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            roomDatabase = RoomModule.provideRoom(applicationContext)
        }
    }

}



