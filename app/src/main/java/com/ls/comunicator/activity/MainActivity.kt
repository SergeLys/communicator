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
import com.ls.comunicator.core.loadCardsList
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mTTS: TextToSpeech
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR){
                //if there is no error then set language
                mTTS.language = Locale.UK
            }
            if (status == TextToSpeech.SUCCESS) {
                if (mTTS.isLanguageAvailable(Locale(Locale.getDefault().language))
                    == TextToSpeech.LANG_AVAILABLE) {
                    mTTS.language = Locale(Locale.getDefault().language)
                } else {
                    mTTS.language = Locale.US
                }
                mTTS.setPitch(1.3f)
                mTTS.setSpeechRate(0.7f)
            } else if (status == TextToSpeech.ERROR) {
                // TODO error msg
            }
        })

        val cards = loadCardsList("test")

        val adapter =
            ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(PageFragment(), "Дейсвтия")
        adapter.addFragment(PageFragment(), "Природа")
        adapter.addFragment(PageFragment(), "Техника")
        adapter.addFragment(PageFragment(), "Животные")
        adapter.addFragment(PageFragment(), "Растения")

        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = adapter

        tabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)

        recyclerView = findViewById(R.id.communicative_line)
        recyclerView.layoutManager = LinearLayoutManager( this, RecyclerView.HORIZONTAL, false)
        val cardAdapter = CardAdapter(cards, this, CardAdapterEnum.COMMUNICATIVE_LINE)
        recyclerView.adapter = cardAdapter

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
                var speech = ""
                cards.forEach{
                    speech += it.name + " "
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    mTTS.speak(speech,TextToSpeech.QUEUE_FLUSH,null,null)
                else
                    mTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null)
            }
    }
}
