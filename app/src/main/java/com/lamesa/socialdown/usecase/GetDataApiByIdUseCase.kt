package com.lamesa.socialdown.usecase

import androidx.lifecycle.MutableLiveData
import com.lamesa.socialdown.domain.model.api.ModelApi
import com.lamesa.socialdown.domain.repository.FirebaseRepository

class GetDataApiByIdUseCase {

    internal fun getApiById( app : String, apiId: String): MutableLiveData<ModelApi?> {
        return FirebaseRepository().getDataApiById(app,apiId)
    }

}