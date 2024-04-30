package com.emirpetek.mybirthdayreminder.ui.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.Birthdays
import com.emirpetek.mybirthdayreminder.databinding.FragmentBirthdayAddBinding
import com.emirpetek.mybirthdayreminder.viewmodel.BirthdayAddViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBirthdayAddBinding.inflate(layoutInflater, container, false)

        (activity as? AppCompatActivity)?.supportActionBar?.hide()
        setUserDegreeSpinner(binding.spinnerDBUserDegree)
        Log.e("ksdlşfkdsşflsd",userDegree)
        birthdaySelecter(binding.editTextBDAddDate)
        sharedPreferences = requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        userkey = sharedPreferences.getString("userKey", "")!!

        bindBackButton()
        bindAddButton()

        return binding.root

    }

    private fun bindBackButton(){
        binding.imageViewAddBirthdayArrowBack.setOnClickListener {
            returnPreviousPage()
        }
    }

    private fun returnPreviousPage(){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainactivity_constraint_layout,BirthdaysFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun bindAddButton(){
        binding.buttonAddBirthday.setOnClickListener {
            setUserDegreeSpinner(binding.spinnerDBUserDegree)
            Log.e("userDegree ",userDegree)
            birthdaySelecter(binding.editTextBDAddDate)

            name_surname = binding.editTextBDAddNameSurname.text.toString()
            bdDate = binding.editTextBDAddDate.text.toString()
            gift_idea = binding.editTextBDAddGiftIdea.text.toString()
            Log.e("sdkfsd","bindAddButton")
            Log.e("bdDate ve name surname","$bdDate ve $name_surname")

            val checkStatus = checkEditTextField(bdDate,name_surname)
            if (checkStatus == 2) { // namesurname and birthday were selected
                val birthdate = Birthdays(
                    userkey,
                    "",
                    name_surname,
                    bdDate,
                    "",
                    gift_idea,
                    userDegree,
                    System.currentTimeMillis(),
                    ""
                )
                viewModel.insertBirthday(birthdate)
                returnPreviousPage()

            }
        }

    }

    private fun checkEditTextField(birthdate: String, nameSurname: String):Int{
        var checkStatus = 0
        if (birthdate.equals("")) {
            Toast.makeText(requireContext(),getText(R.string.select_birthday), Toast.LENGTH_SHORT).show()
        }else{
            checkStatus++
        }
        if (nameSurname.equals("")){
            Toast.makeText(requireContext(),getText(R.string.text_name_surname), Toast.LENGTH_SHORT).show()
        }else{
            checkStatus++
        }

        return checkStatus

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
