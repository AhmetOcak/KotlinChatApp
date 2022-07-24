package com.ahmet.features.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahmet.core.databinding.OutgoingMessagesBinding
import com.ahmet.features.utils.DateConverter

class OutgoingMessageAdapter(private val userMessages: List<Map<String, Any>>) :
    RecyclerView.Adapter<OutgoingMessageAdapter.OutgoingMesViewHolder>() {

    class OutgoingMesViewHolder(binding: OutgoingMessagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val message: TextView = binding.outgoingMessage
        val date: TextView = binding.messageTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutgoingMesViewHolder =
        OutgoingMesViewHolder(OutgoingMessagesBinding.inflate(LayoutInflater.from(parent.context)))

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: OutgoingMesViewHolder, position: Int) {
        holder.message.text = userMessages[position].getValue("message") as String
        holder.date.text =
            DateConverter.convertLongToTime((userMessages[position].getValue("date") as com.google.firebase.Timestamp).seconds)
    }

    override fun getItemCount(): Int = userMessages.size
}