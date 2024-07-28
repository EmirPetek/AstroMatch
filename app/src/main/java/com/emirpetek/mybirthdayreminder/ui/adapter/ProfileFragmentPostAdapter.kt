package com.emirpetek.mybirthdayreminder.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.Post
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
        val convertedTime = unixtsToDate(post.timestamp.toString())
        holder.textViewCardProfileOwnPostShareTime.setText(convertedTime)

        // kaç tane cevap geldiğini gösterir
        var answerNum = ""
        if (answerSize == 0 || answerSize == 1) answerNum = "$answerSize ${mContext.getString(R.string.answer)}"
        if (answerSize > 1) answerNum = "$answerSize ${mContext.getString(R.string.answers)}"
        holder.textViewCardProfileOwnPostAnswerNumber.setText(answerNum)

        /*

        answernum 0 ise butona tıklatma toast ile answer yok response döndür

         */

        if (answerSize == 0){
            holder.imageViewCardProfileQuestionSeeAnswers.setOnClickListener { showToastMessage(mContext.getString(R.string.no_answer)) }
        }else{
            holder.imageViewCardProfileQuestionSeeAnswers.setOnClickListener { (showToastMessage((answerNum))) }
            // answers sayfasına git ve orada cevapları göster, bundle ile postID gönder
        }


    }

    private fun showToastMessage(msg: String){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show()
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
            text = "$min ${mContext.getString(R.string.minutes_ago)}"

        } else if (hour >= 1 && hour < 24) {
            text = "$hour ${mContext.getString(R.string.hours_ago)}"
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