package com.ls.comunicator.presenter

import android.widget.Toast
import com.ls.comunicator.view.PageCreationActivity
import com.ls.comunicator.model.CardModel

class PageCreationPresenter(private val view: PageCreationActivity, private val model: CardModel) {

    fun savePage(pageName: String) {
        model.savePage(view.baseContext, pageName, null, object : CardModel.CompleteCallback {
            override fun onComplete(result: Boolean) {
                Toast.makeText(view.baseContext, if (result) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun renamePage(oldName: String, newName: String) {
        model.renamePage(view.baseContext, oldName, newName, object : CardModel.CompleteCallback {
            override fun onComplete(result: Boolean) {
                Toast.makeText(view.baseContext, if (result) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
            }
        })
    }


}