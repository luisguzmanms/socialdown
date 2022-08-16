package com.lamesa.socialdown.domain.response.instagram

import com.google.gson.annotations.SerializedName

/** Created by luis Mesa on 09/08/22 */
data class InstaResMaatootz(
    @SerializedName("media") var linkDownloadMedia: String? = "",
    @SerializedName("thumbnail") var thumbnail: String? = "",
    @SerializedName("type") var Type: String? = "",
    @SerializedName("title") var title: String? = "",
    @SerializedName("error") var error: String? = "",
    @SerializedName("API") var api: String? = ""
)

