package com.ls.comunicator.presenter

import android.widget.Toast
import com.ls.comunicator.model.Card
import com.ls.comunicator.model.CardModel
import com.ls.comunicator.view.CasesActivity
import kotlinx.android.synthetic.main.activity_cases.*

class CasesPresenter(private val view: CasesActivity, private val model: CardModel) {

    private val error = "Заполните падеж"

    fun save(card: Card) {
        if (card.isHasCases) {
            if (isFullCases()) saveCard(card)
        }
        else {
            if (view.nominativeText.text.isNullOrEmpty()) view.nominativeText.error = error
            else saveCard(card)
        }
    }

    fun loadCard(page: String, card: String) {
        model.loadCard(view.baseContext, page, card, object: CardModel.LoadCardCallback {
            override fun onLoad(card: Card?) {
                if (card != null)
                    view.init(card)
            }
        })
    }

    private fun saveCard(card: Card) {
        model.savePage(view.baseContext, card.page, card, object: CardModel.CompleteCallback {
            override fun onComplete(result: Boolean) {
                Toast.makeText(view.baseContext, if (result) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isFullCases(): Boolean {
        var isFull = true
        if (view.nominativeText.text.isNullOrEmpty()) {
            view.nominativeText.error = error
            isFull = false
        }
        if (view.genitiveText.text.isNullOrEmpty()){
            view.genitiveText.error = error
            isFull = false
        }
        if (view.dativeText.text.isNullOrEmpty()){
            view.dativeText.error = error
            isFull = false
        }
        if (view.accusativeText.text.isNullOrEmpty()){
            view.accusativeText.error = error
            isFull = false
        }
        if (view.instrumentalText.text.isNullOrEmpty()){
            view.instrumentalText.error = error
            isFull = false
        }
        if (view.prepositionalText.text.isNullOrEmpty()){
            view.prepositionalText.error = error
            isFull = false
        }
        return isFull
    }
}