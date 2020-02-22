package com.ls.comunicator.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.ls.comunicator.R
import com.ls.comunicator.core.Card
import com.ls.comunicator.core.SingletonCard

class CasesActivity : AppCompatActivity() {

    lateinit var card: Card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cases)

        card = SingletonCard.card

        findViewById<MaterialButton>(R.id.back_button)
            .setOnClickListener {
                val casesActivity = Intent(this, CardSettingsActivity::class.java)
                startActivity(casesActivity)
            }
    }

}
