package com.ls.comunicator.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ls.comunicator.R
import com.ls.comunicator.core.Card

class MainActivity : AppCompatActivity() {

    val cards = arrayListOf(
        Card(null, null, null),
        Card(null, null, null),
        Card(null, null, null),
        Card(null, null, null),
        Card(null, null, null)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.communicative_line)
            .layoutManager = LinearLayoutManager( this, RecyclerView.HORIZONTAL, true)
        findViewById<RecyclerView>(R.id.communicative_line).adapter = CardAdapter(cards,this)

        findViewById<FloatingActionButton>(R.id.settings_button)
            .setOnClickListener {
                val settingsPasswordActivity = Intent(this, SettingsPasswordActivity::class.java)
                startActivity(settingsPasswordActivity)
            }
    }
}
