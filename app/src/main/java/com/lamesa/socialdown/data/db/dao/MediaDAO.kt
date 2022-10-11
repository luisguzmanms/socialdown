package com.lamesa.socialdown.data.db.dao

import androidx.room.*
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import kotlinx.coroutines.flow.Flow


@Dao
interface MediaDAO {

    @Query("SELECT * FROM ModelMediaDownloaded ORDER BY dateInsert DESC")
    fun getAllMedia(): Flow<List<ModelMediaDownloaded>>

    @Query("SELECT * FROM ModelMediaDownloaded WHERE fromApp = :app")
    fun getAllMediaByApp(app: String): Flow<List<ModelMediaDownloaded>>

    @Query("SELECT * FROM ModelMediaDownloaded WHERE fromApp = :app")
    suspend fun getMediaByApp(app: String): List<ModelMediaDownloaded>

    @Query("SELECT * FROM ModelMediaDownloaded WHERE id = :id")
    suspend fun getMediaById(id: Int): ModelMediaDownloaded

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(mediaDownloaded: ModelMediaDownloaded)

    @Delete
    suspend fun delete(mediaDownloaded: ModelMediaDownloaded)


}