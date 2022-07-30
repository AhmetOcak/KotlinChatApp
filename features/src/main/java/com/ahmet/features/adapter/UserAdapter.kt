package com.ahmet.features.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.ahmet.domain.model.User
import com.ahmet.features.R
import com.ahmet.features.databinding.MessageBinding
import com.ahmet.features.utils.Constants

class UserAdapter(
    private var friends: MutableList<User>?,
    private val findNavController: NavController,
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(binding: MessageBinding) : RecyclerView.ViewHolder(binding.root) {
        val userImage: ImageView = binding.personImageCard
        val userName: TextView = binding.userName
        val unreadTime: TextView = binding.unreadTime
        val unreadMessage: TextView = binding.unreadMessage
    }

    // problem :(
    fun filterList(filterList: MutableList<User>?) {
        friends = filterList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(MessageBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userName.text = friends?.get(position)?.userName ?: ""
        holder.unreadMessage.text = "test message"
        holder.unreadTime.text = "test date"

        holder.itemView.setOnClickListener {
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

    override fun getItemCount(): Int = friends?.size ?: 0

}