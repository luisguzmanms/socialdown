package com.lamesa.socialdown.domain.repository

import com.lamesa.socialdown.app.SDApp.Context.roomDatabase
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class RoomRepository @Inject constructor() {

    var room = roomDatabase

    fun getAllMediaFromRoom(): Flow<List<ModelMediaDownloaded>> {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

        return room.mediaDAO().getAllMedia().map { mediaList ->
            mediaList.sortedByDescending {
                try {
                    formatter.parse(it.dateInsert)
                } catch (e: ParseException) {
                    Date(Long.MIN_VALUE)
                }
            }
        }
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