package com.lamesa.socialdown.data.remote.api

import com.lamesa.socialdown.domain.response.tiktok.TikTokResMaatootz
import com.lamesa.socialdown.domain.response.tiktok.TikTokResMaatootz2
import com.lamesa.socialdown.domain.response.tiktok.TiktokResYi005
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url

/** Created by luis Mesa on 09/08/22 */
interface APITiktok {
    enum class APIs(val id: String) {
        Maatootz("Maatootz"),
        Maatootz2("Maatootz2"),
        Yi005("Yi005")
    }

    /**
     * url: https://rapidapi.com/maatootz/api/tiktok-downloader-download-tiktok-videos-without-watermark/
     * author: Maatootz
     * X-RapidAPI-Host: tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.comtiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com"
     * baseUrl: https://tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com/vid/
     * patchQuery: index?url=
     */
    @Headers(
        "X-RapidAPI-Host: tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.comtiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com"
    )
    @GET()
    suspend fun getDataMaatootz(
        @Header("X-RapidAPI-Key") X_RapidAPI_Key: String,
        @Url url: String
    ): Response<TikTokResMaatootz>

    /**
     * url: https://rapidapi.com/yi005/api/tiktok-download-without-watermark/
     * author: Yi005
     * X-RapidAPI-Host: tiktok-download-without-watermark.p.rapidapi.com
     * baseUrl: https://tiktok-download-without-watermark.p.rapidapi.com/
     * patchQuery: analysis?url=
     */
    @Headers("X-RapidAPI-Host: tiktok-download-without-watermark.p.rapidapi.com")
    @GET()
    suspend fun getDataYi005(
        @Header("X-RapidAPI-Key") X_RapidAPI_Key: String,
        @Url url: String
    ): Response<TiktokResYi005>

    /**
     * url: https://rapidapi.com/maatootz/api/tiktok-full-info-without-watermark/
     * author: Maatootz2
     * X-RapidAPI-Host: tiktok-full-info-without-watermark.p.rapidapi.com
     * baseUrl: https://tiktok-full-info-without-watermark.p.rapidapi.com/vid/
     * QueryPatch: index?url=
     */
    @Headers("X-RapidAPI-Host: tiktok-full-info-without-watermark.p.rapidapi.com")
    @GET()
    suspend fun getDataMaatootz2(
        @Header("X-RapidAPI-Key") X_RapidAPI_Key: String,
        @Url url: String
    ): Response<TikTokResMaatootz2>
}
