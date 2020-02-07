package com.ls.comunicator.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.ls.comunicator.R

class SettingsPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_password)

        findViewById<MaterialButton>(R.id.submit_password_button)
            .setOnClickListener {
                val settingsActivity = Intent(this, SettingsActivity::class.java)
                startActivity(settingsActivity)
            }
    }

}
