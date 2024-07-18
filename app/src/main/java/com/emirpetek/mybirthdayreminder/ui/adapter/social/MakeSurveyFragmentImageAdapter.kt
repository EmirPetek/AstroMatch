package com.emirpetek.mybirthdayreminder.ui.adapter.social

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.ui.fragment.social.MakeSurveyFragment

class MakeSurveyFragmentImageAdapter(
    val mContext: Context,
    val imageUriList:ArrayList<Uri>
) :  RecyclerView.Adapter<MakeSurveyFragmentImageAdapter.ImageViewHolder>(){

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val imageViewSelectedPhoto : ImageView = view.findViewById(R.id.imageViewCardSelectedPhoto)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MakeSurveyFragmentImageAdapter.ImageViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_selected_photo,parent,false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MakeSurveyFragmentImageAdapter.ImageViewHolder, position: Int) {
        val pos = imageUriList[position]

        Glide
            .with(mContext)
            .load(pos)
            .into(holder.imageViewSelectedPhoto)
    }

    override fun getItemCount(): Int {
        return imageUriList.size
    }

}
