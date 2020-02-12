package com.ls.comunicator.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
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
import com.ls.comunicator.core.Image

class PageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_page, container, false)
        // Inflate the layout for this fragment
        val bitMap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.car)

        val cards = arrayListOf(
            Card("Машинка", null, Image(bitMap, 10.5F, Color.RED,null, 10, Color.RED)),
            Card("Грузовик", null, Image(bitMap, 8.5F, Color.BLUE,null, 8, Color.BLUE)),
            Card("Ложка", null, Image(bitMap, 10.5F, Color.GREEN,null, 10, Color.GREEN)),
            Card("Тарелка", null, Image(bitMap, 10.5F, Color.RED,null, 10, Color.GREEN)),
            Card("Машинка", null, Image(bitMap, 10.5F, Color.YELLOW,null, 10, Color.YELLOW))
        )

        view.findViewById<RecyclerView>(R.id.page_list)
            .layoutManager = GridLayoutManager( view.context, 3)
        view.findViewById<RecyclerView>(R.id.page_list).adapter =
            CardAdapter(cards, view.context, false)

        return view
    }

}
