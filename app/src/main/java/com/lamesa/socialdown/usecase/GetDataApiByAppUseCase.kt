package com.lamesa.socialdown.usecase

import androidx.lifecycle.MutableLiveData
import com.lamesa.socialdown.domain.model.api.ModelApi
import com.lamesa.socialdown.domain.repository.FirebaseRepository

class GetDataApiByAppUseCase {

    internal fun getApiByApp( app : String): MutableLiveData<List<ModelApi>> {
        return FirebaseRepository().getDataApiByApp(app)
    }

}