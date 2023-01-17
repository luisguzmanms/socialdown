package com.lamesa.socialdown.domain.model.api

data class ModelMediaDataExtracted(
    var author: String? = "",
    var app: String? = "",
    var body: String? = "",
    var codeResponse: String? = "",
    var key: String? = "",
    var linksToDownload: List<String>? = emptyList(),
    var mediaType: String? = "",
    var queryLink: String? = "",
    var raw: String? = "",
    var title: String? = ""
)
