package com.lamesa.socialdown.api

import com.lamesa.socialdown.model.tiktok.TikTokResMaatootz
import com.lamesa.socialdown.model.tiktok.TiktokResYi005
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url


interface APITiktok  {

    @Headers(
        "X-RapidAPI-Host: tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.comtiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com"
    )

    /**
     * url: https://rapidapi.com/maatootz/api/tiktok-downloader-download-tiktok-videos-without-watermark/
     * author: maatootz
     */
    @GET()
    suspend fun getLinkVideoMaatootz(@Header("X-RapidAPI-Key") X_RapidAPI_Key : String, @Url url:String): Response<TikTokResMaatootz>



    /**
     * url: https://rapidapi.com/yi005/api/tiktok-download-without-watermark/
     * author: yi005
     * X-RapidAPI-Key: 8a310e002fmsh7b0a3a11dfb8f16p1159c1jsnf5580def1147
     * X-RapidAPI-Host: tiktok-download-without-watermark.p.rapidapi.com
     */
    @Headers(
        "X-RapidAPI-Host: tiktok-download-without-watermark.p.rapidapi.com"
    )
    @GET()
    suspend fun getLinkVideoYi005(@Header("X-RapidAPI-Key") X_RapidAPI_Key : String, @Url url:String): Response<TiktokResYi005>


}