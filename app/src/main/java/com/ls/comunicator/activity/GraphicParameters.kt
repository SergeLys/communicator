package com.ls.comunicator.activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.ls.comunicator.R
import com.ls.comunicator.core.Card
import com.ls.comunicator.core.Image
import com.ls.comunicator.core.SingletonCard
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
    }

}
