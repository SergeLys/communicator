package com.ls.comunicator.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.ls.comunicator.R
import com.ls.comunicator.core.*
import com.ls.comunicator.core.Consts.Companion.CAMERA_REQUEST
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
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graphic_parameters)

        card = SingletonCard.card
        if (card.image == null)
            card.image = Image()
        textColorLayout = findViewById(R.id.text_colour)
        borderColorLayout = findViewById(R.id.border_colour)
        textSizeSlider = findViewById(R.id.text_size_slider)
        frameSizeSlider = findViewById(R.id.frame_size_slider)
        upCheckBox = findViewById(R.id.up_check_box)
        bottomCheckBox = findViewById(R.id.bottom_check_box)

        textColorPicker = ColorPicker(this, 100,100,100)
        textColorPicker.enableAutoClose()
        textColorPicker.setCallback(object : ColorPickerCallback{
            override fun onColorChosen(color: Int) {
                card.image.textColour = color
                textColorLayout.setBackgroundColor(color)
            }
        })
        borderColorPicker = ColorPicker(this, 100,100,100)
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
//                imageView.load() // file
            }
        findViewById<FloatingActionButton>(R.id.image_net_button)
            .setOnClickListener {
//                imageView.load() // net
            }
        findViewById<FloatingActionButton>(R.id.image_camera_button)
            .setOnClickListener {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            val thumbnailBitmap = data?.extras?.get("data") as Bitmap
            card.image.image = ProxyBitMap(thumbnailBitmap)
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

}
