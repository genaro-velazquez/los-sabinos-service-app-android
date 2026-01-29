package com.lossabinos.serviceapp.utils

import android.content.Context
import java.io.File

fun createImageFile(context: Context): File {
    val dir = File(context.filesDir, "work_requests")

    if (!dir.exists()) {
        dir.mkdirs()
    }

    val fileName = "WR_${System.currentTimeMillis()}.jpg"
    return File(dir, fileName)
}
