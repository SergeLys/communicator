package com.ls.comunicator.presenter

import com.ls.comunicator.core.showToastShort
import com.ls.comunicator.view.CardSettingsActivity
import com.ls.comunicator.model.Card
import com.ls.comunicator.model.CardModel
import kotlinx.android.synthetic.main.activity_card_settings.*

class CardSettingsPresenter(private val view: CardSettingsActivity, private val model: CardModel) {

    lateinit var oldName: String

    fun save(card: Card) {
        if (card.name.isNullOrEmpty()) {
            view.cardName.error = "Поле не должно быть пустым!"
            return
        }
        if (oldName != card.name) {
            model.deletePage(view.baseContext, card.page, oldName, object: CardModel.CompleteCallback {
                override fun onComplete(result: Boolean) {
                    if (result) saveCard(card)
                    else showToastShort(view, "Ошибка при сохранении")
                }
            })
        } else saveCard(card)
    }

    fun saveCard(card: Card) {
        model.savePage(view.baseContext, card.page, card, object: CardModel.CompleteCallback {
            override fun onComplete(result: Boolean) {
                oldName = card.name
                if (result) showToastShort(view.baseContext, "Сохранено")
                else showToastShort(view, "Ошибка при сохранении")
            }
        })
    }

    fun loadCard(page: String, card: String) {
        model.loadCard(view.baseContext, page, card, object: CardModel.LoadCardCallback {
            override fun onLoad(card: Card?) {
                if (card != null) {
                    oldName = card.name
                    view.init(card)
                }
            }
        })
    }
}