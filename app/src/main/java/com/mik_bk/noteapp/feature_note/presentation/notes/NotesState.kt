package com.mik_bk.noteapp.feature_note.presentation.notes

import com.mik_bk.noteapp.feature_note.domain.model.Note
import com.mik_bk.noteapp.feature_note.util.NoteOrder
import com.mik_bk.noteapp.feature_note.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
