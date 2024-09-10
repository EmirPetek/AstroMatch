package com.emirpetek.mybirthdayreminder.data.entity.user.userFilter

data class UserHoroscopeFilter(var horoscopeList: ArrayList<Int>){
    constructor(): this(arrayListOf())
}