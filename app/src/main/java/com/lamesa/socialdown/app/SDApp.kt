package com.lamesa.socialdown.app

import android.app.Application
import com.lamesa.socialdown.app.SDApp.Context.roomDatabase
import com.lamesa.socialdown.data.db.MediaDB
import com.lamesa.socialdown.di.RoomModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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



