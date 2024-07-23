package com.emirpetek.mybirthdayreminder.ui.adapter.social.main

import android.content.Context
import android.graphics.Path.Op
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.viewmodel.social.MakeSurveyViewModel

class SocialPostSurveyOptionsAdapter(
    val mContext: Context,
    val optionList: ArrayList<String>,
    val viewModelSurvey: MakeSurveyViewModel
): RecyclerView.Adapter<SocialPostSurveyOptionsAdapter.OptionViewHolder>() {

    inner class OptionViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val radioButtonOption : RadioButton = view.findViewById(R.id.radioButtonSocialSurveyOption)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SocialPostSurveyOptionsAdapter.OptionViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_social_survey_options,parent,false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SocialPostSurveyOptionsAdapter.OptionViewHolder,
        position: Int
    ) {
        val option = optionList[position]
        holder.radioButtonOption.text = option
    }

    override fun getItemCount(): Int {
        return optionList.size
    }
}