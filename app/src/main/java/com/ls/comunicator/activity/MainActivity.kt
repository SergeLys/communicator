package com.ls.comunicator.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import com.ls.comunicator.adapter.CardAdapter
import com.ls.comunicator.adapter.CardAdapterEnum
import com.ls.comunicator.adapter.ViewPagerAdapter
import com.ls.comunicator.core.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var cardAdapter: CardAdapter
    private lateinit var cards: ArrayList<Card>
    private lateinit var mTTS: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_main)

        mTTS = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
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

        var mApp = MyApp()
        speakLineRecyclerView.visibility = View.GONE
        emptySpeakLine.visibility = View.VISIBLE

        cards = ArrayList()
        speakLineRecyclerView.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        cardAdapter = CardAdapter(cards, this, CardAdapterEnum.COMMUNICATIVE_LINE, null)
        speakLineRecyclerView.adapter = cardAdapter

        deleteAllButton.setOnClickListener { cardAdapter.deleteAll() }
        deleteButton.setOnClickListener { cardAdapter.delete() }
        playButton.setOnClickListener {
            PlayAllTask(this, mTTS).execute()
        }
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.RECORD_AUDIO
                ), 1
            )
        } else {
            initTabs()
        }
    }

    private fun initTabs() {
        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val tabs: TabLayout = findViewById(R.id.tabLayout)
        val fragmentAdapter = ViewPagerAdapter(this, supportFragmentManager)
        SingletonCard.pages = loadPagesList(baseContext)
        if (SingletonCard.pages.size >= 1)
            fragmentAdapter.addFragment(TableContentFragment(tabLayout), "Оглавление")
        SingletonCard.pages.forEachIndexed { index, s ->
            try {
                val page = s.split("_")[0]
                fragmentAdapter.addFragment(PageFragment(getCardAmount(this), cardAdapter, s), page)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)?.setIcon(baseContext.resources.getIdentifier("i59", "drawable", packageName))
        SingletonCard.pages.forEachIndexed { index, s ->
            try {
                val icon = s.split("_")[1]
                tabs.getTabAt(index + 1)?.setIcon(baseContext.resources.getIdentifier(icon, "drawable", packageName))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                ) {
                    initTabs()
                } else
                    closeApp()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun closeApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            finishAffinity()
        else
            finish()
    }

    override fun onResume() {
        initTabs()
        super.onResume()
    }

    override fun onDestroy() {
        mTTS.stop()
        mTTS.shutdown()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.settings -> {
                if (getIsPassword(this)) {
                    showPasswordAlert()
                } else {
                    val settingsActivity = Intent(this, SettingsActivity::class.java)
                    startActivity(settingsActivity)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPasswordAlert() {
        val passwordBuilder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogTheme))
        passwordBuilder.setTitle("Пароль")
        val view = layoutInflater.inflate(R.layout.dialog_password, null)
        val passwordTextView = view.findViewById<TextView>(R.id.password_text)
        val inputPassword = view.findViewById<TextInputEditText>(R.id.password_input_text)
        val password = Random.nextInt(1000, 9999)
        passwordTextView.text = password.toString()
        passwordBuilder.setView(view)
        passwordBuilder.setPositiveButton("Ок", null)
        val dialog = passwordBuilder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (inputPassword.text.toString() == password.toString()) {
                val settingsActivity = Intent(this, SettingsActivity::class.java)
                startActivity(settingsActivity)
                dialog.dismiss()
            } else {
                inputPassword.error = "Пароль не совпадает!"
            }
        }
    }

    companion object {
        class PlayAllTask internal constructor(context: MainActivity,
                                               private val mTTS: TextToSpeech
        ) :
            AsyncTask<Int, String, String?>() {

            private var cards: ArrayList<Card> = ArrayList()
            private val activityReference: WeakReference<MainActivity> = WeakReference(context)
            private var mediaPlayer: MediaPlayer = MediaPlayer()


            override fun onPreExecute() {
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                activity.shadowView.visibility = View.VISIBLE
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                cards = activity.cards
            }


            override fun doInBackground(vararg params: Int?): String? {
                try {
                    val activity = activityReference.get()
                    if (activity != null)
                        play(activity.baseContext, cards, mediaPlayer , mTTS)
                } catch (e: InterruptedException) {

                } finally {
                    return null
                }
            }

            override fun onPostExecute(result: String?) {
                mediaPlayer.release()
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                activity.shadowView.visibility = View.GONE
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }
}
