package com.ls.comunicator.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ls.comunicator.R
import com.ls.comunicator.adapters.CardAdapter
import com.ls.comunicator.adapters.CardAdapterEnum
import com.ls.comunicator.core.Card
import com.ls.comunicator.core.loadCardsList

class PageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_page, container, false)

        val cards = loadCardsList("test")

        recyclerView = view.findViewById(R.id.page_list)
        recyclerView.layoutManager = GridLayoutManager( view.context, 3)
        recyclerView.adapter = CardAdapter(cards, view.context, CardAdapterEnum.PAGE)

        return view
    }

}
