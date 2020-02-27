package com.ls.comunicator.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.ls.comunicator.R
import com.ls.comunicator.adapters.CardAdapter
import com.ls.comunicator.adapters.ViewPagerAdapter
import com.ls.comunicator.core.Card
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mTTS: TextToSpeech
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

//        val bitMap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_image_black_24dp)

//        val cards = arrayListOf(
//            Card("Машинка", null, Image(ProxyBitMap(bitMap), 10.5F, Color.RED,null, 10, Color.RED)),
//            Card("Грузовик", null, Image(ProxyBitMap(bitMap), 8.5F, Color.BLUE,null, 8, Color.BLUE)),
//            Card("Ложка", null, Image(ProxyBitMap(bitMap), 10.5F, Color.GREEN,null, 10, Color.GREEN)),
//            Card("Тарелка", null, Image(ProxyBitMap(bitMap), 10.5F, Color.RED,null, 10, Color.GREEN)),
//            Card("Машинка", null, Image(ProxyBitMap(bitMap), 10.5F, Color.YELLOW,null, 10, Color.YELLOW))
//        )
        val cards = arrayListOf<Card>()

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
        val cardAdapter = CardAdapter(cards, this, true)
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
