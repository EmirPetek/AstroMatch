package com.emirpetek.mybirthdayreminder.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val repo = UserRepo()
    private val _userAdded = MutableStateFlow(false)
    val userAdded: StateFlow<Boolean> get() = _userAdded

    fun addUser(user: User){
        viewModelScope.launch {
            val result = repo.addUser(user)
            _userAdded.value = result
        }
    }




}