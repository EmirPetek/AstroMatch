package com.emirpetek.mybirthdayreminder.ui.adapter.profile.userGalleryPhotos

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

class ProfileGalleryViewAllAdapter
    (val mContext: Context,
     val imageList: ArrayList<UserGalleryPhoto>
): RecyclerView.Adapter<ProfileGalleryViewAllAdapter.CardImage>() {

    inner class CardImage(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageViewCardProfileGalleryPhoto)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBarCardProfileGalleryPhoto)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardImage {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.card_profile_gallery_photo, parent, false)
        return CardImage(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: CardImage, position: Int) {
        val item = imageList[position]
        holder.progressBar.visibility = View.VISIBLE
        Glide
            .with(mContext)
            .load(item.imageURL)
            .into(holder.imageView)
        holder.progressBar.visibility = View.GONE


        val bundle = Bundle()
        bundle.putInt("imageIndex", position)
        bundle.putStringArrayList("imageList",getImageUrlList())

        holder.imageView.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.action_profileGalleryViewAllFragment_to_showPhotosFragment,bundle)
        }


        //holder.progressBar.visibility = View.GONE
        /*  val storage = Firebase.storage.reference.child(item)
          storage.downloadUrl.addOnSuccessListener { uri ->
              Glide
                  .with(mContext)
                  .load(uri)
                  .into(holder.imageView)
              //holder.progressBar.visibility = View.GONE
          }*/

    }

    fun getImageUrlList(): ArrayList<String> {
        val urlList : ArrayList<String>  = arrayListOf()
        for (i in imageList) urlList.add(i.imageURL)
        return urlList
    }
}