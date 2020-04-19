package com.ls.comunicator.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import com.ls.comunicator.core.*
import com.ls.comunicator.core.Consts.Companion.AUDIO_BROWSER_REQUEST
import com.ls.comunicator.core.Consts.Companion.WRITE_CODE
import com.ls.comunicator.model.Card
import com.ls.comunicator.model.CardModel
import com.ls.comunicator.presenter.CasesPresenter
import kotlinx.android.synthetic.main.activity_cases.*
import java.io.File
import java.util.*

class CasesActivity : AppCompatActivity() {

    lateinit var card: Card
    lateinit var presenter: CasesPresenter
    private lateinit var watcher: TextChange
    lateinit var mediaRecorder: MediaRecorder
    lateinit var mediaPlayer: MediaPlayer
    lateinit var nEditText: TextInputEditText
    lateinit var gEditText: TextInputEditText
    lateinit var dEditText: TextInputEditText
    lateinit var aEditText: TextInputEditText
    lateinit var iEditText: TextInputEditText
    lateinit var pEditText: TextInputEditText
    private lateinit var fileBtn: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cases)

        presenter = CasesPresenter(this, CardModel)
        presenter.loadCard(intent.getStringExtra("page"), intent.getStringExtra("name"))
    }

    fun init(c: Card) {
        card = c
        if (card.cases == null) card.addCases()
        if (card.isHasCases) {
            casesLayout.visibility = View.VISIBLE
            isHasCases.isChecked = true
        } else {
            casesLayout.visibility = View.GONE
            isHasCases.isChecked = false
        }
        try {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (card.cases == null) throw Exception()
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
        } catch (e: Exception) {
            onBackPressed()
        }

        isHasCases.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                if (isChecked){
                    casesLayout.visibility = View.VISIBLE
                    card.isHasCases = true
                }
                else {
                    casesLayout.visibility = View.GONE
                    card.isHasCases = false
                }
            }
        })

        findViewById<MaterialButton>(R.id.back_button)
            .setOnClickListener {
                presenter.saveCard(card)
            }
    }

    fun onVoiceBtnClick(button: View) {
        val permissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
        if (permissionStatus == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), WRITE_CODE)
        else {
            val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogTheme))
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
        fileBtn = view
        val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
        fileIntent.type = "audio/*"
        startActivityForResult(fileIntent, AUDIO_BROWSER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AUDIO_BROWSER_REQUEST -> {
                    try {
                        copyFile(File(getPathForAudio(baseContext, data?.data)), File(getPath(fileBtn)))
                    } catch (e: Exception) {
                        Toast.makeText(baseContext, "Ошибка копирования", Toast.LENGTH_SHORT)
                    }
                }
            }
        }
    }

    fun getPathForAudio(context: Context, uri: Uri?): String? {
        var result: String? = null
        var cursor: Cursor? = null
        try {
            val proj =
                arrayOf(MediaStore.Audio.Media.DATA)
            cursor = context.contentResolver.query(uri, proj, null, null, null)
            if (cursor == null) {
                result = uri?.path
            } else {
                cursor.moveToFirst()
                val column_index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)
                result = cursor.getString(column_index)
                cursor.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return result
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
        return getFilesDir(baseContext)?.absolutePath +
                "/${Consts.LISTS_FOLDER}/${page}/${name}/sound/${case.text}.3gp"
//        return getFilesDir(baseContext)?.absolutePath +
//                "/${Consts.LISTS_FOLDER}/${page}/${name}/sound/${case.text}"
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
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
