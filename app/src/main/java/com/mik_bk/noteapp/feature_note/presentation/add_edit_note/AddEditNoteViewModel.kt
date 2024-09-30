package com.mik_bk.noteapp.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mik_bk.noteapp.feature_note.data.network.GptCommunicator
import com.mik_bk.noteapp.feature_note.domain.model.InvalidNoteException
import com.mik_bk.noteapp.feature_note.domain.model.Note
import com.mik_bk.noteapp.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        textFieldValue = TextFieldValue(),
        hint = "Enter title..."
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(
        textFieldValue = TextFieldValue(),
        hint = "Enter some content"
    ))
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _selectedText = mutableStateOf("")
    private val selectedText: State<String> = _selectedText

    private val _keyboardHeight = mutableStateOf(0)
    val keyboardHeight: State<Int> = _keyboardHeight

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    private val gpt = GptCommunicator()
    private val _chatGptResponse = mutableStateOf("")

    private val _isInfoBoardVisible = MutableStateFlow(false)
    val isInfoBoardVisible: StateFlow<Boolean> = _isInfoBoardVisible


    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if(noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            textFieldValue = TextFieldValue(note.title),
                            isHintVisible = false
                        )
                        _noteContent.value = _noteContent.value.copy(
                            textFieldValue = TextFieldValue(note.content),
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when(event){
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    textFieldValue = event.value
                )
            }
            is AddEditNoteEvent.TextSelected -> {
                _selectedText.value = event.selectedText
            }
            is AddEditNoteEvent.KeyboardHeightChanged -> {
                _keyboardHeight.value = event.height
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.textFieldValue.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(
                    textFieldValue = event.value
                )
            }
            is AddEditNoteEvent.ChangeContentFocus -> {
                _noteContent.value = _noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            _noteContent.value.textFieldValue.text.isBlank()
                )
            }
            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.textFieldValue.text,
                                content = noteContent.value.textFieldValue.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch(e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }

            is AddEditNoteEvent.ToggleInfoBoard ->{
                viewModelScope.launch {
                    _isInfoBoardVisible.value = !_isInfoBoardVisible.value
                }
            }
        }
    }

    fun onChatGPTEvent(event: ChatGptEvent)
    {
        when(event)
        {
            is ChatGptEvent.RequestGptResponse -> {
                viewModelScope.launch {
                    val prompt = selectedText.value.ifBlank { noteTitle.value.textFieldValue.text }
                    try {

                        val result = gpt.fetchGptResponse(prompt = prompt)

                        if (result.isFailure) {
                            onChatGPTEvent(ChatGptEvent.ErrorOccurred("Failed to get response"))
                            return@launch
                        }
                        val mess = result.getOrNull()?.choices?.firstOrNull()?.message?.content

                        if (mess != null) {
                            _chatGptResponse.value = mess
                            onChatGPTEvent(ChatGptEvent.ResponseReceived(mess))
                        } else {
                            onChatGPTEvent(ChatGptEvent.ErrorOccurred("Empty response from ChatGPT"))
                        }
                    } catch (e: Exception) {
                        onChatGPTEvent(ChatGptEvent.ErrorOccurred(e.message ?: "Unknown error"))
                    }
                }
            }
            is ChatGptEvent.ErrorOccurred -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowSnackbar(event.errorMessage))
                }
            }
            is ChatGptEvent.ResponseReceived -> {
                viewModelScope.launch {
                    _noteContent.value = _noteContent.value.copy(
                        textFieldValue = TextFieldValue(
                            text = if (_noteContent.value.textFieldValue.text.isBlank()) {
                                event.response
                            } else {
                                _noteContent.value.textFieldValue.text + "\n\n" + event.response
                            }
                        ),
                        isHintVisible = false
                    )
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data object SaveNote: UiEvent()
    }
}