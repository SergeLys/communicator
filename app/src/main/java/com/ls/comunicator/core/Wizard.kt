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
import com.ls.comunicator.R
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

fun renamePage(oldName: String, newName: String): Boolean {
    lateinit var oldPage: File
    lateinit var newPage: File

    val oldPath = "/${Consts.LISTS_FOLDER}/${oldName.toLowerCase(Locale.getDefault())}"
    val newPath = "/${Consts.LISTS_FOLDER}/${newName.toLowerCase(Locale.getDefault())}"
    if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        oldPage = File(Environment.getExternalStorageDirectory().absoluteFile, oldPath)
        newPage = File(Environment.getExternalStorageDirectory().absoluteFile, newPath)
    }
    return oldPage.renameTo(newPage)
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

fun play(cards: ArrayList<Card>, mediaPlayer: MediaPlayer, mTTS: TextToSpeech) {
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
            if (!hasSound(case, it)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    mTTS.speak(getCase(case, it.cases), TextToSpeech.QUEUE_FLUSH,null,null)
                else
                    mTTS.speak(getCase(case, it.cases), TextToSpeech.QUEUE_FLUSH, null)
                while (mTTS.isSpeaking) {}
            } else {
                try {
                    mediaPlayer.setDataSource(getPath(case, it))
                    mediaPlayer.prepare()
                } catch (e : Exception) {
                    e.printStackTrace()
                }
                mediaPlayer.start()
                while(mediaPlayer.isPlaying) {}
            }
        } else {
            if (!hasSound(case, it)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    mTTS.speak(it.name, TextToSpeech.QUEUE_FLUSH,null,null)
                else
                    mTTS.speak(it.name, TextToSpeech.QUEUE_FLUSH, null)
                while (mTTS.isSpeaking) {}
            } else {
                try {
                    mediaPlayer.setDataSource(getPath(case, it))
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

fun hasSound(case: CaseEnum, card: Card): Boolean {
    return File(getPath(case, card)).exists()
}

fun getPath(case: CaseEnum, card: Card): String {
    val page = card.page.toLowerCase(Locale.getDefault())
    val name = card.name.toLowerCase(Locale.getDefault())
    return if (card.cases == null) {
        Environment.getExternalStorageDirectory().absolutePath +
                "/${Consts.LISTS_FOLDER}/${page}/${name}/sound/sound.3gp"
    } else {
        Environment.getExternalStorageDirectory().absolutePath +
                "/${Consts.LISTS_FOLDER}/${page}/${name}/sound/${case.text}.3gp"
    }
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