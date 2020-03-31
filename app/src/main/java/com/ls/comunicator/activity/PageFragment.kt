package com.ls.comunicator.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ls.comunicator.R
import com.ls.comunicator.adapter.CardAdapter
import com.ls.comunicator.adapter.CardAdapterEnum
import com.ls.comunicator.core.loadPage

class PageFragment(val cardAmount: Int, val communicate : CardAdapter, val page: String) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: StaggeredGridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_page, container, false)

        val cards = loadPage(page)

        recyclerView = view.findViewById(R.id.page_list)
        layoutManager = StaggeredGridLayoutManager(cardAmount, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = CardAdapter(cards, view.context, CardAdapterEnum.PAGE, communicate)

        return view
    }

    fun updateSpanCount(spanCount: Int) {
        layoutManager.spanCount = spanCount
    }

}
