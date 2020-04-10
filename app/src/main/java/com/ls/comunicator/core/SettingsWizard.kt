package com.ls.comunicator.core

import android.content.Context
import android.preference.PreferenceManager

private val CARD_AMOUNT = "amount"
private val IS_SEARCH = "search"
private val IS_PASSWORD = "password"
private val MEMORY = "memory"

fun getCardAmount(context: Context): Int {
    val settings = PreferenceManager.getDefaultSharedPreferences(context)
    return settings.getString(CARD_AMOUNT, "3").toInt()
}

fun getIsSearch(context: Context): Boolean {
    val settings = PreferenceManager.getDefaultSharedPreferences(context)
    return settings.getBoolean(IS_SEARCH, false)
}

fun getIsPassword(context: Context): Boolean {
    val settings = PreferenceManager.getDefaultSharedPreferences(context)
    return settings.getBoolean(IS_PASSWORD, false)
}

fun getMemory(context: Context?): String? {
    val settings = PreferenceManager.getDefaultSharedPreferences(context)
    return settings.getString(MEMORY, "internal")
}