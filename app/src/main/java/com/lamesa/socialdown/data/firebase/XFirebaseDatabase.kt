package com.lamesa.socialdown.data.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.lamesa.socialdown.BuildConfig.VERSION_CODE
import com.lamesa.socialdown.domain.model.api.ModelApi
import com.lamesa.socialdown.utils.DialogXUtils.NotificationX.showCustomMessage
import com.lamesa.socialdown.utils.DialogXUtils.NotificationX.showUpdate
import com.lamesa.socialdown.utils.DialogXUtils.ToastX
import com.lamesa.socialdown.utils.SDAnalytics

class XFirebaseDatabase : InterfaceFirebase {

    val listApi = ArrayList<ModelApi>()
    val listApiLive = MutableLiveData<List<ModelApi>>()
    var dataApiApp = MutableLiveData<ModelApi?>()

    override fun getReferenceDataApi(): DatabaseReference {
        return getDatabaseReference().child("data/api")
    }

    override fun getReferenceNotifications(): DatabaseReference {
        return getDatabaseReference().child("tool/notification")
    }

    private fun getReferenceDataApiApp(app: String, apiId: String): DatabaseReference {
        return getDatabaseReference().child("data/api/$app/$apiId")
    }

    override fun getDatabaseReference(): DatabaseReference {
        return Firebase.database.reference
    }

    override fun getDataApis(): ArrayList<ModelApi> {

        val listApi = ArrayList<ModelApi>()

        val dataApiListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    listApi.clear()
                    for (api in dataSnapshot.children) {
                        val appApi = api.getValue(ModelApi::class.java)
                        listApi.add(appApi!!)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val error = "Error code:${databaseError.code} msg:${databaseError.message}"
                SDAnalytics().eventError(error)
                ToastX.showError(error)
            }
        }

        getReferenceDataApi().addValueEventListener(dataApiListener)
        return listApi

    }

    override fun getDataApiByApp(app: String): MutableLiveData<List<ModelApi>> {

        val dataApiListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    listApi.clear()
                    val appApi = dataSnapshot.children.filter { it.child("app").value == app }
                    for (api in appApi) {
                        listApi.add(api.getValue(ModelApi::class.java)!!)
                    }
                    return listApiLive.postValue(listApi)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val error = "Error code:${databaseError.code} msg:${databaseError.message}"
                SDAnalytics().eventError(error)
                ToastX.showError(error)
            }
        }

        getReferenceDataApi().addListenerForSingleValueEvent(dataApiListener)
        return listApiLive
    }

    override fun getDataApiByAppID(app: String, apiId: String): MutableLiveData<ModelApi?> {

        val dataApiListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val api = dataSnapshot.getValue(ModelApi::class.java)
                    return dataApiApp.postValue(api)
                } else {
                    dataApiApp.postValue(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val error = "Error code:${databaseError.code} msg:${databaseError.message}"
                SDAnalytics().eventError(error)
                ToastX.showError(error)
            }
        }

        getReferenceDataApiApp(app, apiId).addListenerForSingleValueEvent(dataApiListener)
        return dataApiApp
    }

    override fun getDialogMsg() {
        val ref = "message"
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val show = dataSnapshot.child(ref).child("show").getValue(Boolean::class.java)
                val cancelable =
                    dataSnapshot.child(ref).child("cancelable").getValue(Boolean::class.java)
                if (show != null && show) {
                    val title = dataSnapshot.child(ref).child("title").getValue(String::class.java)
                    val msg = dataSnapshot.child(ref).child("msg").getValue(String::class.java)

                    if (title != null && msg != null) {
                        if (cancelable != null) {
                            if (cancelable) {
                                showCustomMessage(title, msg)
                            } else {
                                showCustomMessage(title, msg).autoDismiss(100000)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        getReferenceNotifications().addValueEventListener(listener)
    }

    override fun getDialogUpdate() {
        val ref = "update"
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val show = dataSnapshot.child(ref).child("show").getValue(Boolean::class.java)
                val version = dataSnapshot.child(ref).child("version").getValue(Int::class.java)
                val cancelable =
                    dataSnapshot.child(ref).child("cancelable").getValue(Boolean::class.java)
                if (version != null && version > VERSION_CODE) {
                    if (show != null && show) {
                        if (cancelable != null) {
                            if (cancelable) {
                                showUpdate()!!.show()
                            } else {
                                showUpdate()!!.setCancelable(cancelable).show()
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        getReferenceNotifications().addValueEventListener(listener)
    }

}