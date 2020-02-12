package com.ls.comunicator.core

interface Builder {
    fun reset()
    fun edit(card: Card)
    fun buildName(name: String)
    fun buildCase(cases: MutableMap<CaseEnum, Case>)
    fun buildImage(image: Image)
}