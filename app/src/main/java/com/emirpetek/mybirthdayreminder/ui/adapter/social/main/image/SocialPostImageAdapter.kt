package com.emirpetek.mybirthdayreminder.ui.adapter.social.main.image

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

class SocialPostImageAdapter(
    val mContext: Context,
    val imgList: ArrayList<String>,
    val imgPath: String,
    val fragmentName:String
): RecyclerView.Adapter<SocialPostImageAdapter.ImageHolder>() {

        inner class ImageHolder(view:View) : RecyclerView.ViewHolder(view){
            val imageView : ImageView = view.findViewById(R.id.imageViewSocialPostPhoto)
            val progressBar : ProgressBar = view.findViewById(R.id.progressBarSocialPostPhoto)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_social_post_photo,parent,false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {

        holder.progressBar.visibility = View.VISIBLE
        Glide
            .with(mContext)
            .load(imgList[position])
            .into(holder.imageView)
        holder.progressBar.visibility = View.GONE

     /*   //holder.progressBar.visibility = View.GONE
        val storage = Firebase.storage.reference.child(imgList[position])
        storage.downloadUrl.addOnSuccessListener { uri ->

            Log.e("imguri : ", uri.toString())
        }*/

        val bundle = Bundle()
        bundle.putStringArrayList("imageList",imgList)

        holder.imageView.setOnClickListener { view ->

            if (fragmentName.equals("QuestionAnswersFragment")){
                Navigation.findNavController(view).navigate(R.id.action_questionAnswersFragment_to_showPhotosFragment,bundle)
            }else if (fragmentName.equals("SocialPostAdapter")){
                Navigation.findNavController(view).navigate(R.id.action_socialFragment_to_showPhotosFragment,bundle)
            }


        }


    }

    override fun getItemCount(): Int {
        return imgList.size
    }
}