package com.mik_bk.noteapp.feature_note.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Usage(
    @SerialName("completion_tokens")
    val completionTokens: Int?,
    @SerialName("completion_tokens_details")
    val completionTokensDetails: CompletionTokensDetails?,
    @SerialName("prompt_tokens")
    val promptTokens: Int?,
    @SerialName("total_tokens")
    val totalTokens: Int?
)