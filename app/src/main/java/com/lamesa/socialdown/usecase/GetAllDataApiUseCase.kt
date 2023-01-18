package com.lamesa.socialdown.usecase

import com.lamesa.socialdown.domain.model.api.ModelApi
import com.lamesa.socialdown.domain.repository.FirebaseRepository

class GetAllDataApiUseCase {

    operator fun invoke(): ArrayList<ModelApi> {
        return FirebaseRepository().getAllDataApi()
    }

}