package com.emirpetek.mybirthdayreminder.ui.adapter.profile.profileVisitors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.user.ProfileVisit
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateShareTime
import com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant.CalculateZodiacAscendant
import com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant.GetZodiacAscendant

class ProfileVisitorsAdapter(
    val mContext: Context,
    val visitorList: List<ProfileVisit>
): RecyclerView.Adapter<ProfileVisitorsAdapter.CardViewHolder>(){

    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imageViewCardProfileVisitorUserProfile: ImageView = view.findViewById(R.id.imageViewCardProfileVisitorUserProfile)
        val imageViewCardProfileVisitorUserZodiac: ImageView = view.findViewById(R.id.imageViewCardProfileVisitorUserZodiac)
        val textViewCardProfileUserFullname: TextView = view.findViewById(R.id.textViewCardProfileUserFullname)
        val textViewCardProfileVisitorVisitingTime: TextView = view.findViewById(R.id.textViewCardProfileVisitorVisitingTime)
        val textViewCardProfileVisitorUserZodiac: TextView = view.findViewById(R.id.textViewCardProfileVisitorUserZodiac)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_profile_visitor,parent,false)
        return CardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return visitorList.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = visitorList[position]

        val zodiacDrawable = GetZodiacAscendant(mContext).getZodiacDrawableID(item.user?.zodiac!!)

        holder.textViewCardProfileUserFullname.text = item.user?.fullname
        holder.textViewCardProfileVisitorVisitingTime.text = CalculateShareTime(mContext).unixtsToDate(item.timestamp.toString())
        holder.textViewCardProfileVisitorUserZodiac.text =
            GetZodiacAscendant(mContext).getZodiacOrAscendantSignByIndex(item.user?.zodiac!!)
        Glide.with(mContext).load(zodiacDrawable).into(holder.imageViewCardProfileVisitorUserZodiac)
        Glide.with(mContext).load(item.user?.profile_img).into(holder.imageViewCardProfileVisitorUserProfile)

    }


}