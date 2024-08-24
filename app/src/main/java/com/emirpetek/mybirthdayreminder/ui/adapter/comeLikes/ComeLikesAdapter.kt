package com.emirpetek.mybirthdayreminder.ui.adapter.comeLikes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.like.Like
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.enum.LikeType
import com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant.CalculateCompatibility
import com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant.CalculateZodiacAscendant
import com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant.GetZodiacAscendant

class ComeLikesAdapter(
    val mContext: Context,
    val likeList: List<Like>,
    val ownUser: User,
): RecyclerView.Adapter<ComeLikesAdapter.LikeViewHolder>() {

    inner class LikeViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val cardViewComeLikes : CardView = view.findViewById(R.id.cardViewComeLikes)
        val imageViewCardComeLikesUserImg: ImageView = view.findViewById(R.id.imageViewCardComeLikesUserImg)
        val imageViewCardComeLikesLikeType: ImageView = view.findViewById(R.id.imageViewCardComeLikesLikeType)
        val imageViewCardComeLikesUserHoroscope: ImageView = view.findViewById(R.id.imageViewCardComeLikesUserHoroscope)
        val textViewCardComeLikesUsername: TextView = view.findViewById(R.id.textViewCardComeLikesUsername)
        val textViewCardComeLikesUserHoroscope: TextView = view.findViewById(R.id.textViewCardComeLikesUserHoroscope)
        val textViewCardComeLikesCompatibilityRate: TextView = view.findViewById(R.id.textViewCardComeLikesCompatibilityRate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_come_likes,parent,false)
        return LikeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return likeList.size
    }

    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        val item = likeList[position]
        val itemUser = item.user

        // asenkronik problem var onu çöz

        // own userdan gelen veriler ile calc comp işlemini yap
       // Log.e("comelikes user: ", item.user!!.toString())



        Glide.with(mContext).load(item.user?.profile_img).circleCrop().into(holder.imageViewCardComeLikesUserImg)
        if (item.type.equals(LikeType.NORMAL)) {
            Glide.with(mContext).load(R.drawable.like_person).into(holder.imageViewCardComeLikesLikeType)
            holder.cardViewComeLikes.setCardBackgroundColor(mContext.getColor(R.color.card_normal_like))

        }else{
            Glide.with(mContext).load(R.drawable.mega_like_person).into(holder.imageViewCardComeLikesLikeType)
            holder.cardViewComeLikes.setCardBackgroundColor(mContext.getColor(R.color.card_mega_like))
        }

        // load horoscope
        Glide.with(mContext)
            .load(GetZodiacAscendant(mContext).getZodiacDrawableID(itemUser?.zodiac!!))
            .circleCrop()
            .into(holder.imageViewCardComeLikesUserHoroscope)
        holder.textViewCardComeLikesUserHoroscope.text =
                GetZodiacAscendant(mContext).getZodiacOrAscendantSignByIndex(CalculateZodiacAscendant(itemUser.birthdate,itemUser.birthTime).getZodiac())



        holder.textViewCardComeLikesCompatibilityRate.text = "%" +
                CalculateCompatibility().calculateCompatibility(ownUser.zodiac,itemUser.zodiac).toString()
        //Glide.with(mContext).load(item.user?.profile_img).into(holder.imageViewCardComeLikesUserImg)

        holder.textViewCardComeLikesUsername.text = item.user?.fullname
    }




}