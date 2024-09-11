package com.emirpetek.mybirthdayreminder.ui.adapter.matchPerson.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.user.userFilter.UserGenderFilter
import com.emirpetek.mybirthdayreminder.ui.adapter.matchPerson.filter.MatchPersonFilterHoroscopeAdapter.CardHoroscopeHolder
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateGenderString

class MatchPersonFilterGenderAdapter(
    val mContext: Context,
    val itemList: ArrayList<Int>,
    val filteredItemsDB: UserGenderFilter? = null,
    val clickedState : ArrayList<Boolean>
): RecyclerView.Adapter<MatchPersonFilterGenderAdapter.GenderHolder>() {
    var selectedItems: ArrayList<Int> = arrayListOf()


    interface OnItemClickListener {
        fun onItemClicked(selectedItems: List<Int>)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class GenderHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewCardSelectedItem: TextView =
            view.findViewById(R.id.textViewUserFilterSelectedItem)
        val cardSelectedItem: CardView = view.findViewById(R.id.cardUserFilterItem)
        val cardSelectedItemInside: CardView = view.findViewById(R.id.cardUserFilterItemInside)
        val imageView: ImageView = view.findViewById(R.id.imageViewUserFilterItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.card_user_filter_item, parent, false)
        return GenderHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: GenderHolder, position: Int) {
        val item = itemList[position]


        holder.textViewCardSelectedItem.text = CalculateGenderString(mContext).getGenderString(item)
        holder.imageView.visibility = View.GONE

        if (filteredItemsDB != null){
            for (p in filteredItemsDB.userGender.indices) {
                // Log.e("for içi", "filt item $p ${filteredItemsDB[p]} ve pos $position")

                if (filteredItemsDB.userGender[p] == position) {
                    //   Log.e("if içi", "filt item $p ${filteredItemsDB[p]} ve pos $position")
                    holder.cardSelectedItemInside.setCardBackgroundColor(mContext.getColor(R.color.light_blue))
                    clickedState[position] = true
                    selectedItems.add(position)
                }
            }
        }



        holder.cardSelectedItem.setOnClickListener {
            if (!clickedState[position]) {
                holder.cardSelectedItemInside.setCardBackgroundColor(mContext.getColor(R.color.light_blue))
                clickedState[position] = true
                selectedItems.add(position)
            } else {
                holder.cardSelectedItemInside.setCardBackgroundColor(mContext.getColor(R.color.text_white))
                clickedState[position] = false
                selectedItems.remove(position)
            }

            listener?.onItemClicked(selectedItems)
        }
    }

}