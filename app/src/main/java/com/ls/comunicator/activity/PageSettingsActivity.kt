package com.ls.comunicator.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import com.ls.comunicator.adapter.CardAdapter
import com.ls.comunicator.adapter.CardAdapterEnum
import com.ls.comunicator.core.*
import com.ls.comunicator.core.Consts.Companion.WRITE_CODE


class PageSettingsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pageNameEditText: TextInputEditText
    private lateinit var alert: AlertDialog
    private var cards = ArrayList<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_settings)

        val permissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else
            cards = loadPage(intent.getStringExtra("page"))

        pageNameEditText = findViewById(R.id.page_name)
        pageNameEditText.setText(intent.getStringExtra("page"))

        recyclerView = findViewById(R.id.page_list)
        recyclerView.layoutManager = GridLayoutManager( this, 3)
        recyclerView.adapter = CardAdapter(cards, this, CardAdapterEnum.EDIT_PAGE, null)

        findViewById<MaterialButton>(R.id.add_symbol_button)
            .setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Название карты")
                val view = layoutInflater.inflate(R.layout.dialog_new_card, null)
                val cardEditText = view.findViewById<TextInputEditText>(R.id.card_name)
                builder.setView(view)
                builder.setPositiveButton("Ok", null)
                alert = builder.create()
                alert.show()
                alert.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener {
                        if (cardEditText.text.toString() != "") {
                            MyApp.card = Card(cardEditText.text.toString(), baseContext)
                            MyApp.card.page = pageNameEditText.text.toString()
                            val permissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            if (permissionStatus == PackageManager.PERMISSION_DENIED) {
                                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 54)
                            } else {
                                saveCard()
                            }
                        } else cardEditText.error = "Введите название!"
                    }
            }

        findViewById<MaterialButton>(R.id.save_page_button)
            .setOnClickListener {
                val oldPageName = intent.getStringExtra("page")
                val pageName  = pageNameEditText.text.toString()
                if (oldPageName == null) {
                    if (pageName != "") {
                        val permissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 55)
                        } else {
                            savePage()
                        }
                    } else pageNameEditText.error = "Имя страницы пустое!"
                }
                else {
                    if (oldPageName != pageName) {
                        val permissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 56)
                        } else {
                            renamePage()
                        }
                    }
                }
            }
    }

    private fun saveCard() {
        val success = savePage(baseContext,  SingletonCard.card.page, SingletonCard.card)
        Toast.makeText(baseContext, if (success) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
        if (success) {
            val cardSettingsActivity = Intent(this, CardSettingsActivity::class.java)
            startActivity(cardSettingsActivity)
            alert.dismiss()
        }
    }

    private fun savePage() {
        val success = savePage(baseContext,  pageNameEditText.text.toString(), null)
        Toast.makeText(baseContext, if (success) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
        if (success)
            SingletonCard.pages.add(pageNameEditText.text.toString())
    }

    private fun renamePage() {
        val oldPageName = intent.getStringExtra("page")
        Toast.makeText(baseContext, "oldPageName: {$oldPageName}", Toast.LENGTH_SHORT).show()
        val pageName  = pageNameEditText.text.toString()
        Toast.makeText(baseContext, "pageName: {$pageName}", Toast.LENGTH_SHORT).show()
        val success = renamePage(oldPageName, pageName)
        Toast.makeText(baseContext, if (success) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            54 -> {saveCard()}
            55 -> {savePage()}
            56 -> {renamePage()}
            1 -> {cards = loadPage(intent.getStringExtra("page"))}
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
    }

}
