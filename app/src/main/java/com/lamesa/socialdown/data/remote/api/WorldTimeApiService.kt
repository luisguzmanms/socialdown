package com.lamesa.socialdown.data.remote.api

import com.lamesa.socialdown.domain.response.WorldTimeApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WorldTimeApiService {

    @GET("api/timezone/{timeZone}")
    suspend fun getTime(
        @Path(
            "timeZone",
            encoded = true
        ) timeZone: String
    ): Response<WorldTimeApiResponse>

}
