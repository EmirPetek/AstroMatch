package com.emirpetek.mybirthdayreminder.ui.adapter.birthdays.giftIdeas

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.emirpetek.mybirthdayreminder.R

class BirthdaysAnotherUserGiftIdeaBottomBarAdapter(
    val mContext: Context,
    val itemList: ArrayList<String>,
    val filteredItems: List<Int>
): RecyclerView.Adapter<BirthdaysAnotherUserGiftIdeaBottomBarAdapter.ItemHolder>() {

    val clickedState : ArrayList<Boolean> = arrayListOf(false,false,false,false,false,false,false)
    var selectedItems: ArrayList<Int> = arrayListOf()

    interface OnItemClickListener {
        fun onItemClicked(selectedItems: List<Int>)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view){
        val textViewCardSelectedItem: TextView = view.findViewById(R.id.textViewCardSelectedItem)
        val cardSelectedItem: CardView = view.findViewById(R.id.cardSelectedItem)
        val cardSelectedItemInside: CardView = view.findViewById(R.id.cardSelectedItemInside)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_select_item_text,parent,false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = itemList[position]
        holder.textViewCardSelectedItem.text = item



        for (p in filteredItems.indices){
            Log.e("for içi", "filt item $p ${filteredItems[p]} ve pos $position")

            if (filteredItems[p]-1 == position){
                Log.e("if içi", "filt item $p ${filteredItems[p]} ve pos $position")
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

            listener?.onItemClicked(selectedItems)
        }




        // Log.e("position ve eleman text: ", "$position ve ${holder.textViewCardSelectedItem.text}")



        /*if (filteredItems.isNotEmpty()){
                if (position == filteredItems[position]){
                    if (!clickedState[position]){
                        holder.cardSelectedItemInside.setCardBackgroundColor(mContext.getColor(R.color.light_blue))
                        clickedState[position] = true
                        selectedItems.add(position+1)
                    }else{
                        holder.cardSelectedItemInside.setCardBackgroundColor(mContext.getColor(R.color.text_white))
                        clickedState[position] = false
                        selectedItems.remove(position+1)
                    }
                }

        }*/

    }


    fun getSelectedItems(list: ArrayList<String>): ArrayList<String>{
        return list
    }


}