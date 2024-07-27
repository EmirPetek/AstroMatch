package com.emirpetek.mybirthdayreminder.data.repo.social

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.Post
import com.emirpetek.mybirthdayreminder.data.entity.QuestionAnswers
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class QuestionRepo {

    private val dbRefQuestions = Firebase.firestore.collection("posts").document("question").collection("questions")
    private val dbRefQuestionAnswer = Firebase.firestore.collection("posts").document("questionAnswers").collection("answers")

    val questionList: MutableLiveData<List<Post>> = MutableLiveData()
    var answerSize: MutableLiveData<Pair<String, Int>> = MutableLiveData()

    suspend fun insertQuestion(q: Post): Boolean {
        return try {
            dbRefQuestions.add(q)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getQuestionList() {
        dbRefQuestions.addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Handle error
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val questionl = ArrayList<Post>()
                for (q in snapshot.documents) {
                    val questionModel = q.toObject(Post::class.java)!!
                    if (questionModel.deleteState == "0") {
                        questionModel.postID = q.id
                        questionl.add(questionModel)
                    }
                }
                questionList.value = questionl
            }
        }
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

    fun getUserQuestionList(userID: String) {
        dbRefQuestions.addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Handle error
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val questionl = ArrayList<Post>()
                for (q in snapshot.documents) {
                    val questionModel = q.toObject(Post::class.java)!!
                    if (questionModel.deleteState == "0" && questionModel.userID == userID) {
                        questionModel.postID = q.id
                        questionl.add(questionModel)
                    }
                }
                questionList.value = questionl
            }
        }
    }
}
