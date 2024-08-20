package com.emirpetek.mybirthdayreminder.data.entity.chat

import java.io.Serializable

data class Chat(
    val chatID: String = "",
    val participants: Map<String, Boolean> = emptyMap(),
    val createTime: Long = 0L, // timestamp
    val deleteState: Int = 0,
    val deleteTime: Long = 0L,
    val lastMessage: String = "",
    val lastMessageTimestamp: Long = 0L, // timestamp
    val unreadCount: Map<String, Int> = emptyMap()
): Serializable{
    constructor() : this("", mapOf(),0,0,0,"",0, mapOf())
}
