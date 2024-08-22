package com.emirpetek.mybirthdayreminder.ui.adapter.chats

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.chat.Chat
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateShareTime
import com.emirpetek.mybirthdayreminder.viewmodel.chats.ChatListViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatListFragmentAdapter(
    val mContext: Context,
    var chatList: List<Chat>,
    val viewModel: ChatListViewModel,
    val viewLifecycleOwner: LifecycleOwner,
    val progressBarChatListChats: ProgressBar,
    ): RecyclerView.Adapter<ChatListFragmentAdapter.ChatViewHolder>() {

        val ownUserID = Firebase.auth.currentUser!!.uid
        var anotherUserID:String = ""
        private var itemCountBound = 0


    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val cardView : CardView = view.findViewById(R.id.cardViewChatListItem)
        val fullname : TextView = view.findViewById(R.id.textViewCardChatListMessageUserFullname)
        val messageContent : TextView = view.findViewById(R.id.textViewCardChatListMessageContent)
        val messageTime : TextView = view.findViewById(R.id.textViewCardChatListMessageTime)
        val userImg : ImageView = view.findViewById(R.id.imageViewCardChatListUserImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.card_chat_list,parent,false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = chatList[position]

        holder.messageContent.text = item.lastMessage
        holder.messageTime.text = CalculateShareTime(mContext).unixtsToDate(item.lastMessageTimestamp.toString())

        val firstParticipant = item.participants.entries.toList()[0].key.toString()
        val secondParticipant = item.participants.entries.toList()[1].key.toString()
        if (firstParticipant.equals(ownUserID)) anotherUserID = secondParticipant
        if (secondParticipant.equals(ownUserID)) anotherUserID = firstParticipant


        holder.fullname.text = item.user?.fullname
        Glide.with(mContext).load(item.user?.profile_img).circleCrop().into(holder.userImg)

        holder.cardView.setOnClickListener { it ->
            val bundle : Bundle = Bundle().apply {
                putString("anotherUserID",item.user?.userID)
            }
            Navigation.findNavController(it).navigate(R.id.action_chatListFragment_to_messagesFragment,bundle)
        }


        itemCountBound++
        if (itemCountBound == chatList.size) progressBarChatListChats.visibility = View.GONE


       /* viewModel.getUserData(anotherUserID)

        viewModel.userData.observeForever{ a ->
            //Log.e("ksdjflksd","${a.userID} vee ${a.fullname} another user id: $anotherUserID")
            holder.fullname.text = a.fullname
            Glide.with(mContext).load(a.profile_img).circleCrop().into(holder.userImg)
        }
        Log.e("finish: ", System.currentTimeMillis().toString())
*/


     /*   runBlocking {

            viewModel.getUserData(anotherUserID)

            viewModel.userData.observe(viewLifecycleOwner,Observer{ a ->
                if (a.userID.equals(anotherUserID))
                Log.e("ksdjflksd","${a.userID} vee ${a.fullname} another user id: $anotherUserID")
                holder.fullname.text = a.fullname
                Glide.with(mContext).load(a.profile_img).circleCrop().into(holder.userImg)

            })
        }*/

      /*  runBlocking {
            async {
                viewModel.getUserData(anotherUserID)

                viewModel.userData.observe(viewLifecycleOwner,Observer{ a ->
                    Log.e("ksdjflksd","${a.userID} vee ${a.fullname} another user id: $anotherUserID")
                    holder.fullname.text = a.fullname
                    Glide.with(mContext).load(a.profile_img).circleCrop().into(holder.userImg)

                })
            }
        }*/



        /*viewModel.userData.observe(, Observer { user ->
            Log.e("ksdjflksd","${user.userID} vee ${user.fullname} another user id: $anotherUserID")
        holder.fullname.text = user.fullname
        Glide.with(mContext).load(user.profile_img).circleCrop().into(holder.userImg)
        })*/

    }


}