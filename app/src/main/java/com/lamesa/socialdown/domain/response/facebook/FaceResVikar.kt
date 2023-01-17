package com.lamesa.socialdown.domain.response.facebook

import com.google.gson.annotations.SerializedName

data class FaceResVikar(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("thumbnail") var thumbnail: String? = null,
    @SerializedName("links") var body: Links? = Links()
)

data class Links(
    @SerializedName("Download Low  Quality") var video: String? = null,
    @SerializedName("Download High Quality") var videoFHD: String? = null
)
