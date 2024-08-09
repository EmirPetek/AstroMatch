package com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant

class CalculateZodiacAscendant(
    val birthdate:String,
    val birthHour:String
) {

    fun getZodiac(): Int{
        val date = birthdate
        val (day, month, year) = date.split("/").map { it.toInt() }
        return calculateZodiacSign(day,month)
    }

    fun getAscendant(): Int{
        val time = birthHour
        val (hour, minute) = time.split(":").map { it.toInt() }
        return calculateAscendant(hour)
    }

    fun calculateZodiacAndAscendant() {
        val date = birthdate
        val time = birthHour

        if (date.isNotEmpty() && time.isNotEmpty()) {
            val (day, month, year) = date.split("/").map { it.toInt() }
            val (hour, minute) = time.split(":").map { it.toInt() }

            //zodiacSign = calculateZodiacSign(day, month)
           // ascendant = calculateAscendant(hour)

        }
    }

    private fun calculateZodiacSign(day: Int, month: Int): Int {
        return when (month) {
            1 -> if (day < 20) 10 else 11
            2 -> if (day < 19) 11 else 12
            3 -> if (day < 21) 12 else 1
            4 -> if (day < 20) 1 else 2
            5 -> if (day < 21) 2 else 3
            6 -> if (day < 21) 3 else 4
            7 -> if (day < 23) 4 else 5
            8 -> if (day < 23) 5 else 6
            9 -> if (day < 23) 6 else 7
            10 -> if (day < 23) 7 else 8
            11 -> if (day < 22) 8 else 9
            12 -> if (day < 22) 9 else 10
            else -> -1 // Unknown index
        }
    }

    private fun calculateAscendant(hour: Int): Int {
        return when (hour) {
            in 0..1 -> 1
            in 2..3 -> 2
            in 4..5 -> 3
            in 6..7 -> 4
            in 8..9 -> 5
            in 10..11 -> 6
            in 12..13 -> 7
            in 14..15 -> 8
            in 16..17 -> 9
            in 18..19 -> 10
            in 20..21 -> 11
            in 22..23 -> 12
            else -> -1 // Unknown index
        }
    }
}