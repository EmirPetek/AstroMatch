package com.emirpetek.mybirthdayreminder.ui.util.calculateTime

import android.content.Context
import com.emirpetek.mybirthdayreminder.R

class CalculateGenderString(val context: Context) {

    fun getGenderString(gender: Int) : String{
        if (gender == 0) return context.getString(R.string.male)
        if (gender == 1) return context.getString(R.string.female)
        if (gender == 2) return context.getString(R.string.other)
        return ""
    }
}