package com.emirpetek.mybirthdayreminder.ui.fragment.earnGold

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.apiKey.getAdmobApiKey
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.databinding.FragmentEarnGoldBinding
import com.emirpetek.mybirthdayreminder.viewmodel.earnGold.EarnGoldViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EarnGoldFragment : Fragment() {

    private val viewModel: EarnGoldViewModel by viewModels()
    private lateinit var binding: FragmentEarnGoldBinding
    private var timeLeft : Long = 0
    private var rewardedInterstitialAd : RewardedInterstitialAd? = null
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEarnGoldBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        //loadAds()


        binding.cardEarnGoldAdFirst.setOnClickListener {
            loadAds()
            binding.cardEarnGoldAdFirst.isClickable = false
        }
        binding.cardEarnGoldAdSecond.setOnClickListener { viewModel.incrementUserCredit(5) }

        binding.cardBuyGold1.setOnClickListener { viewModel.incrementUserCredit(50) }
        binding.cardBuyGold2.setOnClickListener { viewModel.incrementUserCredit(100) }
        binding.cardBuyGold3.setOnClickListener { viewModel.incrementUserCredit(200) }
        binding.cardBuyGold4.setOnClickListener { viewModel.incrementUserCredit(500) }
        binding.cardBuyGold5.setOnClickListener { viewModel.incrementUserCredit(1000) }
        binding.cardBuyGold6.setOnClickListener { viewModel.incrementUserCredit(5000) }

        viewModel.getCreditData()
        viewModel.credit.observe(viewLifecycleOwner, Observer { credit ->
            Log.e("veri crt: ", System.currentTimeMillis().toString())
            Log.e("veri lcbts: ", credit.lastCreditBalanceTimestamp.toString())
            timeLeft = 86400000 - (System.currentTimeMillis() - credit.lastCreditBalanceTimestamp)
            Log.e("veri lcbts: ", timeLeft.toString())

            startCountdownTimer(timeLeft,
                onTick = { timeFormatted ->
                    binding.textViewEarnGoldRemainTimeFreeGoldLike.text = "$timeFormatted"
                    // Her saniye kalan zamanı günceller
                },
                onFinish = {
                    println("Time's up!") // Geri sayım bittiğinde çalışacak kod
                }
            )


        })


        return binding.root
    }

    fun startCountdownTimer(milliseconds: Long, onTick: (String) -> Unit, onFinish: () -> Unit) {
        val countDownTimer = object : CountDownTimer(milliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = millisUntilFinished / (1000 * 60 * 60)
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                val seconds = (millisUntilFinished / 1000) % 60
                val timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                onTick(timeFormatted) // Her saniye güncelleme
            }

            override fun onFinish() {
                //onFinish() // Geri sayım bittiğinde yapılacak işlem
            }
        }
        countDownTimer.start()
    }

    private fun loadAds(){

        binding.progressBarEarnGoldWatchAd.visibility = View.VISIBLE
        binding.layoutGoldPrices1.visibility = View.GONE
        binding.layoutGoldPrices2.visibility = View.GONE

        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),getAdmobApiKey().getInterstitialAdmobApiKey(), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
                binding.progressBarEarnGoldWatchAd.visibility = View.GONE
                binding.layoutGoldPrices1.visibility = View.VISIBLE
                binding.layoutGoldPrices2.visibility = View.VISIBLE
                binding.cardEarnGoldAdFirst.isClickable = false
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                showAd()
            }
        })


    }

    fun loadEarnAd(){
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                mInterstitialAd = null
                viewModel.incrementUserCredit(1)
                binding.progressBarEarnGoldWatchAd.visibility = View.GONE
                binding.layoutGoldPrices1.visibility = View.VISIBLE
                binding.layoutGoldPrices2.visibility = View.VISIBLE
                binding.cardEarnGoldAdFirst.isClickable = true
            }
        }
    }
    fun showAd(){
        if (mInterstitialAd != null) {
            loadEarnAd()
            mInterstitialAd?.show(requireActivity())

        } else {
            Log.e("earngoldfragment", "reklam gösterilirken hata oluştu")
        }
    }

}