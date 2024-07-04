package com.lamesa.socialdown.utils

import com.lamesa.socialdown.data.remote.api.WorldTimeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object WorldTimeUtil {
    private const val BASE_URL = "http://worldtimeapi.org/"

    suspend fun fetchCurrentDateTime(timeZone: String): Calendar? {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WorldTimeApiService::class.java)

        return try {
            val response = service.getTime(timeZone)
            if (response.isSuccessful) {
                println("Raw date response: ${response.body()?.datetime}")

                response.body()?.datetime?.let {
                    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
                    val date = format.parse(it)
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    calendar
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
