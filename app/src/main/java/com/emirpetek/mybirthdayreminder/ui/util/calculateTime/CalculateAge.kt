package com.emirpetek.mybirthdayreminder.ui.util.calculateTime

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

    fun convertDateStringToMillis(dateString: String): Long {
        // Tarih formatını belirleyin
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            // Tarih string'ini Date objesine dönüştür
            val date: Date = dateFormat.parse(dateString)

            // Date objesinin zamanını milisaniye cinsinden döndür
            return date.time

    }
}