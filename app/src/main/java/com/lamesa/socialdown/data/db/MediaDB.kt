package com.lamesa.socialdown.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lamesa.socialdown.data.db.dao.MediaDAO
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded

@Database(entities = [ModelMediaDownloaded::class], version = 1)
abstract class MediaDB : RoomDatabase() {

    abstract fun mediaDAO(): MediaDAO

}