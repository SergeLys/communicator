package com.ls.comunicator.presenter

import android.content.Context
import android.widget.Toast
import com.ls.comunicator.activity.MainActivity
import com.ls.comunicator.core.Card
import com.ls.comunicator.model.CardModel
import java.util.ArrayList

class MainPresenter(private val model: CardModel) {

    lateinit var view: MainActivity

    fun loadPagesList(context: Context) {
        model.loadPage(context, "вп_i14", object: CardModel.Companion.LoadPageCardsCallback {
            override fun onLoad(cards: ArrayList<Card>?) {
                Toast.makeText(context, "callback ${cards?.size}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}