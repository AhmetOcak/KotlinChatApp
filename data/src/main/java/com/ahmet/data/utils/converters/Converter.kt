package com.ahmet.data.utils.converters

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

// type converter for user database
class Converter {

    @TypeConverter
    fun fromString(str: String): List<String> {
        val listType =  object : TypeToken<List<String>>(){}.type
        return Gson().fromJson(str, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String = Gson().toJson(list)

}