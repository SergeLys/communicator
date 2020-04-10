package com.ls.comunicator.core

import android.content.Context
import android.os.storage.StorageVolume
import java.io.File

fun getFilesDir(context: Context): File? {
    val memoryMode = getMemory(context)
    var files: File?
    try {
        when (memoryMode) {
            "internal" -> {
                files = context.getExternalFilesDirs(null)[0]
            }
            "external" -> {
                files = context.getExternalFilesDirs(null)[1]
            }
            else -> {
                files = context.getExternalFilesDirs(null)[0]
            }
        }
    } catch (e: Exception) {
      files = context.getExternalFilesDir(null)
        e.printStackTrace()
    }
    return files
}