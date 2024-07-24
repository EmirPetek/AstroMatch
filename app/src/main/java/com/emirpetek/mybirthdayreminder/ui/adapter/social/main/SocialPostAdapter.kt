package com.emirpetek.mybirthdayreminder.ui.adapter.social.main

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.Post
import com.emirpetek.mybirthdayreminder.data.entity.QuestionAnswers
import com.emirpetek.mybirthdayreminder.data.entity.SelectedOptions
import com.emirpetek.mybirthdayreminder.viewmodel.social.AskQuestionViewModel
import com.emirpetek.mybirthdayreminder.viewmodel.social.MakeSurveyViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SocialPostAdapter(
    val mContext: Context,
    val postList: ArrayList<Post>,
    val viewModelQuestion: AskQuestionViewModel,
    val viewModelSurvey: MakeSurveyViewModel,
    val viewLifecycleOwner: LifecycleOwner,
    val lifecycleScope: CoroutineScope
): RecyclerView.Adapter<SocialPostAdapter.PostCardHolder>() {


    abstract class PostCardHolder(view: View) : RecyclerView.ViewHolder(view)

    class QuestionViewHolder(view: View) : PostCardHolder(view) {
        val recyclerViewPhoto: RecyclerView =
            view.findViewById(R.id.recyclerViewSocialQuestionPhoto)
        val textViewQuestionText: TextView = view.findViewById(R.id.textViewSocialQuestionText)
        val buttonReply: Button = view.findViewById(R.id.buttonSocialQuestionReply)
        val constraintLayoutSocialQuestionPhoto : ConstraintLayout =
            view.findViewById(R.id.constraintLayoutSocialQuestionPhoto)
    }

    class SurveyViewHolder(view: View) : PostCardHolder(view) {
        val recyclerViewSurveyPhoto: RecyclerView =
            view.findViewById(R.id.recyclerViewSocialSurveyPhoto)
        val textViewSurveyText: TextView = view.findViewById(R.id.textViewSocialSurveyText)
        val recyclerViewSurveyOptions: RecyclerView =
            view.findViewById(R.id.recyclerViewSocialSurveyOptions)
        val constraintLayoutSocialSurveyPhoto: ConstraintLayout =
            view.findViewById(R.id.constraintLayoutSocialSurveyPhoto)
        val constraintLayoutCardSocialSurvey: ConstraintLayout =
            view.findViewById(R.id.constraintLayoutCardSocialSurvey)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCardHolder {
        val inflater = LayoutInflater.from(mContext)
        return when (viewType) {
            VIEW_TYPE_QUESTION -> {
                val view = inflater.inflate(R.layout.card_social_question, parent, false)
                QuestionViewHolder(view)
            }

            VIEW_TYPE_SURVEY -> {
                val view = inflater.inflate(R.layout.card_social_survey, parent, false)
                SurveyViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (postList[position].postType) {
            "question" -> VIEW_TYPE_QUESTION
            "survey" -> VIEW_TYPE_SURVEY
            else -> throw IllegalArgumentException("Invalid type of data at position $position")
        }
    }

    companion object {
        private const val VIEW_TYPE_QUESTION = 0
        private const val VIEW_TYPE_SURVEY = 1
    }

    override fun onBindViewHolder(holder: PostCardHolder, position: Int) {
        val post = postList[position]
        when (holder) {
            is QuestionViewHolder -> {
                holder.textViewQuestionText.text = post.questionText
                if (post.imageURL[0].equals("null")){ holder.constraintLayoutSocialQuestionPhoto.visibility = View.GONE }
                else{
                    var imgList : ArrayList<String> = arrayListOf()
                    imgList = (post.imageURL)

                    holder.recyclerViewPhoto.setHasFixedSize(true)
                    holder.recyclerViewPhoto.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)
                    val imgAdapter = SocialPostImageAdapter(mContext,imgList,"posts/askQuestionPhoto")
                    holder.recyclerViewPhoto.adapter = imgAdapter
                }
                holder.buttonReply.setOnClickListener { showReplyAlertDialog(post) }



            }
            is SurveyViewHolder -> {

                holder.textViewSurveyText.text = post.questionText


                if (post.imageURL[0] == "null"){ holder.constraintLayoutSocialSurveyPhoto.visibility = View.GONE }
                else{
                    holder.constraintLayoutSocialSurveyPhoto.visibility = View.VISIBLE
                    val imgList : ArrayList<String> = post.imageURL
                    holder.recyclerViewSurveyPhoto.setHasFixedSize(true)
                    holder.recyclerViewSurveyPhoto.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)
                    val imgAdapter = SocialPostImageAdapter(mContext,imgList,"posts/makeSurveyPhoto")
                    holder.recyclerViewSurveyPhoto.adapter = imgAdapter
                }


                holder.recyclerViewSurveyOptions.setHasFixedSize(true)
                holder.recyclerViewSurveyOptions.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false)
                val optionList : ArrayList<String> = post.options!!
                //val userSelectedOptionList : ArrayList<SelectedOptions> ?= post.selectedOptions
                var userSelectedOptionList : ArrayList<SelectedOptions>
                val ownUserID =  Firebase.auth.currentUser!!.uid

                var optionPosition : Int
                viewModelSurvey.getSelectedSurveyUsers(post.postID)
                viewModelSurvey.surveyOptionList.observe(viewLifecycleOwner, Observer { it ->
                    userSelectedOptionList = it as ArrayList<SelectedOptions>
                    userSelectedOptionList.reverse()
                    for (i in userSelectedOptionList){
                        if (i.userID == ownUserID && i.postID == post.postID) {
                            optionPosition = i.selectedOption
                            val optionAdapter = SocialPostSurveyOptionsAdapter(mContext,optionList,post.postID, viewModelSurvey,viewLifecycleOwner,userSelectedOptionList,optionPosition)
                            holder.recyclerViewSurveyOptions.adapter = optionAdapter

                        }else{
                            optionPosition = -1
                            val optionAdapter = SocialPostSurveyOptionsAdapter(mContext,optionList,post.postID, viewModelSurvey,viewLifecycleOwner,userSelectedOptionList,optionPosition)
                            holder.recyclerViewSurveyOptions.adapter = optionAdapter
                        }
                    }

                })



                /*


                    radiobutton 1 kere tıklandı mı diğerlerinin clickablelerini false yap
                    card yapısına isim, eklenme tarihi koy
                    lazyloading
                    5 taneden sonra daha fazla yükle ile ++5 tane daha göster

                 */

            }
        }
    }

    private fun showReplyAlertDialog(post: Post) {
        Log.e("alert içi", "alert var")
        // Özel layout'u şişir
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.alert_social_reply_question, null)

        // Layout içindeki bileşenleri bulun
        val recyclerViewAlertSocialReplyQuestion = view.findViewById<RecyclerView>(R.id.recyclerViewAlertSocialReplyQuestion)
        val textViewAlertSocialQuestionText = view.findViewById<TextView>(R.id.textViewAlertSocialQuestionText)
        val buttonAlertSocialReplyQuestionReply = view.findViewById<Button>(R.id.buttonAlertSocialReplyQuestionReply)
        val buttonAlertSocialReplyQuestionCancel = view.findViewById<Button>(R.id.buttonAlertSocialReplyQuestionCancel)
        val editTextAlertReplyQuestionMessage = view.findViewById<EditText>(R.id.editTextAlertReplyQuestionMessage)
        val constraintLayoutAlertSocialQuestionPhoto = view.findViewById<ConstraintLayout>(R.id.constraintLayoutAlertSocialQuestionPhoto)

        // AlertDialog.Builder oluştur
        val builder = AlertDialog.Builder(mContext)
            .setView(view)
            .setCancelable(true)

        // Dialog'u oluştur ve göster
        val dialog = builder.create()

        buttonAlertSocialReplyQuestionCancel.setOnClickListener { dialog.dismiss() }
        textViewAlertSocialQuestionText.text = post.questionText

        if (post.imageURL[0] == "null"){ constraintLayoutAlertSocialQuestionPhoto.visibility = View.GONE }
        else{
            constraintLayoutAlertSocialQuestionPhoto.visibility = View.VISIBLE
            val imgList : ArrayList<String> = post.imageURL
            recyclerViewAlertSocialReplyQuestion.setHasFixedSize(true)
            recyclerViewAlertSocialReplyQuestion.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)
            val imgAdapter = SocialPostImageAdapter(mContext,imgList,"posts/askQuestionPhoto")
            recyclerViewAlertSocialReplyQuestion.adapter = imgAdapter
        }



        buttonAlertSocialReplyQuestionReply.setOnClickListener {
            val replyMessage = editTextAlertReplyQuestionMessage.text.toString()
            if (replyMessage.isEmpty()){
                showToastMessage(mContext.getString(R.string.fill_all_place))
            }else{
                val q = QuestionAnswers(
                    Firebase.auth.currentUser!!.uid,
                    post.postID,
                    replyMessage,
                    System.currentTimeMillis()
                )
                viewModelQuestion.insertQuestionAnswer(q)
                lifecycleScope.launch {
                    viewModelQuestion.questionAnswerAdded.collect{ isAdded ->
                        if (isAdded){
                            showToastMessage(mContext.getString(R.string.your_message_sended))
                            dialog.dismiss()
                        }
                    }
                }


            }
        }

        dialog.show()
        Log.e("alert içi", "alert var en sonda")

    }

    private fun showToastMessage(msg:String){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show()
    }
}