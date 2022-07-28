package com.ahmet.domain.model

data class Message(
    val userMessage: MutableList<MutableMap<String, Any>>?,
    val friendMessage: MutableList<MutableMap<String, Any>>?
)