package com.ls.comunicator.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ls.comunicator.R
import com.ls.comunicator.adapters.CardAdapter
import com.ls.comunicator.core.Card

class PageFragment : Fragment() {

    val cards = arrayListOf(
        Card("Машинка", null, null),
        Card("Грузовик", null, null),
        Card("Ложка", null, null),
        Card("Тарелка", null, null),
        Card("Машинка", null, null)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_page, container, false)
        // Inflate the layout for this fragment
        view.findViewById<RecyclerView>(R.id.page_list)
            .layoutManager = GridLayoutManager( view.context, 3)
        view.findViewById<RecyclerView>(R.id.page_list).adapter =
            CardAdapter(cards, view.context, false)

        return view
    }

}
