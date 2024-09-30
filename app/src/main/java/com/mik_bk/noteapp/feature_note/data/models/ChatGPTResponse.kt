package com.mik_bk.noteapp.feature_note.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatGPTResponse(
    @SerialName("choices")
    val choices: List<Choice?>?,
    @SerialName("created")
    val created: Int?,
    @SerialName("id")
    val id: String?,
    @SerialName("model")
    val model: String?,
    @SerialName("object")
    val objectX: String?,
    @SerialName("usage")
    val usage: Usage?
)