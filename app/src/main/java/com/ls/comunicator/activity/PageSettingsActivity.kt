package com.ls.comunicator.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import com.ls.comunicator.adapters.CardAdapter
import com.ls.comunicator.adapters.CardAdapterEnum
import com.ls.comunicator.core.SingletonCard
import com.ls.comunicator.core.loadPage
import com.ls.comunicator.core.savePage
import com.ls.comunicator.core.savePagesDictionary


class PageSettingsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pageNameEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_settings)

        val cards = loadPage(intent.getStringExtra("page"))

        pageNameEditText = findViewById(R.id.page_name)
        pageNameEditText.setText(intent.getStringExtra("page"))

        recyclerView = findViewById(R.id.page_list)
        recyclerView.layoutManager = GridLayoutManager( this, 3)
        recyclerView.adapter = CardAdapter(cards, this, CardAdapterEnum.EDIT_PAGE, null)

        findViewById<MaterialButton>(R.id.add_symbol_button)
            .setOnClickListener {
                val cardSettingsActivity = Intent(this, CardSettingsActivity::class.java)
                SingletonCard.card.page = pageNameEditText.text.toString()
                startActivity(cardSettingsActivity)
            }

        findViewById<MaterialButton>(R.id.save_page_button)
            .setOnClickListener {
                var success = savePage(baseContext,  pageNameEditText.text.toString(), null)
                if (success) {
                    SingletonCard.pages.add(pageNameEditText.text.toString())
                    success = savePagesDictionary(SingletonCard.pages)
                    Toast.makeText(baseContext,if (success)  "Сохранено" else "Ошибка при сохранении" , Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
