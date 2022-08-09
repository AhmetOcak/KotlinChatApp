package com.ahmet.features.adapter

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahmet.features.databinding.FriendRequestBinding
import com.ahmet.features.utils.resource.ImpUserImage
import javax.inject.Inject

class FriendRequestsAdapter @Inject constructor(
    private val friendRequest: List<String>,
    private val sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<FriendRequestsAdapter.FriendRequestViewHolder>() {

    class FriendRequestViewHolder(binding: FriendRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val userImage: ImageView = binding.requestUserImage
        val userEmail: TextView = binding.requestUserEmail
        val acceptButton: ImageButton = binding.acceptFriendRequest
        val declineButton: ImageButton = binding.declineFriendRequest
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder =
        FriendRequestViewHolder(FriendRequestBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        setFriendImages(friendRequest[position], holder)
        holder.userEmail.text = friendRequest[position]

        holder.acceptButton.setOnClickListener {  }

        holder.declineButton.setOnClickListener {  }
    }

    override fun getItemCount(): Int = friendRequest.size

    private fun setFriendImages(email: String, holder: FriendRequestViewHolder) {
        val path = sharedPreferences.getString(email, null)
        if (path != null) {
            val bitmap = ImpUserImage.implementUserImage(path)
            holder.userImage.setImageBitmap(bitmap)
        } else {
            holder.userImage.setImageResource(com.ahmet.core.R.drawable.blank_profile_picture)
        }
    }
}