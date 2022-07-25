package com.ahmet.domain.model

data class Message(
    val userMessage: List<Map<String, Any>>?,
    val friendMessage: List<Map<String, Any>>?
)