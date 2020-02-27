package com.ls.comunicator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.ls.comunicator.R
import com.ls.comunicator.core.Card
import kotlinx.android.synthetic.main.card_list_item.view.*
import java.io.File

class CardAdapter(val cards : ArrayList<Card>, val context: Context, val isCommunicativeLine: Boolean) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return cards.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_list_item, parent, false))
        if(isCommunicativeLine)
            viewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_list_communicative_item, parent, false))
        return viewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cards.get(position))
    }

    fun delete() {
        if (cards.isNotEmpty()) {
            cards.removeAt(cards.lastIndex)
            notifyDataSetChanged()

        }
    }

    fun deleteAll() {
        if (cards.isNotEmpty()) {
            cards.clear()
            notifyDataSetChanged()

        }
    }

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val cardFrame = view.card_frame
        val cardImage = view.card_image
        val cardText = view.card_text

        fun bind(card: Card) {
//        TODO textPlace
            cardFrame.strokeColor = card.image.borderColour
            cardFrame.strokeWidth = card.image.borderSize
            cardImage.load(File(card.image.imagePath))
            cardText.setTextColor(card.image.textColour)
            cardText.textSize = card.image.textSize
            cardText.text = card.name
        }
    }

    interface Callback {
        fun onItemClicked(card: Card)
    }
}


