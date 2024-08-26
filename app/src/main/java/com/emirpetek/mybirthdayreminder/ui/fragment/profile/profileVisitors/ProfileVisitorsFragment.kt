package com.emirpetek.mybirthdayreminder.ui.fragment.profile.profileVisitors

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentProfileVisitorsBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.profile.profileVisitors.ProfileVisitorsAdapter
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.profile.ProfileVisitorsViewModel

class ProfileVisitorsFragment : Fragment() {

    private val viewModel: ProfileVisitorsViewModel by viewModels()
    private lateinit var binding: FragmentProfileVisitorsBinding
    private lateinit var adapter: ProfileVisitorsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileVisitorsBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        binding.recyclerViewVisitors.setHasFixedSize(true)
        binding.recyclerViewVisitors.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        viewModel.getVisitorList()
        viewModel.visitList.observe(viewLifecycleOwner, Observer { visitList ->
            var userCounter = 0

            for (visitor in visitList){
                viewModel.getUser(visitor.visitorID)
                viewModel.user.observe(viewLifecycleOwner, Observer { user ->
                    visitor.user = user
                    userCounter++
                })
            }

            if (userCounter == visitList.size){
                adapter = ProfileVisitorsAdapter(requireContext(),visitList)
                binding.recyclerViewVisitors.adapter = adapter
                Log.e("vewrikf",visitList.toString())
            }
        })


        return binding.root
    }
}