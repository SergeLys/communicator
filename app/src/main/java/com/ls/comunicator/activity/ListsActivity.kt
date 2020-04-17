package com.ls.comunicator.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.HORIZONTAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ls.comunicator.R
import com.ls.comunicator.core.deletePage
import com.ls.comunicator.core.loadPagesList
import kotlinx.android.synthetic.main.lists_settings.*
import kotlinx.android.synthetic.main.page_list_item.view.*


class ListsActivity : AppCompatActivity() {

    private lateinit var pages: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lists_settings)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            pages = loadPagesList(baseContext)
            initPageList()
        }
        addNewList.setOnClickListener {
                val newPageSettingsActivity = Intent(this, PageCreationActivity::class.java)
                startActivity(newPageSettingsActivity)
            }
    }

    override fun onResume() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            pages = loadPagesList(baseContext)
            initPageList()
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
                    initPageList()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initPageList() {
        val layoutManager = LinearLayoutManager(baseContext, RecyclerView.VERTICAL, false)
        pageList.layoutManager = layoutManager
        pageList.adapter = PageAdapter(pages, baseContext)
        val itemDecor = DividerItemDecoration(baseContext, DividerItemDecoration.VERTICAL)
        pageList.addItemDecoration(itemDecor)
    }

    companion object {
        class PageAdapter(val pages: ArrayList<String>, val context: Context?) : RecyclerView.Adapter<PageAdapter.ViewHolder>() {

            override fun getItemCount(): Int {
                return pages.size
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val viewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.page_list_item, parent, false))
                return viewHolder
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(pages[position], position)
            }

            inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

                var pageText = view.page_text
                var icon = view.icon

                fun bind(page: String, i: Int) {
                    try {
                        val pageName = page.split("_")[0]
                        pageText.text = pageName
                    } catch (e: Exception) { e.printStackTrace() }
                    try {
                        val iconCode = page.split("_")[1]
                        icon.setImageResource(itemView.context.resources.getIdentifier(iconCode,
                            "drawable", itemView.context.packageName))
                    } catch (e: Exception) {e.printStackTrace()}
                    itemView.setOnLongClickListener {
                        val popupMenu = PopupMenu(it.context, it)
                        popupMenu.inflate(R.menu.cardmenu)
                        popupMenu.setOnMenuItemClickListener(fun(menu: MenuItem): Boolean {
                            return when (menu.itemId) {
                                R.id.menu_edit -> {
                                    val editPageSettingsActivity =
                                        Intent(context, PageCreationActivity::class.java)
                                    editPageSettingsActivity.putExtra("page", pages[i])
                                    context?.let { it1 -> startActivity(it1, editPageSettingsActivity, null) }
                                    true
                                }
                                R.id.menu_delete -> {
                                    val success = deletePage(it.context, pages[i], null)
                                    if (success) {
                                        pages.removeAt(i)
                                        this@PageAdapter.notifyDataSetChanged()
                                    }
                                    Toast.makeText(
                                        context,
                                        if (success) "Удалено" else "Не удалось",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    true
                                }
                                else -> false
                            }
                        })
                        popupMenu.show()
                        true
                    }
                }
            }
        }
    }
}
