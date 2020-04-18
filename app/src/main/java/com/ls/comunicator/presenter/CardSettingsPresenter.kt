package com.ls.comunicator.presenter

import android.widget.Toast
import com.ls.comunicator.activity.CardSettingsActivity
import com.ls.comunicator.core.Card
import com.ls.comunicator.model.CardModel

class CardSettingsPresenter(private val view: CardSettingsActivity, private val model: CardModel) {

    fun saveCard(oldName: String, card: Card) {
        if (oldName != card.name) {
            model.deletePage(view.baseContext, card.page, oldName, object: CardModel.Companion.CompleteCallback {
                override fun onComplete(result: Boolean) {}
            })
        } else {
            model.savePage(view.baseContext, card.page, card, object: CardModel.Companion.CompleteCallback {
                override fun onComplete(result: Boolean) {
                    Toast.makeText(view.baseContext, if (result) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun loadCard(page: String, card: String) {

    }
}