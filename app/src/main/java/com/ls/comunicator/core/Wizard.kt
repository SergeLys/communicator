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
import kotlin.collections.ArrayList


fun checkCard(appContext: Context, card: Card?, isToast: Boolean): Boolean {

    var isValid = true
    if (card?.name == null) {
        if (isToast) showToast(appContext ,CARD_NAME_WARNING)
        isValid = false
    }
    if (card?.cases == null) {
        if (isToast) showToast(appContext, CARD_CASES_WARNING)
        isValid = false
    }
    if (card?.image == null) {
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

fun savePage(context: Context, listName: String, card: Card?): Boolean {

    lateinit var pageData: File
    lateinit var fos: FileOutputStream
    lateinit var os: ObjectOutputStream
    val pageDirPath  = "/${Consts.LISTS_FOLDER}/${listName.toLowerCase(Locale.getDefault())}"
    val cardDirPath = "$pageDirPath/${card?.name?.toLowerCase(Locale.getDefault())}"
    val cardSoundDirPath = "$cardDirPath/sound"
    val cardInfo = "card_info"
    val cardImage = "image.jpg"
    var success = true

    if (checkCard(context, card, false) || true) {
        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
            pageData = File(Environment.getExternalStorageDirectory().absoluteFile, pageDirPath)

        if (!pageData.exists()) success = pageData.mkdirs()

        if (success && card != null) {
            val cardDir =  File(Environment.getExternalStorageDirectory().absoluteFile, cardDirPath)
            cardDir.mkdirs()
            val cardInfoFile = File(cardDir, cardInfo) // save card_info
            if (cardInfoFile.exists()) cardInfoFile.delete() else cardInfoFile.createNewFile()
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
            val cardImageFile = File(cardDir, cardImage) // save card image
            if (cardImageFile.exists()) cardImageFile.delete()
            try {
                fos = FileOutputStream(cardImageFile)
                card?.image?.imageView?.drawable?.toBitmap()
                    ?.compress(Bitmap.CompressFormat.PNG, 50, fos)
            } catch (e : Exception) {
                success = false
                e.printStackTrace()
            } finally {
                fos.close()
            }
            File(Environment.getExternalStorageDirectory().absoluteFile, cardSoundDirPath).mkdirs()
        }
    }
    return success
}

fun loadPage(listName: String?): ArrayList<Card> {

    lateinit var fis: FileInputStream
    lateinit var ins:  ObjectInputStream
    lateinit var listDir: File
    val cards = arrayListOf<Card>()
    val listDirPath = "/${Consts.LISTS_FOLDER}/${listName?.toLowerCase(Locale.getDefault())}"

    if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
        listDir = File(Environment.getExternalStorageDirectory().absoluteFile, listDirPath)

    if (listDir.exists()) {
        listDir.listFiles().forEach {
            if (it.isDirectory) {
                try {
                    fis = FileInputStream(File("${it.absolutePath}/card_info"))
                    ins = ObjectInputStream(fis)
                    val card = ins.readObject() as Card
                    card.page = listName
                    cards.add(card)

                } catch (e: java.lang.Exception) {
                } finally {
                    ins.close()
                    fis.close()
                }
            }
        }
    }
    return cards
}
fun deletePage(page: String, card: String?): Boolean {
    lateinit var pageData: File
    lateinit var path: String

    if (card == null)
        path = "/${Consts.LISTS_FOLDER}/${page.toLowerCase(Locale.getDefault())}"
    else
        path = "/${Consts.LISTS_FOLDER}/${page.toLowerCase(Locale.getDefault())}/${card.toLowerCase(Locale.getDefault())}"

    if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
        pageData = File(Environment.getExternalStorageDirectory().absoluteFile, path)

    pageData.deleteRecursively()
    return (!pageData.exists())
}

fun loadPagesList(): ArrayList<String> {
    lateinit var fis: FileInputStream
    lateinit var ins:  ObjectInputStream
    lateinit var list:  ArrayList<String>
    val pagesMapPath = "/${Consts.APP_FOLDER}/page_dict"
    lateinit var pagesMap: File

    if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
        pagesMap = File(Environment.getExternalStorageDirectory().absoluteFile, pagesMapPath)

    if (pagesMap.exists()) {
        try {
            fis = FileInputStream(pagesMap)
            ins = ObjectInputStream(fis)
            list = ins.readObject() as ArrayList<String>

        } catch (e: java.lang.Exception) {
        } finally {
            ins.close()
            fis.close()
        }
    } else {
        list = ArrayList()
        savePagesDictionary(list)
    }
    return  list
}

fun savePagesDictionary(pages:ArrayList<String>): Boolean {
    lateinit var fos: FileOutputStream
    lateinit var os: ObjectOutputStream
    val pagesMapPath = "/${Consts.APP_FOLDER}/page_dict"
    lateinit var pagesMap: File
    var success = true

    if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
        pagesMap = File(Environment.getExternalStorageDirectory().absoluteFile, pagesMapPath)

    if (pagesMap.exists()) pagesMap.delete()
    try {
        fos = FileOutputStream(pagesMap)
        os = ObjectOutputStream(fos)
        os.writeObject(pages)
    } catch (e : Exception) {
        success = false
        e.printStackTrace()
    } finally {
        os.flush()
        os.close()
        fos.close()
    }
    return  success
}