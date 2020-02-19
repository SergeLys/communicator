package com.ls.comunicator.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.ls.comunicator.R
import com.ls.comunicator.adapters.CardAdapter
import com.ls.comunicator.core.Card
import com.ls.comunicator.core.Image


class PageSettingsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_settings)

        val bitMap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.car)

        val cards = arrayListOf(
            Card("Машинка", null, Image(bitMap, 10.5F, Color.RED,null, 10, Color.RED)),
            Card("Грузовик", null, Image(bitMap, 8.5F, Color.BLUE,null, 8, Color.BLUE)),
            Card("Ложка", null, Image(bitMap, 10.5F, Color.GREEN,null, 10, Color.GREEN)),
            Card("Тарелка", null, Image(bitMap, 10.5F, Color.RED,null, 10, Color.GREEN)),
            Card("Машинка", null, Image(bitMap, 10.5F, Color.YELLOW,null, 10, Color.YELLOW))
        )

        recyclerView = findViewById(R.id.page_list)
        recyclerView.layoutManager = GridLayoutManager( this, 3)
        recyclerView.adapter = CardAdapter(cards, this, false)

        findViewById<MaterialButton>(R.id.add_symbol_button)
            .setOnClickListener {
                val cardSettingsActivity = Intent(this, CardSettingsActivity::class.java)
                startActivity(cardSettingsActivity)
            }
    }

}
