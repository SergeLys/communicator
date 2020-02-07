package com.ls.comunicator.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.ls.comunicator.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<MaterialButton>(R.id.new_page_button)
            .setOnClickListener {
                val newPageSettingsActivity = Intent(this, PageSettingsActivity::class.java)
                startActivity(newPageSettingsActivity)
            }

        findViewById<MaterialButton>(R.id.edit_page_button)
            .setOnClickListener {
                val editPageSettingsActivity = Intent(this, PageSettingsActivity::class.java)
                startActivity(editPageSettingsActivity)
            }
    }

}
