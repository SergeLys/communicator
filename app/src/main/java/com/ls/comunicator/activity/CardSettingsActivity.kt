package com.ls.comunicator.activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import com.ls.comunicator.core.*
import com.ls.comunicator.core.Consts.Companion.CAMERA_REQUEST
import com.ls.comunicator.core.Consts.Companion.FILE_BROWSER_REQUEST
import com.ls.comunicator.core.Consts.Companion.WRITE_CODE
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*

class CardSettingsActivity : AppCompatActivity() {

    private lateinit var cardFrame: MaterialCardView
    private lateinit var cardImage: ImageView
    private lateinit var cardText: TextView
    private lateinit var cardName: TextInputEditText
    private lateinit var isCasesCheckBox: CheckBox
    lateinit var mediaRecorder: MediaRecorder
    lateinit var card: Card
    lateinit var textColorPicker: ColorPicker
    lateinit var borderColorPicker: ColorPicker
    lateinit var upCheckBox: CheckBox
    lateinit var bottomCheckBox: CheckBox
    lateinit var oldCardName: String
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_settings)

        cardName = findViewById(R.id.card_name)
        cardFrame = findViewById(R.id.card_preview)
        cardImage = findViewById(R.id.card_image)
        cardText = findViewById(R.id.card_text)
        isCasesCheckBox = findViewById(R.id.is_cases_check_box)

        card = Card()
        val permissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            card = MyApp.card
            oldCardName = card.name
            cardName.setText(card.name)
            updateCardPreview(card)
        }

        val isEdit = intent.getBooleanExtra("isEdit", false)

        val openCasesBtn = findViewById<MaterialButton>(R.id.open_cases_button)
        if (isEdit) {
            findViewById<LinearLayout>(R.id.is_cases).visibility = View.GONE
            openCasesBtn.setOnClickListener {
                if (card.cases != null) {
                    val casesActivity = Intent(this, CasesActivity::class.java)
                    startActivity(casesActivity)
                } else {
                    showCasesDialog()
                }
            }
        } else {
            openCasesBtn.setOnClickListener {
                if (isCasesCheckBox.isChecked) {
                    if (card.cases == null)
                        card.addCases()
                    val casesActivity = Intent(this, CasesActivity::class.java)
                    startActivity(casesActivity)
                } else {
                    card.cases = null
                    showCasesDialog()
                }
            }
        }

        cardImage.setOnClickListener { showImageDialog() }
        cardText.setOnClickListener { showCardTextDialog() }
        cardFrame.setOnClickListener { showCardFrameDialog() }

        findViewById<MaterialButton>(R.id.save_card_button)
            .setOnClickListener {
                val permissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                if (permissionStatus == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 45)
                else
                    saveCard()
            }

        cardName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString() != "") {
                    card.name = p0.toString()
                    updateCardPreview(card)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    fun saveCard() {
        if (oldCardName != card.name)
            deletePage(card.page, oldCardName)
        val success = savePage(baseContext, card.page, card)
        Toast.makeText(baseContext, if (success) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
    }

    private fun getPath(card: Card): String {
        val page = card.page.toLowerCase(Locale.getDefault())
        val name = card.name.toLowerCase(Locale.getDefault())
        return Environment.getExternalStorageDirectory().absolutePath +
                "/${Consts.LISTS_FOLDER}/${page}/${name}/sound/sound.3gp"
    }

    private fun updateCardPreview(card: Card) {
        cardFrame = findViewById(R.id.card_preview)
        cardImage = findViewById(R.id.card_image)
        cardText = findViewById(R.id.card_text)

        if (card.name != "" && card.name != null)
            cardText.text = card.name
        if (card.image != null) {
            if (card.image.borderColour != 0)
                cardFrame.strokeColor = card.image.borderColour
            if (card.image.borderSize != 0)
                cardFrame.strokeWidth = card.image.borderSize
            if (card.image.imageView != null)
                cardImage.setImageDrawable(card.image.imageView.drawable)
            if (card.image.textColour != 0)
                cardText.setTextColor(card.image.textColour)
            if (card.image.textSize != 0F)
                cardText.textSize = card.image.textSize
            if (card.image.textPlace != null) {
                val imageParams = cardImage.layoutParams as RelativeLayout.LayoutParams
                val textParams = cardText.layoutParams as RelativeLayout.LayoutParams
                when(card.image.textPlace) {
                    TextPositionEnum.UP -> {
                        textParams.removeRule(RelativeLayout.BELOW)
                        textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                        textParams.topMargin = 10
                        textParams.bottomMargin = 0
                        imageParams.addRule(RelativeLayout.BELOW, R.id.card_text)
                        imageParams.topMargin = 0
                        imageParams.bottomMargin = 20
                    }
                    TextPositionEnum.BOTTOM -> {
                        textParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
                        imageParams.removeRule(RelativeLayout.BELOW)
                        textParams.addRule(RelativeLayout.BELOW, R.id.card_image)
                        textParams.topMargin = 0
                        textParams.bottomMargin = 10
                        imageParams.topMargin = 20
                        imageParams.bottomMargin = 0
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    Picasso.get()
                        .load(imageUri)
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .resize(400, 400)
                        .into(card.image.imageView, object: Callback {
                            override fun onSuccess() {
                                try {
                                    val proj = arrayOf(MediaStore.Images.Media.DATA)
                                    val cursor = managedQuery(imageUri, proj, null, null, null)
                                    val columnIndex: Int = cursor
                                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                                    cursor.moveToFirst()
                                    val img = File(cursor.getString(columnIndex))
                                    if (img.exists())
                                        img.delete()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            override fun onError(e: java.lang.Exception?) {}
                        })
                }
                FILE_BROWSER_REQUEST -> {
                    Picasso.get()
                        .load(data?.data)
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .resize(400, 400)
                        .into(card.image.imageView)
                }
            }
            updateCardPreview(card)
        }
    }

    fun onCheckBoxClicked(checkBox: CheckBox) {
        if (checkBox.isChecked) {

            when(checkBox.id) {
                R.id.up_check_box -> {
                    card.image.textPlace = TextPositionEnum.UP
                    if (bottomCheckBox.isChecked)
                        bottomCheckBox.isChecked = false
                }
                R.id.bottom_check_box -> {
                    card.image.textPlace = TextPositionEnum.BOTTOM
                    if (upCheckBox.isChecked)
                        upCheckBox.isChecked = false
                }
            }
            updateCardPreview(card)
        }
    }

    private fun showCasesDialog() {
        val permissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
        if (permissionStatus == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), WRITE_CODE)
        else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Озвучка")
            val view = layoutInflater.inflate(R.layout.dialog_case, null)
            val voiceBtn = view.findViewById<FloatingActionButton>(R.id.voice_button)
            val fileBtn = view.findViewById<FloatingActionButton>(R.id.file_button)
            val playBtn = view.findViewById<FloatingActionButton>(R.id.play_button)
            voiceBtn.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Запись")
                val view = layoutInflater.inflate(R.layout.dialog_voice, null)
                val startBtn = view.findViewById<MaterialButton>(R.id.start_play)
                val stopBtn = view.findViewById<MaterialButton>(R.id.stop_play)

                startBtn.setOnClickListener {
                    mediaRecorder = MediaRecorder()
                    createMediaRecorder(mediaRecorder)
                    startBtn.isEnabled = false
                    stopBtn.isEnabled = true
                    mediaRecorder.setOutputFile(getPath(card))
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
                    mediaRecorder.stop()
                    mediaRecorder.release()
                }
                builder.setView(view)
                builder.setPositiveButton("Ok") { dialogInterface, i ->
                }
                builder.show()
            }
            fileBtn.setOnClickListener {

            }
            playBtn.setOnClickListener {
                val mediaPlayer = MediaPlayer()
                try {
                    mediaPlayer.setDataSource(getPath(card))
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
            builder.setView(view)
            builder.setPositiveButton("Ok") { dialogInterface, i ->
            }
            builder.show()
        }
    }

    private fun showImageDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Картинка")
        val view = layoutInflater.inflate(R.layout.dialog_image, null)
        view.findViewById<FloatingActionButton>(R.id.image_file_button)
            .setOnClickListener {
                val fileintent = Intent(Intent.ACTION_GET_CONTENT)
                fileintent.type = "image/*"
                startActivityForResult(fileintent, FILE_BROWSER_REQUEST)
            }

        view.findViewById<FloatingActionButton>(R.id.image_camera_button)
            .setOnClickListener {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "${card.name}_picture")
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
                values.put(MediaStore.Images.Media.ORIENTATION, ExifInterface.ORIENTATION_ROTATE_90)
                imageUri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        builder.setView(view)
        builder.setPositiveButton("Ok") { dialogInterface, i ->
        }
        builder.show()
    }

    fun showCardTextDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Текст")
        val view = layoutInflater.inflate(R.layout.dialog_text_graphic, null)
        val textColorLayout = view.findViewById<LinearLayout>(R.id.text_colour_btn)
        val textSizeSlider = view.findViewById<Slider>(R.id.text_size_slider)
        upCheckBox = view.findViewById(R.id.up_check_box)
        bottomCheckBox = view.findViewById(R.id.bottom_check_box)
        textColorPicker = ColorPicker(this, 0,0,0)
        textColorPicker.enableAutoClose()
        textColorPicker.setCallback(object : ColorPickerCallback {
            override fun onColorChosen(color: Int) {
                card.image.textColour = color
                textColorLayout.setBackgroundColor(color)
                updateCardPreview(card)
            }
        })
        upCheckBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                onCheckBoxClicked(p0 as CheckBox)
            }

        })
        bottomCheckBox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
                onCheckBoxClicked(p0 as CheckBox)
            }

        })
        textSizeSlider.setOnChangeListener(object : Slider.OnChangeListener{
            override fun onValueChange(slider: Slider?, value: Float) {
                card.image.textSize = value
                updateCardPreview(card)
            }

        })
        textColorLayout.setOnClickListener {
            textColorPicker.show()
        }
        builder.setView(view)
        builder.setPositiveButton("Ok") { dialogInterface, i ->
        }
        builder.show()
    }

    private fun showCardFrameDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Рамка")
        val view = layoutInflater.inflate(R.layout.dialog_border_graphic, null)
        val borderColorLayout = view.findViewById<LinearLayout>(R.id.border_color_button)
        val  frameSizeSlider = view.findViewById<Slider>(R.id.frame_size_slider)
        borderColorPicker = ColorPicker(this, 0,0,0)
        borderColorPicker.enableAutoClose()
        borderColorPicker.setCallback(object : ColorPickerCallback {
            override fun onColorChosen(color: Int) {
                card.image.borderColour = color
                borderColorLayout.setBackgroundColor(color)
                updateCardPreview(card)
            }
        })
        frameSizeSlider.setOnChangeListener(object : Slider.OnChangeListener{
            override fun onValueChange(slider: Slider?, value: Float) {
                card.image.borderSize = value.toInt()
                updateCardPreview(card)
            }

        })
        borderColorLayout.setOnClickListener {
            borderColorPicker.show()
        }
        builder.setView(view)
        builder.setPositiveButton("Ok") { dialogInterface, i ->
        }
        builder.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            45 -> {saveCard()}
            33 -> {
                card = MyApp.card
                oldCardName = card.name
                cardName.setText(card.name)
                updateCardPreview(card)}
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
//    fun setParameters(card: Card) {
//        if (card.image != null) {
//            if(card.image.borderColour != 0)
//                borderColorLayout.setBackgroundColor(card.image.borderColour)
//            if(card.image.textColour != 0)
//                textColorLayout.setBackgroundColor(card.image.textColour)
//            if(!card.image.textSize.equals(0F))
//                textSizeSlider.value = card.image.textSize
//            if(card.image.borderSize != 0)
//                frameSizeSlider.value = card.image.borderSize.toFloat()
//            if(card.image.textPlace != null) {
//                when(card.image.textPlace) {
//                    TextPositionEnum.UP -> {
//                        upCheckBox.isChecked = true
//                    }
//                    TextPositionEnum.BOTTOM -> {
//                        bottomCheckBox.isChecked = true
//                    }
//                    else -> {}
//                }
//            }
//        }
//    }
}
