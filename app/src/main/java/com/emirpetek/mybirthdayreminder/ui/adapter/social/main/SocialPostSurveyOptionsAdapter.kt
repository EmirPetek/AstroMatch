package com.emirpetek.mybirthdayreminder.ui.adapter.social.main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.SelectedOptions
import com.emirpetek.mybirthdayreminder.viewmodel.social.MakeSurveyViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SocialPostSurveyOptionsAdapter(
    val mContext: Context,
    val optionList: ArrayList<String>,
  //  val userSelectList: ArrayList<SelectedOptions>? = null,
    val postID: String,
    val viewModelSurvey: MakeSurveyViewModel,
    val postPosition: Int,
    val socialPostAdapter: SocialPostAdapter,
    val viewLifecycleOwner: LifecycleOwner,
): RecyclerView.Adapter<SocialPostSurveyOptionsAdapter.OptionViewHolder>() {

    private var isAnyRadioButtonChecked = false
    private val radioButtonList = mutableListOf<RadioButton>()


    inner class OptionViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val radioButtonOption : RadioButton = view.findViewById(R.id.radioButtonSocialSurveyOption)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SocialPostSurveyOptionsAdapter.OptionViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_social_survey_options,parent,false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SocialPostSurveyOptionsAdapter.OptionViewHolder,
        position: Int
    ) {
        val option = optionList[position]
        holder.radioButtonOption.text = option


        val selectedOptions = SelectedOptions(
            Firebase.auth.currentUser!!.uid,
            postID,
            position,
            System.currentTimeMillis()
        )

        viewModelSurvey.getSelectedSurveyUsers(selectedOptions)
        viewModelSurvey.surveyOptionList.observe(viewLifecycleOwner,Observer{ it ->
            if (it != null) {
                radioButtonList.add(holder.radioButtonOption)
                val userSelectList =
                    it as ArrayList<SelectedOptions>
                for (u in userSelectList){
                    if (u.userID == selectedOptions.userID && u.postID == selectedOptions.postID){
                        val pos = u.selectedOption
                        if (position == pos){
                            holder.radioButtonOption.isChecked = true
                            for (radioButton in radioButtonList) {
                                radioButton.isClickable = false
                            }
                        }
                    }else{

                        holder.radioButtonOption.setOnClickListener {
                            for (radioButton in radioButtonList) {
                                radioButton.isClickable = false
                                radioButton.isChecked = true


                            }
                            val selectedOptionsData = SelectedOptions(
                                Firebase.auth.currentUser!!.uid,
                                postID,
                                position,
                                System.currentTimeMillis()
                            )
                            viewModelSurvey.insertSelectedSurvey(selectedOptionsData)
                            notifyItemChanged(position)
                        }
                    }

                }
            } else {
                Log.e("userseelctlist: ", "nulllll")
                radioButtonList.add(holder.radioButtonOption)

                holder.radioButtonOption.setOnClickListener {
                    for (radioButton in radioButtonList) {
                        radioButton.isClickable = false
                        radioButton.isChecked = true


                    }
                    val selectedOptionsData = SelectedOptions(
                        Firebase.auth.currentUser!!.uid,
                        postID,
                        position,
                        System.currentTimeMillis()
                    )
                    viewModelSurvey.insertSelectedSurvey(selectedOptionsData)
                    notifyItemChanged(position)
                }


                /*   if (userSelectList != null){

            }else{

                val selectedOptions = SelectedOptions(
                    Firebase.auth.currentUser!!.uid,
                    postID,
                    position,
                    System.currentTimeMillis()
                )
                viewModelSurvey.insertSelectedSurvey(selectedOptions)
            }*/


                /*   if (userSelectList != null){


              //  Log.e("kslşdak","diğer kullanıcı falan" + Firebase.auth.currentUser!!.uid)

                for (u in 0 until userSelectList.size){
                if (userSelectList[u].userID.equals(Firebase.auth.currentUser!!.uid)) {
                    //Log.e("kslşdak","diğer kullanıcı falan" + Firebase.auth.currentUser!!.uid)
                    // seçeneği seçen kullanıcılar arasında cihaz kullanıcısı varsa, yani kullanıcı bu postta seçim yaptıysa
                    val selectedOptionPositionDB = userSelectList[u].selectedOption
                    if (position == selectedOptionPositionDB) {
                        holder.radioButtonOption.isChecked = true
                        isAnyRadioButtonChecked = true
                    }
                    holder.radioButtonOption.isClickable = false // kullanıcı seçim yapmış ve diğer seçimleri yapamaz

                }else{
                    radioButtonList.add(holder.radioButtonOption)

                    holder.radioButtonOption.setOnClickListener {
                        for (radioButton in radioButtonList){
                            radioButton.isClickable = false
                            val selectedOptions = SelectedOptions(
                                Firebase.auth.currentUser!!.uid,
                                postID,
                                position,
                                System.currentTimeMillis()
                            )
                            viewModelSurvey.insertSelectedSurvey(selectedOptions)
                            notifyItemChanged(position)
                        }


                    }
                }
            }
            }else{

                radioButtonList.add(holder.radioButtonOption)

                holder.radioButtonOption.setOnClickListener {
                    for (radioButton in radioButtonList){
                        radioButton.isClickable = false
                        val selectedOptions = SelectedOptions(
                            Firebase.auth.currentUser!!.uid,
                            postID,
                            position,
                            System.currentTimeMillis()
                        )
                        viewModelSurvey.insertSelectedSurvey(selectedOptions)
                        notifyItemChanged(position)
                    }


                }

            }*/
            }
        })


    }



    override fun getItemCount(): Int {
        return optionList.size
    }
}