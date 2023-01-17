package com.lamesa.socialdown.data.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.lamesa.socialdown.domain.model.api.ModelApi

/**
 * Created by luis Mesa on 31/10/22.
 */
interface InterfaceFirebase {

    fun getDatabaseReference(): DatabaseReference
    fun getReferenceDataApi(): DatabaseReference
    fun getDataApis(): ArrayList<ModelApi>
    fun getDataApiByApp(app: String): MutableLiveData<List<ModelApi>>
    fun getDataApiByAppID(app: String, apiId: String): MutableLiveData<ModelApi?>

    fun getDialogMsg()
    fun getDialogUpdate()
    fun getReferenceNotifications(): DatabaseReference
}