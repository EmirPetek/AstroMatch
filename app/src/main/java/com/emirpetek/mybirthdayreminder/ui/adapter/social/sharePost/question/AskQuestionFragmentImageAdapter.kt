package com.emirpetek.mybirthdayreminder.ui.adapter.social.sharePost.question

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R

class AskQuestionFragmentImageAdapter(
    val mContext:Context,
    val imageUriList:ArrayList<Uri>
): RecyclerView.Adapter<AskQuestionFragmentImageAdapter.ImageViewHolder>(){

    inner class ImageViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageViewPhoto: ImageView = view.findViewById(R.id.imageViewCardSelectedPhoto)
        val imageViewDeletePhoto: ImageView = view.findViewById(R.id.imageViewCardSelectedPhotoDeleteImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_selected_photo,parent,false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageUriList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val pos = imageUriList[position]

        Glide
            .with(mContext)
            .load(pos)
            .into(holder.imageViewPhoto)

        holder.imageViewDeletePhoto.setOnClickListener {
            imageUriList.removeAt(position)
            notifyDataSetChanged()
        }

        //holder.imageViewPhoto.setImageURI(pos)

    }
}