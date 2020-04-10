package com.ls.comunicator.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.ls.comunicator.R
import com.ls.comunicator.adapter.CardAdapter
import com.ls.comunicator.adapter.CardAdapterEnum
import com.ls.comunicator.core.*
import kotlinx.android.synthetic.main.activity_page_cards.*


class ListCardsActivity : AppCompatActivity() {

    private var cards = ArrayList<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page_cards)

        val permissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else
            cards = loadPage(baseContext, intent.getStringExtra("page"))

        pageCardList.layoutManager = GridLayoutManager( this, 3)
        pageCardList.adapter = CardAdapter(cards, this, CardAdapterEnum.EDIT_PAGE, null)

        addNewCard.setOnClickListener {
            val cardSettingsActivity = Intent(baseContext, CardSettingsActivity::class.java)
            cardSettingsActivity.putExtra("isEdit", true)
            ContextCompat.startActivity(baseContext, cardSettingsActivity, null)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            1 -> {cards = loadPage(baseContext, intent.getStringExtra("page"))}
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
    }

}
