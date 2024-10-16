package com.emirpetek.mybirthdayreminder.ui.adapter.matchPerson

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.like.Like
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.enum.LikeType
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateAge
import com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant.CalculateCompatibility
import com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant.GetZodiacAscendant
import com.emirpetek.mybirthdayreminder.viewmodel.matchPerson.MatchPersonViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MatchPersonListUsersAdapter(
    val mContext: Context,
    val userList: ArrayList<User>,
    val viewModel: MatchPersonViewModel,
    val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<MatchPersonListUsersAdapter.CardUser>(){

    companion object {
        private val MEGA_LIKE_CREDIT_COST : Long = 5
        private val LIKE_CREDIT_COST : Long = 1
        private val LIKE_RIGHT_COST : Long = 1
        private val MEGA_LIKE_RIGHT_COST : Long = 1
    }

    inner class CardUser(view: View) : RecyclerView.ViewHolder(view){
        val username: TextView = view.findViewById(R.id.textViewMatchPersonFullname)
        val age: TextView = view.findViewById(R.id.textViewMatchPersonAge)
        val horoscope: TextView = view.findViewById(R.id.textViewMatchPersonHoroscope)
        val compatibility: TextView = view.findViewById(R.id.textViewMatchPersonCompitabilityRate)
        val userPhoto: ImageView = view.findViewById(R.id.imageViewMatchPersonImage)
        val horoscopePhoto: ImageView = view.findViewById(R.id.imageViewMatchPersonHoroscope)
        val cancelButton: ImageView = view.findViewById(R.id.imageViewMatchPersonCancel)
        val superLikeButton: ImageView = view.findViewById(R.id.imageViewMatchPersonSuperLike)
        val likeButton: ImageView = view.findViewById(R.id.imageViewMatchPersonLike)
        val constraintLayoutTopSide: ConstraintLayout = view.findViewById(R.id.constraintLayoutCardMatchTopSide)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardUser {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_match_person,parent,false)
        return CardUser(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: CardUser, position: Int) {
        val userItem = userList[position]
        val username = userItem.fullname
        val age = CalculateAge().calculateAge(userItem.birthdate)
        val horoscope = GetZodiacAscendant(mContext).getZodiacOrAscendantSignByIndex(userItem.zodiac)
        val userImage = userItem.profile_img

        holder.username.text = username
        holder.age.text = age.toString()
        holder.horoscope.text = horoscope
        holder.constraintLayoutTopSide.setOnClickListener {
            val bundle : Bundle = Bundle().apply {
                putString("userID",userItem.userID)
            }
            Navigation.findNavController(it).navigate(R.id.action_matchPersonFragment_to_profileFragment,bundle)
        }
        bindHoroscopeImage(userItem.zodiac,holder.horoscopePhoto)
        bindCompitabilityRate(holder.compatibility,userItem.zodiac)

        Glide.with(mContext)
            .load(userImage)
            .placeholder(android.R.drawable.screen_background_dark_transparent)
            .centerCrop()
            .into(holder.userPhoto)


        viewModel.getUserCreditsAmount()
        viewModel.credit.observe(viewLifecycleOwner, Observer { credit ->

            holder.likeButton.setOnClickListener {
                val like = Like(Firebase.auth.currentUser!!.uid,userItem.userID,System.currentTimeMillis(),LikeType.NORMAL,"",0,0)

                if (credit.likeRights > 0){
                    // hak var, haktan harcama yapar
                    viewModel.decrementLikeRight(LIKE_RIGHT_COST)
                    viewModel.insertLikeUser(like)
                    userList.removeAt(position)
                    notifyItemRemoved(position)
                }else{
                    // hak yok, krediden harcama yapar
                    if (credit.amount > 0){
                        showToastMessage(mContext.getString(R.string.liked_for_one_gold))
                        viewModel.decrementUserCredit(LIKE_CREDIT_COST)
                        viewModel.insertLikeUser(like)
                        userList.removeAt(position)
                        notifyItemRemoved(position)
                    }else{
                        showToastMessage(mContext.getString(R.string.not_enough_gold_and_right))
                    }


                }
            }

            holder.superLikeButton.setOnClickListener {
                val like = Like(Firebase.auth.currentUser!!.uid,userItem.userID,System.currentTimeMillis(),LikeType.MEGA,"",0,0)
                if (credit.megaLikeRights > 0){
                    // hak var, haktan harcama yapar
                    viewModel.decrementMegaLikeRight(MEGA_LIKE_RIGHT_COST)
                    viewModel.insertLikeUser(like)
                    userList.removeAt(position)
                    notifyItemRemoved(position)
                }else{
                    // hak yok, krediden harcama yapar
                    if (credit.amount > 5) {
                        showToastMessage(mContext.getString(R.string.mega_like_for_five_gold))
                        viewModel.decrementUserCredit(MEGA_LIKE_CREDIT_COST)
                        viewModel.insertLikeUser(like)
                        userList.removeAt(position)
                        notifyItemRemoved(position)
                    }else{
                        showToastMessage(mContext.getString(R.string.not_enough_gold_and_right))
                    }

                }
            }
        })


        holder.cancelButton.setOnClickListener {
            userList.removeAt(position)
            notifyItemRemoved(position)
        }

    }

    private fun bindHoroscopeImage(zodiac: Int, imageView: ImageView){
        val horoscopeDrawableId = when (zodiac) {
            10 -> R.drawable.capricorn
            11 -> R.drawable.aquarius
            12 -> R.drawable.pisces
            1 -> R.drawable.aries
            2 -> R.drawable.taurus
            3 -> R.drawable.gemini
            4 -> R.drawable.cancer
            5 -> R.drawable.leo
            6 -> R.drawable.virgo
            7 -> R.drawable.libra
            8 -> R.drawable.scorpio
            9 -> R.drawable.sagittarius
            else -> R.drawable.baseline_error_24 // Varsayılan resim
        }

        Glide.with(mContext)
            .load(horoscopeDrawableId)
            .into(imageView)

    }

    fun bindCompitabilityRate(textView: TextView, anotherZodiac: Int){
        viewModel.getOwnUserZodiac(Firebase.auth.currentUser!!.uid)
        viewModel.userOwnZodiac.observe(viewLifecycleOwner, Observer { it ->
            val ownUserZodiac = it
            val anotherUserZodiac = anotherZodiac
            val calculation = CalculateCompatibility().calculateCompatibility(ownUserZodiac,anotherUserZodiac)
            textView.text = "% $calculation"
        })
    }

    fun showToastMessage(msg: String){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show()
    }


}