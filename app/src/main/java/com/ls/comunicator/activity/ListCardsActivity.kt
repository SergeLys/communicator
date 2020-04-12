package com.ls.comunicator.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import com.ls.comunicator.adapter.CardAdapter
import com.ls.comunicator.adapter.CardAdapterEnum
import com.ls.comunicator.core.*
import kotlinx.android.synthetic.main.activity_page_cards.*
import kotlinx.android.synthetic.main.lists_settings.*


class ListCardsActivity : AppCompatActivity() {

    private var cards = ArrayList<Card>()
    private lateinit var createNewCardAlert: AlertDialog
    private lateinit var adapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_cards)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else
            cards = loadPage(baseContext, intent.getStringExtra("page"))

        pageCardList.layoutManager = GridLayoutManager( this, 3)
        adapter = CardAdapter(cards, this, CardAdapterEnum.EDIT_PAGE, null)
        pageCardList.adapter = adapter

        addNewCard.setOnClickListener {
            val builder = AlertDialog.Builder(this)
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
                            saveCard()
                        }
                    } else cardEditText.error = "Введите название!"
                }
        }
    }

    private fun saveCard() {
        val success = savePage(baseContext,  MyApp.card.page, MyApp.card)
        Toast.makeText(baseContext, if (success) "Карточка создана!" else "Ошибка при создании!", Toast.LENGTH_SHORT).show()
        if (success) {
            val cardSettingsActivity = Intent(this, CardSettingsActivity::class.java)
            startActivity(cardSettingsActivity)
            createNewCardAlert.dismiss()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cards = loadPage(baseContext, intent.getStringExtra("page"))
                    adapter = CardAdapter(cards, this, CardAdapterEnum.EDIT_PAGE, null)
                    pageCardList.adapter = adapter
                }
            }
            2 -> {saveCard()}
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            cards = loadPage(baseContext, intent.getStringExtra("page"))
            adapter = CardAdapter(cards, this, CardAdapterEnum.EDIT_PAGE, null)
            pageCardList.adapter = adapter
        }
        super.onResume()
    }

}
