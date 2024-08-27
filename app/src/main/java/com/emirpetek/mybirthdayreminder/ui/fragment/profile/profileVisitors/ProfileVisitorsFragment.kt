package com.emirpetek.mybirthdayreminder.ui.fragment.profile.profileVisitors

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.recyclerViewVisitors.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        binding.imageViewProfileVisitorsBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.progressBarProfileVisitors.visibility = View.VISIBLE
        viewModel.getVisitorList()
        viewModel.visitList.observe(viewLifecycleOwner, Observer { visitList ->
            val list = visitList.sortedByDescending { it.timestamp }
            adapter = ProfileVisitorsAdapter(requireContext(),list,binding.progressBarProfileVisitors)
            binding.recyclerViewVisitors.adapter = adapter

            if (visitList.isNullOrEmpty()){
                binding.progressBarProfileVisitors.visibility = View.GONE
                binding.textViewProfileVisitorsNoVisitHere.visibility = View.VISIBLE
            }
        })


        return binding.root
    }
}