package com.ls.comunicator.core

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.graphics.drawable.toBitmap
import com.ls.comunicator.core.Consts.Companion.CARD_CASES_WARNING
import com.ls.comunicator.core.Consts.Companion.CARD_IMAGE_WARNING
import com.ls.comunicator.core.Consts.Companion.CARD_NAME_WARNING
import com.ls.comunicator.core.Consts.Companion.TEXT_PLACE_WARNING
import java.io.*
import java.util.*


fun checkCard(appContext: Context, card: Card, isToast: Boolean): Boolean {

    var isValid = true
    if (card.name == null) {
        if (isToast) showToast(appContext ,CARD_NAME_WARNING)
        isValid = false
    }
    if (card.cases == null) {
        if (isToast) showToast(appContext, CARD_CASES_WARNING)
        isValid = false
    }
    if (card.image == null) {
        if (isToast) showToast(appContext, CARD_IMAGE_WARNING)
        isValid = false
    }
    else
        isValid = checkImage(appContext, card.image, isToast)

    return isValid
}

fun checkImage(appContext: Context, image: Image, isToast: Boolean): Boolean {

    var isValid = true
    if (image.imageView == null) {
        if (isToast) showToast(appContext, CARD_IMAGE_WARNING)
        isValid = false
    }
    if (image.textPlace == null) {
        if (isToast) showToast(appContext, TEXT_PLACE_WARNING)
        isValid = false
    }
    return  isValid
}

fun showToast(appContext: Context,message: String) {
    Toast.makeText(appContext, message, LENGTH_SHORT).show()
}

fun saveCard(context: Context, listName: String, card: Card): Boolean {

    lateinit var pageData: File
    lateinit var fos: FileOutputStream
    lateinit var os: ObjectOutputStream
    val cardDirPath = "/${Consts.CARD_FOLDER}/$listName/${SingletonCard.card.name.toLowerCase(Locale.getDefault())}"
    val cardSoundDirPath = "/$cardDirPath/sound"
    val cardInfo = "card_info"
    val cardImage = "image.jpg"
    var success = true

    if (checkCard(context, card, true) || true) {
        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
            pageData = File(Environment.getExternalStorageDirectory().absoluteFile, cardDirPath)

        if (!pageData.exists()) {
            success = pageData.mkdirs()
            success = File(Environment.getExternalStorageDirectory().absoluteFile, cardSoundDirPath).mkdirs()
        }
        if (success) {
            val cardInfoFile = File(pageData, cardInfo) // save card_info
            if (cardInfoFile.exists()) cardInfoFile.delete()
            try {
                fos = FileOutputStream(cardInfoFile)
                os = ObjectOutputStream(fos)
                os.writeObject(card)
            } catch (e : Exception) {
                success = false
                e.printStackTrace()
            } finally {
                os.flush()
                os.close()
                fos.close()
            }
            val cardImageFile = File(pageData, cardImage) // save card image
            if (cardImageFile.exists()) cardImageFile.delete()
            try {
                fos = FileOutputStream(cardImageFile)
                card.image.imageView.drawable.toBitmap()
                    .compress(Bitmap.CompressFormat.PNG, 50, fos)
            } catch (e : Exception) {
                success = false
                e.printStackTrace()
            } finally {
                fos.close()
            }
        }
    }
    return success
}

fun loadCard(): Card {

    lateinit var fos: FileOutputStream
    lateinit var os: ObjectOutputStream
    try {
//                        ActivityCompat.requestPermissions(this,
//                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        val fis = FileInputStream(File(Environment.getExternalStorageDirectory().toString() + "/Communicator/Машинка"))
        val ins = ObjectInputStream(fis)
        val test = ins.readObject()
        ins.close()
        fis.close()
    } catch (e: java.lang.Exception) {
    }
    return Card()
}