package com.ls.comunicator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.ls.comunicator.R
import kotlinx.android.synthetic.main.page_list_item.view.*
import kotlin.collections.ArrayList

class ContentTableAdapter(val pages: ArrayList<String>, val tabLayout: TabLayout, val context: Context?) : RecyclerView.Adapter<ContentTableAdapter.ViewHolder>() {

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

        fun bind(page: String, i: Int) {
            val text = "${i+1}. $page"
            pageText.text = text
            itemView.setOnClickListener {
                tabLayout.getTabAt(i+1)?.select()
            }
        }
    }
}


