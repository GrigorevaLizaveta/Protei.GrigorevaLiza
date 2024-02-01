package ru.protei.grigorevaed.compose.domain

import kotlinx.coroutines.flow.Flow
import ru.protei.myapplication.data.local.NotesRepositoryDB
import ru.protei.myapplication.data.remote.GitHubIssue
import ru.protei.myapplication.data.remote.NotesGitHubRepository

class NotesUseCase(
    private val notesRepo: NotesRepositoryDB,
    private val notesApi: NotesGitHubRepository
){

//    suspend fun fillWithInitialNotes(initialNotes: List<Note>){
//        notesRepo.clearDatabase()
//        notesRepo.fillDatabase(initialNotes)
//    }

    suspend fun loadRemoteNotes() {
        val remoteNotes = notesApi.list()
        remoteNotes.forEach { remoteNote ->
            val localNote = remoteNote.remoteId?.let { notesRepo.byRemoteId(it) }
            if (localNote == null) {
                notesApi.add(remoteNote)
            } else {
                remoteNote.id = localNote.id // сохраняем локальный id в полученной с сервера заметке
                notesApi.update(remoteNote)
            }
        }
    }


    fun notesFlow(): Flow<List<Note>> {
        var flow: Flow<List<Note>>
        return notesRepo.loadALlNotesFlow()
        }

    suspend fun save(note: Note) {
           // notesRepo.add(note)
        // Добавление или обновление заметки в локальной базе
        if (note.id == null) {
            val newNoteId = notesRepo.add(note) // добавление новой заметки
            // Добавление новой заметки на сервер
            val remoteNoteId = notesApi.add(note)
            // Обновление внешнего идентификатора в локальной базе
            //notesRepo.update(remoteNote)
        } else {
            notesRepo.update(note) // обновление существующей заметки
            notesApi.update(note) // обновление заметки на сервере
        }
    }

    suspend fun add(note: Note) {
        return notesRepo.add(note)
    }

    suspend fun update(note: Note) {
        notesRepo.update(note)
    }
}