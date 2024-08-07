package com.emirpetek.mybirthdayreminder.ui.adapter.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.UserGalleryPhoto

class ProfileFragmentProfileGalleryPhotosAdapter
    (
    val mContext: Context,
    val imgList: ArrayList<UserGalleryPhoto>,
): RecyclerView.Adapter<ProfileFragmentProfileGalleryPhotosAdapter.ImageHolder>() {

    inner class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageViewCardProfilePhotos)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBarCardProfilePhotos)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileFragmentProfileGalleryPhotosAdapter.ImageHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.card_profile_photos, parent, false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProfileFragmentProfileGalleryPhotosAdapter.ImageHolder,
        position: Int
    ) {

        holder.progressBar.visibility = View.VISIBLE
        Glide
            .with(mContext)
            .load(imgList[position].imageURL)
            .into(holder.imageView)
        holder.progressBar.visibility = View.GONE

        /*   //holder.progressBar.visibility = View.GONE
           val storage = Firebase.storage.reference.child(imgList[position])
           storage.downloadUrl.addOnSuccessListener { uri ->

               Log.e("imguri : ", uri.toString())
           }*/

        val bundle = Bundle()
        //bundle.putStringArrayList("imageList", imgList)


    }

    override fun getItemCount(): Int {
        return imgList.size
    }

}