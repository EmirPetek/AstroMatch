package com.emirpetek.mybirthdayreminder.data.entity

import java.io.Serializable

data class UserGalleryPhoto(
    val imageURL:String,
    val timestamp: Long
): Serializable{
    constructor() : this("",0)
}
