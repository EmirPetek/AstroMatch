package com.emirpetek.mybirthdayreminder.ui.adapter.social.main.deprecated

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.question.SelectedOptions
import com.emirpetek.mybirthdayreminder.viewmodel.social.MakeSurveyViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SocialPostSurveyOptionsAdapter(
    val mContext: Context,
    val optionList: ArrayList<String>,
    val postID: String,
    val viewModelSurvey: MakeSurveyViewModel,
    val viewLifecycleOwner: LifecycleOwner,
    val userSelectedOptionList: ArrayList<SelectedOptions>,
    var optionPosition: Int,
): RecyclerView.Adapter<SocialPostSurveyOptionsAdapter.OptionViewHolder>() {

    private var isAnyRadioButtonChecked = false
    private val radioButtonList = mutableListOf<RadioButton>()


    inner class OptionViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val radioButtonOption : RadioButton = view.findViewById(R.id.radioButtonSocialSurveyOption)
        val constraintLayoutCardSurveyOption : ConstraintLayout = view.findViewById(R.id.constraintLayoutCardSurveyOption)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_social_survey_options,parent,false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: OptionViewHolder,
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

        if (position == 0) {
            radioButtonList.clear()
        }

        radioButtonList.add(holder.radioButtonOption)
        //Log.e("numbers: ", "optionPosition: $optionPosition ve position: $position ve eleman adı $option")

        if (optionPosition == -1){ // kullanıcı ilgili postta survey işareti hiç yapmamış
            //Log.e("hangi ifte -1: ", holder.radioButtonOption.text.toString())

        }else{//(optionPosition == position){
            if (optionPosition == position) {

                holder.radioButtonOption.isChecked = true


            }
            for (radioButton in radioButtonList) {
                radioButton.setOnClickListener {
                    Log.e("hangi ifte else if: ",radioButton.text.toString())
                }
                radioButton.isClickable = false
            }
        }


      /*  for (i in userSelectedOptionList){

            if (i.userID == selectedOptions.userID && i.postID == postID && i.selectedOption == selectedOptions.selectedOption) {
//                Log.e("i post id: ", i.postID)
//                Log.e("anlık post id: ", postID)
//                Log.e("i selected option: ", i.selectedOption.toString())
//                Log.e("anlık selected option: ", selectedOptions.selectedOption.toString())
//                Log.e("i userid: ", i.userID)
              //  if (i.selectedOption == selectedOptions.selectedOption) {
                    Log.e("checked text: ", holder.radioButtonOption.text.toString())
                    Log.e("gelen obj: ", i.postID.toString())
                //holder.radioButtonOption.isChecked = true
                holder.radioButtonOption.isSelected = true
                //}
                for (radioButton in radioButtonList) {
                    radioButton.isClickable = false
                }
                continue
            }


        }*/



     /*   viewModelSurvey.getSelectedSurveyUsers(selectedOptions)
       viewModelSurvey.surveyOptionList.observe(viewLifecycleOwner,Observer{ it ->
           Log.e("userselectlist size: ", it.size.toString())

           if (it != null) {
                radioButtonList.add(holder.radioButtonOption)
                val userSelectList = it as ArrayList<SelectedOptions>
                for (u in userSelectList){
//                    if (u.userID == selectedOptions.userID
//                        && u.postID == selectedOptions.postID){
                    if (u.selectedOption == selectedOptions.selectedOption
                        && u.userID == selectedOptions.userID
                        && u.postID == selectedOptions.postID){
                        //Log.e("socialpostsurveyoptionsadapter","iki u şartı altı postID: ${u.postID}")
                      //  Log.e("post içeriği" ,"u ların olduğu if ve ${u.selectedOption} ? $option")
                        val pos = u.selectedOption
                        for (radioButton in radioButtonList) {
                            radioButton.isClickable = false
                        }
                        holder.radioButtonOption.isChecked = true


                    }else{
                       // Log.e("socialpostsurveyoptionsadapter","iki u olmaayan taraf postID: ${u.postID}")
                      //  Log.e("post içeriği" ,"u ların olduğu else ve ${u.selectedOption} ?  $option")
                        // kullanıcı seçim yapmamış
                        holder.radioButtonOption.setOnClickListener {
                            Log.e("options survey", "else kısmı ve u: ${u.postID}")
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
            }
            else {
                radioButtonList.add(holder.radioButtonOption)
                Log.e("socialpostsurveyoptionsadapter","it null")

                holder.radioButtonOption.setOnClickListener {
                    Log.e("socialpostsurveyoptionsadapter","it null")

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
        })*/


    }



    override fun getItemCount(): Int {
        return optionList.size
    }
}