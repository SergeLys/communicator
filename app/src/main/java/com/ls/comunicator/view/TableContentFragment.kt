package com.ls.comunicator.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.ls.comunicator.R
import com.ls.comunicator.adapter.ContentTableAdapter
import kotlinx.android.synthetic.main.fragment_table_content.view.contentTableList
import java.util.ArrayList


class TableContentFragment(private val tabs: TabLayout,private val pages: ArrayList<String>) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_table_content, container, false)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        view.contentTableList.layoutManager = layoutManager
        view.contentTableList.adapter = ContentTableAdapter(pages, tabs, context)
        val itemDecor = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        view.contentTableList.addItemDecoration(itemDecor)
        return view
    }

}
