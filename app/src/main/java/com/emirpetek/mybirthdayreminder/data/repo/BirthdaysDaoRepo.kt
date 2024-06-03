package com.emirpetek.mybirthdayreminder.data.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.Birthdays
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BirthdaysDaoRepo {

    val birthdayList: MutableLiveData<List<Birthdays>> = MutableLiveData()
    private val dbReference = FirebaseDatabase.getInstance().getReference("birthdays")

    fun getBirthdays(): MutableLiveData<List<Birthdays>> {
        return birthdayList
    }

    fun getBirthdaysData(userID:String){
        val refGetBirthdays = dbReference.child(userID)
        refGetBirthdays.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bdList = ArrayList<Birthdays>()
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val birthday = i.getValue(Birthdays::class.java)!!
                        if (birthday.saverID.equals(userID)  && birthday.deletedState.equals("0")){
                            Log.e("sdkflşds",birthday.toString())
                            birthday.birthdayKey = i.key!!
                            bdList.add(birthday)
                        }
                    }
                }
                birthdayList.value = bdList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun addBirthdays(birthday: Birthdays){
        dbReference.child(birthday.saverID).push().setValue(birthday)
    }

    fun getSpecialBirthdayData(userID:String,birthdayKey:String){
        val refGetBirthdays = dbReference.child(userID)//.child(birthdayKey)
        refGetBirthdays.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bdList = ArrayList<Birthdays>()
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val birthday = i.getValue(Birthdays::class.java)!!
                    //    Log.e("getspecialbirthdaydata fun"," if üstünde")
                       // Log.e("birhtdaykeyler eşit mi", birthdayKey == i.)
                        if (birthday.saverID.equals(userID) && i.key.equals(birthdayKey) && birthday.deletedState.equals("0")){
                            birthday.birthdayKey = i.key!!
                            bdList.add(birthday)
                           // Log.e("getspecialbirthdaydata fun"," if içinde")
                        }
                    }
                }
                birthdayList.value = bdList
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    fun updateBirthday(userID: String, birthdayKey: String, birthday: Map<String, String>){
        dbReference.child(userID).child(birthdayKey).updateChildren(birthday)
    }

    fun deleteBirthday(userID: String, birthdayKey: String, delete: Map<String, Any>){
        dbReference.child(userID).child(birthdayKey).updateChildren(delete)
    }



}
