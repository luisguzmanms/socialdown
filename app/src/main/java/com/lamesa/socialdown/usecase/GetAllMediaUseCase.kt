package com.lamesa.socialdown.usecase

import androidx.lifecycle.LiveData
import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.domain.repository.RoomRepository

class GetAllMediaUseCase {

    private val roomRepository = RoomRepository()

    operator fun invoke(): LiveData<List<ModelMediaDownloaded>> {
        return roomRepository.getAllMediaFromRoom()
    }
}
