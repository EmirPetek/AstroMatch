package com.emirpetek.mybirthdayreminder.data.entity

import java.io.Serializable

data class UserCredits(
    val userID:String,
    val amount: Long,
    val likeRights:Long,
    val megaLikeRights:Long,
    val lastCreditBalanceTimestamp:Long
): Serializable{

    constructor() : this("",0,0,0,0)
}
