package com.emirpetek.mybirthdayreminder.ui.adapter.messages

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.chat.Message
import com.emirpetek.mybirthdayreminder.data.entity.chat.MessageType
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateShareTime
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MessagesFragmentAdapter(
    val mContext: Context,
    val messageList: List<Message>,
    val anotherUserImage: String
): RecyclerView.Adapter<MessagesFragmentAdapter.MessageViewHolder>() {

    companion object{
        private var VIEW_TYPE_SENDER_RIGHT = 1
        private var VIEW_TYPE_RECEIVER_LEFT = 2
    }

    abstract class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view)


    inner class MessageViewLeftHolder(view : View): MessageViewHolder(view){
        val imageViewCardMessageLeftUserPhoto : ImageView = view.findViewById(R.id.imageViewCardMessageLeftUserPhoto)
        val textViewCardMessageLeftContent:TextView = view.findViewById(R.id.textViewCardMessageLeftContent)
        val textViewCardMessageLeftSendTime : TextView = view.findViewById(R.id.textViewCardMessageLeftSendTime)
        val layoutProfileImage : LinearLayout = view.findViewById(R.id.layoutMessageLeftPersonImage)
    }

    inner class MessageViewRightHolder(view : View): MessageViewHolder(view){
        val textViewCardMessageRightContent:TextView = view.findViewById(R.id.textViewCardMessageRightContent)
        val textViewCardMessageRightSendTime : TextView = view.findViewById(R.id.textViewCardMessageRightSendTime)
        val textViewCardMessageRightSeenState : TextView = view.findViewById(R.id.textViewCardMessageRightSeenState)
        val imageViewReadState : ImageView = view.findViewById(R.id.imageViewCardMessageRightReadState)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessagesFragmentAdapter.MessageViewHolder {
        if (viewType == VIEW_TYPE_SENDER_RIGHT){
            val view = LayoutInflater.from(mContext).inflate(R.layout.card_message_right, parent, false)
            return MessageViewRightHolder(view)
        }else{
            val view = LayoutInflater.from(mContext).inflate(R.layout.card_message_left, parent, false)
            return MessageViewLeftHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (messageList[position].senderID.equals(Firebase.auth.currentUser!!.uid)){ // mesajı gönderen ana kullanıcıysa
            return VIEW_TYPE_SENDER_RIGHT
        }else{
            return VIEW_TYPE_RECEIVER_LEFT
        }
    }

    override fun onBindViewHolder(holder: MessagesFragmentAdapter.MessageViewHolder, position: Int) {
        val msg = messageList[position]

        when(holder){

            is MessageViewLeftHolder -> {
                if (msg.messageType == MessageType.TEXT) holder.textViewCardMessageLeftContent.text = msg.messageText.toString()

                holder.textViewCardMessageLeftSendTime.text = CalculateShareTime(mContext).unixtsToDate(msg.timestamp.toString())
                Glide.with(mContext).load(anotherUserImage).circleCrop().into(holder.imageViewCardMessageLeftUserPhoto)
                holder.layoutProfileImage.setOnClickListener { it ->
                    Navigation.findNavController(it)
                        .navigate(R.id.action_messagesFragment_to_profileFragment, Bundle().apply { putString("userID",msg.senderID) })
                }
            }

            is MessageViewRightHolder -> {
                if (msg.messageType == MessageType.TEXT) holder.textViewCardMessageRightContent.text = msg.messageText.toString()
                holder.textViewCardMessageRightSendTime.text = CalculateShareTime(mContext).unixtsToDate(msg.timestamp.toString())
                holder.textViewCardMessageRightSeenState.text = msg.isRead.toString()
                if (msg.isRead)Glide.with(mContext).load(R.drawable.seen_msg_eye).into(holder.imageViewReadState)
                else Glide.with(mContext).load(R.drawable.not_seen_msg_eye).into(holder.imageViewReadState)
            }
        }
    }


}