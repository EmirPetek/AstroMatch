package com.emirpetek.mybirthdayreminder.ui.adapter.matchPerson.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.user.userFilter.UserFilter
import com.emirpetek.mybirthdayreminder.data.entity.user.userFilter.UserHoroscopeFilter
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateGenderString
import com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant.GetZodiacAscendant

class MatchPersonFilterHoroscopeAdapter(
    val mContext: Context,
    val itemList: ArrayList<Int>,
    val filteredItemsDB: UserHoroscopeFilter? = null,
    val clickedState: ArrayList<Boolean>, // bu kısım itemlist sizesi kadar olacak ve default olarak her elemanı false olacaktır
): RecyclerView.Adapter<MatchPersonFilterHoroscopeAdapter.CardHoroscopeHolder>() {

    var selectedItems: ArrayList<Int> = arrayListOf()


    interface OnItemClickListener {
        fun onItemClicked(selectedItems: List<Int>)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class CardHoroscopeHolder(view: View): RecyclerView.ViewHolder(view) {
        val textViewCardSelectedItem: TextView = view.findViewById(R.id.textViewUserFilterSelectedItem)
        val cardSelectedItem: CardView = view.findViewById(R.id.cardUserFilterItem)
        val cardSelectedItemInside: CardView = view.findViewById(R.id.cardUserFilterItemInside)
        val imageView: ImageView = view.findViewById(R.id.imageViewUserFilterItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHoroscopeHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_user_filter_item,parent,false)
        return CardHoroscopeHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CardHoroscopeHolder, position: Int) {
        val item = itemList[position]
        val zodiacObj = GetZodiacAscendant(mContext)



            holder.textViewCardSelectedItem.text = zodiacObj.getZodiacOrAscendantSignByIndex(item)
            Glide.with(mContext).load(zodiacObj.getZodiacDrawableID(item)).into(holder.imageView)

        if (filteredItemsDB != null){
            for (p in filteredItemsDB.horoscopeList.indices){
                // Log.e("for içi", "filt item $p ${filteredItemsDB[p]} ve pos $position")

                if (filteredItemsDB.horoscopeList[p]-1 == position){
                    //   Log.e("if içi", "filt item $p ${filteredItemsDB[p]} ve pos $position")
                    holder.cardSelectedItemInside.setCardBackgroundColor(mContext.getColor(R.color.light_blue))
                    clickedState[position] = true
                    selectedItems.add(position+1)
                }
            }
        }


            holder.cardSelectedItem.setOnClickListener {
                if (!clickedState[position]){
                    holder.cardSelectedItemInside.setCardBackgroundColor(mContext.getColor(R.color.light_blue))
                    clickedState[position] = true
                    selectedItems.add(position+1)
                }else{
                    holder.cardSelectedItemInside.setCardBackgroundColor(mContext.getColor(R.color.text_white))
                    clickedState[position] = false
                    selectedItems.remove(position+1)
                }

                listener?.onItemClicked(selectedItems)
            }


        /*    holder.textViewCardSelectedItem.text = CalculateGenderString(mContext).getGenderString(item)
            holder.imageView.visibility = View.GONE

            for (p in filteredItemsDB.gender?.userGender?.indices!!){
                // Log.e("for içi", "filt item $p ${filteredItemsDB[p]} ve pos $position")

                if (filteredItemsDB.gender.userGender[p]-1 == position){
                    //   Log.e("if içi", "filt item $p ${filteredItemsDB[p]} ve pos $position")
                    holder.cardSelectedItemInside.setCardBackgroundColor(mContext.getColor(R.color.light_blue))
                    clickedState[position] = true
                    selectedItems.add(position+1)
                }
            }

            holder.cardSelectedItem.setOnClickListener {
                if (!clickedState[position]){
                    holder.cardSelectedItemInside.setCardBackgroundColor(mContext.getColor(R.color.light_blue))
                    clickedState[position] = true
                    selectedItems.add(position+1)
                }else{
                    holder.cardSelectedItemInside.setCardBackgroundColor(mContext.getColor(R.color.text_white))
                    clickedState[position] = false
                    selectedItems.remove(position+1)
                }

                listenerGender?.onItemClicked(selectedItems)
            }*/








    }


}