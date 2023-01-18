package com.lamesa.socialdown.domain.response.tiktok

import com.google.gson.annotations.SerializedName

data class TikTokResMaatootz(
    @SerializedName("video") var linkDownloadMp4: List<String>? = null,
    @SerializedName("music") var linkDownloadMp3: List<String>? = null,
    @SerializedName("error") var error: List<String>? = null
)

data class TikTokResMaatootz2(
    @SerializedName("video") var linkDownloadMp4: List<String>? = null,
    @SerializedName("music") var linkDownloadMp3: List<String>? = null,
    @SerializedName("error") var error: List<String>? = null
)
