package com.lamesa.socialdown.data.network.api

import com.lamesa.socialdown.domain.response.instagram.InstaResMaatootz
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url

/** Created by luis Mesa on 09/08/22 */
interface APIInsta {
    enum class APIs {
        Maatootz
    }

    /**
     * url: https://rapidapi.com/maatootz/api/instagram-downloader-download-instagram-videos-stories/
     * author: Maatootz X-RapidAPI-Key: 8a310e002fmsh7b0a3a11dfb8f16p1159c1jsnf5580def1147
     * X-RapidAPI-Host: instagram-downloader-download-instagram-videos-stories.p.rapidapi.com
     * baseURL: https://instagram-downloader-download-instagram-videos-stories.p.rapidapi.com/
     * patchQuery: index?url=
     */
    @Headers(
        "X-RapidAPI-Host: instagram-downloader-download-instagram-videos-stories.p.rapidapi.com"
    )
    @GET()
    suspend fun getDataMaatootz(
        @Header("X-RapidAPI-Key") X_RapidAPI_Key: String,
        @Url url: String
    ): Response<InstaResMaatootz>
}
