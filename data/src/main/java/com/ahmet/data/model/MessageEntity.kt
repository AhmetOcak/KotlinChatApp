package com.ahmet.data.model

data class MessageEntity(
    val userMessage: MutableList<MutableMap<String, Any>>?,
    val friendMessage: MutableList<MutableMap<String, Any>>?
)