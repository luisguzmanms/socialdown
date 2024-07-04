package com.lamesa.socialdown.domain.response

data class WorldTimeApiResponse(
    val abbreviation: String,
    val client_ip: String,
    val datetime: String,
    val day_of_week: Int,
    val day_of_year: Int,
    val dst: Boolean,
    val dst_from: Any?,
    val dst_offset: Int,
    val dst_until: Any?,
    val raw_offset: Int,
    val timezone: String,
    val unixtime: Long,
    val utc_datetime: String,
    val utc_offset: String,
    val week_number: Int
)

