package com.lamesa.socialdown.data.network.api

import FaceResVikar
import com.lamesa.socialdown.domain.response.facebook.FaceResCrashBash
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url

/** Created by luis Mesa on 09/08/22 */
interface APIFacebook {
    enum class APIs {
        Crashbash,
        Vikas
    }

    /**
     * url: https://rapidapi.com/CrashBash/api/socialdownloader/
     * author: CrashBash
     * X-RapidAPI-Host: instagram-downloader-download-instagram-videos-stories.p.rapidapi.com
     * baseUrl: https://socialdownloader.p.rapidapi.com/api/facebook/
     * queryPath: video?video_link=
     */
    @Headers("X-RapidAPI-Host: socialdownloader.p.rapidapi.com")
    @GET()
    suspend fun getDataCrashBash(
        @Header("X-RapidAPI-Key") X_RapidAPI_Key: String,
        @Url url: String
    ): Response<FaceResCrashBash>

    /**
     * url: https://rapidapi.com/CrashBash/api/socialdownloader/ author: Vikas X-RapidAPI-Key:
     * 8a310e002fmsh7b0a3a11dfb8f16p1159c1jsnf5580def1147 X-RapidAPI-Host:
     * facebook-reel-and-video-downloader.p.rapidapi.com baseUrl:
     * https://facebook-reel-and-video-downloader.p.rapidapi.com/app/ queryPath: main.php?url=
     */
    @Headers("X-RapidAPI-Host: facebook-reel-and-video-downloader.p.rapidapi.com")
    @GET()
    suspend fun getDataVikas(
        @Header("X-RapidAPI-Key") X_RapidAPI_Key: String,
        @Url url: String
    ): Response<FaceResVikar>
}
