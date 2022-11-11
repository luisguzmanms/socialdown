package com.lamesa.socialdown.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.timeNow(): String{
    val now = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    return now.format(this)
}






