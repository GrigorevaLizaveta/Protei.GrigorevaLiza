package ru.protei.grigorevaed.compose.ui.notes

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.protei.grigorevaed.compose.domain.Note
import ru.protei.grigorevaed.compose.domain.NotesUseCase
import javax.inject.Inject

@HiltViewModel
class NotesVeiwModel @Inject constructor(private val notesUseCase: NotesUseCase):ViewModel()
{
    val notes = MutableStateFlow<List<Note>>(emptyList())

    init {
        viewModelScope.launch {
            //notesUseCase.fillWithInitialNotes(emptyList())
            notesUseCase.loadRemoteNotes()
        }
        viewModelScope.launch {
            notesUseCase.notesFlow()
                .collect{
                        note ->
                    notes.value = note
                }
        }
    }

    var selected = mutableStateOf<Note?>(null )

    fun onNoteChange(title: String, text: String){
        selected.value!!.title = title
        selected.value!!.text = text
    }

    fun saveNote(note: Note){
        viewModelScope.launch {
            notesUseCase.save(selected.value!!)
        }
        onEditComplete()
    }

    fun onEditComplete(){
        selected.value = null
    }

}