package com.emirpetek.mybirthdayreminder.ui.fragment

import android.app.AlertDialog
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentBirthdaysBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.BirthdaysAdapter
import com.emirpetek.mybirthdayreminder.viewmodel.BirthdaysViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BirthdaysFragment : Fragment() {


    private val viewModel: BirthdaysViewModel by viewModels()
    private lateinit var binding: FragmentBirthdaysBinding
    private var userDegrees = arrayListOf<String>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: BirthdaysAdapter
    private var userkey = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBirthdaysBinding.inflate(inflater,container,false)

        sharedPreferences = requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        userkey = sharedPreferences.getString("userKey","")!!

        binding.progressBarFragmentBirthdays.visibility = View.VISIBLE
        binding.textViewNoAddedBirthday.visibility = View.GONE

        bindFABbtn()
        hideNoBirthdayText()
        getBirthdayList()

        return binding.root

    }

    private fun bindFABbtn(){
        binding.floatingActionButtonAddBirthday.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.mainactivity_constraint_layout,BirthdayAddFragment())
            transaction.commit()
        }
    }





    private fun getBirthdayList(){
        binding.recyclerViewBirthdays.setHasFixedSize(true)
        binding.recyclerViewBirthdays.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getBirthdayList(userkey)
        viewModel.birthdayList.observe(viewLifecycleOwner, Observer {

            adapter = BirthdaysAdapter(requireContext(),it,viewModel)
            binding.recyclerViewBirthdays.adapter = adapter
            binding.progressBarFragmentBirthdays.visibility = View.GONE
        })
    }

    private fun hideNoBirthdayText(){
        viewModel.birthdayList.observe(viewLifecycleOwner, Observer {
            val size = it.size
            if (size == 0) {
                binding.progressBarFragmentBirthdays.visibility = View.GONE
                binding.textViewNoAddedBirthday.visibility = View.VISIBLE
            }else{
                binding.textViewNoAddedBirthday.visibility = View.GONE
            }
        })

    }



   /* fun alertBirthdayAdd(){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.alert_add_birthday,null)

        val spinnerAlertDBUserDegree = dialogView.findViewById<Spinner>(R.id.spinnerAlertDBUserDegree)
        val editTextBirthdate = dialogView.findViewById<EditText>(R.id.editTextAlertBDDate)
        val editTextNameSurname = dialogView.findViewById<EditText>(R.id.editTextAlertBDNameSurname)
        val editTextGiftIdea = dialogView.findViewById<EditText>(R.id.editTextAlertBDGiftIdea)

        val userkey = sharedPreferences.getString("userKey","")!!



        val userDegree = setUserDegreeSpinner(spinnerAlertDBUserDegree)
        birthdaySelecter(editTextBirthdate)

        val checkStatus = checkEditTextField(editTextBirthdate,editTextNameSurname)
        if (checkStatus == 2){ // namesurname and birthday were selected
            val birthdate = Birthdays(
                userkey,
                "",
                editTextNameSurname.text.toString(),
                editTextBirthdate.text.toString(),
                "",
                editTextGiftIdea.text.toString(),
                userDegree,
                System.currentTimeMillis()
            )

            viewModel.insertBirthday(birthdate)
        }





        builder.setView(dialogView)
        builder.create().show()
    }

    private fun getSpinnerSelectedItem(spinner: Spinner){

    }

    private fun checkEditTextField(editTextBirthdate: EditText, editTextNameSurname: EditText):Int{
        var checkStatus = 0
        if (editTextBirthdate.equals("")) {
            Toast.makeText(requireContext(),getText(R.string.select_birthday), Toast.LENGTH_SHORT).show()
        }else{
            checkStatus++
        }
        if (editTextNameSurname.equals("")){
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
                getUserDegree = userDegrees[p2]
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
*/
}