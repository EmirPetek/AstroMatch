package com.emirpetek.mybirthdayreminder.ui.adapter.horoscopeCompatibility

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.horoscopeCompatibility.CompatibilityAnalysis
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateShareTime
import com.emirpetek.mybirthdayreminder.viewmodel.horoscopeCompatibility.HoroscopeCompatibilityViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class HoroscopeCompatibilityAdapter(
    val mContext: Context,
    val list: List<CompatibilityAnalysis>,
    val progressBarHoroscopeCompatibility: ProgressBar,
    val viewModel: HoroscopeCompatibilityViewModel,
    val requireActivity: FragmentActivity
): RecyclerView.Adapter<HoroscopeCompatibilityAdapter.CardHolder>() {

    var timeLeft : Long = 0
    private var mInterstitialAd: InterstitialAd? = null


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
            holder.layoutNames.setOnClickListener { it ->
                val bundle : Bundle = Bundle().apply { putString("analysisDetail",item.compatibilityDescription) }
                Navigation.findNavController(it).navigate(R.id.action_horoscopeCompatibilityFragment_to_horoscopeCompatibilityDetailFragment,bundle)
            }
        }else{
            holder.imageViewAd.visibility = View.VISIBLE
            holder.imageViewAd.setOnClickListener { it ->
                progressBarHoroscopeCompatibility.visibility = View.VISIBLE
                loadAds(holder.imageViewAd,item)
            }
            startCountdownTimer(timeLeft,
                onTick = { timeFormatted ->
                    holder.textViewTime.text = "$timeFormatted"
                    // Her saniye kalan zamanı günceller
                },
                onFinish = {
                    //holder.textViewTime.text = "00:00" // Geri sayım bittiğinde çalışacak kod
                    holder.textViewTime.text = CalculateShareTime(mContext).unixtsToDate(item.timestamp.toString())
                }
            )
        }


        if (position+1 == list.size){
            progressBarHoroscopeCompatibility.visibility = View.GONE
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

    private fun loadAds(imageView: ImageView,analysis: CompatibilityAnalysis){

        progressBarHoroscopeCompatibility.visibility = View.VISIBLE
        imageView.isClickable = false

        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(mContext,mContext.getString(R.string.ad_interstitial_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
                progressBarHoroscopeCompatibility.visibility = View.GONE
                imageView.isClickable = true
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                showAd(imageView,analysis)
            }
        })


    }

    fun loadEarnAd(imageView: ImageView,analysis: CompatibilityAnalysis){
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                mInterstitialAd = null
                progressBarHoroscopeCompatibility.visibility = View.GONE
                imageView.isClickable = true
                viewModel.decrementCompatibilityTime(analysis)
            }
        }
    }
    fun showAd(imageView: ImageView,analysis: CompatibilityAnalysis){
        if (mInterstitialAd != null) {
            loadEarnAd(imageView,analysis)
            mInterstitialAd?.show(requireActivity)

        } else {
            Log.e("earngoldfragment", "reklam gösterilirken hata oluştu")
        }
    }
}