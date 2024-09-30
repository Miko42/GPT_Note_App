package com.mik_bk.noteapp.feature_note.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompletionTokensDetails(
    @SerialName("reasoning_tokens")
    val reasoningTokens: Int?
)