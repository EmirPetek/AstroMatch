package com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant

import android.content.Context
import android.graphics.drawable.Drawable
import com.emirpetek.mybirthdayreminder.R

class GetZodiacAscendant(
    val mContext: Context

) {

    fun getZodiacOrAscendantSignByIndex(index: Int): String {
        return when (index) {
            1 -> mContext.getString(R.string.aries)       // Koç
            2 -> mContext.getString(R.string.taurus)      // Boğa
            3 -> mContext.getString(R.string.gemini)      // İkizler
            4 -> mContext.getString(R.string.cancer)      // Yengeç
            5 -> mContext.getString(R.string.leo)         // Aslan
            6 -> mContext.getString(R.string.virgo)       // Başak
            7 -> mContext.getString(R.string.libra)       // Terazi
            8 -> mContext.getString(R.string.scorpio)     // Akrep
            9 -> mContext.getString(R.string.sagittarius) // Yay
            10 -> mContext.getString(R.string.capricorn)  // Oğlak
            11 -> mContext.getString(R.string.aquarius)   // Kova
            12 -> mContext.getString(R.string.pisces)     // Balık
            else -> mContext.getString(R.string.unknown)  // Bilinmeyen
        }
    }

    fun getZodiacDrawableID(zodiac: Int) : Int{
        val ownUserHorosopeDrawableResId = when (zodiac) {
            10 -> R.drawable.capricorn
            11 -> R.drawable.aquarius
            12 -> R.drawable.pisces
            1 -> R.drawable.aries
            2 -> R.drawable.taurus
            3 -> R.drawable.gemini
            4 -> R.drawable.cancer
            5 -> R.drawable.leo
            6 -> R.drawable.virgo
            7 -> R.drawable.libra
            8 -> R.drawable.scorpio
            9 -> R.drawable.sagittarius
            else -> R.drawable.baseline_error_24 // Varsayılan resim
        }
        return ownUserHorosopeDrawableResId
    }

    fun getAscendantDrawableID(ascendant: Int) : Int{
        val anotherUserHoroscopeDrawableResId = when (ascendant) {
            10 -> R.drawable.capricorn
            11 -> R.drawable.aquarius
            12 -> R.drawable.pisces
            1 -> R.drawable.aries
            2 -> R.drawable.taurus
            3 -> R.drawable.gemini
            4 -> R.drawable.cancer
            5 -> R.drawable.leo
            6 -> R.drawable.virgo
            7 -> R.drawable.libra
            8 -> R.drawable.scorpio
            9 -> R.drawable.sagittarius
            else -> R.drawable.baseline_error_24 // Varsayılan resim
        }
        return anotherUserHoroscopeDrawableResId
    }

}