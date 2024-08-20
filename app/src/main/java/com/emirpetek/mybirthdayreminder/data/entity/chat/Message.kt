package com.emirpetek.mybirthdayreminder.data.entity.chat

import java.io.Serializable

data class Message(
    val messageID: String,
    val chatID:String,
    val senderID: String,
    val messageText: String,
    val timestamp: Long, // timestamp
    val messageType : MessageType,
    val isRead: Boolean
) : Serializable{
    constructor() : this("","","","",0,MessageType.TEXT,true)
}
