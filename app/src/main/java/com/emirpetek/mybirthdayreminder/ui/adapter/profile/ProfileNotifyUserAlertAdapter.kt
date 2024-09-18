package com.emirpetek.mybirthdayreminder.ui.adapter.profile

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.notify.NotifyPerson
import com.emirpetek.mybirthdayreminder.viewmodel.profile.ProfileViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileNotifyUserAlertAdapter(
    val mContext: Context,
    val notifyList: ArrayList<String>,
    val notifyButton: Button,
    val viewModel: ProfileViewModel,
    val reportedUserID: String,
    val dialog: AlertDialog
): RecyclerView.Adapter<ProfileNotifyUserAlertAdapter.NotifyCardHolder>() {

    val selectedNotifyList: ArrayList<String> = arrayListOf()
    val selectedNotifyListBoolean = MutableList(notifyList.size) { false }

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

        notifyButton.setOnClickListener {
            if (selectedNotifyList.isEmpty()){
                Toast.makeText(mContext,mContext.getString(R.string.should_select_notify_to_notify),Toast.LENGTH_SHORT).show()
            }else{
                val notify = NotifyPerson(
                    reportedUserID,
                    Firebase.auth.currentUser!!.uid,
                    System.currentTimeMillis(),
                    selectedNotifyList,
                    holder.editTextNotifyAnother.text.toString()
                )
                viewModel.insertNotify(notify)
                dialog.dismiss()
                Toast.makeText(mContext,mContext.getString(R.string.notify_success),Toast.LENGTH_SHORT).show()

            }
        }

        holder.radioButton.setOnClickListener {
            if (position == 10){
                holder.editTextNotifyAnother.visibility = View.VISIBLE
            }

            if (!selectedNotifyListBoolean[position]){
                selectedNotifyList.add(position.toString())
                selectedNotifyListBoolean[position] = true
            }
            Log.e("selectedNotifyList: ", selectedNotifyList.toString())

        }


    }


}