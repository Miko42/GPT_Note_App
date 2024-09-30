package com.mik_bk.noteapp.feature_note.presentation.add_edit_note


import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mik_bk.noteapp.R
import com.mik_bk.noteapp.feature_note.domain.model.Note
import com.mik_bk.noteapp.feature_note.presentation.add_edit_note.components.InformationBox
import com.mik_bk.noteapp.feature_note.presentation.add_edit_note.components.ObserveKeyboard
import com.mik_bk.noteapp.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    val keyboardHeight by viewModel.keyboardHeight
    ObserveKeyboard(viewModel)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }
    val darkerNoteColor = noteBackgroundAnimatable.value.copy(
        red = noteBackgroundAnimatable.value.red * 0.5f,
        green = noteBackgroundAnimatable.value.green * 0.5f,
        blue = noteBackgroundAnimatable.value.blue * 0.5f
    )

    val isInfoBoardVisible by viewModel.isInfoBoardVisible.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }

                else -> {}
            }
        }

    }

    Scaffold(
        floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(AddEditNoteEvent.SaveNote)
                    },
                        backgroundColor = noteBackgroundAnimatable.value.copy(
                        red = noteBackgroundAnimatable.value.red * 1.4f,
                        green = noteBackgroundAnimatable.value.green * 1.4f,
                        blue = noteBackgroundAnimatable.value.blue * 1.4f,
                        alpha = 0.8f
                    ),
                        shape = RoundedCornerShape(16),
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
                        modifier = Modifier
                        .padding(bottom=keyboardHeight.dp)
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        tint = darkerNoteColor,
                        contentDescription = "Save note"
                    )
                }

        },
        scaffoldState = scaffoldState
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(innerPadding)
                .padding(start = 20.dp, end = 20.dp, top = 16.dp)

        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .height(64.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ){
                Column (
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                    ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Note.noteColors.take(Note.noteColors.size / 2).forEach { color ->
                            val colorInt = color.toArgb()
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .shadow(3.dp, RoundedCornerShape(16))
                                    .clip(RoundedCornerShape(16))
                                    .background(color)
                                    .border(
                                        width = 1.dp,
                                        color = if (viewModel.noteColor.value == colorInt) {
                                            Color.Black
                                        } else Color.Transparent,
                                        shape = RoundedCornerShape(16)
                                    )
                                    .clickable {
                                        scope.launch {
                                            noteBackgroundAnimatable.animateTo(
                                                targetValue = Color(colorInt),
                                                animationSpec = tween(
                                                    durationMillis = 500
                                                )
                                            )
                                        }
                                        viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                                    }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Note.noteColors.drop(Note.noteColors.size / 2).forEach { color ->
                            val colorInt = color.toArgb()
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(16))
                                    .background(color)
                                    .border(
                                        width = 1.dp,
                                        color = if (viewModel.noteColor.value == colorInt) {
                                            Color.Black
                                        } else Color.Transparent,
                                        shape = RoundedCornerShape(16)
                                    )
                                    .clickable {
                                        scope.launch {
                                            noteBackgroundAnimatable.animateTo(
                                                targetValue = Color(colorInt),
                                                animationSpec = tween(
                                                    durationMillis = 500
                                                )
                                            )
                                        }
                                        viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                                    }
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                    ) {
                    Row (
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ){
                        Button(
                            onClick = {
                                viewModel.onChatGPTEvent(ChatGptEvent.RequestGptResponse)
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = noteBackgroundAnimatable.value.copy(),
                            ),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.chatgpt_icon),
                                contentDescription = "Info",
                                tint = darkerNoteColor,
                                modifier = Modifier
                                    .size(50.dp)
                            )
                        }
                        Button(
                            onClick = {
                                viewModel.onEvent(AddEditNoteEvent.ToggleInfoBoard)
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = noteBackgroundAnimatable.value.copy(),
                            ),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize(),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Info",
                                tint = darkerNoteColor,
                                modifier = Modifier
                                    .size(50.dp)
                            )
                        }
                    }
                }
            }

            if (isInfoBoardVisible) {
                Spacer(modifier = Modifier.height(8.dp))
                InformationBox(noteColor =noteColor, darkerNoteColor =darkerNoteColor)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(noteBackgroundAnimatable.value)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                TransparentHintTextField(
                    value =  titleState.textFieldValue,
                    hint = titleState.hint,
                    onValueChange = {
                        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                    },
                    onSelectionChange = {
                        viewModel.onEvent(AddEditNoteEvent.TextSelected(it))
                    },
                    isHintVisible = titleState.isHintVisible,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h5,
                )
                Spacer(modifier = Modifier.height(16.dp))
                TransparentHintTextField(
                    value = contentState.textFieldValue,
                    hint = contentState.hint,
                    onValueChange = {
                        viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                    },
                    onSelectionChange = {
                        viewModel.onEvent(AddEditNoteEvent.TextSelected(it))
                    },
                    isHintVisible = contentState.isHintVisible,
                    textStyle = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom=keyboardHeight.dp)
                )
            }
        }
    }
}