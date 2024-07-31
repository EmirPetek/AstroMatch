package com.emirpetek.mybirthdayreminder.ui.fragment.birthdays

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.birthdays.Birthdays
import com.emirpetek.mybirthdayreminder.databinding.FragmentBirthdayAddBinding
import com.emirpetek.mybirthdayreminder.viewmodel.birthdays.BirthdayAddViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class BirthdayAddFragment : Fragment() {
    private lateinit var binding: FragmentBirthdayAddBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var userDegrees = arrayListOf<String>()
    private val viewModel: BirthdayAddViewModel by viewModels()
    private lateinit var name_surname:String
    private lateinit var bdDate:String
    private lateinit var userkey:String
    private lateinit var gift_idea:String
    private var userDegree:String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var mAdView : AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBirthdayAddBinding.inflate(layoutInflater, container, false)

        auth = Firebase.auth


        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        setUserDegreeSpinner(binding.spinnerDBUserDegree)
        birthdaySelecter(binding.editTextBDAddDate)
        sharedPreferences = requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        userkey = auth.currentUser!!.uid//sharedPreferences.getString("userKey", "")!!

        hideBottomNav()
        bindBackButton()
        bindAddButton()
        bindAdMob()


        return binding.root

    }

    fun bindAdMob(){
        // birthdays sayfası reklam gösterme kısmı
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(requireContext()) {}
        }
        mAdView = binding.adViewFragmentAddBirthday
        val adView = AdView(requireContext())
        adView.adUnitId = getString(R.string.ad_unit_id)
        val adSize = AdSize(400,80)
        adView.setAdSize(adSize)
        this.mAdView = adView
        binding.adViewFragmentAddBirthday.removeAllViews()
        binding.adViewFragmentAddBirthday.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun hideBottomNav(){
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }


    private fun bindBackButton(){
        binding.imageViewAddBirthdayArrowBack.setOnClickListener {
            returnPreviousPage()
        }
    }

    private fun returnPreviousPage(){

        findNavController().popBackStack()
        /*
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainactivity_constraint_layout,BirthdaysFragment())
        transaction.addToBackStack(null)
        transaction.commit()*/
    }

    private fun bindAddButton(){
        binding.buttonAddBirthday.setOnClickListener {
            setUserDegreeSpinner(binding.spinnerDBUserDegree)
            birthdaySelecter(binding.editTextBDAddDate)

            name_surname = binding.editTextBDAddNameSurname.text.toString()
            bdDate = binding.editTextBDAddDate.text.toString()
            gift_idea = binding.editTextBDAddGiftIdea.text.toString()

            val checkStatus = checkEditTextField(bdDate,name_surname)
            if (!checkStatus) { // namesurname and birthday were selected
                val birthdate = Birthdays(
                    userkey,
                    "",
                    name_surname,
                    bdDate,
                    "",
                    gift_idea,
                    userDegree,
                    System.currentTimeMillis(),
                    "",
                    "0",
                    0
                )
                viewModel.insertBirthday(birthdate)
                returnPreviousPage()

            }
        }

    }

    private fun checkEditTextField(birthdate: String, nameSurname: String):Boolean{
        if (birthdate.isEmpty()){
            Toast.makeText(requireContext(),getText(R.string.select_birthday), Toast.LENGTH_SHORT).show()
        }else if (nameSurname.isEmpty()){
            Toast.makeText(requireContext(),getText(R.string.text_name_surname), Toast.LENGTH_SHORT).show()
        }else{
            return false
        }
        return true

    }

    private fun setUserDegreeSpinner(spinner: Spinner) : String{
        val family = getString(R.string.family)
        val friend = getString(R.string.friends)
        val work = getString(R.string.work)
        val bros = getString(R.string.bros)
        userDegrees = arrayListOf(family,bros,friend,work)
        var getUserDegree = ""

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, userDegrees)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                userDegree = p0!!.getItemAtPosition(p2).toString()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        return getUserDegree
    }

    private fun birthdaySelecter(editTextBirthdate: EditText){
        editTextBirthdate.setOnClickListener {
            showDatePickerDialog(editTextBirthdate)
        }
    }

    private fun showDatePickerDialog(editTextBirthdate: EditText) {
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
                editTextBirthdate.setText(selectedDate)
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
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
