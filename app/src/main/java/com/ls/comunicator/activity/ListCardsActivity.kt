package com.ls.comunicator.activity

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import com.ls.comunicator.core.*
import com.ls.comunicator.model.CardModel
import com.ls.comunicator.presenter.ListCardsPresenter
import kotlinx.android.synthetic.main.activity_page_cards.*

class ListCardsActivity : AppCompatActivity() {

    private lateinit var createNewCardAlert: AlertDialog
    private lateinit var presenter: ListCardsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_cards)

        presenter = ListCardsPresenter(this, CardModel())

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else
            presenter.loadPageCards(intent.getStringExtra("page"))

        addNewCard.setOnClickListener {
            val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogTheme))
            builder.setTitle("Название карты")
            val view = layoutInflater.inflate(R.layout.dialog_new_card, null)
            val cardEditText = view.findViewById<TextInputEditText>(R.id.card_name)
            builder.setView(view)
            builder.setPositiveButton("Ok", null)
            createNewCardAlert = builder.create()
            createNewCardAlert.show()
            createNewCardAlert.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener {
                    if (cardEditText.text.toString() != "") {
                        MyApp.card = Card(cardEditText.text.toString(), baseContext)
                        MyApp.card.page =  intent.getStringExtra("page")
                        if (ContextCompat.checkSelfPermission(this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
                        } else {
                            presenter.saveCard(MyApp.card)
                        }
                    } else cardEditText.error = "Введите название!"
                }
        }
    }

    fun closeNewCardAlert() { createNewCardAlert.dismiss() }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.loadPageCards(intent.getStringExtra("page"))
                }
            }
            2 -> { presenter.saveCard(MyApp.card) }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            presenter.loadPageCards(intent.getStringExtra("page"))
        }
        super.onResume()
    }

}
