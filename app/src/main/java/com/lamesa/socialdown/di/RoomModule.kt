package com.lamesa.socialdown.di

import android.content.Context
import androidx.room.Room
import com.lamesa.socialdown.data.db.MediaDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
object RoomModule {

    private const val MEDIA_DOWNLOADED_DATABASE = "media_database"

    @Singleton
    @Provides
    fun provideRoom(context: Context) =
        Room.databaseBuilder(context, MediaDB::class.java, MEDIA_DOWNLOADED_DATABASE)
            .allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun provideMediaDao(db: MediaDB) = db.mediaDAO()
}