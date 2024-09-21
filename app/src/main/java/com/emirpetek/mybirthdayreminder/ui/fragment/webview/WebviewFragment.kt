package com.emirpetek.mybirthdayreminder.ui.fragment.webview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentWebviewBinding
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility

class WebviewFragment : Fragment() {

    private lateinit var binding : FragmentWebviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebviewBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        binding.imageViewWebviewFragmentBackButton.setOnClickListener { findNavController().popBackStack() }

        val url = arguments?.getString("URL")!!


        binding.webview.webViewClient = WebViewClient()

        // this will load the url of the website
        binding.webview.loadUrl(url)

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        binding.webview.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        binding.webview.settings.setSupportZoom(true)


        // WebViewClient'i ayarla
        binding.webview.webViewClient = object : WebViewClient() {

            // Sayfa yüklenmesi tamamlandığında bu metod çağrılır
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

            }
        }

        // Geri basma işlemini dinleyici ile yönet
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (binding.webview.canGoBack()) {
                binding.webview.goBack()
            } else {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }




}