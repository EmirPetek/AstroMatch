package com.emirpetek.mybirthdayreminder.ui.fragment.login

import android.app.DatePickerDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.User
import com.emirpetek.mybirthdayreminder.databinding.FragmentRegisterBinding
import com.emirpetek.mybirthdayreminder.viewmodel.login.RegisterViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "RegisterFragment Log"
    private var isFillAllPlace = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)

        binding.imageButtonSignUpBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
        hideBottomNav()

        auth = Firebase.auth
        mockEditText()


        binding.editTextRegisterBirthdate.setOnClickListener {
            showDatePickerDialog()
        }


        binding.buttonRegisterSignUp.setOnClickListener {

            val fullname = binding.editTextRegisterNameSurname.text.toString()
            val email = binding.editTextRegisterEmail.text.toString()
            val passwordEdit = binding.editTextRegisterPassword.text.toString()
            val passwordEditAgain = binding.editTextRegisterPasswordAgain.text.toString()
            val birthdate = binding.editTextRegisterBirthdate.text.toString()
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
            }
            else{
                password = passwordEditAgain
                registerUser(email, password)
            }

            /*
             burada
             kullanıcı zaten kayıtlı,
             parola boş geçilemez,
             parolalar eşleşmiyor,
             email formatı hatalı

             bu exceptionları throw etsin. buna göre kontrolleri yap
             sonra users adlı dataclassta uid_234DFSgbddv43ver4gfdvdd gibi şey node olacak
             şekilde realtime dbye kayıtları sağla. mvvm ile yap bunu

             sonra login işlemlerini yap.
             */



        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun toastShow(text:String){
        Toast.makeText(context,text, Toast.LENGTH_SHORT).show()
    }

    private fun hideBottomNav(){
        Log.e("dsskfl","jdfksdfsf")
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }

    private fun mockEditText(){
        binding.editTextRegisterNameSurname.setText("Emir Petek")
        binding.editTextRegisterEmail.setText("emirpetek2002@gmail.com")
        binding.editTextRegisterPassword.setText("emir2002")
        binding.editTextRegisterPasswordAgain.setText("emir2002")
        binding.editTextRegisterBirthdate.setText("25/01/2002")
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            //reload()

            Log.e("state: ", "register fragment onstart if içi")
        }
    }

    private fun registerUser(email: String, password: String){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        var userAuth = auth.currentUser!!
                        toastShow(requireContext().getString(R.string.register_successfull))

                        var user = User(
                                userAuth.uid,
                                binding.editTextRegisterNameSurname.text.toString(),
                                binding.editTextRegisterEmail.text.toString(),
                                binding.editTextRegisterPassword.text.toString(),
                                binding.editTextRegisterBirthdate.text.toString(),
                            "null",
                                System.currentTimeMillis()
                                )
                        viewModel.addUser(user)

                        lifecycleScope.launchWhenStarted {
                            viewModel.userAdded.collect { isAdded ->
                                if (isAdded) {
                                    findNavController().popBackStack()
                                }
                            }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        try {
                            throw task.exception!!
                        }catch (e : FirebaseAuthUserCollisionException){
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

        val maxDate = System.currentTimeMillis()

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
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}