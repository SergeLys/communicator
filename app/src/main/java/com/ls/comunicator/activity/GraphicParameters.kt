package com.ls.comunicator.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.api.get
import coil.api.load
import coil.request.CachePolicy
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.ls.comunicator.R
import com.ls.comunicator.core.*
import com.ls.comunicator.core.Consts.Companion.CAMERA_REQUEST
import com.ls.comunicator.core.Consts.Companion.FILE_BROWSER_REQUEST
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback


class GraphicParameters : AppCompatActivity() {

    lateinit var card: Card
    lateinit var textColorPicker: ColorPicker
    lateinit var borderColorPicker: ColorPicker
    lateinit var textColorLayout: LinearLayout
    lateinit var borderColorLayout: LinearLayout
    lateinit var textSizeSlider: Slider
    lateinit var frameSizeSlider: Slider
    lateinit var upCheckBox: CheckBox
    lateinit var bottomCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphic_parameters)

        card = SingletonCard.card
        if (card.image == null) {
            card.image = Image()
            card.image.imageView = ImageView(baseContext)
        }
        textColorLayout = findViewById(R.id.text_colour)
        borderColorLayout = findViewById(R.id.border_colour)
        textSizeSlider = findViewById(R.id.text_size_slider)
        frameSizeSlider = findViewById(R.id.frame_size_slider)
        upCheckBox = findViewById(R.id.up_check_box)
        bottomCheckBox = findViewById(R.id.bottom_check_box)

        setParameters(card)

        textColorPicker = ColorPicker(this, 0,0,0)
        textColorPicker.enableAutoClose()
        textColorPicker.setCallback(object : ColorPickerCallback{
            override fun onColorChosen(color: Int) {
                card.image.textColour = color
                textColorLayout.setBackgroundColor(color)
            }
        })
        borderColorPicker = ColorPicker(this, 0,0,0)
        borderColorPicker.enableAutoClose()
        borderColorPicker.setCallback(object : ColorPickerCallback{
            override fun onColorChosen(color: Int) {
                card.image.borderColour = color
                borderColorLayout.setBackgroundColor(color)
            }
        })

        textSizeSlider.setOnChangeListener(object : Slider.OnChangeListener{
            override fun onValueChange(slider: Slider?, value: Float) {
                card.image.textSize = value
            }

        })
        frameSizeSlider.setOnChangeListener(object : Slider.OnChangeListener{
            override fun onValueChange(slider: Slider?, value: Float) {
                card.image.borderSize = value.toInt()
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

        findViewById<MaterialButton>(R.id.text_color_button)
            .setOnClickListener {
                textColorPicker.show()
            }
        findViewById<MaterialButton>(R.id.border_color_button)
            .setOnClickListener {
                borderColorPicker.show()
            }
        findViewById<MaterialButton>(R.id.back_button)
            .setOnClickListener {
                val casesActivity = Intent(this, CardSettingsActivity::class.java)
                startActivity(casesActivity)
            }

        findViewById<FloatingActionButton>(R.id.image_file_button)
            .setOnClickListener {
                val fileintent = Intent(Intent.ACTION_GET_CONTENT)
                fileintent.type = "image/*"
                startActivityForResult(fileintent, FILE_BROWSER_REQUEST)
            }

        findViewById<FloatingActionButton>(R.id.image_camera_button)
            .setOnClickListener {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    Coil.load(baseContext, bitmap) {
                        size(350, 450)
                        memoryCachePolicy(CachePolicy.DISABLED)
                        target {drawable ->
                            card.image.imageView.setImageDrawable(drawable)
                        }
                    }
                }
                FILE_BROWSER_REQUEST -> {
                    Coil.load(baseContext, data?.data) {
                        size(350, 450)
                        memoryCachePolicy(CachePolicy.DISABLED)
                        target {drawable ->
                            card.image.imageView.setImageDrawable(drawable)
                        }
                    }
                }
            }
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
        }
    }

    fun setParameters(card: Card) {
        if (card.image != null) {
            if(card.image.borderColour != 0)
                borderColorLayout.setBackgroundColor(card.image.borderColour)
            if(card.image.textColour != 0)
                textColorLayout.setBackgroundColor(card.image.textColour)
            if(!card.image.textSize.equals(0F))
                textSizeSlider.value = card.image.textSize
            if(card.image.borderSize != 0)
                frameSizeSlider.value = card.image.borderSize.toFloat()
            if(card.image.textPlace != null) {
                when(card.image.textPlace) {
                    TextPositionEnum.UP -> {
                        upCheckBox.isChecked = true
                    }
                    TextPositionEnum.BOTTOM -> {
                        bottomCheckBox.isChecked = true
                    }
                    else -> {}
                }
            }
        }
    }

}
