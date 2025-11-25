package com.lossabinos.data.dto.utilities

import org.json.JSONArray
import org.json.JSONObject
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.ArrayList

fun JSONObject.asDouble(name: String, fallback: Double = 0.0): Double {
    if (this.isNull(name)) {
        return fallback
    }

    return when (val value = this.get(name)) {
        is Double -> value
        is Int -> value.toDouble()
        is String -> value.toDoubleOrNull() ?: fallback
        else -> fallback
    }
}

fun JSONObject.asInt(name: String, fallback: Int = 0): Int {
    if (this.isNull(name)) {
        return fallback
    }

    return when (val value = this.get(name)) {
        is Int -> value
        is Double -> value.toInt()
        is String -> value.toIntOrNull() ?: fallback
        else -> fallback
    }
}

fun JSONObject.asString(name: String, fallback: String = ""): String {
    if (this.isNull(name)) {
        return fallback
    }

    return when (val value = this.get(name)) {
        is Number, is Boolean -> "$value"
        is String -> value
        else -> fallback
    }
}

fun JSONObject.asBoolean(name: String, fallback: Boolean = false): Boolean {
    if (this.isNull(name)) {
        return fallback
    }

    return when (val value = this.get(name)) {
        is Boolean -> return value
        is String -> {
            val mValue = value.lowercase()
            return mValue == "true" || mValue == "si" || mValue == "yes"
        }

        is Int -> return value == 1
        else -> fallback
    }
}

fun JSONObject.asJSONObject(name: String, fallback: JSONObject = JSONObject()): JSONObject {
    if (!isValidJSON(name)) {
        return fallback
    }
    return getJSONObject(name)
}

fun JSONObject.asJSONArray(name: String, fallback: JSONArray = JSONArray()): JSONArray {
    if (this.isNull(name) || this.get(name) !is JSONArray) {
        return fallback
    }
    return getJSONArray(name)
}

fun JSONObject.optDate(name: String, def: Date? = null): Date? {
    if (this.isNull(name)) {
        return def
    }

    try {
        val value = getString(name)

        var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (value.contains("T")) {
            simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        } else if (value.contains(":")) {
            simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        }

        return simpleDateFormat.parse(value)
    }catch (e: Exception) {
        return def
    }
}

fun JSONObject.isValidJSON(name: String): Boolean {
    return this.has(name) && get(name) is JSONObject && (get(name) is JSONObject && getJSONObject(name).length() > 0)
}

fun JSONObject.asStringValues(parent: String? = null): List<Pair<String, String>> {

    val list = ArrayList<Pair<String, String>>()

    this.keys().forEach { key ->
        when(val value = this[key]) {
            is JSONObject -> {
                val newParent = if (parent != null) "[$parent][$key]" else key
                list.addAll(value.asStringValues(newParent))
            }

            is JSONArray -> {
                var newParent = "[$key]"
                if(parent != null) {
                    newParent = if(parent.contains("[")){
                        "$parent[$key]"
                    } else {
                        "[$parent][$key]"
                    }
                }
                list.addAll(value.asStringValues(newParent))
            }

            else -> {
                var mKey = "[$key]"
                if(parent != null) {
                    mKey = if(parent.contains("[")){
                        "$parent[$key]"
                    } else {
                        "[$parent][$key]"
                    }
                }
                list.add(Pair(mKey, "$value"))
            }
        }
    }

    return  list
}

fun JSONArray.asStringValues(parent: String? = null): List<Pair<String, String>> {
    val list = ArrayList<Pair<String, String>>()

    for (i in 0 until length()) {
        val value = get(i)

        when (value) {
            is JSONObject -> {
                val newParent = if (parent != null) "$parent[$i]" else "[$i]"
                list.addAll(value.asStringValues(newParent))
            }

            is JSONArray -> {
                val newParent = if (parent != null) "$parent[$i]" else "[$i]"
                list.addAll(value.asStringValues(newParent))
            }

            else -> {
                val mKey = if (parent != null) "$parent[$i]" else "[$i]"
                list.add(Pair(mKey, "$value"))
            }
        }
    }

    return list
}
