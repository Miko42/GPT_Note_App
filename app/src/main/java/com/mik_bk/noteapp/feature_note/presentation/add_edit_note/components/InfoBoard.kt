package com.mik_bk.noteapp.feature_note.presentation.add_edit_note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun InformationBox(
    noteColor: Int,
    darkerNoteColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16))
            .background(
                color = Color.Transparent,)
            .border(
                width = 3.dp,

                color = darkerNoteColor,
                shape = RoundedCornerShape(16)
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "How to use",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h5,
                color = Color.Black
            )
            Text(
                text = "1. Write prompt in title OR write in note text and select",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
            Text(
                text = "2. Use chat GPT button",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
        }
    }
}