package com.emirpetek.mybirthdayreminder.data.repo.social

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.question.Post
import com.emirpetek.mybirthdayreminder.data.entity.question.QuestionAnswers
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class QuestionRepo {

    private val dbRefQuestions = Firebase.firestore.collection("posts").document("question").collection("questions")
    private val dbRefQuestionAnswer = Firebase.firestore.collection("posts").document("questionAnswers").collection("answers")
    private val dbRefUsers = Firebase.firestore.collection("users")



    val questionList: MutableLiveData<List<Post>> = MutableLiveData()
    var answerSize: MutableLiveData<Pair<String, Int>> = MutableLiveData()
    private var questionListener: ListenerRegistration? = null
    private var answerListener: ListenerRegistration? = null

    val answerList: MutableLiveData<List<QuestionAnswers>> = MutableLiveData()


    suspend fun insertQuestion(q: Post): Boolean {
        val docID = "${q.timestamp}_${q.userID}"
        return try {
            dbRefQuestions.document(docID).set(q)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun deletePost(postID: String){

        val updated = hashMapOf(
            "deleteState" to "1"
        )

        dbRefQuestions.document(postID).update(updated as Map<String, Any>)

    }

    fun getQuestionList() {
        questionListener?.remove()
        questionListener = dbRefQuestions.addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Handle error
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val questionl = ArrayList<Post>()
                val usersMap = mutableMapOf<String, User>()

                for (q in snapshot.documents) {
                    val questionModel = q.toObject(Post::class.java)!!
                    if (questionModel.deleteState == "0") {
                        questionModel.postID = q.id
                        questionl.add(questionModel)
                    }
                }

                // Kullanıcı bilgilerini güncelleme
                val job = CoroutineScope(Dispatchers.IO).launch {
                    questionl.forEach { post ->
                        if (!usersMap.containsKey(post.userID)) {
                            val userSnapshot = dbRefUsers.document(post.userID).get().await()
                            val user = userSnapshot.toObject(User::class.java)
                            if (user != null) {
                                usersMap[post.userID] = user
                            }
                        }
                    }

                    questionl.forEach { post ->
                        val user = usersMap[post.userID]
                        if (user != null) {
                            post.userFullname = user.fullname
                            post.userImg = user.profile_img
                        }
                    }

                    // Güncellenmiş veriyi ana iş parçacığında ayarlama
                    withContext(Dispatchers.Main) {
                        questionList.value = questionl
                    }
                }
            }
        }
    }

    fun removeListener() {
        questionListener?.remove()
    }

    fun insertQuestionAnswer(q: QuestionAnswers): Boolean {
        return try {
            dbRefQuestionAnswer.document(q.postID).collection("answers").add(q)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getQuestionAnswerNumber(postID: String): Int {
        return suspendCoroutine { continuation ->
            dbRefQuestionAnswer.document(postID).collection("answers").get()
                .addOnSuccessListener { snapshot ->
                    val answerSizeDB = snapshot.size()
                    continuation.resume(answerSizeDB)
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }

    fun getQuestionAnswers(postID: String) {
        answerListener?.remove()
        answerListener = dbRefQuestionAnswer.document(postID).collection("answers")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val answersList = ArrayList<QuestionAnswers>()
                    val usersMap = mutableMapOf<String, User>()

                    for (a in snapshot.documents) {
                        val answerModel = a.toObject(QuestionAnswers::class.java)!!
                        answersList.add(answerModel)
                    }

                    // Kullanıcı bilgilerini güncelleme
                    val job = CoroutineScope(Dispatchers.IO).launch {
                        answersList.forEach { answer ->
                            if (!usersMap.containsKey(answer.userID)) {
                                val userSnapshot = dbRefUsers.document(answer.userID).get().await()
                                val user = userSnapshot.toObject(User::class.java)
                                if (user != null) {
                                    usersMap[answer.userID] = user
                                }
                            }
                        }

                        answersList.forEach { answer ->
                            val user = usersMap[answer.userID]
                            if (user != null) {
                                answer.userFullname = user.fullname
                            }
                        }

                        // Güncellenmiş veriyi ana iş parçacığında ayarlama
                        withContext(Dispatchers.Main) {
                            answerList.value = answersList
                        }
                    }
                }
            }
    }



    fun getUserQuestionList(userID: String) {
        questionListener?.remove()
        questionListener = dbRefQuestions.addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Handle error
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val questionl = ArrayList<Post>()
                val usersMap = mutableMapOf<String, User>()

                for (q in snapshot.documents) {
                    val questionModel = q.toObject(Post::class.java)!!
                    if (questionModel.deleteState == "0" && questionModel.userID == userID) {
                        questionModel.postID = q.id
                        questionl.add(questionModel)
                    }
                }

                // Kullanıcı bilgilerini güncelleme
                val job = CoroutineScope(Dispatchers.IO).launch {
                    questionl.forEach { post ->
                        if (!usersMap.containsKey(post.userID)) {
                            val userSnapshot = dbRefUsers.document(post.userID).get().await()
                            val user = userSnapshot.toObject(User::class.java)
                            if (user != null) {
                                usersMap[post.userID] = user
                            }
                        }
                    }

                    questionl.forEach { post ->
                        val user = usersMap[post.userID]
                        if (user != null) {
                            post.userFullname = user.fullname
                            post.userImg = user.profile_img
                        }
                    }

                    // Güncellenmiş veriyi ana iş parçacığında ayarlama
                    withContext(Dispatchers.Main) {
                        questionList.value = questionl
                    }
                }
            }
        }
    }
}
