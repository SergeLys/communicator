package com.ls.comunicator.core

class SingletonCard {
    companion object {
        var card: Card = Card()
        var pages: ArrayList<String> = ArrayList()
    }
}