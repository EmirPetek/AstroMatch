package com.emirpetek.mybirthdayreminder.ui.fragment.matchPerson

import android.graphics.Rect
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.entity.user.userFilter.UserAgeFilter
import com.emirpetek.mybirthdayreminder.data.entity.user.userFilter.UserFilter
import com.emirpetek.mybirthdayreminder.data.entity.user.userFilter.UserGenderFilter
import com.emirpetek.mybirthdayreminder.databinding.FragmentMatchPersonBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.matchPerson.MatchPersonListUsersAdapter
import com.emirpetek.mybirthdayreminder.ui.adapter.matchPerson.filter.MatchPersonFilterGenderAdapter
import com.emirpetek.mybirthdayreminder.ui.adapter.matchPerson.filter.MatchPersonFilterHoroscopeAdapter
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.matchPerson.MatchPersonViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections

class MatchPersonFragment : Fragment() {

    private val viewModel: MatchPersonViewModel by viewModels()
    private lateinit var binding: FragmentMatchPersonBinding
    private lateinit var adapter: MatchPersonListUsersAdapter
    private val userID: String = Firebase.auth.currentUser!!.uid
    var userListDB = ArrayList<User>()
    var emptyUserList = ArrayList<User>()
    private var isDailyWinChecked = false
    private lateinit var horoscopeAdapter: MatchPersonFilterHoroscopeAdapter
    private lateinit var genderAdapter: MatchPersonFilterGenderAdapter
    private val filterItems = UserFilter()
    private var ageRanges: MutableList<Float> = mutableListOf(0f, 0f)
    var selectedHoroscopeItems: List<Int>? = null
    var selectedGenderItems: List<Int>? = null
    private var isUserFilterBefore = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchPersonBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).showBottomNav()

        binding.imageViewMatchPersonUserFilter.setOnClickListener { setupBottomSheetDialog() }


        getUserList()



        viewModel.getUserCreditsAmount()
        viewModel.credit.observe(viewLifecycleOwner, Observer { credit ->
            binding.textViewUserGold.text = credit.amount.toString()
            binding.textViewMatchPersonRemainLike.text = credit.likeRights.toString()
            binding.textViewMatchPersonRemainMegaLike.text = credit.megaLikeRights.toString()
            checkDailyWinGold(credit.lastCreditBalanceTimestamp)
        })
        binding.imageViewUserGold.setOnClickListener { findNavController().navigate(R.id.action_matchPersonFragment_to_earnGoldFragment) }


        return binding.root
    }

    private fun getUserList(){

        viewModel.getUserFilterItems()
        viewModel.userFilters.observe(viewLifecycleOwner, Observer { userFilters ->

            if (userFilters == null) {
                viewModel.getCompatibleUsersData(userID)
                viewModel.user.observe(viewLifecycleOwner, Observer { it ->
                    userListDB.clear()
                    var itTemp = it.shuffled()
                    userListDB.addAll(itTemp)// = itTemp
                    updateUI(userListDB)
                    //Log.e("MatchPersonFragment", "getUserList if içi")
                })
            }else{
                viewModel.getFilteredUsers(userFilters)
                viewModel.filteredUsers.observe(viewLifecycleOwner, Observer { it ->
                    if (it != null) {
                        if (it.isNotEmpty()){
                            binding.textViewMatchPersonNoUserText.visibility = View.GONE
                            userListDB.clear()
                            //userListDB = ArrayList(it)

                            var itTemp = it.shuffled()
                            userListDB.addAll(itTemp)// = itTemp

                            updateUI(userListDB)
                            //Log.e("MatchPersonFragment", "getUserList else içi if ve dblistsize: ${userListDB.size} dblist: $userListDB")

                        }else {
                            updateUI(emptyUserList)
                            //Log.e("MatchPersonFragment", "getUserList else içi else ve dblistsize: ${userListDB.size} dblist: $userListDB")
                            binding.textViewMatchPersonNoUserText.visibility = View.VISIBLE
                        }
                    }
                 })
            }

        })
    }

    fun updateUI(userList: ArrayList<User>){

        binding.recyclerViewMatchPersonListUser.setHasFixedSize(true)
        binding.recyclerViewMatchPersonListUser.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = MatchPersonListUsersAdapter(requireContext(),userList,viewModel,viewLifecycleOwner)
        adapter.notifyDataSetChanged()
        /*binding.recyclerViewMatchPersonListUser.addItemDecoration(object : RecyclerView.ItemDecoration() {
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
        })*/

        //val snapHelper: SnapHelper = LinearSnapHelper()
        //if (userList.size > 0) snapHelper.attachToRecyclerView(binding.recyclerViewMatchPersonListUser)

        binding.recyclerViewMatchPersonListUser.adapter = adapter


    }

    fun checkDailyWinGold(lastTime : Long){
        if (isDailyWinChecked) return

        val currentTimestamp = System.currentTimeMillis()
        val lastTimestamp = lastTime
        val winFrequency : Long = 86400000 // 1 day as millis

        val intervalTime = currentTimestamp - lastTimestamp

        if (intervalTime > winFrequency){
            isDailyWinChecked = true

            val inflater = LayoutInflater.from(requireContext())
            val view = inflater.inflate(R.layout.alert_win_daily_gold, null)

            val builder = android.app.AlertDialog.Builder(requireContext())
                .setView(view)
                .setCancelable(true)

            val dialog = builder.create()

            val button = view.findViewById<Button>(R.id.buttonAlertWinDailyGold)
            button.setOnClickListener { dialog.dismiss() }
            dialog.setOnDismissListener { viewModel.setDailyBonusValues() }
            dialog.show()
        }
    }

    private fun setupBottomSheetDialog(){
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_user_filter,null)
        val rvGender : RecyclerView = view.findViewById(R.id.recyclerViewBottomSheetUserFilterGender)
        val rvHoroscope : RecyclerView = view.findViewById(R.id.recyclerViewBottomSheetUserFilterHoroscope)
        val textViewApply : TextView = view.findViewById(R.id.textViewBottomSheetUserFilterApply)
        val rangeSliderAge : RangeSlider = view.findViewById(R.id.rangeSliderBottomSheetUserFilterAge)
        val btnClose = view.findViewById<ImageView>(R.id.imageViewBottomSheetUserFilterCloseButton)
        btnClose.setOnClickListener { dialog.dismiss() }

        rvGender.setHasFixedSize(true)
        rvGender.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        rvHoroscope.setHasFixedSize(true)
        rvHoroscope.layoutManager = StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL)

        viewModel.getUserFilterItems()
        viewModel.userFilters.observe(viewLifecycleOwner, Observer { userFilters ->

            val horoscopeList = arrayListOf(1,2,3,4,5,6,7,8,9,10,11,12)
            val horoscopeClickedState =  ArrayList<Boolean>(Collections.nCopies(12, false))
            if (userFilters?.horoscope != null) {
                horoscopeAdapter = MatchPersonFilterHoroscopeAdapter(requireContext(),horoscopeList,userFilters.horoscope,horoscopeClickedState,)
                selectedHoroscopeItems = userFilters.horoscope!!.horoscopeList
            }else {
                horoscopeAdapter = MatchPersonFilterHoroscopeAdapter(requireContext(),horoscopeList,null,horoscopeClickedState,)
            }
            rvHoroscope.adapter = horoscopeAdapter

            val genderList = arrayListOf(0,1,2)
            val genderClickedState =  ArrayList<Boolean>(Collections.nCopies(3, false))
            if (userFilters?.gender != null) {
                genderAdapter = MatchPersonFilterGenderAdapter(requireContext(),genderList,userFilters.gender,genderClickedState,)
                selectedGenderItems = userFilters.gender.userGender
            }else{
                genderAdapter = MatchPersonFilterGenderAdapter(requireContext(),genderList,null,genderClickedState,)
            }
            rvGender.adapter = genderAdapter



            horoscopeAdapter.setOnItemClickListener(object : MatchPersonFilterHoroscopeAdapter.OnItemClickListener {
                override fun onItemClicked(selectedItems: List<Int>) {
                    selectedHoroscopeItems = selectedItems // Seçilen burçlar burada tutuluyor
                    //Log.e("horoscopeItems: ", selectedItems.toString())
                }
            })

            genderAdapter.setOnItemClickListener(object : MatchPersonFilterGenderAdapter.OnItemClickListener {
                override fun onItemClicked(selectedItems: List<Int>) {
                    selectedGenderItems = selectedItems // Seçilen cinsiyet burada tutuluyor
                    //Log.e("genderItems: ", selectedItems.toString())
                }
            })


            if (userFilters?.age != null){
                rangeSliderAge.values = listOf(userFilters.age.min,userFilters.age.max)
                ageRanges[0] = userFilters.age.min
                ageRanges[1] = userFilters.age.max
            }

            rangeSliderAge.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener{
                override fun onStartTrackingTouch(slider: RangeSlider) {
                    ageRanges = rangeSliderAge.values
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    ageRanges = rangeSliderAge.values
                }
            })






            textViewApply.setOnClickListener {

                val filterObject = UserFilter()

                // Burç filtreleme seçildi mi?
                selectedHoroscopeItems?.let {
                    filterItems.horoscope!!.horoscopeList = it as ArrayList<Int>
                    filterObject.horoscope!!.horoscopeList = it
                   // Log.e("horoscopeItems: ", it.toString())
                }

                // Cinsiyet filtreleme seçildi mi?
                selectedGenderItems?.let {
                    filterItems.gender!!.userGender = it as ArrayList<Int>
                    filterObject.gender!!.userGender = it
                    //Log.e("genderItems: ", it.toString())
                }


                filterObject.age!!.min = ageRanges[0]
                filterObject.age.max = ageRanges[1]

               // Log.e("yaş aralığı", ageRanges.toString())


               // UserAgeFilter(ageRanges[0],ageRanges[1])
                viewModel.setUserFilterItems(filterObject)
                getUserList()


                dialog.dismiss()
                }

            })







        setFilterItems(textViewApply)

        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    fun setFilterItems(tv: TextView){


        tv.setOnClickListener {
            Log.e("tıklanan itemler: ", filterItems.toString())
        }
       // viewModel.setUserFilterItems()


    }

    fun filterItems(filteredItems: UserFilter){

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