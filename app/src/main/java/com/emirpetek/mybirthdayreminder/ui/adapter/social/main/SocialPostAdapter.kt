package com.emirpetek.mybirthdayreminder.ui.adapter.social.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.Post
import com.emirpetek.mybirthdayreminder.viewmodel.social.AskQuestionViewModel
import com.emirpetek.mybirthdayreminder.viewmodel.social.MakeSurveyViewModel

class SocialPostAdapter(
    val mContext: Context,
    val postList: ArrayList<Post>,
    val viewModelQuestion: AskQuestionViewModel,
    val viewModelSurvey: MakeSurveyViewModel
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
                    val imgAdapter = SocialPostImageAdapter(
                        mContext,
                        imgList,
                        "posts/askQuestionPhoto"
                    )
                    holder.recyclerViewPhoto.adapter = imgAdapter
                }


            }
            is SurveyViewHolder -> {
                holder.textViewSurveyText.text = post.questionText
                if (post.imageURL[0].equals("null")){ holder.constraintLayoutSocialSurveyPhoto.visibility = View.GONE }
                else{
                    var imgList : ArrayList<String> = arrayListOf()
                    imgList = (post.imageURL)

                    holder.recyclerViewSurveyPhoto.setHasFixedSize(true)
                    holder.recyclerViewSurveyPhoto.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)
                    val imgAdapter = SocialPostImageAdapter(mContext,imgList,"posts/makeSurveyPhoto")
                    holder.recyclerViewSurveyOptions.adapter = imgAdapter
                }

                holder.recyclerViewSurveyOptions.setHasFixedSize(true)
                holder.recyclerViewSurveyOptions.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false)

                var optionList : ArrayList<String> = arrayListOf()
                optionList = post.options!!

                val optionAdapter = SocialPostSurveyOptionsAdapter(mContext,optionList,viewModelSurvey)
                holder.recyclerViewSurveyOptions.adapter = optionAdapter


                /*


                resimlerin urlsini alıp glide ile yükle.
                 */



            }
        }
    }
}