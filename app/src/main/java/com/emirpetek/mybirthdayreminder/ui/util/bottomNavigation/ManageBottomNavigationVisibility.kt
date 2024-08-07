package com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation

import android.app.Activity
import android.content.Context
import android.view.View
import com.emirpetek.mybirthdayreminder.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ManageBottomNavigationVisibility(
    val requireActivity: Activity
) {

    fun showBottomNav(){
        val bottomNav = requireActivity.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.VISIBLE
    }

    fun hideBottomNav(){
        val bottomNav = requireActivity.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }
}