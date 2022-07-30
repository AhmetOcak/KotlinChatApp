package com.ahmet.features.utils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*

object DateConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    fun convertLongToTime(time: Long): String {
        val date = Instant.ofEpochSecond(time)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        return "${date.hour}:${date.minute}"
    }
}