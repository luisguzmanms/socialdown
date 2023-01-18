package com.lamesa.socialdown.usecase

import com.lamesa.socialdown.domain.model.room.ModelMediaDownloaded
import com.lamesa.socialdown.domain.repository.RoomRepository

class DeleteMediaUseCase {

    private val roomRepository = RoomRepository()

    suspend operator fun invoke(mediaDownloaded: ModelMediaDownloaded){
        return roomRepository.deleteMediaFromRoom(mediaDownloaded)
    }

}