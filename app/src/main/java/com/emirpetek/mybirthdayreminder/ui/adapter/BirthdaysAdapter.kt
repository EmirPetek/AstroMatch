package com.emirpetek.mybirthdayreminder.ui.adapter

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.data.entity.Birthdays
import com.emirpetek.mybirthdayreminder.viewmodel.BirthdaysViewModel
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.ui.fragment.BirthdayUpdateFragment
import kotlin.contracts.contract


class BirthdaysAdapter(
    var mContext: Context,
    var birthdayList: List<Birthdays>,
    viewModel: BirthdaysViewModel
):
    RecyclerView.Adapter<BirthdaysAdapter.CardViewObjHolder>() {

    private var age = -1


    inner class CardViewObjHolder(view : View) : RecyclerView.ViewHolder(view){
        var textViewBirthdayNameSurname = view.findViewById<TextView>(R.id.textViewBirthdayNameSurname)
        var textViewBirthdateDate = view.findViewById<TextView>(R.id.textViewBirthdateDate)
        var textViewRemainDay = view.findViewById<TextView>(R.id.textViewRemainDay)
        var textViewCardGiftIdea = view.findViewById<TextView>(R.id.textViewCardGiftIdea)
        var imageViewMore = view.findViewById<ImageView>(R.id.imageViewCardBirthdayMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewObjHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_birthday,parent,false)
        return CardViewObjHolder(view)
    }

    override fun getItemCount(): Int {
        return birthdayList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CardViewObjHolder, position: Int) {
        //birthdayList.sortedByDescending { it.date }

        val newList = birthdayList.sortedBy{ calculateRemainDay(it.date).toInt() }
        val pos = newList.get(position)

        val remainDay = calculateRemainDay(pos.date)
        val textGiftIdea = mContext.getString(R.string.gift_idea) + ": ${pos.giftIdea}"


        val name_surname = pos.name
        val date = pos.date

        holder.textViewBirthdayNameSurname.text = name_surname
        holder.textViewBirthdateDate.text = date
        holder.textViewRemainDay.text = "After $remainDay days, new age will be $age"
        holder.textViewCardGiftIdea.text = textGiftIdea
        //holder.imageViewMore.visibility = View.GONE

       // holder.imageViewMore.setOnClickListener {popUpMenu(holder.imageViewMore,pos.birthdayKey) }
        holder.imageViewMore.setOnClickListener {
        //    openUpdateFragment(pos.birthdayKey, it)
            val bundle = Bundle()
            bundle.putString("BIRTHDAY_KEY", pos.birthdayKey)

            val updateFragment = BirthdayUpdateFragment()
            updateFragment.arguments = bundle
            it.findNavController().navigate(R.id.action_birthdaysFragment_to_birthdayUpdateFragment,bundle)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateRemainDay(birthdate:String): String{
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val birthday = LocalDate.parse(birthdate, formatter)
        var agee = Period.between(birthday, today).years
        val newAgeDate = birthday.plusYears(agee.toLong() + 1)
        val remainDays = ChronoUnit.DAYS.between(today, newAgeDate)
        age = agee++
        age++
        val output = "After $remainDays days, new age will be $age"

        return remainDays.toString()

    }

    private fun openUpdateFragment(birthdayKey: String, view: View){
        val bundle = Bundle()
        bundle.putString("BIRTHDAY_KEY", birthdayKey)

        val updateFragment = BirthdayUpdateFragment()
        updateFragment.arguments = bundle

        //Navigation.findNavController(mContext).navigate(R.id.action_birthdaysFragment_to_birthdayUpdateFragment, bundle)

        val transaction = (mContext as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.constraintLayoutFragmentBirthdays, updateFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
