package com.ls.comunicator.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ls.comunicator.R
import com.ls.comunicator.core.deletePage
import com.ls.comunicator.core.loadPagesList
import kotlinx.android.synthetic.main.lists_settings.*

class ListsActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var pages: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lists_settings)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            pages = loadPagesList(baseContext)
            adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, pages)
            pageList.adapter = adapter
        }

        pageList.setOnItemLongClickListener { adapterView, view, i, l ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.inflate(R.menu.cardmenu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.menu_edit -> {
                        val editPageSettingsActivity = Intent(this, PageCreationActivity::class.java)
                        editPageSettingsActivity.putExtra("page", pages[i])
                        startActivity(editPageSettingsActivity)
                        true
                    }
                    R.id.menu_delete -> {
                        val success = deletePage(baseContext ,pages[i], null)
                        if (success) {
                            pages.removeAt(i)
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
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pages = loadPagesList(baseContext)
            adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, pages)
            pageList.adapter = adapter
        }
        super.onResume()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pages = loadPagesList(baseContext)
                    adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, pages)
                    pageList.adapter = adapter
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
