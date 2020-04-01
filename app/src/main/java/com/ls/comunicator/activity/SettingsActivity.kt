package com.ls.comunicator.activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.ls.comunicator.R
import com.ls.comunicator.core.SingletonCard
import com.ls.comunicator.core.deletePage

class SettingsActivity : AppCompatActivity() {

    lateinit var pageList: ListView
    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        pageList = findViewById(R.id.page_list)
        adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, SingletonCard.pages)
        pageList.adapter = adapter

        pageList.setOnItemLongClickListener { adapterView, view, i, l ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.inflate(R.menu.cardmenu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.menu_edit -> {
                        val editPageSettingsActivity = Intent(this, PageSettingsActivity::class.java)
                        editPageSettingsActivity.putExtra("page", SingletonCard.pages[i])
                        startActivity(editPageSettingsActivity)
                        true
                    }
                    R.id.menu_delete -> {
                        val success = deletePage(SingletonCard.pages[i], null)
                        if (success) {
                            SingletonCard.pages.removeAt(i)
                            adapter.notifyDataSetChanged()
                        }
                        Toast.makeText(this, if (success) "Удалено" else "Не удалось", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
            true
        }

        findViewById<MaterialButton>(R.id.new_page_button)
            .setOnClickListener {
                val newPageSettingsActivity = Intent(this, PageSettingsActivity::class.java)
                startActivity(newPageSettingsActivity)
            }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
