package com.emirpetek.mybirthdayreminder.ui.adapter.social.main

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.processNextEventInCurrentThread
import javax.security.auth.login.LoginException

class SocialPostImageAdapter(
    val mContext: Context,
    val imgList: ArrayList<String>,
    val imgPath: String
): RecyclerView.Adapter<SocialPostImageAdapter.ImageHolder>() {

        inner class ImageHolder(view:View) : RecyclerView.ViewHolder(view){
            val imageView : ImageView = view.findViewById(R.id.imageViewSocialPostPhoto)
            val progressBar : ProgressBar = view.findViewById(R.id.progressBarSocialPostPhoto)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialPostImageAdapter.ImageHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_social_post_photo,parent,false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: SocialPostImageAdapter.ImageHolder, position: Int) {

        holder.progressBar.visibility = View.VISIBLE
        val storage = Firebase.storage.reference.child(imgList[position])
        storage.downloadUrl.addOnSuccessListener { uri ->
            Glide
                .with(mContext)
                .load(uri)
                .into(holder.imageView)
            holder.progressBar.visibility = View.GONE
        }








    }

    override fun getItemCount(): Int {
        return imgList.size
    }
}