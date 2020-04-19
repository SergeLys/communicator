package com.ls.comunicator.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ls.comunicator.R
import com.ls.comunicator.model.CardModel
import com.ls.comunicator.presenter.ListsPresenter
import kotlinx.android.synthetic.main.lists_settings.*

class ListsActivity : AppCompatActivity() {

    private lateinit var presenter: ListsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lists_settings)

        presenter = ListsPresenter(this, CardModel)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            presenter.loadPagesList()
        }
        addNewList.setOnClickListener {
                val newPageSettingsActivity = Intent(this, PageCreationActivity::class.java)
                startActivity(newPageSettingsActivity)
            }
    }

    override fun onResume() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            presenter.loadPagesList()
        }
        super.onResume()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.loadPagesList()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
