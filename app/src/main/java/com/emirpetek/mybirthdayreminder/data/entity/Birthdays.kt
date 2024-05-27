package com.emirpetek.mybirthdayreminder.data.entity


import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Birthdays(val saverID:String,
                     val id:String,
                     val name:String,
                     val date:String,
                     val photo:String,
                     val giftIdea:String,
                     val userDegree:String,
                     val dataAddTime:Long,
                     var birthdayKey:String,
                     val deletedState: String,
                     val deleteTime : Long) : Serializable{
    constructor() : this("","","","","","","",11111111111,"","0",0){}

}
