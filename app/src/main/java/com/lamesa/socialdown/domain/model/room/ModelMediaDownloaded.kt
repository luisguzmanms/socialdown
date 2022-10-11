package com.lamesa.socialdown.domain.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ModelMediaDownloaded(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "mediaType") var mediaType: String,
    @ColumnInfo(name = "fromApp") val fromApp: String,
    @ColumnInfo(name = "fileName") var fileName: String,
    @ColumnInfo(name = "folderPatch") val folderPatch: String,
    @ColumnInfo(name = "filePatch") val filePatch: String,
    @ColumnInfo(name = "dateInsert") val dateInsert: String
)