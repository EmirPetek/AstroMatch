package com.emirpetek.mybirthdayreminder.ui.adapter.profile.userGalleryPhotos

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R

class ShareProfileGalleryPhotosFragmentAdapter (
    val mContext: Context,
val imageList: ArrayList<Uri>
): RecyclerView.Adapter<ShareProfileGalleryPhotosFragmentAdapter.CardImage>(){

    var currentPosition = 0

    inner class CardImage(view: View) : RecyclerView.ViewHolder(view){
        val imageView : ImageView = view.findViewById(R.id.imageViewCardShowPhoto)
        val imageViewDelete : ImageView = view.findViewById(R.id.imageViewCardShowPhotoDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardImage {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_show_photo,parent,false)
        return CardImage(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: CardImage, position: Int) {
        val item = imageList[position]
        Glide
            .with(mContext)
            .load(item)
            .into(holder.imageView)



        holder.imageViewDelete.visibility = View.VISIBLE
        holder.imageViewDelete.setOnClickListener { view ->
            if (itemCount == 1){
                Navigation.findNavController(view).popBackStack()
            }
            imageList.remove(item)
            notifyDataSetChanged()
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

        currentPosition = holder.absoluteAdapterPosition

    }

    fun getPosition(): Int{
        return currentPosition
    }
}