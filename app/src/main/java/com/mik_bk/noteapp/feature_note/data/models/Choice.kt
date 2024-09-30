package com.mik_bk.noteapp.feature_note.data.models


import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    @SerialName("finish_reason")
    val finishReason: String?,
    @SerialName("index")
    val index: Int?,
    @Contextual
    @SerialName("logprobs")
    val logprobs: Any?,
    @SerialName("message")
    val message: Message?
)