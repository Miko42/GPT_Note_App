package com.mik_bk.noteapp.feature_note.presentation.add_edit_note

import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.text.input.TextFieldValue

sealed class AddEditNoteEvent{
    data class EnteredTitle(val value: TextFieldValue): AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditNoteEvent()
    data class EnteredContent(val value: TextFieldValue): AddEditNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditNoteEvent()
    data class TextSelected(val selectedText: String) : AddEditNoteEvent()
    data class KeyboardHeightChanged(val height: Int): AddEditNoteEvent()
    data class ChangeColor(val color: Int): AddEditNoteEvent()
    data object ToggleInfoBoard : AddEditNoteEvent()
    object SaveNote: AddEditNoteEvent()
}

