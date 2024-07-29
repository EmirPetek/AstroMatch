package com.emirpetek.mybirthdayreminder.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.QuestionAnswers
import com.emirpetek.mybirthdayreminder.viewmodel.profile.QuestionAnswersViewModel
import com.google.android.material.snackbar.Snackbar
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
        val convertedTime = unixtsToDate(item.timestamp.toString())
        holder.textViewCardAnswerQuestionAnswerTime.setText(convertedTime)

    }




    private fun unixtsToDate(timestamp:String):String{
        // post zamanını gösterme kodu
        val unixTimestamp = timestamp
        val formattedDateTime = getLocalizedDateTime(unixTimestamp)
        var postTime = formattedDateTime.substring(11,16)
        var yyyy = formattedDateTime.substring(0,4)
        var mm = formattedDateTime.substring(5,7)
        var dd = formattedDateTime.substring(8,10)
        var postDate = "$dd/$mm/$yyyy"

        val nowTimeStamp = System.currentTimeMillis().toString()

        val timeDifference = (nowTimeStamp.substring(0, nowTimeStamp.length - 3).toLong() - unixTimestamp.substring(0, unixTimestamp.length - 3).toLong())
        // timedifference saniye cinsinden gelir

        val min = timeDifference/60 // üstünden kaç dakika geçmiş onu gösterir
        val hour = timeDifference/3600 // üstünden kaç saat geçmiş onu gösterir
        //Log.e("times: ", "min: $min hour: $hour")
        var text: String = String()
        if (min >= 0 && min < 60) {
            text = "$min ${requireContext.getString(R.string.minutes_ago)}"

        } else if (hour >= 1 && hour < 24) {
            text = "$hour ${requireContext.getString(R.string.hours_ago)}"
        } else {
            text =  postTime + " - " + postDate
        }

        return text
    }

    private fun getLocalizedDateTime(unixTime: String): String {
        // Unix zamanını milisaniye cinsine çevir
        val date = Date(unixTime.toLong() * 1)

        // Cihazın mevcut dil ve bölge ayarlarını al
        val locale = Locale.getDefault()

        // Tarih ve saat formatını belirle
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)

        // Cihazın zaman dilimini al
        val timeZone = TimeZone.getDefault()
        dateFormat.timeZone = timeZone

        // Tarih ve saati formatla ve döndür
        return dateFormat.format(date)
    }


}
