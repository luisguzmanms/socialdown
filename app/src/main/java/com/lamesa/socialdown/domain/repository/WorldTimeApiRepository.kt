package com.lamesa.socialdown.domain.repository

import com.lamesa.socialdown.data.remote.api.WorldTimeApiService
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.TimeZone

class WorldTimeApiRepository(
    private val worldTimeApiService: WorldTimeApiService
) {
    suspend fun getCurrentTime(): Date {
        val timeZone = TimeZone.getDefault().id
        val response = worldTimeApiService.getTime(timeZone)

        if (response.isSuccessful) {
            val datetimeString = response.body()?.datetime
            val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val localDateTime = LocalDateTime.parse(datetimeString, formatter)
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
        } else {
            throw Exception("Could not fetch current time from API")
        }
    }
}
