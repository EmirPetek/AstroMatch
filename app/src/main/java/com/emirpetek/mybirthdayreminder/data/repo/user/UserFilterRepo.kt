package com.emirpetek.mybirthdayreminder.data.repo.user

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.user.userFilter.UserFilter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserFilterRepo {

    private val filterRef = Firebase.firestore.collection("userFilters").document("filters").collection("filters").document(Firebase.auth.currentUser!!.uid)

    var userFilter : MutableLiveData<UserFilter?> = MutableLiveData()

    fun getFilterItems(): MutableLiveData<UserFilter?> {
        return userFilter
    }

    fun setUserFilterItems(filter:UserFilter){
        filterRef.set(filter)
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

    fun getFilteredUser(filter: UserFilter){

    }

}