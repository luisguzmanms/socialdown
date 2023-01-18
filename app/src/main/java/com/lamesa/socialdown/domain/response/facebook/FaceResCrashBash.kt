package com.lamesa.socialdown.domain.response.facebook

import com.google.gson.annotations.SerializedName


data class FaceResCrashBash(
    @SerializedName("hasError") var hasError: Boolean? = null,
    @SerializedName("errorCode") var errorCode: Int? = null,
    @SerializedName("errorMessage") var errorMessage: String? = null,
    @SerializedName("errorDescription") var errorDescription: String? = null,
    @SerializedName("body") var body: Body? = Body()
)

data class Body(

    @SerializedName("video") var video: String? = null,
    @SerializedName("videoHD") var videoFHD: String? = null

)