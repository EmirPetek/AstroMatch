package com.emirpetek.mybirthdayreminder.ui.fragment.login

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.databinding.FragmentRegisterBinding
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateAge
import com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant.CalculateZodiacAscendant
import com.emirpetek.mybirthdayreminder.viewmodel.login.RegisterViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "RegisterFragment Log"
    private var isFillAllPlace = false
    private var zodiacSign:Int = -1
    private var ascendant:Int = -1
    private lateinit var mAdView : AdView
    private var selectedGender = -1
    private lateinit var profileImage : String
    val PICK_IMAGE_REQUEST = 1
    private var alertDialog: AlertDialog? = null
    private lateinit var profilePhotoUri: Uri
    private var isProfilePhotoUriExist = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)

        binding.imageButtonSignUpBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
        hideBottomNav()
        bindAdMob()

        auth = Firebase.auth
        mockEditText()


        binding.editTextRegisterBirthdate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.editTextRegisterBirthTime.setOnClickListener {
            showTimePickerDialog() { hour, minute ->
                binding.editTextRegisterBirthTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute))
            }
        }


        binding.radioGroupSelectGender.setOnCheckedChangeListener { group, i ->
            val selectedRadioButton = group.findViewById<RadioButton>(i)
            val index = group.indexOfChild(selectedRadioButton)
            selectedGender = index
        }

        binding.imageViewCardSelectedPhoto.setOnClickListener {
            selectProfilePhoto()
        }

        binding.buttonRegisterSignUp.setOnClickListener {

            val fullname = binding.editTextRegisterNameSurname.text.toString()
            val email = binding.editTextRegisterEmail.text.toString()
            val passwordEdit = binding.editTextRegisterPassword.text.toString()
            val passwordEditAgain = binding.editTextRegisterPasswordAgain.text.toString()
            val birthdate = binding.editTextRegisterBirthdate.text.toString()
            val birthTime = binding.editTextRegisterBirthTime.text.toString()
            val biography = binding.editTextFragmentRegisterBio.text.toString()
            var password = ""




            if ((passwordEdit.isEmpty() && passwordEditAgain.isEmpty()) || (passwordEdit.isEmpty() || passwordEditAgain.isEmpty())){
                toastShow(requireContext().getString(R.string.not_empty_password_fields))
            }else if(email.isEmpty()){
                toastShow(requireContext().getString(R.string.not_empty_email_field))
            }else if(birthdate.isEmpty()){
                toastShow(requireContext().getString(R.string.not_empty_birthday_field))
            }else if (passwordEdit != passwordEditAgain){
                toastShow(requireContext().getString(R.string.not_match_passwords))
            }else if((passwordEdit.isNotEmpty() && passwordEditAgain.isNotEmpty() && passwordEditAgain.length < 6)){
                toastShow(requireContext().getString(R.string.password_charachters_at_least_six))
            }else if (fullname.isEmpty()){
                toastShow(requireContext().getString(R.string.not_empty_fullname_field))
            }else if (birthTime.isEmpty()){
                toastShow(requireContext().getString(R.string.not_empty_birthTime_field))
            }else if (selectedGender == -1){
                toastShow(requireContext().getString(R.string.not_empty_gender_field))
            }else if (biography.isEmpty()){
                toastShow(requireContext().getString(R.string.not_empty_biography_field))
            }else if (!isProfilePhotoUriExist){
                toastShow(requireContext().getString(R.string.not_empty_profile_photo_field))
            }
            else{
                password = passwordEditAgain
                registerUser(email, password)
            }
        }


        return binding.root
    }

    private fun bindAdMob(){
        mAdView = binding.adViewFragmentRegister
        val adView = AdView(requireContext())
        adView.adUnitId = getString(R.string.ad_unit_id)
        val adSize = AdSize(LayoutParams.MATCH_PARENT,50)
        adView.setAdSize(adSize)
        this.mAdView = adView
        binding.adViewFragmentRegister.removeAllViews()
        binding.adViewFragmentRegister.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun toastShow(text:String){
        Toast.makeText(context,text, Toast.LENGTH_SHORT).show()
    }

    private fun hideBottomNav(){
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }

    private fun mockEditText(){
        binding.editTextRegisterNameSurname.setText("Emir Petek")
        binding.editTextRegisterEmail.setText("emirpetek2002@gmail.com")
        binding.editTextRegisterPassword.setText("emir2002")
        binding.editTextRegisterPasswordAgain.setText("emir2002")
        binding.editTextRegisterBirthdate.setText("25/01/2002")
        binding.editTextRegisterBirthTime.setText("04:20")
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //reload()

            //Log.e("state: ", "register fragment onstart if içi")
        }
    }

    private fun registerUser(email: String, password: String){
        calculateZodiacAndAscendant()
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        var userAuth = auth.currentUser!!
                        registerUserAndLoadImg(profilePhotoUri)
                    } else {
                        // If sign in fails, display a message to the user.
                        try {
                            throw task.exception!!
                        }catch (e : FirebaseAuthUserCollisionException){
                            closeLoadingAlert()
                            toastShow(requireContext().getString(R.string.email_already_using))
                        }
                    }
                }

    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.set(1900, 0, 1)
        val minDate = calendar.timeInMillis

        calendar.set(currentYear - 18, currentMonth, currentDay)
        val maxDate = calendar.timeInMillis

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = formatDate(year, monthOfYear, dayOfMonth)
                binding.editTextRegisterBirthdate.setText(selectedDate)
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

    private fun showTimePickerDialog(onTimeSet: (hourOfDay: Int, minute: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                onTimeSet(selectedHour, selectedMinute)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun calculateZodiacAndAscendant() {
        val date = binding.editTextRegisterBirthdate.text.toString()
        val time = binding.editTextRegisterBirthTime.text.toString()

        if (date.isNotEmpty() && time.isNotEmpty()) {
            val (day, month, year) = date.split("/").map { it.toInt() }
            val (hour, minute) = time.split(":").map { it.toInt() }

            val zodiacAscendantObj = CalculateZodiacAscendant(date,time)

            zodiacSign = zodiacAscendantObj.getZodiac()//calculateZodiacSign(day, month)
            ascendant =  zodiacAscendantObj.getAscendant()//calculateAscendant(hour)

        }
    }

    private fun showLoadingAlert() {
        if (alertDialog == null) {
            val dialogView = layoutInflater.inflate(R.layout.alert_wait_screen, null)
            dialogView.findViewById<TextView>(R.id.textViewAlertWaitScreenPleaseWait).setText(getString(R.string.your_account_registering))
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


    private fun selectProfilePhoto(){
        openSingleFileChooser()
    }

    fun openSingleFileChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // tekli foto seçimi
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri? = data.data
            Glide.with(requireContext())
                .load(imageUri)
                .into(binding.imageViewCardSelectedPhoto)

            if (imageUri != null) {
                profilePhotoUri = imageUri
                isProfilePhotoUriExist = true
            }
        }
    }

    private fun registerUserAndLoadImg(imageUri: Uri?) { // pp yükleme işi tekli
        showLoadingAlert()
        if (imageUri != null) {
            val pathName = "${System.currentTimeMillis()}_${UUID.randomUUID()}"
            val storageReference = FirebaseStorage.getInstance().getReference("users/profileImages/$pathName.jpg")
            storageReference.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        profileImage = downloadUrl

                        toastShow(requireContext().getString(R.string.register_successfull))

                        var user = User(
                            auth.currentUser!!.uid,
                            binding.editTextRegisterNameSurname.text.toString(),
                            binding.editTextRegisterEmail.text.toString(),
                            binding.editTextRegisterPassword.text.toString(),
                            binding.editTextRegisterBirthdate.text.toString(),
                            binding.editTextRegisterBirthTime.text.toString(),
                            profileImage,
                            System.currentTimeMillis(),
                            "0",
                            0,
                            zodiacSign,
                            ascendant,
                            selectedGender,
                            binding.editTextFragmentRegisterBio.text.toString(),
                            arrayListOf(),
                            CalculateAge().convertDateStringToMillis(binding.editTextRegisterBirthdate.text.toString())
                        )
                        viewModel.addUser(user)

                        lifecycleScope.launchWhenStarted {
                            viewModel.userAdded.collect { isAdded ->
                                if (isAdded) {
                                    closeLoadingAlert()
                                    findNavController().popBackStack()
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                }
        }
    }



}