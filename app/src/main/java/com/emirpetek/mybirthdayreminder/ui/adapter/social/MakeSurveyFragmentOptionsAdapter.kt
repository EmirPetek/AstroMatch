package com.emirpetek.mybirthdayreminder.ui.adapter.social

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import org.w3c.dom.Text

class MakeSurveyFragmentOptionsAdapter (
    val mContext : Context,
    val optionList: ArrayList<String>
) : RecyclerView.Adapter<MakeSurveyFragmentOptionsAdapter.CardViewHolder>(){



    inner class CardViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewOption: TextView = view.findViewById(R.id.textViewCardMakeSurveyOption)
        val imageViewOptionDelete: ImageView = view.findViewById(R.id.imageViewMakeSurveyDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_make_survey_option,parent,false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = optionList[position]
        val pos = position

        holder.imageViewOptionDelete.setOnClickListener {
            Log.e("silinen eleman", "$item ve indexi $pos")
            optionList.removeAt(position)
            notifyDataSetChanged()
        }
        holder.textViewOption.text = item
    }

}
