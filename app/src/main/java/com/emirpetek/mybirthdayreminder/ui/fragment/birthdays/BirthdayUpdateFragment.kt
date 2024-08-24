package com.emirpetek.mybirthdayreminder.ui.fragment.birthdays

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.birthdays.Birthdays
import com.emirpetek.mybirthdayreminder.databinding.FragmentBirthdayUpdateBinding
import com.emirpetek.mybirthdayreminder.viewmodel.birthdays.BirthdayUpdateViewModel
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

class BirthdayUpdateFragment : Fragment() {


    private val viewModel: BirthdayUpdateViewModel by viewModels()
    private lateinit var binding: FragmentBirthdayUpdateBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userkey : String
    private lateinit var birthdayKey : String
    private var birthdayData : List<Birthdays> = listOf()
    private var userDegrees = arrayListOf<String>()
    private var userDegree:String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var mAdView : AdView




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBirthdayUpdateBinding.inflate(inflater,container,false)

        auth = Firebase.auth

        sharedPreferences = requireActivity().getSharedPreferences("userInfo", AppCompatActivity.MODE_PRIVATE)
        userkey = auth.currentUser!!.uid//sharedPreferences.getString("userKey", "0")!!

        birthdayKey = arguments?.getString("BIRTHDAY_KEY").toString()
        //birthdayKey = Bundle().getBundle("BIRTHDAY_KEY").toString()
        getBirthdayList()
        bindBackButton()
        bindUpdateButton()
        bindDeleteButton()
        hideBottomNav()
        bindAdMob()


        return binding.root
    }
    private fun hideBottomNav(){
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }

    fun bindAdMob(){
        // birthdays sayfası reklam gösterme kısmı
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(requireContext()) {}
        }
        mAdView = binding.adViewFragmentBirthdayUpdate
        val adView = AdView(requireContext())
        adView.adUnitId = getString(R.string.ad_unit_id)
        val adSize = AdSize(LayoutParams.MATCH_PARENT,80)
        adView.setAdSize(adSize)
        this.mAdView = adView
        binding.adViewFragmentBirthdayUpdate.removeAllViews()
        binding.adViewFragmentBirthdayUpdate.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun getBirthdayList(){

        viewModel.getBirthdayList(userkey,birthdayKey)
        viewModel.birthdayList.observe(viewLifecycleOwner, Observer {
            birthdayData = it.toList()
            bindComponents()
        })
    }

    private fun bindComponents(){

        for (b in birthdayData){
            binding.editTextBDUpdateDate.setText(b.date)
            binding.editTextBDUpdateGiftIdea.setText(b.giftIdea)
            binding.editTextBDUpdateNameSurname.setText(b.name)
            setUserDegreeSpinner(binding.spinnerDBUserDegree,b.userDegree)
            birthdaySelecter(binding.editTextBDUpdateDate,b.date)
        }
    }

    private fun bindBackButton(){
        binding.imageViewUpdateBirthdayArrowBack.setOnClickListener {
            returnPreviousPage()
        }
    }

    private fun bindUpdateButton(){
        binding.buttonUpdateBirthday.setOnClickListener {
           val update = mapOf(
               "name" to binding.editTextBDUpdateNameSurname.text.toString(),
               "date" to binding.editTextBDUpdateDate.text.toString(),
               "giftIdea" to binding.editTextBDUpdateGiftIdea.text.toString(),
               "userDegree" to userDegree
           )

            viewModel.updateBirthday(userkey,birthdayKey,update).apply {
                Toast.makeText(context,getString(R.string.datas_updated),Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }

        }
    }

    private fun bindDeleteButton(){
        binding.buttonUpdateDelete.setOnClickListener {
            showAlertDialogAskDelete()
        }
    }

    private fun showAlertDialogAskDelete(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.delete_birthday))
        builder.setMessage(getString(R.string.delete_birthday_ask))

        val delete = mapOf(
            "deletedState" to "1",
            "deleteTime" to System.currentTimeMillis(),
        )

        builder.setPositiveButton("OK") { dialog, which ->
            viewModel.deleteBirthday(userkey,birthdayKey,delete)//.apply {
                Toast.makeText(context,getString(R.string.datas_updated),Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
         //   }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }



    private fun returnPreviousPage(){
        findNavController().popBackStack()
        /*val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainactivity_constraint_layout,BirthdaysFragment())
        transaction.addToBackStack(null)
        transaction.commit()*/
    }

    private fun setUserDegreeSpinner(spinner: Spinner, userDegreeStr: String): String {
        val family = getString(R.string.family)
        val friend = getString(R.string.friends)
        val work = getString(R.string.work)
        val bros = getString(R.string.bros)
        userDegrees = arrayListOf(family, bros, friend, work)

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, userDegrees)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // Spinner'da userDegreeStr'ye göre öğeyi seçili hale getir
        val position = userDegrees.indexOf(userDegreeStr)
        if (position >= 0) {
            spinner.setSelection(position)
        }

        // Seçilen öğeyi saklamak için bir değişken
        var selectedDegree = userDegreeStr

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedDegree = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Hiçbir şey seçilmediğinde yapılacak işlemler
            }
        }

        return selectedDegree
    }

    private fun birthdaySelecter(editTextBirthdate: EditText, date: String){
        editTextBirthdate.setOnClickListener {
            showDatePickerDialog(editTextBirthdate,date)
        }
    }

    private fun showDatePickerDialog(editTextBirthdate: EditText, date: String) {
        val calendar = Calendar.getInstance()

        // Gelen date stringini parse et
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val parsedDate = dateFormat.parse(date)
        calendar.time = parsedDate

        val selectedYear = calendar.get(Calendar.YEAR)
        val selectedMonth = calendar.get(Calendar.MONTH)
        val selectedDay = calendar.get(Calendar.DAY_OF_MONTH)

        // DatePickerDialog için minimum ve maksimum tarihleri ayarla
        calendar.set(1900, 0, 1)
        val minDate = calendar.timeInMillis

        val maxDate = System.currentTimeMillis()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = formatDate(year, monthOfYear, dayOfMonth)
                editTextBirthdate.setText(selectedDate)
            },
            selectedYear, selectedMonth, selectedDay
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