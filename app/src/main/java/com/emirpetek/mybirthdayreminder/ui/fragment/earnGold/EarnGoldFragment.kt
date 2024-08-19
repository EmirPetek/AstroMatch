package com.emirpetek.mybirthdayreminder.ui.fragment.earnGold

import android.R
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.databinding.FragmentEarnGoldBinding
import com.emirpetek.mybirthdayreminder.viewmodel.earnGold.EarnGoldViewModel

class EarnGoldFragment : Fragment() {

    private val viewModel: EarnGoldViewModel by viewModels()
    private lateinit var binding: FragmentEarnGoldBinding
    private var timeLeft : Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEarnGoldBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        binding.cardEarnGoldAdFirst.setOnClickListener { viewModel.incrementUserCredit(1) }
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

}