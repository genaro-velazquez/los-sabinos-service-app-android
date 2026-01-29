package com.lossabinos.data.utilities

import androidx.room.TypeConverter
import org.json.JSONArray

class StringListConverter {

    @TypeConverter
    fun fromList(list: List<String>): String =
        JSONArray(list).toString()

    @TypeConverter
    fun toList(json: String): List<String> =
        JSONArray(json).let { array ->
            List(array.length()) { i -> array.getString(i) }
        }
}
