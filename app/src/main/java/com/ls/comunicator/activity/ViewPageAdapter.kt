package com.ls.comunicator.activity

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentList = ArrayList<PageFragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): PageFragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: PageFragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }
}