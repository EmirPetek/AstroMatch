package com.emirpetek.mybirthdayreminder.data.entity.user

import java.io.Serializable

data class User(
    val userID:String,
    val fullname:String,
    val email:String,
    val password:String,
    val birthdate:String,
    val birthTime:String,
    var profile_img:String,
    val created_at:Long,
    val accountDeleteState:String,
    val accountDeleteTime:Long,
    val zodiac:String,
    val ascendant:String,
    val gender: Int,
    val biography:String,
    var profileGalleryPhotos: ArrayList<String>? = null
) : Serializable{

    constructor(): this("","","","","","","",0,"",0,"","",0,"", arrayListOf())

}
