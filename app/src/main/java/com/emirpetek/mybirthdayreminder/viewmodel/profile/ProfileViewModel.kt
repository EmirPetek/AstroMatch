package com.emirpetek.mybirthdayreminder.viewmodel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirpetek.mybirthdayreminder.data.entity.question.Post
import com.emirpetek.mybirthdayreminder.data.entity.user.ProfileVisit
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo
import com.emirpetek.mybirthdayreminder.data.repo.social.QuestionRepo
import com.emirpetek.mybirthdayreminder.data.repo.user.ProfileVisitRepo
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {


    private val repo = UserRepo()
    private val profileViewRepo = ProfileVisitRepo()


    var user = MutableLiveData<User>()// = MutableLiveData()
    var userAsync = MutableLiveData<User>()// = MutableLiveData()
    var userFullname = MutableLiveData<String>()// = MutableLiveData()
    var userImgURL = MutableLiveData<String>()// = MutableLiveData()
    var userZodiac = MutableLiveData<Int>()// = MutableLiveData()

    //  var answerSize: MutableLiveData<Pair<String,Int>>// = MutableLiveData()
    var questionList = MutableLiveData<List<Post>>()
    private val _answerSizeList = MutableLiveData<List<Int>>()
    var answerSizeList: LiveData<List<Int>> get() = _answerSizeList
    private val postRepo = QuestionRepo()


    var visitorQuerySize = MutableLiveData<Int>()

    init {
        user = repo.getUser()
        userAsync = repo.getUserAsync()
        userFullname = repo.getUserFullname()
        userImgURL = repo.getUserImage()
        answerSizeList = _answerSizeList
        questionList = postRepo.questionList
        visitorQuerySize = profileViewRepo.querySize
    }

    fun getUser(userID: String) {
        repo.getUserData(userID)
    }

    fun getUserAsync(userID: String) {
        repo.getUserDataAsync(userID)
    }

    fun getUserFromUserID(userID: String) {
        repo.getUserFullnameFromUserID(userID)
    }

    fun getQuestions(userID: String) {
        postRepo.getUserQuestionList(userID)
    }

    fun getAllQuestionAnswerNumbers(postList: List<Post>) {
        viewModelScope.launch {
            val answerSizes = postList.map { post ->
                postRepo.getQuestionAnswerNumber(post.postID)
            }
            _answerSizeList.value = answerSizes
        }
    }

    fun getUserZodiac(userID: String){
        repo.getUserZodiac(userID)
    }

    fun insertProfileView(view:ProfileVisit){
        profileViewRepo.insertProfileView(view)
    }

    fun getProfileViewerSize(){
        profileViewRepo.getProfileViewerSize()
    }
}