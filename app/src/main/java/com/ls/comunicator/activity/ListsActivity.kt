package com.ls.comunicator.activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.ls.comunicator.R
import com.ls.comunicator.core.SingletonCard
import com.ls.comunicator.core.deletePage
import kotlinx.android.synthetic.main.lists_settings.*

class ListsActivity : AppCompatActivity() {

    lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lists_settings)

        adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, SingletonCard.pages)
        pageList.adapter = adapter

        pageList.setOnItemLongClickListener { adapterView, view, i, l ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.inflate(R.menu.cardmenu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.menu_edit -> {
                        val editPageSettingsActivity = Intent(this, PageCreationActivity::class.java)
                        editPageSettingsActivity.putExtra("page", SingletonCard.pages[i])
                        startActivity(editPageSettingsActivity)
                        true
                    }
                    R.id.menu_delete -> {
                        val success = deletePage(baseContext ,SingletonCard.pages[i], null)
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
        addNewList.setOnClickListener {
                val newPageSettingsActivity = Intent(this, PageCreationActivity::class.java)
                startActivity(newPageSettingsActivity)
            }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
