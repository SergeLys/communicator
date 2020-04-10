package com.ls.comunicator.activity

import android.content.pm.PackageManager
import android.media.*
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import com.ls.comunicator.core.*
import com.ls.comunicator.core.Consts.Companion.WRITE_CODE
import java.lang.Exception
import java.util.*


class CasesActivity : AppCompatActivity() {

    lateinit var card: Card
    private lateinit var watcher: TextChange
    lateinit var mediaRecorder: MediaRecorder
    lateinit var mediaPlayer: MediaPlayer
    lateinit var nEditText: TextInputEditText
    lateinit var gEditText: TextInputEditText
    lateinit var dEditText: TextInputEditText
    lateinit var aEditText: TextInputEditText
    lateinit var iEditText: TextInputEditText
    lateinit var pEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cases)

        card = Card()
        val permissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            card = MyApp.card
            watcher = TextChange()
            nEditText = findViewById(R.id.nominative_text)
            nEditText.addTextChangedListener(watcher)
            gEditText = findViewById(R.id.genitive_text)
            gEditText.addTextChangedListener(watcher)
            dEditText=  findViewById(R.id.dative_text)
            dEditText.addTextChangedListener(watcher)
            aEditText = findViewById(R.id.accusative_text)
            aEditText.addTextChangedListener(watcher)
            iEditText = findViewById(R.id.instrumental_text)
            iEditText.addTextChangedListener(watcher)
            pEditText = findViewById(R.id.prepositional_text)
            pEditText.addTextChangedListener(watcher)
        }

        findViewById<MaterialButton>(R.id.back_button)
            .setOnClickListener {
                onBackPressed()
            }
    }

    fun onVoiceBtnClick(button: View) {
        val permissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
        if (permissionStatus == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), WRITE_CODE)
        else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Запись")
            val view = layoutInflater.inflate(R.layout.dialog_voice, null)
            val startBtn = view.findViewById<MaterialButton>(R.id.start_play)
            val stopBtn = view.findViewById<MaterialButton>(R.id.stop_play)
            builder.setView(view)
            builder.setPositiveButton("Ok") { dialogInterface, i ->
            }
            builder.show()
            startBtn.setOnClickListener {
                mediaRecorder = MediaRecorder()
                createMediaRecorder(mediaRecorder)
                startBtn.isEnabled = false
                stopBtn.isEnabled = true
                mediaRecorder.setOutputFile(getPath(button))
                try {
                    mediaRecorder.prepare()
                    mediaRecorder.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            stopBtn.setOnClickListener {
                startBtn.isEnabled = true
                stopBtn.isEnabled = false
                try {
                    mediaRecorder.stop()
                    mediaRecorder.release()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun onFileBtnClick(view: View) {
        when (view.id) {
            R.id.nominative_file_button -> {}
            R.id.genitive_file_button -> {}
            R.id.dative_file_button -> {}
            R.id.accusative_file_button -> {}
            R.id.instrumental_file_button -> {}
            R.id.prepositional_file_button -> {}
        }
    }

    fun getPath(view: View): String {
        var case: CaseEnum = CaseEnum.NOMINATIVE
        val page = card.page.toLowerCase(Locale.getDefault())
        val name = card.name.toLowerCase(Locale.getDefault())

        when (view.id) {
            R.id.nominative_voice_button -> {case = CaseEnum.NOMINATIVE}
            R.id.genitive_voice_button -> {case = CaseEnum.GENITVIE}
            R.id.dative_voice_button -> {case = CaseEnum.DATIVE}
            R.id.accusative_voice_button -> {case = CaseEnum.ACCUSATIVE}
            R.id.instrumental_voice_button -> {case = CaseEnum.INSTRUMENTAL}
            R.id.prepositional_voice_button -> {case = CaseEnum.PREPOSITIONAL}
        }
        when (view.id) {
            R.id.nominative_play_button -> {case = CaseEnum.NOMINATIVE}
            R.id.genitive_play_button -> {case = CaseEnum.GENITVIE}
            R.id.dative_play_button -> {case = CaseEnum.DATIVE}
            R.id.accusative_play_button -> {case = CaseEnum.ACCUSATIVE}
            R.id.instrumental_play_button -> {case = CaseEnum.INSTRUMENTAL}
            R.id.prepositional_play_button -> {case = CaseEnum.PREPOSITIONAL}
        }
        return getFilesDir(baseContext)?.absolutePath +
                "/${Consts.LISTS_FOLDER}/${page}/${name}/sound/${case.text}.3gp"
    }

    inner class TextChange : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            when (p0.hashCode()) {
                nEditText.editableText.hashCode() -> { card.cases.nominative = p0.toString() }
                gEditText.editableText.hashCode() -> { card.cases.genitive = p0.toString() }
                dEditText.editableText.hashCode() -> { card.cases.dative = p0.toString() }
                aEditText.editableText.hashCode() -> { card.cases.accusative = p0.toString() }
                iEditText.editableText.hashCode() -> { card.cases.instrumental = p0.toString() }
                pEditText.editableText.hashCode() -> { card.cases.prepositional = p0.toString() }
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    }

    fun onPlayBtnClick(view: View) {
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(getPath(view))
            mediaPlayer.prepare()
            mediaPlayer.setOnPreparedListener {
                it.start()
                while (it.isPlaying) {}
                it.release()
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
//        mediaRecorder.release()
//        mediaPlayer.release()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
