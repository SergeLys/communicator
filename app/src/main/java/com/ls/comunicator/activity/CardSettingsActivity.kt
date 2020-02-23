package com.ls.comunicator.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import com.ls.comunicator.core.*
import com.ls.comunicator.core.Consts.Companion.APP_NAME
import com.ls.comunicator.core.Consts.Companion.CARD
import com.ls.comunicator.core.SingletonCard.card
import java.io.*

class CardSettingsActivity : AppCompatActivity() {

    lateinit var page: String
    lateinit var pageData: File
    lateinit var cardFrame: MaterialCardView
    lateinit var cardImage: ImageView
    lateinit var cardText: TextView
    lateinit var cardName: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_settings)

        cardName = findViewById(R.id.card_name)

        if (intent.hasExtra(CARD))
            card = intent.getSerializableExtra(CARD) as Card

//        page = intent.getStringExtra(PAGE)
        val options = BitmapFactory.Options()
        options.inScaled = false
        val bitMap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.car, options)

        card.name = "Машинка"
//        card.image = Image(ProxyBitMap(bitMap), 20F, Color.BLACK,null, 20, Color.YELLOW)
        updateCardPreview(card)

        findViewById<MaterialButton>(R.id.open_cases_button)
            .setOnClickListener {
                val casesActivity = Intent(this, CasesActivity::class.java)
                startActivity(casesActivity)
            }

        findViewById<MaterialButton>(R.id.open_graphic_button)
            .setOnClickListener {
                val graphicParameters = Intent(this, GraphicParameters::class.java)
                startActivity(graphicParameters)
            }

        findViewById<MaterialButton>(R.id.save_card_button)
            .setOnClickListener {
                if (checkCard(baseContext, card, true)) {
                    var appRoot = File("/storage/emmc","/$APP_NAME/$page")
                    if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
                        pageData = File(Environment.getExternalStorageDirectory().absoluteFile,"/$APP_NAME/$page")

                    ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)

                    var success = true
                    if (!pageData.exists()) {
                        success = pageData.mkdirs()
                    }
                    if (success) {
                        val fileCard = File(pageData, card.name)
                        if (fileCard.exists()) fileCard.delete()
                        try {
                            val fos = FileOutputStream(fileCard)
                            val os = ObjectOutputStream(fos)
                            os.writeObject(card)
                            os.flush()
                            os.close()
                            fos.close()
                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    }
                    try {
                        ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                        val fis = FileInputStream(File(Environment.getExternalStorageDirectory().toString() + "/Communicator/Машинка"))
                        val ins = ObjectInputStream(fis)
                        val test = ins.readObject()
                        ins.close()
                        fis.close()
                    } catch (e: java.lang.Exception) {

                    }
                }
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

        if (card.name != "")
            cardText.text = card.name
        if (card.image != null) {
            if (card.image.borderColour != 0)
                cardFrame.strokeColor = card.image.borderColour
            if (card.image.borderSize != 0)
                cardFrame.strokeWidth = card.image.borderSize
            if (card.image.image != null)
                cardImage.setImageBitmap(card.image.image.bitmap)
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
                        imageParams.addRule(RelativeLayout.BELOW, R.id.card_text)
                    }
                    TextPositionEnum.BOTTOM -> {
                        textParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
                        imageParams.removeRule(RelativeLayout.BELOW)
                        textParams.addRule(RelativeLayout.BELOW, R.id.card_image)
                    }
                    else -> {}
                }
            }
        }
    }
}
