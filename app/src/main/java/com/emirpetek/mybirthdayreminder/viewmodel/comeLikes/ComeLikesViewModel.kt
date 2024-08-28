package com.emirpetek.mybirthdayreminder.viewmodel.comeLikes

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirpetek.mybirthdayreminder.data.entity.like.Like
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.repo.like.LikeRepo
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo
import com.emirpetek.mybirthdayreminder.ui.fragment.comeLikes.ComeLikesFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ComeLikesViewModel : ViewModel() {
    private val likeRepo = LikeRepo()
    private val userRepo = UserRepo()

    var likeList = MutableLiveData<List<Like>>()
    var userData = MutableLiveData<User>()

    init {
        likeList = likeRepo.getLikeList()

       // userData = userRepo.getUserAsync()
    }


    fun getOwnUserData(){
        userRepo.getUserData(Firebase.auth.currentUser!!.uid)
        userData = userRepo.getUser()
    }


    fun getLikes(userID:String){
        viewModelScope.launch {
            likeRepo.getLikes(userID)
        }

    }

    fun deleteLike(likeID:String){
        likeRepo.deleteLike(likeID)
    }
}