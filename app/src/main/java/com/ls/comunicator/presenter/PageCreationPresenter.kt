package com.ls.comunicator.presenter

import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.ls.comunicator.core.showToastShort
import com.ls.comunicator.view.PageCreationActivity
import com.ls.comunicator.model.CardModel
import com.ls.comunicator.view.ListCardsActivity
import kotlinx.android.synthetic.main.activity_page_settings.*

class PageCreationPresenter(private val view: PageCreationActivity, private val model: CardModel) {

    var iconCode: String = ""
    var pageName: String = ""
    var oldPageName: String? = null

    fun savePage(pageName: String) {
        model.savePage(view.baseContext, pageName, null, object : CardModel.CompleteCallback {
            override fun onComplete(result: Boolean) {
                Toast.makeText(view.baseContext, if (result) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
                view.cardsListBtn.visibility = View.VISIBLE
                oldPageName = pageName
            }
        })
    }

    fun renamePage(pageName: String) {
        oldPageName?.let {
            model.renamePage(view.baseContext, it, pageName, object : CardModel.CompleteCallback {
                override fun onComplete(success: Boolean) {
                    if (success) {
                        showToastShort(view.baseContext, "Сохранено")
                        oldPageName = pageName
                    } else
                        showToastShort(view.baseContext, "Ошибка при сохранении")
                }
            })
        }
    }

    fun openCardsList() {
        val listCardsActivity = Intent(view.baseContext, ListCardsActivity::class.java)
        listCardsActivity.putExtra("page", "${view.pageNameEditText.text.toString()}_${iconCode}")
        startActivity(view.baseContext, listCardsActivity, null)
    }

    fun save() {
        val pageName  = "${view.pageNameEditText.text.toString()}_${iconCode}"
        if (oldPageName.isNullOrEmpty()) {
            if (!pageName.matches(Regex("^_.*"))) {
                if (ContextCompat.checkSelfPermission(view.baseContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    ActivityCompat.requestPermissions(view, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                else
                    savePage(pageName)
            } else view.pageNameEditText.error = "Имя страницы пустое!"
        }
        else {
            if (!pageName.matches(Regex("^_.*"))) {
                if (oldPageName != pageName) {
                    if (ContextCompat.checkSelfPermission(view.baseContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                        ActivityCompat.requestPermissions(view, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
                    else
                        renamePage(pageName)
                }
            } else view.pageNameEditText.error = "Имя страницы пустое!"
        }
    }

    fun init(oldPageName: String?) {
        this.oldPageName = oldPageName
        try {
            if (oldPageName != null) {
                pageName = oldPageName.split("_")[0]
                iconCode = oldPageName.split("_")[1]
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}