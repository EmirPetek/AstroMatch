package com.emirpetek.mybirthdayreminder.data.entity.chat

import java.io.Serializable

data class UserChats(
    val participantUserID:String,
    val chatID:String
) : Serializable{

}
