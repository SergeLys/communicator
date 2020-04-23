package com.ls.comunicator.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import com.ls.comunicator.R
import com.ls.comunicator.model.CardModel
import com.ls.comunicator.presenter.PageCreationPresenter
import kotlinx.android.synthetic.main.activity_page_settings.*
import kotlinx.android.synthetic.main.dialog_list_image.view.*

class PageCreationActivity : AppCompatActivity() {

    private lateinit var presenter: PageCreationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_settings)
        presenter = PageCreationPresenter(this, CardModel)
        presenter.init(intent.getStringExtra("page"))
        val isCardListBtnVisible = intent.getBooleanExtra("isEdit", false)
        cardsListBtn.visibility = if (isCardListBtnVisible) View.VISIBLE else View.GONE
        if (presenter.iconCode != "")
            listIconBtn.setImageResource(baseContext.resources.getIdentifier(presenter.iconCode, "drawable", packageName))
        pageNameEditText.setText(presenter.pageName)

        savePageButton.setOnClickListener {
           presenter.save()
        }
        listCardsBtn.setOnClickListener {
            presenter.openCardsList()
        }
        listIconBtn.setOnClickListener {
            showIconsListDialog()
        }
    }

    fun showIconsListDialog() {
        val iconsBuilder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogTheme))
        iconsBuilder.setTitle("Иконка листа")
        val view = layoutInflater.inflate(R.layout.dialog_list_image, null)
        val scale = view.context.resources.displayMetrics.density
        val dp = (20 * scale + 0.5f).toInt()
        var previousButton: ImageButton? = null
        var iconId = ""
        for (i in 1..256) {
            val icon = ImageButton(view.context)
            val params = GridLayout.LayoutParams()
            icon.contentDescription = "i${i}"
            icon.setImageResource(resources.getIdentifier("i${i}", "drawable", packageName))
            icon.setBackgroundColor(Color.WHITE)
            icon.setPadding(dp, dp, dp, dp)
            params.width = GridLayout.LayoutParams.WRAP_CONTENT
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            icon.layoutParams = params
            icon.setOnClickListener {
                previousButton?.background = null
                previousButton = it as ImageButton
                it.setBackgroundResource(R.drawable.list_icon_border)
                iconId = it.contentDescription.toString()
            }
            view.listIcons.addView(icon)
        }
        iconsBuilder.setView(view)
        iconsBuilder.setPositiveButton("Ок", null)
        iconsBuilder.setNegativeButton("Отмена", null)
        val dialog = iconsBuilder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            presenter.iconCode = iconId
            listIconBtn.setImageResource(resources.getIdentifier(iconId, "drawable", packageName))
            dialog.dismiss()
        }
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener { dialog.dismiss() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            1 -> {presenter.save()}
            2 -> {presenter.renamePage("${pageNameEditText.text.toString()}_${presenter.iconCode}")}
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
    }

}
