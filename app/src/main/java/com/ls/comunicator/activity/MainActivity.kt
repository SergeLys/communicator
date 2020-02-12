package com.ls.comunicator.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.ls.comunicator.R
import com.ls.comunicator.adapters.CardAdapter
import com.ls.comunicator.adapters.ViewPagerAdapter
import com.ls.comunicator.core.Card
import com.ls.comunicator.core.Image

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bitMap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.car)

        val cards = arrayListOf(
            Card("Машинка", null, Image(bitMap, 10.5F, Color.RED,null, 10, Color.RED)),
            Card("Грузовик", null, Image(bitMap, 8.5F, Color.BLUE,null, 8, Color.BLUE)),
            Card("Ложка", null, Image(bitMap, 10.5F, Color.GREEN,null, 10, Color.GREEN)),
            Card("Тарелка", null, Image(bitMap, 10.5F, Color.RED,null, 10, Color.GREEN)),
            Card("Машинка", null, Image(bitMap, 10.5F, Color.YELLOW,null, 10, Color.YELLOW))
        )

        val adapter =
            ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(PageFragment(), "Дейсвтия")
        adapter.addFragment(PageFragment(), "Природа")
        adapter.addFragment(PageFragment(), "Техника")
        adapter.addFragment(PageFragment(), "Животные")
        adapter.addFragment(PageFragment(), "Растения")

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = adapter
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)

        findViewById<RecyclerView>(R.id.communicative_line)
            .layoutManager = LinearLayoutManager( this, RecyclerView.HORIZONTAL, true)
        findViewById<RecyclerView>(R.id.communicative_line).adapter =
            CardAdapter(cards, this, true)

        findViewById<FloatingActionButton>(R.id.settings_button)
            .setOnClickListener {
                val settingsPasswordActivity = Intent(this, SettingsPasswordActivity::class.java)
                startActivity(settingsPasswordActivity)
            }
    }
}
