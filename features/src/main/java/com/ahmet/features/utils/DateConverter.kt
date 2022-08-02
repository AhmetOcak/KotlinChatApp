package com.ahmet.features.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId

object DateConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    fun convertLongToTime(time: Long): String {
        val date = Instant.ofEpochSecond(time)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        return if(Instant.now().epochSecond - time >= 86400) {
            if(date.monthValue < 10) {
                "${date.dayOfMonth}.0${date.monthValue}.${date.year}"
            }else {
                "${date.dayOfMonth}.${date.monthValue}.${date.year}"
            }
        }else {
            return if(date.hour < 10 && date.minute < 10) {
                "0${date.hour}:0${date.minute}"
            }else if(date.hour < 10) {
                "0${date.hour}:${date.minute}"
            }else if(date.minute < 10) {
                "${date.hour}:${date.minute}"
            }else {
                "${date.hour}:${date.minute}"
            }
        }

    }
}