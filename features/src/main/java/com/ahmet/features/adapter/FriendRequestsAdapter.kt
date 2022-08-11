package com.ahmet.features.adapter

import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.ahmet.data.usecase.firebase.AddUser
import com.ahmet.data.usecase.firebase.DeleteFriendRequest
import com.ahmet.data.usecase.firebase.GetCurrentUserEmail
import com.ahmet.data.usecase.messages.CreateMessageDoc
import com.ahmet.features.R
import com.ahmet.features.databinding.FriendRequestBinding
import com.ahmet.features.utils.helpers.ImpUserImage
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class FriendRequestsAdapter @Inject constructor(
    private val friendRequest: MutableList<String>,
    private val sharedPreferences: SharedPreferences,
    private val addUser: AddUser,
    private val deleteFriendRequest: DeleteFriendRequest,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val getCurrentUserEmail: GetCurrentUserEmail,
    private val createMessageDoc: CreateMessageDoc
) : RecyclerView.Adapter<FriendRequestsAdapter.FriendRequestViewHolder>() {

    private var resultMessageOne: String? = null
    private var resultMessageTwo: String? = null

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

        // if friend added successfully then we are deleting friend request
        holder.acceptButton.setOnClickListener {
            lifecycleScope.launch {
                resultMessageOne = addUser.addUser(friendRequest[position], getCurrentUserEmail.getCurrentUser()!!)
                resultMessageTwo = addUser.addUser(getCurrentUserEmail.getCurrentUser()!!, friendRequest[position])
                if (resultMessageOne == "Successful" && resultMessageTwo == "Successful" ) {
                    deleteFriendRequest.deleteRequest(
                        friendRequest[position],
                        getCurrentUserEmail.getCurrentUser()!!
                    )
                    createMessageDoc.createMessageDoc(getCurrentUserEmail.getCurrentUser()!!, friendRequest[position])
                    friendRequest.remove(friendRequest[position])
                    notifyItemRemoved(position)
                }
            }
        }

        holder.declineButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val resultMessage = deleteFriendRequest.deleteRequest(friendRequest[position], getCurrentUserEmail.getCurrentUser()!!)
                    if(resultMessage == "Successful") {
                        friendRequest.remove(friendRequest[position])
                        notifyItemRemoved(position)
                    }
                }catch (e: Exception) {
                    Log.e("decline friend request", e.toString())
                }
            }
        }
    }

    override fun getItemCount(): Int = friendRequest.size

    private fun setFriendImages(email: String, holder: FriendRequestViewHolder) {
        val path = sharedPreferences.getString(email, null)
        if (path != null) {
            val bitmap = ImpUserImage.implementUserImage(path)
            holder.userImage.setImageBitmap(bitmap)
        } else {
            holder.userImage.setImageResource(R.drawable.blank_profile_picture)
        }
    }
}