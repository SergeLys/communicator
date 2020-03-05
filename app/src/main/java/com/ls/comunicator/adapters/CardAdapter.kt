package com.ls.comunicator.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.ls.comunicator.R
import com.ls.comunicator.activity.CardSettingsActivity
import com.ls.comunicator.core.Card
import com.ls.comunicator.core.Consts
import com.ls.comunicator.core.SingletonCard
import kotlinx.android.synthetic.main.card_list_item.view.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class CardAdapter(val cards : ArrayList<Card>, val context: Context, val type: CardAdapterEnum, val communicate: CardAdapter?) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    private lateinit var mTTS: TextToSpeech

    init {
        mTTS = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR){
                //if there is no error then set language
                mTTS.language = Locale.UK
            }
            if (status == TextToSpeech.SUCCESS) {
                if (mTTS.isLanguageAvailable(Locale(Locale.getDefault().language))
                    == TextToSpeech.LANG_AVAILABLE) {
                    mTTS.language = Locale(Locale.getDefault().language)
                } else {
                    mTTS.language = Locale.US
                }
                mTTS.setPitch(1.3f)
                mTTS.setSpeechRate(0.7f)
            } else if (status == TextToSpeech.ERROR) {
                // TODO error msg
            }
        })
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_list_item, parent, false))
        if(type == CardAdapterEnum.COMMUNICATIVE_LINE)
            viewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_list_communicative_item, parent, false))
        return viewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cards.get(position))
    }

    fun add(card: Card) {
        cards.add(card)
        notifyDataSetChanged()
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

    fun playAll() {
        var speech = ""
        cards.forEach{
            speech += it.name + " "
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            mTTS.speak(speech,TextToSpeech.QUEUE_FLUSH,null,null)
        else
            mTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null)
    }

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val cardFrame = view.card_frame
        val cardImage = view.card_image
        val cardText = view.card_text

        fun bind(card: Card) {
            cardFrame.strokeColor = card.image.borderColour
            cardFrame.strokeWidth = card.image.borderSize
            cardImage.load(File(Environment.getExternalStorageDirectory().absoluteFile, "/${Consts.CARD_FOLDER}/test/${card.name.toLowerCase(
                Locale.getDefault())}/image.jpg"))
            cardText.setTextColor(card.image.textColour)
            cardText.textSize = card.image.textSize
            cardText.text = card.name

            when(type) {
                CardAdapterEnum.PAGE -> {
                    itemView.setOnLongClickListener{
                        communicate?.add(card)
                        true
                    }
                    itemView.setOnClickListener{
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            mTTS.speak(card.name,TextToSpeech.QUEUE_FLUSH,null,null)
                        else
                            mTTS.speak(card.name, TextToSpeech.QUEUE_FLUSH, null)
                    }
                }
                CardAdapterEnum.EDIT_PAGE -> {
                    val popupMenu = PopupMenu(context, itemView)
                    popupMenu.inflate(R.menu.cardmenu)
                    popupMenu.setOnMenuItemClickListener {
                        when(it.itemId) {
                            R.id.menu_edit -> {
                                card.image.imageView = ImageView(context)
                                card.image.imageView.setImageDrawable(cardImage.drawable)
                                SingletonCard.card = card
                                val cardSettingsActivity = Intent(context, CardSettingsActivity::class.java)
                                startActivity(context, cardSettingsActivity, null)
                                true
                            }
                            R.id.menu_delete -> {
                                cards.remove(card)
                                notifyDataSetChanged()
                                true
                            }
                            else -> false
                        }
                    }
                    itemView.setOnLongClickListener{
                        popupMenu.show()
                        true
                    }
                }
                else -> {}
            }
        }
    }
}


