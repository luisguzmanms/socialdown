package com.lamesa.socialdown.domain.repository

import com.lamesa.socialdown.app.SDApp.Context.roomDatabase
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomRepository @Inject constructor() {

    var room = roomDatabase

    fun getAllMediaFromRoom(): Flow<List<ModelMediaDownloaded>> {
        return room.mediaDAO().getAllMedia()
    }

    fun getAllMediaByApp(app: String): Flow<List<ModelMediaDownloaded>> {
        return roomDatabase.mediaDAO().getAllMediaByApp(app)
    }

    suspend fun deleteMediaFromRoom(modelMediaDownloaded: ModelMediaDownloaded) {
        return roomDatabase.mediaDAO().deleteMedia(modelMediaDownloaded)
    }

    suspend fun addMediaFromRoom(modelMediaDownloaded: ModelMediaDownloaded) {
        return roomDatabase.mediaDAO().insertMedia(modelMediaDownloaded)
    }

}