package com.lamesa.socialdown.usecase

import com.lamesa.socialdown.domain.repository.FirebaseRepository

class GetUpdateUseCase {

    operator fun invoke() {
        return FirebaseRepository().getDialogUpdate()
    }

}