package com.emirpetek.mybirthdayreminder.data.repo

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.Birthdays
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BirthdaysDaoRepo {

    val birthdayList: MutableLiveData<List<Birthdays>> = MutableLiveData()
    val dbReference = FirebaseDatabase.getInstance().getReference("birthdays")

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
                        if (birthday.saverID.equals(userID)){
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

}
