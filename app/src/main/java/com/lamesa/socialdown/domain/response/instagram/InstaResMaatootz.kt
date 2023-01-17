package com.lamesa.socialdown.domain.response.instagram

import com.google.gson.annotations.SerializedName

data class InstaResMaatootz(
    @SerializedName("media") var linkDownloadMedia: Any? = "",
    @SerializedName("thumbnail") var thumbnail: String? = "",
    @SerializedName("type") var Type: String? = "",
    @SerializedName("title") var title: Any? = "",
    @SerializedName("error") var error: Any? = "",
    @SerializedName("API") var api: String? = ""
)

