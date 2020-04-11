package com.ls.comunicator.core

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.speech.tts.TextToSpeech
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
    val pageDirPath  = "/lists/${listName.toLowerCase(Locale.getDefault())}"
    val cardDirPath = "$pageDirPath/${card?.name?.toLowerCase(Locale.getDefault())}"
    val cardSoundDirPath = "$cardDirPath/sound"
    val cardInfo = "card_info"
    val cardImage = "image.jpg"
    var success = true

    if (checkCard(context, card, false) || true) {

        pageData = File(getFilesDir(context), pageDirPath)

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

fun loadPage(context: Context, listName: String?): ArrayList<Card> {

    lateinit var fis: FileInputStream
    lateinit var ins:  ObjectInputStream
    lateinit var listDir: File
    val cards = arrayListOf<Card>()
    val listDirPath = "/lists/${listName?.toLowerCase(Locale.getDefault())}"

    listDir = File(getFilesDir(context), listDirPath)

    try {
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
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return cards
}

fun deletePage(context: Context, page: String, card: String?): Boolean {
    lateinit var pageData: File
    lateinit var path: String

    return try {
        path = if (card == null)
            "/lists}/${page.toLowerCase(Locale.getDefault())}"
        else
            "/lists/${page.toLowerCase(Locale.getDefault())}/${card.toLowerCase(Locale.getDefault())}"
        pageData = File(getFilesDir(context), path)

        pageData.deleteRecursively()
        (!pageData.exists())
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun renamePage(context: Context ,oldName: String, newName: String): Boolean {
    lateinit var oldPage: File
    lateinit var newPage: File

    return try {
        val oldPath = "/lists/${oldName.toLowerCase(Locale.getDefault())}"
        val newPath = "/lists/${newName.toLowerCase(Locale.getDefault())}"
        val files = getFilesDir(context)
        oldPage = File(files, oldPath)
        newPage = File(files, newPath)
        oldPage.renameTo(newPage)
    } catch (ex: Exception) {
        false
    }
}

fun loadPagesList(context: Context): ArrayList<String> {
    lateinit var listsFolder: File
    val pagesList = ArrayList<String>()

    try {
        listsFolder = File(getFilesDir(context), "/lists")
        if (listsFolder.exists()) {
            listsFolder.listFiles().forEach {
                if (it.isDirectory)
                    pagesList.add(it.name)
            }
        }
    } catch (ex: Exception) { }
    return  pagesList
}

fun play(context: Context, cards: ArrayList<Card>, mediaPlayer: MediaPlayer, mTTS: TextToSpeech) {
    var case: CaseEnum = CaseEnum.NOMINATIVE
    var current: CaseEnum
    cards.forEach {
        current = checkPreposition(it.name)
        if (current != CaseEnum.EMPTY)
            case = current
        current = checkEnding(it.name)
        if (current != CaseEnum.EMPTY)
            case = current

        if (it.cases != null) {
            if (!hasSound(context, case, it)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    mTTS.speak(getCase(case, it.cases), TextToSpeech.QUEUE_FLUSH,null,null)
                else
                    mTTS.speak(getCase(case, it.cases), TextToSpeech.QUEUE_FLUSH, null)
                while (mTTS.isSpeaking) {}
            } else {
                try {
                    mediaPlayer.setDataSource(getPath(context, case, it))
                    mediaPlayer.prepare()
                } catch (e : Exception) {
                    e.printStackTrace()
                }
                mediaPlayer.start()
                while(mediaPlayer.isPlaying) {}
            }
        } else {
            if (!hasSound(context, case, it)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    mTTS.speak(it.name, TextToSpeech.QUEUE_FLUSH,null,null)
                else
                    mTTS.speak(it.name, TextToSpeech.QUEUE_FLUSH, null)
                while (mTTS.isSpeaking) {}
            } else {
                try {
                    mediaPlayer.setDataSource(getPath(context, case, it))
                    mediaPlayer.prepare()
                } catch (e : Exception) {
                    e.printStackTrace()
                }
                mediaPlayer.start()
                while(mediaPlayer.isPlaying) {}
            }
        }
    }
}

fun checkPreposition(text: String): CaseEnum {
    var case = CaseEnum.EMPTY
    val res: Resources = Resources.getSystem()
    val value = text.toLowerCase(Locale.getDefault())
    val genitive = arrayOf("от", "без", "у", "до", "возле", "для", "вокруг")
    val dative = arrayOf("по", "к")
    val accusative = arrayOf("на", "за", "через", "про")
    val instrumental = arrayOf("за", "под", "над", "перед", "с")
    val prepositional = arrayOf("о", "об", "обо", "при", "в")

    if (genitive.contains(value)) case = CaseEnum.GENITVIE
    if (dative.contains(value)) case = CaseEnum.DATIVE
    if (accusative.contains(value)) case = CaseEnum.ACCUSATIVE
    if (instrumental.contains(value)) case = CaseEnum.INSTRUMENTAL
    if (prepositional.contains(value)) case = CaseEnum.PREPOSITIONAL

    return case
}

fun checkEnding(text: String): CaseEnum {
    var case: CaseEnum = CaseEnum.EMPTY
    val value = text.toLowerCase(Locale.getDefault())

    if(value.endsWith("у", true)
        || value.endsWith("ем", true)
        || value.endsWith("ем", true)
        || value.endsWith("ю", true)
        || value.endsWith("им", true)
        || value.endsWith("ешь", true)
        || value.endsWith("ете", true)
        || value.endsWith("ишь", true)
        || value.endsWith("ите", true)
        || value.endsWith("ет", true)
        || value.endsWith("ут", true)
        || value.endsWith("ют", true)
        || value.endsWith("ит", true)
        || value.endsWith("ят", true)
        || value.endsWith("ить", true)
        || value.endsWith("еть", true)
        || value.endsWith("ять", true)
        || value.endsWith("уть", true)
        || value.endsWith("ють", true)
        || value.endsWith("ыть", true)
        || value.endsWith("оть", true))
        case = CaseEnum.GENITVIE

    if(value.endsWith("ать", true)
        || value.endsWith("ем", true)
        || value.endsWith("ем", true))
        case = CaseEnum.INSTRUMENTAL
    return case
}

fun hasSound(context: Context, case: CaseEnum, card: Card): Boolean {
    return File(getPath(context, case, card)).exists()
}

fun getPath(context: Context, case: CaseEnum, card: Card): String {
    val page = card.page.toLowerCase(Locale.getDefault())
    val name = card.name.toLowerCase(Locale.getDefault())
    return if (card.cases == null)
        getFilesDir(context)?.absolutePath + "/${Consts.LISTS_FOLDER}/${page}/${name}/sound/sound.3gp"
     else
        getFilesDir(context)?.absolutePath + "/${Consts.LISTS_FOLDER}/${page}/${name}/sound/${case.text}.3gp"
}

fun getCase(case: CaseEnum, cases: Card.Cases): String {
    return when (case) {
        CaseEnum.NOMINATIVE -> cases.nominative
        CaseEnum.GENITVIE -> cases.genitive
        CaseEnum.DATIVE -> cases.dative
        CaseEnum.ACCUSATIVE -> cases.accusative
        CaseEnum.INSTRUMENTAL -> cases.instrumental
        CaseEnum.PREPOSITIONAL -> cases.prepositional
        else -> ""
    }
}

fun createMediaRecorder(mediaRecorder: MediaRecorder) {
    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
    mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
}