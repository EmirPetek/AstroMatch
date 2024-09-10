package com.emirpetek.mybirthdayreminder.data.entity.user.userFilter

data class UserFilter(
    val age: UserAgeFilter? = null,
    val gender: UserGenderFilter? = null,
    var horoscope: UserHoroscopeFilter? = null) {
    constructor() : this(
        UserAgeFilter(),
        UserGenderFilter(),
        UserHoroscopeFilter()
    )
}
