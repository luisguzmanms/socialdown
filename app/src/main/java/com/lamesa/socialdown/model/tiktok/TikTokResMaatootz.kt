package com.lamesa.socialdown.model.tiktok

import com.google.gson.annotations.SerializedName

data class TikTokResMaatootz(
    @SerializedName("video") var video:List<String>,
    @SerializedName("music") var music:List<String>,
    @SerializedName("error") var error:List<String>
    )

