package com.emirpetek.mybirthdayreminder.data.repo.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.entity.user.userFilter.UserFilter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UserFilterRepo {

    private val filterRef = Firebase.firestore.collection("userFilters").document("filters").collection("filters").document(Firebase.auth.currentUser!!.uid)
    private var userRef = Firebase.firestore.collection("users")//.whereNotEqualTo(FieldPath.documentId(),Firebase.auth.currentUser!!.uid)

    var userFilter : MutableLiveData<UserFilter?> = MutableLiveData()
    var filteredUserList: MutableLiveData<List<User>?> = MutableLiveData()

    fun getFilterItems(): MutableLiveData<UserFilter?> {
        return userFilter
    }

    fun setUserFilterItems(filter:UserFilter){
        filterRef.set(filter)
    }

    fun getFilteredUsers() : MutableLiveData<List<User>?>{
        return filteredUserList
    }

    fun getUserFilterItems(){
        filterRef.get().addOnSuccessListener { it ->
            val model = it.toObject(UserFilter::class.java)
            model?.let {
                userFilter.value = it
            } ?: run {
                // Burada null durumunu handle edebilirsiniz
                userFilter.value = null // Eğer null da eklenmesini istiyorsanız
            }
        }
    }

    fun getFilteredUser(filter: UserFilter) {
        var queryRef =
            userRef.whereNotEqualTo(FieldPath.documentId(),Firebase.auth.currentUser!!.uid).whereEqualTo("accountDeleteState","0") // Başlangıç referansı olarak userRef

        // Sorguyu çalıştırma
        queryRef.addSnapshotListener { value, error ->
            val it = value?.toObjects(User::class.java)

            if (it != null) {
                if (filter.horoscope != null || filter.gender != null) {
                    var listFirst = it
                    var list1: List<User> = listFirst // İlk önce tüm kullanıcıları ata

                    // Eğer burç filtresi varsa
                    if (filter.horoscope?.horoscopeList?.isNotEmpty() == true) {
                        // Burç filtrelemesi uygula
                        list1 = list1.filter { user ->
                            filter.horoscope!!.horoscopeList.contains(user.zodiac)
                        }
                        //Log.e("horoscopeFilter: ", list1.toString())
                    }

                    // Eğer cinsiyet filtresi varsa
                    if (filter.gender?.userGender?.isNotEmpty() == true) {
                        // Cinsiyet filtrelemesi uygula
                        list1 = list1.filter { user ->
                            filter.gender.userGender.contains(user.gender)
                        }
                        //Log.e("genderFilter: ", list1.toString())
                    }

                    if (filter.age != null) {
                        val minDate = Calendar.getInstance().apply {
                            add(Calendar.YEAR, -filter.age.max.toInt()) // maxAge yıl önce
                            set(Calendar.MONTH, Calendar.JANUARY)
                            set(Calendar.DAY_OF_MONTH, 1)
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }.time

                        val maxDate = Calendar.getInstance().apply {
                            add(Calendar.YEAR, -filter.age.min.toInt()) // minAge yıl önce
                            set(Calendar.MONTH, Calendar.DECEMBER)
                            set(Calendar.DAY_OF_MONTH, 31)
                            set(Calendar.HOUR_OF_DAY, 23)
                            set(Calendar.MINUTE, 59)
                            set(Calendar.SECOND, 59)
                            set(Calendar.MILLISECOND, 999)
                        }.time

                        val minDateMillis = minDate.time
                        val maxDateMillis = maxDate.time

                        // Yaş aralığına göre filtreleme
                        list1 = list1.filter { user ->
                            user.birthdateTimestamp in minDateMillis..maxDateMillis
                        }
                        //Log.e("ageFilter: ", list1.toString())

                    }
                        // Filtrelenmiş listeyi döndür
                        filteredUserList.value = list1
                        //Log.e("list1: ", list1.toString())
                    } else {
                        filteredUserList.value = it
                    }

                }
                Log.e("err: ", error.toString())
            }
        }
}