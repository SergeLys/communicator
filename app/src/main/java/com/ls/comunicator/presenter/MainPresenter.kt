package com.ls.comunicator.presenter

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ls.comunicator.R
import com.ls.comunicator.view.MainActivity
import com.ls.comunicator.view.PageFragment
import com.ls.comunicator.view.TableContentFragment
import com.ls.comunicator.adapter.CardAdapter
import com.ls.comunicator.adapter.ViewPagerAdapter
import com.ls.comunicator.model.CardModel
import kotlinx.coroutines.*
import java.util.ArrayList

class MainPresenter(private val view: MainActivity, private val model: CardModel) {

    fun loadPagesList(adapter: CardAdapter) {
        CoroutineScope(Dispatchers.Main).launch {
            val pages = model.loadPagesList(view.baseContext)
            initTabs(pages, adapter)
        }
    }

    private fun initTabs(pages: ArrayList<String>?, adapter: CardAdapter) {
        val viewPager: ViewPager = view.findViewById(R.id.viewPager)
        val tabs: TabLayout =view.findViewById(R.id.tabLayout)
        val fragmentAdapter = ViewPagerAdapter(view.baseContext, view.supportFragmentManager)
        if (pages != null && pages.size >= 1)
            fragmentAdapter.addFragment(
                TableContentFragment(
                    tabs
                ), "Оглавление")
        pages?.forEachIndexed { index, s ->
            try {
                val pageName = s.split("_")[0]
                fragmentAdapter.addFragment(
                    PageFragment(
                        adapter,
                        s
                    ), pageName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)?.setIcon(view.resources.getIdentifier("i59", "drawable", view.packageName))
        pages?.forEachIndexed { index, s ->
            try {
                val icon = s.split("_")[1]
                tabs.getTabAt(index + 1)?.setIcon(view.baseContext.resources.getIdentifier(icon, "drawable", view.packageName))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}