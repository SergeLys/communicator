package com.ls.comunicator.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ls.comunicator.R
import com.ls.comunicator.core.*
import kotlinx.android.synthetic.main.activity_page_settings.*
import kotlinx.android.synthetic.main.dialog_list_image.view.*
import kotlin.Exception


class PageCreationActivity : AppCompatActivity() {

    private var iconCode: String = ""
    private var pageName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_settings)
        getIconCode()
        if (iconCode != "")
            listIconBtn.setImageResource(baseContext.resources.getIdentifier(iconCode, "drawable", packageName))
        pageNameEditText.setText(pageName)
        savePageButton.setOnClickListener {
            val oldPageName = intent.getStringExtra("page")
            val pageName  = "${pageNameEditText.text.toString()}_${iconCode}"
            if (oldPageName == "") {
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

        listIconBtn.setOnClickListener {
            showIconsListDialog()
        }
    }

    private fun getIconCode() {
        val page = intent.getStringExtra("page")
        try {
            pageName = page.split("_")[0]
            iconCode = page.split("_")[1]
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun savePage() {
        val success = savePage(baseContext,  "${pageNameEditText.text.toString()}_${iconCode}", null)
        Toast.makeText(baseContext, if (success) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
        if (success)
            SingletonCard.pages.add(pageNameEditText.text.toString())
    }

    private fun renamePage() {
        val oldPageName = intent.getStringExtra("page")
        val pageName  = pageNameEditText.text.toString()
        val success = renamePage(baseContext, oldPageName, "${pageName}_${iconCode}")
        Toast.makeText(baseContext, if (success) "Сохранено" else "Ошибка при сохранении", Toast.LENGTH_SHORT).show()
    }

    private fun showIconsListDialog() {
        val iconsBuilder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogTheme))
        iconsBuilder.setTitle("Иконка листа")
        val view = layoutInflater.inflate(R.layout.dialog_list_image, null)
        val scale = baseContext.resources.displayMetrics.density
        val dp = (20 * scale + 0.5f).toInt()
        var previousButton: ImageButton? = null
        var iconId = ""
        for (i in 1..256) {
            val icon = ImageButton(view.context)
            val params = GridLayout.LayoutParams()
            icon.contentDescription = "i${i}"
            icon.setImageResource(baseContext.resources.getIdentifier("i${i}", "drawable", packageName))
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
            iconCode = iconId
            listIconBtn.setImageResource(baseContext.resources.getIdentifier(iconId, "drawable", packageName))
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
            55 -> {savePage()}
            56 -> {renamePage()}
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
    }

}
