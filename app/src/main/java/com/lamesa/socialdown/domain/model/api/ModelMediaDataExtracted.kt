package com.lamesa.socialdown.domain.model.api

data class ModelMediaDataExtracted(
    var app: String? = "",
    var queryLink: String? = "",
    var linksToDownload: List<String>? = emptyList(),
    var codeResponse: String? = "",
    var body: String? = "",
    var raw: String? = "",
    var title: String? = "",
    var author: String? = "",
    var mediaType: String? = ""
)
