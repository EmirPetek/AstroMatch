package com.emirpetek.mybirthdayreminder.ui.util.calculateTime

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class CalculateAge {

    fun calculateAge(birthDateString: String): Int {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val birthDate: Date = dateFormat.parse(birthDateString) ?: return -1
        val birthCalendar = Calendar.getInstance().apply { time = birthDate }
        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }
}