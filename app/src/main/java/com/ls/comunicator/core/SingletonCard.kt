package com.ls.comunicator.core

object SingletonCard {
    var card: Card = Card()
    lateinit var pages: List<String>
}