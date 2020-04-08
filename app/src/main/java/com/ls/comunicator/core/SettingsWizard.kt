package com.ls.comunicator.core

import android.content.Context

private val APP_SETTINGS = "settings"
private val CARD_AMOUNT = "amount"
private val IS_SEARCH = "search"
private val IS_PASSWORD = "password"

fun saveCardAmount(context: Context, cardAmount: Int) {
    val settings = context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    val editor = settings.edit()
    editor.putInt(CARD_AMOUNT, cardAmount).apply()
}

fun getCardAmount(context: Context): Int {
    val settings = context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    return settings.getInt(CARD_AMOUNT, 3)
}

fun saveIsSearch(context: Context, isSearch: Boolean) {
    val settings = context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    val editor = settings.edit()
    editor.putBoolean(IS_SEARCH, isSearch).apply()
}

fun getIsSearch(context: Context): Boolean {
    val settings = context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    return settings.getBoolean(IS_SEARCH, false)
}

fun saveIsPassword(context: Context, isSearch: Boolean) {
    val settings = context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    val editor = settings.edit()
    editor.putBoolean(IS_PASSWORD, isSearch).apply()
}

fun getIsPassword(context: Context): Boolean {
    val settings = context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    return settings.getBoolean(IS_PASSWORD, false)
}