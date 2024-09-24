package com.emirpetek.mybirthdayreminder.ui.fragment.profile.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.ContactFeedback
import com.emirpetek.mybirthdayreminder.databinding.FragmentProfileSettingsBinding
import com.emirpetek.mybirthdayreminder.viewmodel.profile.ProfileSettingsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileSettingsFragment : Fragment() {

    private val viewModel: ProfileSettingsViewModel by viewModels()
    private lateinit var binding: FragmentProfileSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSettingsBinding.inflate(inflater,container,false)

        hideBottomNav()

        binding.imageViewFragmentSettingsToolbar.setOnClickListener { findNavController().popBackStack() }
        binding.constraintLayoutSettingsAccount.setOnClickListener { findNavController().navigate(R.id.action_profileSettingsFragment_to_settingsAccountFragment) }
        binding.constraintLayoutSettingsLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            logout()
            findNavController().navigate(
                R.id.action_profileSettingsFragment_to_loginFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.loginFragment,true)
                    .build())//navigate(R.id.action_profileSettingsFragment_to_loginFragment,)
        }

        binding.constraintLayoutSettingsPrivacy.setOnClickListener { showPrivacyDialog() }
        binding.constraintLayoutSettingsContact.setOnClickListener { showContactDialog() }
        binding.buttonOnBoardingZero.setOnClickListener { onBoardingFinish() }

        return binding.root
    }

    private fun hideBottomNav(){
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }

    private fun logout() {
        sharedPreferences = requireContext().getSharedPreferences("userAuthentication", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.putBoolean("rememberMe", false)
        editor.apply()
    }

    private fun onBoardingFinish(){
        val sp = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean("finished",false)
        editor.apply()
    }

    fun showPrivacyDialog(){
        val ad = AlertDialog.Builder(requireContext())
        ad.setTitle(getString(R.string.privacy))
        ad.setMessage(getString(R.string.privacy_policy_text))
        ad.setPositiveButton("OK"){ dialog, it ->
            dialog.dismiss()
        }
        ad.create().show()
    }

    fun showContactDialog(){
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.alert_contact_us, null)

        val builder = android.app.AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(true)

        val dialog = builder.create()

        val layoutEmail = view.findViewById<LinearLayout>(R.id.layoutContactEmail)
        val layoutLinkedin = view.findViewById<LinearLayout>(R.id.layoutContactLinkedin)
        val layoutWebsite = view.findViewById<LinearLayout>(R.id.layoutContactWebsite)
        val editTextFeedback = view.findViewById<EditText>(R.id.editTextAlertContactUsFeedback)
        val btnSendFeedback = view.findViewById<Button>(R.id.buttonAlertContactUsSendFeedback)

        layoutEmail.setOnClickListener {
            val recipient = "emirpetek2002@gmail.com"  // Alıcı e-posta adresi
            val subject = "About AstroMatch"  // E-posta konusu
            val message = "Hi! \n I wanna ask something to you about AstroMatch."  // E-posta içeriği

            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))  // Alıcı
                putExtra(Intent.EXTRA_SUBJECT, subject)  // Konu
                putExtra(Intent.EXTRA_TEXT, message)  // Mesaj içeriği
            }

            startActivity(Intent.createChooser(emailIntent,requireContext().getString(R.string.choose_email_client)))
            dialog.dismiss()
        }

        layoutLinkedin.setOnClickListener {
            val bundle : Bundle = Bundle().apply { putString("URL","https://www.linkedin.com/in/emir-petek-6889411b5/") }
            findNavController().navigate(R.id.action_profileSettingsFragment_to_webviewFragment,bundle)
            dialog.dismiss()
        }

        layoutWebsite.setOnClickListener {
            val bundle : Bundle = Bundle().apply { putString("URL","https://www.emirpetek.com/en/index.php") }
            findNavController().navigate(R.id.action_profileSettingsFragment_to_webviewFragment,bundle)
            dialog.dismiss()
        }

        btnSendFeedback.setOnClickListener {
            val feedBackText = editTextFeedback.text.toString()
            if (feedBackText.isEmpty()) Toast.makeText(requireContext(),getString(R.string.enter_feedback),Toast.LENGTH_SHORT).show()
            else{
                val nesne = ContactFeedback("",feedBackText,System.currentTimeMillis(),Firebase.auth.currentUser!!.uid)
                viewModel.addFeedback(nesne)
                Toast.makeText(requireContext(),getString(R.string.thanks_for_feedback),Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }



        dialog.show()

    }



}