package com.ls.comunicator.core

import android.content.Context
import android.content.res.Resources
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.speech.tts.TextToSpeech
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.ls.comunicator.model.Card
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.util.*

fun showToast(appContext: Context,message: String) {
    Toast.makeText(appContext, message, LENGTH_SHORT).show()
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

@Throws(IOException::class)
fun copyFile(sourceFile: File?, destFile: File) {
    if (!destFile.parentFile.exists()) destFile.parentFile.mkdirs()
    if (!destFile.exists()) {
        destFile.createNewFile()
    }
    var source: FileChannel? = null
    var destination: FileChannel? = null
    try {
        source = FileInputStream(sourceFile).channel
        destination = FileOutputStream(destFile).channel
        destination.transferFrom(source, 0, source.size())
    } finally {
        source?.close()
        destination?.close()
    }
}