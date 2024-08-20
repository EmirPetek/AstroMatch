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
    val zodiac:Int,
    val ascendant:Int,
    val gender: Int,
    var biography:String,
    var profileGalleryPhotos: ArrayList<UserGalleryPhoto>? = null
) : Serializable{

    constructor(): this("","","","","","","",0,"",0,-1,-1,0,"", arrayListOf())

}
