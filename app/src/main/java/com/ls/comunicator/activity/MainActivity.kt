package com.ls.comunicator.activity

import android.content.Intent
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

class MainActivity : AppCompatActivity() {

    val cards = arrayListOf(
        Card("Машинка", null, null),
        Card("Грузовик", null, null),
        Card("Ложка", null, null),
        Card("Тарелка", null, null),
        Card("Машинка", null, null)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
