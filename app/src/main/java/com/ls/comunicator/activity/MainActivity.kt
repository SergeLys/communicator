package com.ls.comunicator.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
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
import kotlin.collections.ArrayList
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)

        val cards = ArrayList<Card>()

        recyclerView = findViewById(R.id.communicative_line)
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)

        recyclerView.layoutManager = LinearLayoutManager( this, RecyclerView.HORIZONTAL, false)
        val cardAdapter = CardAdapter(cards, this, CardAdapterEnum.COMMUNICATIVE_LINE, null)
        recyclerView.adapter = cardAdapter

        SingletonCard.pages = loadPagesList()
        val adapter = ViewPagerAdapter(supportFragmentManager)
        if (SingletonCard.pages.size >= 3)
            adapter.addFragment(TableContentFragment(tabLayout), "Оглавление")
        SingletonCard.pages.forEach {
            adapter.addFragment(PageFragment(cardAdapter, it), it)
        }

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.main_menu_settings -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Пароль")
                val view = layoutInflater.inflate(R.layout.dialog_password, null)
                val passwordTextView = view.findViewById<TextView>(R.id.password_text)
                val inputPassword = view.findViewById<TextInputEditText>(R.id.password_input_text)

                val password = Random.nextInt(10000, 99999)
                passwordTextView.text = password.toString()
                builder.setView(view)
                builder.setPositiveButton("Ok") { dialogInterface, i ->
                    if (inputPassword.text.toString() == password.toString()) {
                        val settingsActivity = Intent(this, SettingsActivity::class.java)
                        startActivity(settingsActivity)
                    }
                }
                builder.show()
            }
            R.id.main_menu_search -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
