package com.mik_bk.noteapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mik_bk.noteapp.ui.theme.*

@Entity
data class Note(
val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(RedOrange, RedPink, Yellow, Green,
            Violet, DeepPeach,  LightGreen, Crayola  )
    }
}

class InvalidNoteException(message: String): Exception(message)