package com.ahmet.features.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahmet.core.databinding.IncomingMessagesBinding
import com.ahmet.features.utils.DateConverter

class IncomingMessageAdapter(private val friendMessages: List<Map<String, Any>>) :
    RecyclerView.Adapter<IncomingMessageAdapter.IncomingMesViewHolder>() {

    class IncomingMesViewHolder(binding: IncomingMessagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val message: TextView = binding.incomingMessage
        val date: TextView = binding.messageTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = IncomingMesViewHolder(
        IncomingMessagesBinding.inflate(LayoutInflater.from(parent.context))
    )

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: IncomingMesViewHolder, position: Int) {
        holder.message.text = friendMessages[position].getValue("message") as String
        holder.date.text =
            DateConverter.convertLongToTime((friendMessages[position].getValue("date") as com.google.firebase.Timestamp).seconds)

    }

    override fun getItemCount(): Int = friendMessages.size
}