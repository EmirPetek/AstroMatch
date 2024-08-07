package com.emirpetek.mybirthdayreminder.ui.adapter.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.question.Post
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateShareTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ProfileFragmentPostAdapter(
    val mContext: Context,
    val postList: ArrayList<Post>,
    val answerSizeList: ArrayList<Int>
): RecyclerView.Adapter<ProfileFragmentPostAdapter.PostCardViewHolder>() {


    inner class PostCardViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val textViewCardProfilePostContent : TextView = view.findViewById(R.id.textViewCardProfilePostContent)
        val textViewCardProfileOwnPostShareTime : TextView = view.findViewById(R.id.textViewCardProfileOwnPostShareTime)
        val textViewCardProfileOwnPostAnswerNumber : TextView = view.findViewById(R.id.textViewCardProfileOwnPostAnswerNumber)
        val imageViewCardProfileQuestionSeeAnswers : ImageView = view.findViewById(R.id.imageViewCardProfileQuestionSeeAnswers)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCardViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_profile_own_post,parent,false)
        return PostCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostCardViewHolder, position: Int) {
        val post = postList[position]
        val answerSize = answerSizeList[position]


        holder.textViewCardProfilePostContent.setText(post.questionText)
        val convertedTime = CalculateShareTime(mContext).unixtsToDate(post.timestamp.toString())
        holder.textViewCardProfileOwnPostShareTime.setText(convertedTime)

        // kaç tane cevap geldiğini gösterir
        var answerNum = ""
        if (answerSize == 0 || answerSize == 1) answerNum = "$answerSize ${mContext.getString(R.string.answer)}"
        if (answerSize > 1) answerNum = "$answerSize ${mContext.getString(R.string.answers)}"
        holder.textViewCardProfileOwnPostAnswerNumber.setText(answerNum)

        /*

        answernum 0 ise butona tıklatma toast ile answer yok response döndür
         */

        val bundle : Bundle = Bundle().apply {
            putParcelable("post",post)
        }
//        bundle.putSerializable("postID",post.postID)
//        bundle.putSerializable("postOwnerName",post.userFullname)
//        bundle.putSerializable("postShareTime",convertedTime)
//        bundle.putSerializable("postID",post.postID)

        holder.imageViewCardProfileQuestionSeeAnswers.setOnClickListener { it ->
            Navigation.findNavController(it).navigate(R.id.action_profileFragment_to_questionAnswersFragment,bundle)
        }

       /* if (answerSize == 0){
            holder.imageViewCardProfileQuestionSeeAnswers.setOnClickListener { showToastMessage(mContext.getString(R.string.no_answer)) }
        }else{
            holder.imageViewCardProfileQuestionSeeAnswers.setOnClickListener { (showToastMessage((answerNum))) }
            // answers sayfasına git ve orada cevapları göster, bundle ile postID gönder
        }*/


    }

    private fun showToastMessage(msg: String){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show()
    }




}