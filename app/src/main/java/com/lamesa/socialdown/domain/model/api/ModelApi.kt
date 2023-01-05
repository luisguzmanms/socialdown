package com.lamesa.socialdown.domain.model.api

data class ModelApi(
    var app: String? = "",
    var autor: String? = "",
    var baseUrl: String? = "",
    var key: String? = "",
    var host: String? = "",
    val rapidApiUrl: String? = "",
    var queryPath: String? = ""
)
