package com.emirpetek.mybirthdayreminder.data.entity.like

import com.emirpetek.mybirthdayreminder.data.enum.LikeType

data class Like(
    val senderUserId: String,
    val receiverUserId: String,
    val timestamp: Long,
    val type: LikeType,
    val likeID:String
)
