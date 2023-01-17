package com.lamesa.socialdown.domain.repository

import androidx.lifecycle.MutableLiveData
import com.lamesa.socialdown.data.firebase.XFirebaseDatabase
import com.lamesa.socialdown.domain.model.api.ModelApi

class FirebaseRepository {

    fun getAllDataApi(): ArrayList<ModelApi> {
        return XFirebaseDatabase().getDataApis()
    }

    fun getDataApiByApp(app: String): MutableLiveData<List<ModelApi>> {
        return XFirebaseDatabase().getDataApiByApp(app)
    }

    fun getDataApiById(app: String, apiId: String): MutableLiveData<ModelApi?> {
        return XFirebaseDatabase().getDataApiByAppID(app, apiId)
    }

    fun getDialogMsg() {
        return XFirebaseDatabase().getDialogMsg()
    }

    fun getDialogUpdate() {
        return XFirebaseDatabase().getDialogUpdate()
    }

}