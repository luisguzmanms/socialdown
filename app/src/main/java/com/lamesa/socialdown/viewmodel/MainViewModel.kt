package com.lamesa.socialdown.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lamesa.socialdown.app.SDApp.Context.roomDatabase
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.usecase.GetAllMediaUseCase

class MainViewModel : ViewModel() {

    internal var tvTitleDownloader = MutableLiveData<String>()
    internal var lstMediaDownloadedDB: LiveData<List<ModelMediaDownloaded>> =
        GetAllMediaUseCase().invoke()
    private lateinit var lstDataDBByApp: LiveData<List<ModelMediaDownloaded>>

    fun updateTitleDownloader(newTitle: String) {
        tvTitleDownloader.value = newTitle
    }

    internal fun getMediaByApp(app: String): LiveData<List<ModelMediaDownloaded>> {
        lstDataDBByApp = roomDatabase.mediaDAO().getAllMediaByApp(app).asLiveData()
        return lstDataDBByApp
    }

    fun lstDataDB(): LiveData<List<ModelMediaDownloaded>> {
        return lstMediaDownloadedDB
    }

}