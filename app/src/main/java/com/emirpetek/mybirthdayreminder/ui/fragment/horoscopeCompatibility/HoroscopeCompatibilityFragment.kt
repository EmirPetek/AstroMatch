package com.emirpetek.mybirthdayreminder.ui.fragment.horoscopeCompatibility

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentHoroscopeCompatibilityBinding
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.horoscopeCompatibility.HoroscopeCompatibilityViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HoroscopeCompatibilityFragment : Fragment() {

    private val viewModel: HoroscopeCompatibilityViewModel by viewModels()
    private lateinit var binding: FragmentHoroscopeCompatibilityBinding
    private lateinit var mAdView : AdView
    private var alertDialog: AlertDialog? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoroscopeCompatibilityBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()
        binding.imageViewHoroscopeCompatibilityBackButton.setOnClickListener { findNavController().popBackStack() }
        bindAdMob()

        viewModel.getCredit()


        binding.layoutCalculateCompatibility.setOnClickListener {
            showAlertDialog()

           /* Log.e("time: ", System.currentTimeMillis().toString())
            viewModel.getCompatibility(input)
            viewModel.compatibilityResponse.observe(viewLifecycleOwner, Observer { response ->
                Log.e("ai cevabı: ", response)
                Log.e("time 2: ", System.currentTimeMillis().toString())

            })
            Log.e("time 3: ", System.currentTimeMillis().toString())*/

        }

        return binding.root
    }

    fun bindAdMob(){
        // birthdays sayfası reklam gösterme kısmı
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(requireContext()) {}
        }
        mAdView = binding.adViewHoroscopeCompatibilityFragment
        val adView = AdView(requireContext())
        adView.adUnitId = getString(R.string.ad_unit_id)
        val adSize = AdSize(LayoutParams.MATCH_PARENT,80)
        adView.setAdSize(adSize)
        this.mAdView = adView
        binding.adViewHoroscopeCompatibilityFragment.removeAllViews()
        binding.adViewHoroscopeCompatibilityFragment.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }



    fun showAlertDialog(){

        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.alert_horoscope_compatibility, null)

        val builder = android.app.AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(true)

        val dialog = builder.create()

        viewModel.credit.observe(viewLifecycleOwner, Observer { credit ->
            val buttonShortAnalysis = view.findViewById<CardView>(R.id.cardViewShortAnalysis)
            val buttonDetailedAnalysis = view.findViewById<CardView>(R.id.cardViewDetailedAnalysis)
            val editTextYourName = view.findViewById<EditText>(R.id.editTextHoroscopeCompatibilityAlertYourName)
            val editTextYourBirthdate = view.findViewById<EditText>(R.id.editTextHoroscopeCompatibilityAlertYourBirthdate)
            val editTextAnotherName = view.findViewById<EditText>(R.id.editTextHoroscopeCompatibilityAlertAnotherPersonName)
            val editTextAnotherBirthdate = view.findViewById<EditText>(R.id.editTextHoroscopeCompatibilityAlertAnotherPersonBirthdate)
            val creditText = view.findViewById<TextView>(R.id.textViewHoroscopeCompatibilityAlertCredit)

            creditText.text = credit.amount.toString()

            buttonShortAnalysis.setOnClickListener {
                if (
                    editTextYourName.text.toString().isEmpty() ||
                    editTextAnotherBirthdate.text.toString().isEmpty() ||
                    editTextAnotherName.text.toString().isEmpty() ||
                    editTextAnotherBirthdate.text.toString().isEmpty()
                ){
                    Toast.makeText(requireContext(),getString(R.string.fill_all_place),Toast.LENGTH_SHORT).show()
                }else{
                    if (credit.amount < 5){
                        Toast.makeText(requireContext(),getString(R.string.not_enough_gold),Toast.LENGTH_SHORT).show()
                    }else{
                        val input = getAiPrompt(editTextYourName.text.toString(),editTextYourBirthdate.text.toString(),editTextAnotherName.text.toString(),editTextAnotherBirthdate.text.toString())
                        showLoadingAlert()
                        dialog.dismiss()
                        getAiResponse(input,600,5)
                    }
                }
            }

            buttonDetailedAnalysis.setOnClickListener {
                if (
                    editTextYourName.text.toString().isEmpty() ||
                    editTextAnotherBirthdate.text.toString().isEmpty() ||
                    editTextAnotherName.text.toString().isEmpty() ||
                    editTextAnotherBirthdate.text.toString().isEmpty()
                ){
                    Toast.makeText(requireContext(),getString(R.string.fill_all_place),Toast.LENGTH_SHORT).show()
                }else{
                    if (credit.amount < 10){
                        Toast.makeText(requireContext(),getString(R.string.not_enough_gold),Toast.LENGTH_SHORT).show()
                    }else{
                        val input = getAiPrompt(editTextYourName.text.toString(),editTextYourBirthdate.text.toString(),editTextAnotherName.text.toString(),editTextAnotherBirthdate.text.toString())
                        showLoadingAlert()
                        dialog.dismiss()
                        getAiResponse(input,1000,10)
                    }
                }
            }




            editTextYourBirthdate.setOnClickListener { showDatePickerDialog(editTextYourBirthdate) }
            editTextAnotherBirthdate.setOnClickListener { showDatePickerDialog(editTextAnotherBirthdate) }

            dialog.show()
        })


    }


    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.set(1900, 0, 1)
        val minDate = calendar.timeInMillis

        val maxDate = System.currentTimeMillis()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = formatDate(year, monthOfYear, dayOfMonth)
                editText.setText(selectedDate)
            },
            currentYear, currentMonth, currentDay
        )

        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.datePicker.maxDate = maxDate

        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun getAiPrompt(name1: String, date1: String,name2: String, date2:String): String{

        val deviceLanguage = Locale.getDefault().displayLanguage // cihazın dili

        val input = "You are a helpful assistant, Role: System. Role: User. Make a detailed analysis of the horoscope compatibility of " +
                "$name1, born on $date1, and " +
                "$name2, born on $date2. " +
                "Analyze people according to their birth dates and personality according to birth dates. " +
                "Let your comments be in your own language. " +
                "The analysis should be at least 5-6 paragraphs. Give me analysis as $deviceLanguage language"


        return input
    }

    private fun showLoadingAlert() {
        if (alertDialog == null) {
            val dialogView = layoutInflater.inflate(R.layout.alert_wait_screen, null)
            dialogView.findViewById<TextView>(R.id.textViewAlertWaitScreenPleaseWait).text = getString(R.string.analyse_data_sending)
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setView(dialogView)
            alertDialog = alertDialogBuilder.create().apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
        }
        alertDialog?.show()
    }

    private fun closeLoadingAlert() {
        alertDialog?.let {
            it.dismiss()
            alertDialog = null
        }
    }

    fun getAiResponse(input:String, tokenNumber: Int,decrementCreditAmount: Long){

        viewModel.getCompatibility(input,tokenNumber)
        viewModel.compatibilityResponse.observe(viewLifecycleOwner, Observer { response ->
            Log.e("ai cevabı: ", response)
            viewModel.decrementUserCredit(decrementCreditAmount)
            closeLoadingAlert()
            findNavController().popBackStack()
        })
    }
}