package com.ls.comunicator.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.ls.comunicator.R


class CardSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_settings)

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
    }

}
