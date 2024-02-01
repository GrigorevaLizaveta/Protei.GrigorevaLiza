package ru.protei.grigorevaed.compose.ui.notes

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.protei.grigorevaed.compose.domain.Note
import ru.protei.grigorevaed.compose.domain.NotesUseCase

class NotesVeiwModel(private val notesUseCase: NotesUseCase):ViewModel()
{

    val notesli = mutableStateListOf<Note>(
        Note("Заметка 1","Текст 1"),
        Note("Заметка 2","Текст 2"),
        Note("Заметка 3","Текст 3"),
    )


    val notes = MutableStateFlow<List<Note>>(notesli)


    init {
        viewModelScope.launch {
          //  notesUseCase.fillWithInitialNotes(notes.value)
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