package com.mik_bk.noteapp.feature_note.presentation.add_edit_note.components

import android.view.View
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mik_bk.noteapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.mik_bk.noteapp.feature_note.presentation.add_edit_note.AddEditNoteViewModel

@Composable
fun ObserveKeyboard(viewModel: AddEditNoteViewModel) {
    val density = LocalDensity.current

    AndroidView(
        factory = { ctx ->
            View(ctx).apply {
                viewTreeObserver.addOnGlobalLayoutListener {
                    val insets = ViewCompat.getRootWindowInsets(this)

                    val isKeyboardVisible = insets?.isVisible(WindowInsetsCompat.Type.ime()) == true
                    val navigationBarHeight = insets?.getInsets(WindowInsetsCompat.Type.navigationBars())?.bottom ?: 0

                    if (insets != null) {
                        val keyboardHeight = if (isKeyboardVisible) {
                            (insets.getInsets(WindowInsetsCompat.Type.ime()).bottom - navigationBarHeight) / density.density.toInt()
                        } else {
                            0
                        }
                        viewModel.onEvent(AddEditNoteEvent.KeyboardHeightChanged(keyboardHeight))
                    }
                }
            }
        },
        update = {}
    )
}
