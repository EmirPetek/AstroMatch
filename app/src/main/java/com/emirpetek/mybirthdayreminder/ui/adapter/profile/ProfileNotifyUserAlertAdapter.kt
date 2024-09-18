package com.emirpetek.mybirthdayreminder.ui.adapter.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R

class ProfileNotifyUserAlertAdapter(
    val mContext: Context,
    val notifyList: ArrayList<String>
): RecyclerView.Adapter<ProfileNotifyUserAlertAdapter.NotifyCardHolder>() {

    inner class NotifyCardHolder(view: View) : RecyclerView.ViewHolder(view){
        val radioButton: RadioButton = view.findViewById(R.id.radioButtonCardNotifyPerson)
        val editTextNotifyAnother: EditText = view.findViewById(R.id.editTextCardNotifyPerson)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyCardHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_notify_person,parent,false)
        return NotifyCardHolder(view)
    }

    override fun getItemCount(): Int {
        return notifyList.size
    }

    override fun onBindViewHolder(holder: NotifyCardHolder, position: Int) {
        val item = notifyList[position]

        holder.radioButton.text = item

        holder.radioButton.setOnClickListener {
            if (position == 10){
                holder.editTextNotifyAnother.visibility = View.VISIBLE
            }
        }


    }


}