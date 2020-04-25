package com.ls.comunicator.presenter

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.ls.comunicator.view.CardSettingsActivity
import com.ls.comunicator.view.ListCardsActivity
import com.ls.comunicator.adapter.CardAdapter
import com.ls.comunicator.adapter.CardAdapterEnum
import com.ls.comunicator.model.Card
import com.ls.comunicator.model.CardModel
import kotlinx.android.synthetic.main.activity_page_cards.*
import java.util.ArrayList

class ListCardsPresenter(private val view: ListCardsActivity, private val model: CardModel) {

    private lateinit var adapter: CardAdapter

    fun loadPageCards(page: String) {
        model.loadPage(view.baseContext, page, object : CardModel.LoadPageCardsCallback {
            override fun onLoad(cards: ArrayList<Card>?) {
                if (cards != null) {
                    view.pageCardList.layoutManager = GridLayoutManager( view.baseContext, 3)
                    adapter = CardAdapter(cards, view, CardAdapterEnum.EDIT_PAGE, null)
                    view.pageCardList.adapter = adapter
                }
            }
        })
    }

    fun saveCard(card: Card) {
        model.savePage(view.baseContext, card.page, card, object: CardModel.CompleteCallback {
            override fun onComplete(result: Boolean) {
                Toast.makeText(view.baseContext, if (result) "Карточка создана!" else "Ошибка при создании!", Toast.LENGTH_SHORT).show()
                if (result) {
                    val cardSettingsActivity = Intent(view.baseContext, CardSettingsActivity::class.java)
                    cardSettingsActivity.putExtra("page", card.page)
                    cardSettingsActivity.putExtra("name", card.name)
                    cardSettingsActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(view.baseContext, cardSettingsActivity, null)
                    view.closeNewCardAlert()
                }
            }
        })
    }
}