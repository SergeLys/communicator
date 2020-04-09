package com.ls.comunicator.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.ls.comunicator.R
import com.ls.comunicator.adapter.CardAdapter
import com.ls.comunicator.adapter.CardAdapterEnum
import com.ls.comunicator.adapter.ViewPagerAdapter
import com.ls.comunicator.core.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var cardAdapter: CardAdapter
    private lateinit var fragmentAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_main)

        var mApp = MyApp()
        speakLineRecyclerView.visibility = View.GONE
        emptySpeakLine.visibility = View.VISIBLE

        val cards = ArrayList<Card>()
        speakLineRecyclerView.layoutManager = LinearLayoutManager( this, RecyclerView.HORIZONTAL, false)
        cardAdapter = CardAdapter(cards, this, CardAdapterEnum.COMMUNICATIVE_LINE, null)
        speakLineRecyclerView.adapter = cardAdapter
        fragmentAdapter = ViewPagerAdapter(this, supportFragmentManager)

        deleteAllButton.setOnClickListener { cardAdapter.deleteAll() }
        deleteButton.setOnClickListener { cardAdapter.delete() }
        playButton.setOnClickListener {
            val play = GlobalScope.launch {
                cardAdapter.playAll()
            }
        }

        if ( ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO), 1)
        }
        else {
            SingletonCard.pages = loadPagesList()
            if (SingletonCard.pages.size >= 3)
                fragmentAdapter.addFragment(TableContentFragment(tabLayout), "Оглавление")
            SingletonCard.pages.forEach {
                fragmentAdapter.addFragment(PageFragment(getCardAmount(this), cardAdapter, it), it)
            }
            viewPager.adapter = fragmentAdapter
            tabLayout?.setupWithViewPager(viewPager)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            1 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    SingletonCard.pages = loadPagesList()
                    if (SingletonCard.pages.size >= 3)
                        fragmentAdapter.addFragment(TableContentFragment(tabLayout), "Оглавление")
                    SingletonCard.pages.forEach {
                        fragmentAdapter.addFragment(PageFragment(getCardAmount(this), cardAdapter, it), it)
                    }
                    viewPager.adapter = fragmentAdapter
                    tabLayout?.setupWithViewPager(viewPager)
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                        finishAffinity()
                    else
                        finish()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        SingletonCard.pages = loadPagesList()
        fragmentAdapter.notifyDataSetChanged()
        super.onResume()
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
                    val settingsActivity = Intent(this, SettingsActivity2::class.java)
                    startActivity(settingsActivity)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPasswordAlert() {
        val passwordBuilder = AlertDialog.Builder(this)
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
                val settingsActivity = Intent(this, SettingsActivity2::class.java)
                startActivity(settingsActivity)
                dialog.dismiss()
            } else {
                inputPassword.error = "Пароль не совпадает!"
            }
        }
    }
}
