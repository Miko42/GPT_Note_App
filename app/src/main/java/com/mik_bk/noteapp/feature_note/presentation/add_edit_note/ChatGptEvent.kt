package com.mik_bk.noteapp.feature_note.presentation.add_edit_note

sealed class ChatGptEvent{
    data object RequestGptResponse : ChatGptEvent()
    data class ResponseReceived(val response: String) : ChatGptEvent()
    data class ErrorOccurred(val errorMessage: String) : ChatGptEvent()
}