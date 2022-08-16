package com.lamesa.socialdown.domain.response.facebook

import com.google.gson.annotations.SerializedName

/** Created by luis Mesa on 09/08/22 */
data class FaceResCrashBash(
    @SerializedName("hasError") var hasError: Boolean? = null,
    @SerializedName("errorCode") var errorCode: Int? = null,
    @SerializedName("errorMessage") var errorMessage: String? = null,
    @SerializedName("errorDescription") var errorDescription: String? = null,
    @SerializedName("body") var body: Body? = Body()
)

data class Body(

    @SerializedName("video") var video: String? = null,
    @SerializedName("videoHD") var linkDownloadVideo: String? = null

)