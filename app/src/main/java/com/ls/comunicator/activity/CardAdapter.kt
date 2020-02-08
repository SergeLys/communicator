package com.ls.comunicator.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ls.comunicator.R
import com.ls.comunicator.core.Card

class CardAdapter(val cards : ArrayList<Card>, val context: Context) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return cards.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_list_item, parent, false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cards.get(position))
    }

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val cardImage = view.findViewById<ImageView>(R.id.card_image)
        val cardText = view.findViewById<TextView>(R.id.card_text)

        fun bind(card: Card) {
//        TODO
//        cardImage.setImageDrawable(card.image)
            cardText.text = card.name
        }
    }

    interface Callback {
        fun onItemClicked(card: Card)
    }
}


