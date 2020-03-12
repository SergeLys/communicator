package com.ls.comunicator.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import com.ls.comunicator.core.SingletonCard


class TableContentFragment(val tabLayout: TabLayout) : Fragment() {

    lateinit var searchEditText: TextInputEditText
    lateinit var adapter: ArrayAdapter<String>
    lateinit var contentList: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_table_content, container, false)
        searchEditText = view.findViewById(R.id.search_page_text)
        contentList = view.findViewById(R.id.content_table_list)
        adapter = ArrayAdapter(view.context, android.R.layout.simple_expandable_list_item_1, SingletonCard.pages)
        contentList.adapter = adapter

        contentList.setOnItemClickListener { adapterView, view, i, l ->
            // tab content fragment = +1 tab
            tabLayout.getTabAt(i+1)?.select()
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString() != "") {
                    val iterator = SingletonCard.pages.iterator()
                    var it = ""
                    while (iterator.hasNext()) {
                        it = iterator.next()
                        if (it.contains(p0.toString())) {
                            iterator.remove()
                            break
                        }
                    }
                    if (it != "") SingletonCard.pages.add(0, it)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        return view
    }

}
