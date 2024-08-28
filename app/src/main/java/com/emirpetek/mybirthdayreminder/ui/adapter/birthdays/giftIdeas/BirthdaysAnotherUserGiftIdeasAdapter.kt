package com.emirpetek.mybirthdayreminder.ui.adapter.birthdays.giftIdeas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.birthdays.BirthdayGiftIdea
import com.emirpetek.mybirthdayreminder.ui.util.userDegree.GetUserDegree

class BirthdaysAnotherUserGiftIdeasAdapter(
    val mContext: Context,
    val list : List<BirthdayGiftIdea>,
    val progressBar: ProgressBar
): RecyclerView.Adapter<BirthdaysAnotherUserGiftIdeasAdapter.CardHolder>() {

    inner class CardHolder(view: View) : RecyclerView.ViewHolder(view){
        val textViewUserDegree: TextView = view.findViewById(R.id.textViewCardAnotherUserDegree)
        val textViewIdea: TextView = view.findViewById(R.id.textViewCardAnotherUserGiftIdea)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BirthdaysAnotherUserGiftIdeasAdapter.CardHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_another_users_gift_idea,parent,false)
        return CardHolder(view)
    }

    override fun onBindViewHolder(
        holder: BirthdaysAnotherUserGiftIdeasAdapter.CardHolder,
        position: Int
    ) {
        val item = list[position]

        holder.textViewIdea.text =
            "${mContext.getString(R.string.gift_idea)} : ${item.giftIdea}"

        holder.textViewUserDegree.text =
            "${mContext.getString(R.string.user_degree)} : ${GetUserDegree(mContext).getUserDegree(item.userDegree)}"

        progressBar.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return list.size
    }


}