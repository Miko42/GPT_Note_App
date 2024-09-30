package com.mik_bk.noteapp.feature_note.presentation.add_edit_note

import androidx.compose.ui.text.input.TextFieldValue

data class NoteTextFieldState(
    val textFieldValue: TextFieldValue = TextFieldValue(),
    val hint: String = "",
    val isHintVisible: Boolean = true
)