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
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import com.ls.comunicator.adapter.ContentTableAdapter
import com.ls.comunicator.core.SingletonCard


class TableContentFragment(val tabLayout: TabLayout) : Fragment() {

    lateinit var searchEditText: TextInputEditText
    lateinit var contentList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_table_content, container, false)
        searchEditText = view.findViewById(R.id.search_page_text)
        contentList = view.findViewById(R.id.content_table_list)
        val layoutManager = LinearLayoutManager( context, RecyclerView.VERTICAL, false)
        contentList.layoutManager = layoutManager
        contentList.adapter = ContentTableAdapter(SingletonCard.pages, tabLayout, context)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString() != "") {
                    SingletonCard.pages.forEachIndexed { index, s ->
                        if (s.contains(p0.toString())) {
                            layoutManager.scrollToPositionWithOffset(index, 0)
                        }
                    }
                }
            }
        })
        return view
    }

}
