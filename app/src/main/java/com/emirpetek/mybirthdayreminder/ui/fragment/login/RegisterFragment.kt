package com.emirpetek.mybirthdayreminder.ui.fragment.login

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentRegisterBinding
import com.emirpetek.mybirthdayreminder.viewmodel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "RegisterFragment Log"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)

        binding.imageButtonSignUpBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        auth = Firebase.auth




        binding.buttonRegisterSignUp.setOnClickListener {

            val email = binding.editTextRegisterEmail.text.toString()
            val password = binding.editTextRegisterPassword.text.toString()
            val passwordAgain = binding.editTextRegisterPasswordAgain.text.toString()
            var pwCreateUser = ""

            if (email.isEmpty()) Toast.makeText(context,"E posta adresi boş geçilemez", Toast.LENGTH_SHORT).show()

            if (password != passwordAgain) {
                Toast.makeText(context,"Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show()
            }else{
                pwCreateUser = password
            }

            auth.createUserWithEmailAndPassword(email, pwCreateUser)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            context,
                            "Bu adrese kayıtlı zaten bir kullanıcı bulunmaktadır.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        //updateUI(null)
                    }



                }

        }


        return binding.root
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
}