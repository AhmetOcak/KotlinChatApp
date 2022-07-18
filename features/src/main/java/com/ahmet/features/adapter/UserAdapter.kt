package com.ahmet.features.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.ahmet.domain.model.User
import com.ahmet.features.R
import com.ahmet.features.databinding.MessageBinding

class UserAdapter(
    private val friends: MutableList<User>?,
    private var findNavController: NavController
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(binding: MessageBinding) : RecyclerView.ViewHolder(binding.root) {
        val userImage: ImageView = binding.personImageCard
        val userName: TextView = binding.userName
        val unreadTime: TextView = binding.unreadTime
        val unreadMessage: TextView = binding.unreadMessage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder(MessageBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.userName.text = friends?.get(position)?.userName ?: ""
        holder.unreadMessage.text = "test message"
        holder.unreadTime.text = "test date"

        holder.itemView.setOnClickListener {
            findNavController.navigate(
                R.id.action_messagesFragment_to_chatFragment
            )
        }
    }

    override fun getItemCount(): Int = friends?.size ?: 0

}