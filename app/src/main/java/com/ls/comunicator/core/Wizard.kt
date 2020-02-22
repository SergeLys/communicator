package com.ls.comunicator.core

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.ls.comunicator.core.Consts.Companion.BORDER_COLOUR_WARNING
import com.ls.comunicator.core.Consts.Companion.BORDER_SIZE_WARNING
import com.ls.comunicator.core.Consts.Companion.CARD_CASES_WARNING
import com.ls.comunicator.core.Consts.Companion.CARD_IMAGE_WARNING
import com.ls.comunicator.core.Consts.Companion.CARD_NAME_WARNING
import com.ls.comunicator.core.Consts.Companion.TEXT_COLOUR_WARNING
import com.ls.comunicator.core.Consts.Companion.TEXT_PLACE_WARNING
import com.ls.comunicator.core.Consts.Companion.TEXT_SIZE_WARNING


fun checkCard(appContext: Context, card: Card, isToast: Boolean): Boolean {

    var isValid = true
    if (card.name == null) {
        if (isToast) showToast(appContext ,CARD_NAME_WARNING)
        isValid = false
    }
    if (card.cases == null) {
        if (isToast) showToast(appContext, CARD_CASES_WARNING)
        isValid = false
    }
    if (card.image == null) {
        if (isToast) showToast(appContext, CARD_IMAGE_WARNING)
        isValid = false
    }
    else
        isValid = checkImage(appContext, card.image, isToast)

    return isValid
}

fun checkImage(appContext: Context, image: Image, isToast: Boolean): Boolean {

    var isValid = true
    if (image.borderColour == 0) {
        if (isToast) showToast(appContext, BORDER_COLOUR_WARNING)
        isValid = false
    }
    if (image.borderSize == 0) {
        if (isToast) showToast(appContext, BORDER_SIZE_WARNING)
        isValid = false
    }
    if (image.image == null) {
        if (isToast) showToast(appContext, CARD_IMAGE_WARNING)
        isValid = false
    }
    if (image.textColour == 0) {
        if (isToast) showToast(appContext, TEXT_COLOUR_WARNING)
        isValid = false
    }
    if (image.textPlace == null) {
        if (isToast) showToast(appContext, TEXT_PLACE_WARNING)
        isValid = false
    }
    if (image.textSize == 0F) {
        if (isToast) showToast(appContext, TEXT_SIZE_WARNING)
        isValid = false
    }
    return  isValid
}

fun showToast(appContext: Context,message: String) {
    Toast.makeText(appContext, message, LENGTH_SHORT).show()
}