package com.ls.comunicator.view

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import com.ls.comunicator.core.*
import com.ls.comunicator.core.Consts.Companion.CAMERA_REQUEST
import com.ls.comunicator.core.Consts.Companion.FILE_BROWSER_REQUEST
import com.ls.comunicator.model.Card
import com.ls.comunicator.model.CardModel
import com.ls.comunicator.presenter.CardSettingsPresenter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dev.sasikanth.colorsheet.ColorSheet
import kotlinx.android.synthetic.main.activity_card_settings.*
import java.io.File

class CardSettingsActivity : AppCompatActivity() {

    private lateinit var cardFrame: MaterialCardView
    private lateinit var cardImage: ImageView
    private lateinit var cardText: TextView
    private lateinit var cardName: TextInputEditText
    lateinit var card: Card
    lateinit var upCheckBox: RadioButton
    lateinit var bottomCheckBox: RadioButton
    lateinit var oldCardName: String
    private var imageUri: Uri? = null
    private lateinit var presenter: CardSettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_settings)

        presenter = CardSettingsPresenter(this, CardModel())
        presenter.loadCard(intent.getStringExtra("page"), intent.getStringExtra("name"))
    }

    fun init(c: Card) {
        cardName = findViewById(R.id.card_name)
        cardFrame = findViewById(R.id.card_preview)
        cardImage = findViewById(R.id.card_image)
        cardText = findViewById(R.id.card_text)
        try {
            card = c
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                oldCardName = card.name
                cardName.setText(card.name)
                updateCardPreview(card)
            }
        } catch (e: Exception) {
            onBackPressed()
        }

        openCasesBtn.setOnClickListener {
            val casesActivity = Intent(this, CasesActivity::class.java)
            casesActivity.putExtra("page", card.page)
            casesActivity.putExtra("name", card.name)
            startActivity(casesActivity)
        }

        cardImage.setOnClickListener { showImageDialog() }
        textSettingsBtn.setOnClickListener { showCardTextDialog() }
        frameSettingsBtn.setOnClickListener { showCardFrameDialog() }

        saveCardButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            else
                presenter.saveCard(oldCardName, card)
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

    private fun onCheckBoxClicked(checkBox: CheckBox) {
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

    private fun showImageDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogTheme))
        builder.setTitle("Настройки картинки")
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
        builder.setPositiveButton("Ok") { dialogInterface, i -> }
        builder.setNegativeButton("Отмена") {dialogInterface, i ->  }
        builder.show()
    }

    private fun showCardTextDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogTheme))
        builder.setTitle("Настройки текста")
        val view = layoutInflater.inflate(R.layout.dialog_text_graphic, null)
        val textColorLayout = view.findViewById<LinearLayout>(R.id.text_colour_btn)
        val textSizeSlider = view.findViewById<Slider>(R.id.text_size_slider)
        upCheckBox = view.findViewById(R.id.up_check_box)
        bottomCheckBox = view.findViewById(R.id.bottom_check_box)
        upCheckBox.setOnCheckedChangeListener { p0, isChecked -> onCheckBoxClicked(p0 as CheckBox) }
        bottomCheckBox.setOnCheckedChangeListener { p0, isChecked -> onCheckBoxClicked(p0 as CheckBox) }
        textSizeSlider.setOnChangeListener { slider, value ->
            card.image.textSize = value
            updateCardPreview(card)
        }
        textColorLayout.setOnClickListener {
            ColorSheet().colorPicker(
                colors = baseContext.resources.getIntArray(R.array.colors),
                listener = { color ->
                    card.image.textColour = color
                    textColorLayout.setBackgroundColor(color)
                    updateCardPreview(card)
                })
                .show(supportFragmentManager)
        }
        builder.setView(view)
        builder.setPositiveButton("Ok") { dialogInterface, i -> }
        builder.setNegativeButton("Отмена") {dialogInterface, i ->  }
        builder.show()
    }

    private fun showCardFrameDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogTheme))
        builder.setTitle("Настройки рамки")
        val view = layoutInflater.inflate(R.layout.dialog_border_graphic, null)
        val borderColorLayout = view.findViewById<LinearLayout>(R.id.border_color_button)
        val  frameSizeSlider = view.findViewById<Slider>(R.id.frame_size_slider)
        frameSizeSlider.setOnChangeListener { slider, value ->
            card.image.borderSize = value.toInt()
            updateCardPreview(card)
        }
        borderColorLayout.setOnClickListener {
            ColorSheet().colorPicker(
                colors = baseContext.resources.getIntArray(R.array.colors),
                listener = { color ->
                    card.image.borderColour = color
                    borderColorLayout.setBackgroundColor(color)
                    updateCardPreview(card)
                })
                .show(supportFragmentManager)
        }
        builder.setView(view)
        builder.setPositiveButton("Ok") { dialogInterface, i -> }
        builder.setNegativeButton("Отмена") {dialogInterface, i ->  }
        builder.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            1 -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    presenter.saveCard(oldCardName, card)
                else
                    Toast.makeText(baseContext,"Ошибка при сохранении", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
