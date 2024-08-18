package com.emirpetek.mybirthdayreminder.ui.fragment.matchPerson

import android.graphics.Rect
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.databinding.FragmentMatchPersonBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.matchPerson.MatchPersonListUsersAdapter
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateAge
import com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant.GetZodiacAscendant
import com.emirpetek.mybirthdayreminder.viewmodel.matchPerson.MatchPersonViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MatchPersonFragment : Fragment() {

    private val viewModel: MatchPersonViewModel by viewModels()
    private lateinit var binding: FragmentMatchPersonBinding
    private lateinit var adapter: MatchPersonListUsersAdapter
    private val userID: String = Firebase.auth.currentUser!!.uid
    var userListDB = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchPersonBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).showBottomNav()



        viewModel.getCompatibleUsersData(userID)
        viewModel.user.observe(viewLifecycleOwner, Observer { it ->
            userListDB.clear()
            userListDB = it
            updateUI(userListDB)
        })

        viewModel.getUserCreditsAmount()
        viewModel.credit.observe(viewLifecycleOwner, Observer { credit ->
            binding.textViewUserGold.text = credit.amount.toString()
            binding.textViewMatchPersonRemainLike.text = credit.likeRights.toString()
            binding.textViewMatchPersonRemainMegaLike.text = credit.megaLikeRights.toString()
        })
        binding.imageViewUserGold.setOnClickListener { findNavController().navigate(R.id.action_matchPersonFragment_to_earnGoldFragment) }


        return binding.root
    }

    fun updateUI(userList: ArrayList<User>){
        //userList.shuffle()

        /*for (position in 0..<userList.size){
            val userItem = userList[position]
            val username = userItem.fullname
            val age = CalculateAge().calculateAge(userItem.birthdate)
            val horoscope = GetZodiacAscendant(requireContext()).getZodiacOrAscendantSignByIndex(userItem.zodiac)
            val userImage = userItem.profile_img

            binding.textViewMatchPersonFullname.text = username
            binding.textViewMatchPersonAge.text = age.toString()
            binding.textViewMatchPersonHoroscope.text = horoscope
            bindHoroscopeImage(userItem.zodiac,binding.imageViewMatchPersonHoroscope)

            Glide.with(requireContext())
                .load(userImage)
                .centerCrop()
                .into(binding.imageViewMatchPersonImage)



        }*/




        binding.recyclerViewMatchPersonListUser.setHasFixedSize(true)
        binding.recyclerViewMatchPersonListUser.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = MatchPersonListUsersAdapter(requireContext(),userList,viewModel,viewLifecycleOwner)

        binding.recyclerViewMatchPersonListUser.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildAdapterPosition(view)
                if (position != 0) {
                    outRect.left = 20 // Kartların arasında 16dp boşluk bırakır
                }
            }
        })

        val snapHelper: SnapHelper = LinearSnapHelper()
        if (userList.size > 0) snapHelper.attachToRecyclerView(binding.recyclerViewMatchPersonListUser)

        binding.recyclerViewMatchPersonListUser.adapter = adapter


    }

    fun bindHoroscopeImage(zodiac: Int, imageView: ImageView){
        val horoscopeDrawableId = when (zodiac) {
            10 -> R.drawable.capricorn
            11 -> R.drawable.aquarius
            12 -> R.drawable.pisces
            1 -> R.drawable.aries
            2 -> R.drawable.taurus
            3 -> R.drawable.gemini
            4 -> R.drawable.cancer
            5 -> R.drawable.leo
            6 -> R.drawable.virgo
            7 -> R.drawable.libra
            8 -> R.drawable.scorpio
            9 -> R.drawable.sagittarius
            else -> R.drawable.baseline_error_24 // Varsayılan resim
        }

        Glide.with(requireContext())
            .load(horoscopeDrawableId)
            .into(imageView)

    }
}