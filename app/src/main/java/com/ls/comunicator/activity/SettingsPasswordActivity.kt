package com.ls.comunicator.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import kotlin.random.Random

class SettingsPasswordActivity : AppCompatActivity() {

    private lateinit var passwordTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_password)

        passwordTextView = findViewById(R.id.password_text)
        val password = Random.nextInt(10000, 99999)
        passwordTextView.text = password.toString()

        findViewById<MaterialButton>(R.id.submit_password_button)
            .setOnClickListener {
                val inputPassword = findViewById<TextInputEditText>(R.id.password_input_text)
                if (inputPassword.text.toString() == password.toString()) {
                    val settingsActivity = Intent(this, SettingsActivity::class.java)
                    startActivity(settingsActivity)
                }
            }
    }

}
