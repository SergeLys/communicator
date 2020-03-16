package com.ls.comunicator.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import com.ls.comunicator.core.SingletonCard.card
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class CardSettingsActivity : AppCompatActivity() {

    private lateinit var cardFrame: MaterialCardView
    private lateinit var cardImage: ImageView
    private lateinit var cardText: TextView
    private lateinit var cardName: TextInputEditText
    lateinit var card: Card
    lateinit var textColorPicker: ColorPicker
    lateinit var borderColorPicker: ColorPicker
    lateinit var upCheckBox: CheckBox
    lateinit var bottomCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_settings)

        cardName = findViewById(R.id.card_name)
        cardFrame = findViewById(R.id.card_preview)
        cardImage = findViewById(R.id.card_image)
        cardText = findViewById(R.id.card_text)

        card = SingletonCard.card
        if (card.image == null) {
            card.image = Image()
            card.image.imageView = ImageView(baseContext)
        }

        updateCardPreview(card)

        findViewById<MaterialButton>(R.id.open_cases_button)
            .setOnClickListener {
                val casesActivity = Intent(this, CasesActivity::class.java)
                startActivity(casesActivity)
            }

        cardImage.setOnClickListener {
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
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST)
                }
            builder.setView(view)
            builder.setPositiveButton("Ok") { dialogInterface, i ->
            }
            builder.show()
            true
        }

        cardText.setOnClickListener {
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
            true
        }

        cardFrame.setOnClickListener {
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
            true
        }

        findViewById<MaterialButton>(R.id.save_card_button)
            .setOnClickListener {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_CODE
                )
                val success = savePage(baseContext, card.page, card)
                Toast.makeText(baseContext, if (success) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
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
                    val bitmap = data?.extras?.get("data") as Bitmap
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path = MediaStore.Images.Media.insertImage(baseContext.contentResolver, bitmap, "Title", null)
                    Picasso.get()
                        .load(Uri.parse(path))
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .resize(400, 400)
                        .into(card.image.imageView)
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
