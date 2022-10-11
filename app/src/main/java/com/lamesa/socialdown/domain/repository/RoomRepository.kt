package com.lamesa.socialdown.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.lamesa.socialdown.app.SDApp.Context.roomDatabase
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomRepository @Inject constructor() {

    var room = roomDatabase

    fun getAllMediaFromRoom(): LiveData<List<ModelMediaDownloaded>> {
        return room.mediaDAO().getAllMedia().asLiveData()
    }

    fun getAllMediaByApp(app: String): Flow<List<ModelMediaDownloaded>> {
        return roomDatabase.mediaDAO().getAllMediaByApp(app)
    }

}