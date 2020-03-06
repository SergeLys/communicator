package com.ls.comunicator.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.ls.comunicator.R
import com.ls.comunicator.adapters.CardAdapter
import com.ls.comunicator.adapters.CardAdapterEnum
import com.ls.comunicator.adapters.ViewPagerAdapter
import com.ls.comunicator.core.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        val cards = ArrayList<Card>()

        recyclerView = findViewById(R.id.communicative_line)
        recyclerView.layoutManager = LinearLayoutManager( this, RecyclerView.HORIZONTAL, false)
        val cardAdapter = CardAdapter(cards, this, CardAdapterEnum.COMMUNICATIVE_LINE, null)
        recyclerView.adapter = cardAdapter

        SingletonCard.pages = loadPagesList()
        val adapter = ViewPagerAdapter(supportFragmentManager)
        if (SingletonCard.pages.size > 3)
            adapter.addFragment(DictionaryFragment(), "Словарь")
        SingletonCard.pages.forEach {
            adapter.addFragment(PageFragment(cardAdapter, it), it)
        }

        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = adapter

        tabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)

        findViewById<FloatingActionButton>(R.id.settings_button)
            .setOnClickListener {
                val settingsPasswordActivity = Intent(this, SettingsPasswordActivity::class.java)
                startActivity(settingsPasswordActivity)
            }

        findViewById<FloatingActionButton>(R.id.delete_all_button)
            .setOnClickListener {
               cardAdapter.deleteAll()
            }

        findViewById<FloatingActionButton>(R.id.delete_button)
            .setOnClickListener {
                cardAdapter.delete()
            }

        findViewById<FloatingActionButton>(R.id.play_button)
            .setOnClickListener {
               cardAdapter.playAll()
            }
    }
}
