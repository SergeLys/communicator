package com.ls.comunicator.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.ls.comunicator.R
import com.ls.comunicator.adapters.CardAdapter
import com.ls.comunicator.adapters.CardAdapterEnum
import com.ls.comunicator.core.Card
import com.ls.comunicator.core.loadCardsList


class PageSettingsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_settings)

        val cards = loadCardsList("test")
        recyclerView = findViewById(R.id.page_list)
        recyclerView.layoutManager = GridLayoutManager( this, 3)
        recyclerView.adapter = CardAdapter(cards, this, CardAdapterEnum.EDIT_PAGE, null)

        findViewById<MaterialButton>(R.id.add_symbol_button)
            .setOnClickListener {
                val cardSettingsActivity = Intent(this, CardSettingsActivity::class.java)
                startActivity(cardSettingsActivity)
            }
    }

}
