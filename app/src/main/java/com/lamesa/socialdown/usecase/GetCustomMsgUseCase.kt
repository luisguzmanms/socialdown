package com.lamesa.socialdown.usecase

import com.lamesa.socialdown.domain.repository.FirebaseRepository

class GetCustomMsgUseCase {

    operator fun invoke() {
        return FirebaseRepository().getDialogMsg()
    }

}