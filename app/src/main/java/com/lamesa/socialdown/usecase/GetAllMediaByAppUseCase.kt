package com.lamesa.socialdown.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.domain.repository.RoomRepository

class GetAllMediaByAppUseCase {

    private val roomRepository = RoomRepository()

    operator fun invoke(app : String): LiveData<List<ModelMediaDownloaded>> {
        return roomRepository.getAllMediaByApp(app).asLiveData()
    }

}
