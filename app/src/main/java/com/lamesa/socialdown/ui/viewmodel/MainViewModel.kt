package com.lamesa.socialdown.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamesa.socialdown.app.SDApp
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.usecase.GetAllMediaByAppUseCase
import com.lamesa.socialdown.usecase.GetAllMediaUseCase
import com.lamesa.socialdown.utils.Constansts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    internal var tvTitleDownloader = MutableLiveData<String>()
    internal var lstMediaDownloadedDB: LiveData<List<ModelMediaDownloaded>> =
        GetAllMediaUseCase().invoke()
    private lateinit var lstDataDBByApp: LiveData<List<ModelMediaDownloaded>>

    val showDialogLimitReached = MutableLiveData(false)

    init {
        checkDownloadsLimit()
    }

    fun updateTitleDownloader(newTitle: String) {
        tvTitleDownloader.value = newTitle
    }

    internal fun getMediaByApp(app: String): LiveData<List<ModelMediaDownloaded>> {
        lstDataDBByApp = GetAllMediaByAppUseCase().invoke(app)
        return lstDataDBByApp
    }

    private fun checkDownloadsLimit() {
        viewModelScope.launch(Dispatchers.Default) {
            val currentCount = SDApp.Context.tinyDB.getInt(Constansts.TinyDB.TB_DOWNLOADS_COUNT_KEY)
            if (currentCount >= Constansts.TinyDB.TB_MAX_DOWNLOADS_PER_DAY) {
                showDialogLimitReached.postValue(true)
            }
        }
    }

}