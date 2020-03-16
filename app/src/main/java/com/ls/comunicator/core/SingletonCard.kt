package com.ls.comunicator.core

object SingletonCard {
    var card: Card = Card()
    var pages: ArrayList<String> = ArrayList()
    var cardAmount: Int = 3
    var isSearchLine: Boolean = true
}