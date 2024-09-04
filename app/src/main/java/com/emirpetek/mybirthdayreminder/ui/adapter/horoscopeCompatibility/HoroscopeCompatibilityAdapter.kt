package com.emirpetek.mybirthdayreminder.ui.adapter.horoscopeCompatibility

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.horoscopeCompatibility.CompatibilityAnalysis
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateShareTime

class HoroscopeCompatibilityAdapter(
    val mContext: Context,
    val list: List<CompatibilityAnalysis>
): RecyclerView.Adapter<HoroscopeCompatibilityAdapter.CardHolder>() {

    var timeLeft : Long = 0

    inner class CardHolder(view: View) : RecyclerView.ViewHolder(view){
        val layoutNames : LinearLayout = view.findViewById(R.id.layoutCardHoroscopeCompatibilityNames)
        val textViewNames : TextView = view.findViewById(R.id.textViewCardHoroscopeCompatibilityNames)
        val textViewTime : TextView = view.findViewById(R.id.textViewCardHoroscopeCompatibilityTime)
        val imageViewAd: ImageView = view.findViewById(R.id.imageViewCardHoroscopeCompatibilityAdview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_horoscope_compatibility,parent,false)
        return CardHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val item = list[position]

        holder.textViewNames.text = "${item.firstUsername} & ${item.secondUsername}"


        timeLeft = 600000 - (System.currentTimeMillis() - item.timestamp)

        if (timeLeft < 0){
            holder.textViewTime.text = CalculateShareTime(mContext).unixtsToDate(item.timestamp.toString())

        }else{
            holder.imageViewAd.visibility = View.VISIBLE
            startCountdownTimer(timeLeft,
                onTick = { timeFormatted ->
                    holder.textViewTime.text = "$timeFormatted"
                    // Her saniye kalan zamanı günceller
                },
                onFinish = {
                    holder.textViewTime.text = "00:00" // Geri sayım bittiğinde çalışacak kod
                }
            )
        }

    }

    fun startCountdownTimer(milliseconds: Long, onTick: (String) -> Unit, onFinish: () -> Unit) {
        val countDownTimer = object : CountDownTimer(milliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                val seconds = (millisUntilFinished / 1000) % 60
                val timeFormatted = String.format("%02d:%02d", minutes, seconds)
                onTick(timeFormatted) // Her saniye güncelleme
            }

            override fun onFinish() {
                //onFinish() // Geri sayım bittiğinde yapılacak işlem
            }
        }
        countDownTimer.start()
    }
}