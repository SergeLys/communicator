package com.ls.comunicator.presenter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ls.comunicator.R
import com.ls.comunicator.view.ListsActivity
import com.ls.comunicator.view.PageCreationActivity
import com.ls.comunicator.model.CardModel
import kotlinx.android.synthetic.main.lists_settings.*
import kotlinx.android.synthetic.main.page_list_item.view.*
import java.util.ArrayList

class ListsPresenter(private val view: ListsActivity, private val model: CardModel) {

    private lateinit var pageAdapter: PageAdapter
    private lateinit var pagesList: ArrayList<String>

    fun loadPagesList() {
        model.loadPagesList(view.baseContext, object: CardModel.LoadPagesCallback {
            override fun onLoad(pages: ArrayList<String>?) {
                if (pages != null) {
                    pagesList = pages
                    initPageList(pages)
                }
            }
        })
    }

    fun deletePage(id: Int, card: String?) {
        model.deletePage(view.baseContext, pagesList[id], card, object: CardModel.CompleteCallback {
            override fun onComplete(result: Boolean) {
                if (result)
                    pageAdapter.showSucces(id)
                else
                    pageAdapter.showFail()
            }
        })
    }

    private fun initPageList(pages: ArrayList<String>) {
        val layoutManager = LinearLayoutManager(view.baseContext, RecyclerView.VERTICAL, false)
        view.pageList.layoutManager = layoutManager
        pageAdapter = PageAdapter(pages, view.baseContext)
        view.pageList.adapter = pageAdapter
        val itemDecor = DividerItemDecoration(view.baseContext, DividerItemDecoration.VERTICAL)
        view.pageList.addItemDecoration(itemDecor)
    }

    inner class PageAdapter(val pages: ArrayList<String>, val context: Context?) : RecyclerView.Adapter<PageAdapter.ViewHolder>() {

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
                                context?.let { it1 ->
                                    ContextCompat.startActivity(
                                        it1,
                                        editPageSettingsActivity,
                                        null
                                    )
                                }
                                true
                            }
                            R.id.menu_delete -> {
                                deletePage(i, null)
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

        fun showSucces(i: Int) {
            pages.removeAt(i)
            this@PageAdapter.notifyDataSetChanged()
            Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
        }

        fun showFail() { Toast.makeText(context, "Удалено" , Toast.LENGTH_SHORT).show() }
    }
}