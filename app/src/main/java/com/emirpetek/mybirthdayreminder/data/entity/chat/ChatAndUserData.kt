package com.emirpetek.mybirthdayreminder.data.entity.chat

import com.emirpetek.mybirthdayreminder.data.entity.user.User
import java.io.Serializable

data class ChatAndUserData(
    val chat:Chat,
    val user:User
) : Serializable{
    constructor() : this(Chat(),User())
}
