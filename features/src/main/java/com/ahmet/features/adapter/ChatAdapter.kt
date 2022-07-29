package com.ahmet.features.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ahmet.core.databinding.IncomingMessagesBinding
import com.ahmet.core.databinding.OutgoingMessagesBinding
import com.ahmet.features.utils.DateConverter
import com.google.firebase.Timestamp

class ChatAdapter(private val chat: MutableList<MutableMap<String, Any>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_USER = 1
    private val VIEW_TYPE_OTHER = 0

    class UserViewHolder(binding: OutgoingMessagesBinding) : RecyclerView.ViewHolder(binding.root) {
        val message: TextView = binding.outgoingMessage
        val date: TextView = binding.messageTime
    }

    class OtherViewHolder(binding: IncomingMessagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val message: TextView = binding.incomingMessage
        val date: TextView = binding.messageTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            UserViewHolder(
                OutgoingMessagesBinding.inflate(
                    LayoutInflater.from(parent.context)
                )
            )
        } else {
            OtherViewHolder(
                IncomingMessagesBinding.inflate(
                    LayoutInflater.from(parent.context)
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (chat[position]["isMe"] == true) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_OTHER
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_USER) {
            (holder as UserViewHolder).message.text = chat[position].getValue("message") as String
            holder.date.text =
                DateConverter.convertLongToTime((chat[position].getValue("date") as Timestamp).seconds)
        } else if (holder.itemViewType == VIEW_TYPE_OTHER) {
            (holder as OtherViewHolder).message.text = chat[position].getValue("message") as String
            holder.date.text =
                DateConverter.convertLongToTime((chat[position].getValue("date") as Timestamp).seconds)
        }
    }

    override fun getItemCount(): Int = chat.size
}