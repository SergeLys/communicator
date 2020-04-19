package com.ls.comunicator.presenter

import android.widget.Toast
import com.ls.comunicator.model.Card
import com.ls.comunicator.model.CardModel
import com.ls.comunicator.view.CasesActivity

class CasesPresenter(private val view: CasesActivity, private val model: CardModel) {

    fun loadCard(page: String, card: String) {
        model.loadCard(view.baseContext, page, card, object: CardModel.Companion.LoadCardCallback {
            override fun onLoad(card: Card?) {
                if (card != null)
                    view.init(card)
            }
        })
    }

    fun saveCard(card: Card) {
        model.savePage(view.baseContext, card.page, card, object: CardModel.Companion.CompleteCallback {
            override fun onComplete(result: Boolean) {
                Toast.makeText(view.baseContext, if (result) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
            }
        })
    }
}