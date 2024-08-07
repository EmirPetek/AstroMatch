package com.emirpetek.mybirthdayreminder.ui.adapter.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.question.QuestionAnswers
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateShareTime
import com.emirpetek.mybirthdayreminder.viewmodel.profile.QuestionAnswersViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class QuestionAnswersAnswerAdapter(
    val requireContext: Context,
    val answerList: ArrayList<QuestionAnswers>,
    val viewModel: QuestionAnswersViewModel
)
    : RecyclerView.Adapter<QuestionAnswersAnswerAdapter.AnswerCardHolder>(){


        inner class AnswerCardHolder(view: View) : RecyclerView.ViewHolder(view){
            val textViewCardAnswerQuestionAnswerText: TextView = view.findViewById(R.id.textViewCardAnswerQuestionAnswerText)
            val textViewCardAnswerQuestionAnswerTime: TextView = view.findViewById(R.id.textViewCardAnswerQuestionAnswerTime)
            val textViewCardAnswerQuestionAnswerPerson: TextView = view.findViewById(R.id.textViewCardAnswerQuestionAnswerPerson)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerCardHolder {
        val view = LayoutInflater.from(requireContext).inflate(R.layout.card_answer_question,parent,false)
        return AnswerCardHolder(view)
    }

    override fun getItemCount(): Int { return answerList.size    }

    override fun onBindViewHolder(holder: AnswerCardHolder, position: Int) {
        val item = answerList[position]

        holder.textViewCardAnswerQuestionAnswerText.setText(item.answer)
        holder.textViewCardAnswerQuestionAnswerPerson.setText(item.userFullname)
        val convertedTime = CalculateShareTime(requireContext).unixtsToDate(item.timestamp.toString())
        holder.textViewCardAnswerQuestionAnswerTime.setText(convertedTime)

    }

}
