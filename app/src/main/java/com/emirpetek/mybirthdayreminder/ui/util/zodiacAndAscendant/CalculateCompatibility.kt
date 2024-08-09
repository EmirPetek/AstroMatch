package com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant

class CalculateCompatibility {

    fun calculateCompatibility(index1: Int, index2: Int): Int {
        val compatibilityTable = mapOf(
            1 to mapOf(1 to 100, 2 to 75, 3 to 85, 4 to 65, 5 to 95, 6 to 60, 7 to 80, 8 to 90, 9 to 90, 10 to 55, 11 to 70, 12 to 50), // Koç
            2 to mapOf(1 to 75, 2 to 100, 3 to 70, 4 to 85, 5 to 60, 6 to 95, 7 to 55, 8 to 90, 9 to 55, 10 to 85, 11 to 65, 12 to 90), // Boğa
            3 to mapOf(1 to 85, 2 to 70, 3 to 100, 4 to 65, 5 to 80, 6 to 60, 7 to 95, 8 to 55, 9 to 90, 10 to 75, 11 to 95, 12 to 65), // İkizler
            4 to mapOf(1 to 65, 2 to 85, 3 to 65, 4 to 100, 5 to 55, 6 to 85, 7 to 60, 8 to 95, 9 to 70, 10 to 90, 11 to 60, 12 to 80), // Yengeç
            5 to mapOf(1 to 95, 2 to 60, 3 to 80, 4 to 55, 5 to 100, 6 to 65, 7 to 90, 8 to 85, 9 to 95, 10 to 75, 11 to 55, 12 to 70), // Aslan
            6 to mapOf(1 to 60, 2 to 95, 3 to 60, 4 to 85, 5 to 65, 6 to 100, 7 to 75, 8 to 90, 9 to 70, 10 to 85, 11 to 70, 12 to 95), // Başak
            7 to mapOf(1 to 80, 2 to 55, 3 to 95, 4 to 60, 5 to 90, 6 to 75, 7 to 100, 8 to 65, 9 to 85, 10 to 55, 11 to 85, 12 to 60), // Terazi
            8 to mapOf(1 to 90, 2 to 90, 3 to 55, 4 to 95, 5 to 85, 6 to 90, 7 to 65, 8 to 100, 9 to 70, 10 to 95, 11 to 65, 12 to 75), // Akrep
            9 to mapOf(1 to 90, 2 to 55, 3 to 90, 4 to 70, 5 to 95, 6 to 70, 7 to 85, 8 to 70, 9 to 100, 10 to 80, 11 to 60, 12 to 65), // Yay
            10 to mapOf(1 to 55, 2 to 85, 3 to 75, 4 to 90, 5 to 75, 6 to 85, 7 to 55, 8 to 95, 9 to 80, 10 to 100, 11 to 65, 12 to 90), // Oğlak
            11 to mapOf(1 to 70, 2 to 65, 3 to 95, 4 to 60, 5 to 55, 6 to 70, 7 to 85, 8 to 65, 9 to 60, 10 to 65, 11 to 100, 12 to 75), // Kova
            12 to mapOf(1 to 50, 2 to 90, 3 to 65, 4 to 80, 5 to 70, 6 to 95, 7 to 60, 8 to 75, 9 to 65, 10 to 90, 11 to 75, 12 to 100)  // Balık
        )

        return compatibilityTable[index1]?.get(index2) ?: 0
    }


}