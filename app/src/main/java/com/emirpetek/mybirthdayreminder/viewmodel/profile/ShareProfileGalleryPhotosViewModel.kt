package com.emirpetek.mybirthdayreminder.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirpetek.mybirthdayreminder.data.entity.user.UserGalleryPhoto
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShareProfileGalleryPhotosViewModel : ViewModel() {

    private val repository = UserRepo()
    private val _photosDataAdded = MutableStateFlow(false)
    val photosDataAdded: StateFlow<Boolean> get() = _photosDataAdded

    fun insertProfileGalleryURLs(userID:String, imgList: ArrayList<UserGalleryPhoto>){
        viewModelScope.launch {
            val result = repository.insertProfileGalleryURLs(userID,imgList)
            _photosDataAdded.value = result
        }

    }

}