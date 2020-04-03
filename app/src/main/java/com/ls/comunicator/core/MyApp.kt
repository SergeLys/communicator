package com.ls.comunicator.core

import android.app.Application

class MyApp : Application() {
    companion object {
        var card: Card = Card()
        var pages: ArrayList<String> = ArrayList()
    }

    override fun onCreate() {
        super.onCreate()
    }
}