package com.ls.comunicator.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ls.comunicator.R
import com.ls.comunicator.core.*
import kotlinx.android.synthetic.main.activity_page_settings.*


class PageCreationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_settings)

        pageNameEditText.setText(intent.getStringExtra("page"))
        savePageButton.setOnClickListener {
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
        listCardsBtn.setOnClickListener {
            val listCardsActivity = Intent(this, ListCardsActivity::class.java)
            listCardsActivity.putExtra("page", pageNameEditText.text.toString())
            startActivity(listCardsActivity)
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
        val success = renamePage(baseContext, oldPageName, pageName)
        Toast.makeText(baseContext, if (success) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            55 -> {savePage()}
            56 -> {renamePage()}
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
    }

}
