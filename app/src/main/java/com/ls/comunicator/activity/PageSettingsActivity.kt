package com.ls.comunicator.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.ls.comunicator.R
import com.ls.comunicator.core.Card


class PageSettingsActivity : AppCompatActivity() {

    val cards = arrayListOf(
        Card(null, null, null),
        Card(null, null, null),
        Card(null, null, null),
        Card(null, null, null),
        Card(null, null, null)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_settings)

        findViewById<RecyclerView>(R.id.page_list).layoutManager = GridLayoutManager( this, 3)
        findViewById<RecyclerView>(R.id.page_list).adapter = CardAdapter(cards,this)

        findViewById<MaterialButton>(R.id.add_symbol_button)
            .setOnClickListener {
                val cardSettingsActivity = Intent(this, CardSettingsActivity::class.java)
                startActivity(cardSettingsActivity)
            }
    }

}
