package com.ahmet.features.adapter

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.ahmet.data.utils.EditEmail
import com.ahmet.domain.model.User
import com.ahmet.features.R
import com.ahmet.features.databinding.MessageBinding
import com.ahmet.features.utils.Constants
import com.ahmet.features.utils.DateConverter
import com.ahmet.features.utils.resource.ImpUserImage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

class UserAdapter(
    private var friends: MutableList<User>?,
    private val findNavController: NavController,
    private val sharedPreferences: SharedPreferences,
    private var currentData: List<DocumentSnapshot>,
    private var unreadMessages: List<Int>,
    private var callback: (position: Int) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(binding: MessageBinding) : RecyclerView.ViewHolder(binding.root) {
        val userImage: ImageView = binding.personImageCard
        val userName: TextView = binding.userName
        val unreadTime: TextView = binding.unreadTime
        val messagePreview: TextView = binding.messagePreview
        val isThereUnreadMessage: TextView = binding.isThereMessage
    }

    fun filterList(filterList: MutableList<User>?) {
        friends = filterList
        notifyDataSetChanged()
    }

    fun updateMessageData(updatedData: List<DocumentSnapshot>) {
        currentData = updatedData
    }

    fun takeUnreadMessages(unreadMessageCount: List<Int>) {
        // if not 0 then there are unread messages
        unreadMessages = unreadMessageCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(MessageBinding.inflate(LayoutInflater.from(parent.context)))

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        if(friends?.size ?: 0 == currentData.size) { // to avoid mistakes | possible error -> friends and currentData list may not be updated at the same time
            val data =
                if (currentData[position].data?.get(EditEmail.removeDot(friends?.get(position)!!.emailAddress)) == null) {
                    friends?.reverse() // we are editing list order
                    currentData[position].data?.get(EditEmail.removeDot(friends?.get(position)!!.emailAddress)) as MutableList<MutableMap<String, Any>>
                } else {
                    currentData[position].data?.get(EditEmail.removeDot(friends?.get(position)!!.emailAddress)) as MutableList<MutableMap<String, Any>>
                }

            if (data.size != 0) {
                holder.userName.text = friends?.get(position)?.userName ?: ""
                holder.messagePreview.text = data[data.size - 1]["message"].toString()
                holder.unreadTime.text = DateConverter.convertLongToTime((data[data.size - 1]["date"] as Timestamp).seconds)
                if(!unreadMessages.isNullOrEmpty()) {
                    if(unreadMessages[position] != 0) {
                        holder.isThereUnreadMessage.text = "new messages"
                        holder.isThereUnreadMessage.visibility = View.VISIBLE
                    }
                }else {
                    holder.isThereUnreadMessage.text = ""
                    holder.isThereUnreadMessage.visibility = View.GONE
                }
            } else {
                holder.userName.text = friends?.get(position)?.userName ?: ""
                holder.messagePreview.text = ""
                holder.unreadTime.text = ""
                holder.isThereUnreadMessage.text = ""
                holder.isThereUnreadMessage.visibility = View.GONE
            }

            setFriendImages(friends?.get(position)?.emailAddress ?: "", holder)

            holder.itemView.setOnClickListener {
                callback(position) // if user read the messages, then we delete "new messages" alert

                findNavController.navigate(
                    R.id.action_messagesFragment_to_chatFragment,
                    Bundle().apply {
                        putString(
                            Constants.USER_FRIEND_ARG_KEY,
                            friends?.get(position)!!.emailAddress
                        )
                        putString(
                            Constants.USER_FRIEND_NAME_ARG_KEY,
                            friends!![position].userName
                        )
                    }
                )
            }
        }
    }

    override fun getItemCount(): Int = friends?.size ?: 0

    private fun setFriendImages(email: String, holder: UserViewHolder) {
        val path = sharedPreferences.getString(email, null)
        if (path != null) {
            val bitmap = ImpUserImage.implementUserImage(path)
            holder.userImage.setImageBitmap(bitmap)
        } else {
            holder.userImage.setImageResource(com.ahmet.core.R.drawable.blank_profile_picture)
        }
    }
}