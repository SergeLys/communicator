package com.ls.comunicator.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.ls.comunicator.R
import com.ls.comunicator.adapter.ContentTableAdapter
import com.ls.comunicator.core.SingletonCard
import kotlinx.android.synthetic.main.fragment_table_content.view.*


class TableContentFragment(val tabLayout: TabLayout) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_table_content, container, false)
        val layoutManager = LinearLayoutManager( context, RecyclerView.VERTICAL, false)
        view.contentTableList.layoutManager = layoutManager
        view.contentTableList.adapter = ContentTableAdapter(SingletonCard.pages, tabLayout, context)
        return view
    }

}
