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
import com.ls.comunicator.core.Card

class PageFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_page, container, false)
        // Inflate the layout for this fragment
//        val bitMap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_image_black_24dp)
//
//        val cards = arrayListOf(
//            Card("Машинка", null, Image(ProxyBitMap(bitMap), 10.5F, Color.RED,null, 10, Color.RED)),
//            Card("Грузовик", null, Image(ProxyBitMap(bitMap), 8.5F, Color.BLUE,null, 8, Color.BLUE)),
//            Card("Ложка", null, Image(ProxyBitMap(bitMap), 10.5F, Color.GREEN,null, 10, Color.GREEN)),
//            Card("Тарелка", null, Image(ProxyBitMap(bitMap), 10.5F, Color.RED,null, 10, Color.GREEN)),
//            Card("Машинка", null, Image(ProxyBitMap(bitMap), 10.5F, Color.YELLOW,null, 10, Color.YELLOW))
//        )
        val cards = arrayListOf<Card>()

        recyclerView = view.findViewById(R.id.page_list)
        recyclerView.layoutManager = GridLayoutManager( view.context, 3)
        recyclerView.adapter = CardAdapter(cards, view.context, false)

        return view
    }

}
