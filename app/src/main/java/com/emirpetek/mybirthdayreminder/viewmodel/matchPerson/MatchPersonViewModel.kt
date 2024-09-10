package com.emirpetek.mybirthdayreminder.viewmodel.matchPerson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.UserCredits
import com.emirpetek.mybirthdayreminder.data.entity.like.Like
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.entity.user.userFilter.UserFilter
import com.emirpetek.mybirthdayreminder.data.repo.CreditsRepo
import com.emirpetek.mybirthdayreminder.data.repo.like.LikeRepo
import com.emirpetek.mybirthdayreminder.data.repo.user.UserFilterRepo
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo

class MatchPersonViewModel : ViewModel() {

    private val userRepo = UserRepo()
    private val creditRepo = CreditsRepo()
    private val likeRepo = LikeRepo()
    private val filterRepo = UserFilterRepo()

    var user = MutableLiveData<ArrayList<User>>()
    var credit = MutableLiveData<UserCredits>()
    var userOwnZodiac = MutableLiveData<Int>()
    var likeList = MutableLiveData<List<Like>>()
    var userFilters : MutableLiveData<UserFilter?>

    init {
        user = userRepo.getCompatibleUsers()
        credit = creditRepo.getCreditAmount()
        userOwnZodiac = userRepo.getUserZodiac()
        likeList = likeRepo.likeLiveData
        userFilters = filterRepo.getFilterItems()
    }

    fun getCompatibleUsersData(userID:String){
        userRepo.getCompatibleUsersData(userID)
    }

    fun getUserCreditsAmount(){
        creditRepo.getUserCreditsAmountFun()
    }

    fun getOwnUserZodiac(userID:String){
        userRepo.getUserZodiac(userID)
    }

    fun decrementUserCredit(amount: Long){
        creditRepo.decrementUserCredit(amount)
    }

    fun decrementLikeRight(amount: Long){
        creditRepo.decrementLikeRight(amount)
    }

    fun decrementMegaLikeRight(amount: Long){
        creditRepo.decrementMegaLikeRight(amount)
    }

    fun setDailyBonusValues(){
        creditRepo.setDailyBonusValues()
    }

    fun insertLikeUser(like: Like){
        likeRepo.insertLikeUser(like)
    }

    fun getUserFilterItems(){
        filterRepo.getUserFilterItems()
    }

    fun setUserFilterItems(filter: UserFilter){
        filterRepo.setUserFilterItems(filter)
    }

    fun getFilteredUsers(filter: UserFilter){
        filterRepo.getFilteredUser(filter)
    }

}