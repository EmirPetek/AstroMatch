package com.emirpetek.mybirthdayreminder.ui.fragment.comeLikes

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.emirpetek.mybirthdayreminder.data.entity.like.Like
import com.emirpetek.mybirthdayreminder.databinding.FragmentComeLikesBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.comeLikes.ComeLikesAdapter
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.comeLikes.ComeLikesViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ComeLikesFragment : Fragment() {

    private val viewModel: ComeLikesViewModel by viewModels()
    private lateinit var binding: FragmentComeLikesBinding
    private val ownUserID = Firebase.auth.currentUser!!.uid
    private lateinit var adapter : ComeLikesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComeLikesBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()
        binding.imageViewComeLikesBackButton.setOnClickListener { findNavController().popBackStack() }


        binding.progressBarComeLikes.visibility = View.VISIBLE
        viewModel.getLikes(ownUserID)
        viewModel.getOwnUserData()
        viewModel.likeList.observe(viewLifecycleOwner, Observer { list ->
            val adapterList : List<Like> = listOf()
            binding.recyclerViewComeLikes.setHasFixedSize(true)
            binding.recyclerViewComeLikes.layoutManager = GridLayoutManager(requireContext(),2)

            viewModel.getUser(list)
            viewModel.likeListWithUser.observe(viewLifecycleOwner, Observer { likeList ->
                viewModel.userData.observe(viewLifecycleOwner, Observer { ownUser ->
                    adapter = ComeLikesAdapter(requireContext(), likeList,ownUser)
                    binding.recyclerViewComeLikes.adapter = adapter
                    binding.progressBarComeLikes.visibility = View.GONE
                })
            })


            if (list.isEmpty()){
                binding.progressBarComeLikes.visibility = View.GONE
                binding.textViewComeLikesNoLikeHere.visibility = View.VISIBLE
            }
        })



        return binding.root
    }
}