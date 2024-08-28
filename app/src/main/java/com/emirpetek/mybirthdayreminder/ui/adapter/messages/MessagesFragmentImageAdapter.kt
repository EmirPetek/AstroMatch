package com.emirpetek.mybirthdayreminder.ui.adapter.messages

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R

class MessagesFragmentImageAdapter(
    val mContext:Context,
    val imageUriList: ArrayList<*>
): RecyclerView.Adapter<MessagesFragmentImageAdapter.ImageHolder>() {

    inner class ImageHolder(view:View) : RecyclerView.ViewHolder(view){
        val imageView : ImageView = view.findViewById(R.id.imageViewCardMessageImage)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBarCardMessageImage)
        val layout: ConstraintLayout = view.findViewById(R.id.layoutMessageImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_message_image,parent,false)
        return ImageHolder(view)
    }

    override fun getItemCount(): Int {
        return imageUriList.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val item = imageUriList[position]
        holder.progressBar.visibility = View.VISIBLE
        Glide.with(mContext).load(item).centerCrop().into(holder.imageView)
        holder.progressBar.visibility = View.GONE

        holder.layout.setOnClickListener { it ->
            val bundle : Bundle = Bundle().apply {
                putInt("imageIndex",position)
                putStringArrayList("imageList",imageUriList as ArrayList<String>)
            }
            Navigation.findNavController(it).navigate(R.id.action_messagesFragment_to_showPhotosFragment,bundle)
        }
    }


}