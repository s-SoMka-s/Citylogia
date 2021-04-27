package com.solution.citylogia.utils

import java.text.SimpleDateFormat
import java.util.*


fun FromTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MM/dd/yyyy")
    val netDate = Date(timestamp * 1000)
    return sdf.format(netDate)
}