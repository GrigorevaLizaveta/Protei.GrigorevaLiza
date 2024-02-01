package ru.protei.grigorevaed.compose.domain

import android.annotation.SuppressLint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.protei.myapplication.data.local.NotesRepositoryDB
import ru.protei.myapplication.data.remote.NotesGitHubRepository

class NotesUseCase(
    private val notesRepo: NotesRepositoryDB,
    private val notesApi: NotesGitHubRepository
){
    suspend fun fillWithInitialNotes(initialNotes: List<Note>){
        notesRepo.clearDatabase()
        notesRepo.fillDatabase(initialNotes)
    }

    suspend fun loadRemoteNotes(){
        val listNotes: List<Note> = notesApi.list()
        listNotes.forEach {
            updateLocalDB(it)
        }
    }

    fun notesFlow(): Flow<List<Note>> {
        var flow: Flow<List<Note>>
        return notesRepo.loadALlNotesFlow()
        }

    suspend fun save(note: Note)= withContext(Dispatchers.IO){

        if (note.id == null){
            notesRepo.add(note)
        }
        else{
            notesRepo.update(note)
        }

        if(note.id == null){
            val remoteID = notesApi.add(note)
            val addedNote = notesRepo.byEquals(note.title, note.text)
            if (addedNote != null) {
                if (remoteID != null) {
                    addedNote.remoteId = remoteID
                }
                notesRepo.update(addedNote)
            }
            else return@withContext
        }
        else{
            notesApi.update(note)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun updateLocalDB(note: Note) = withContext(Dispatchers.IO){
        val localList: List<Note> = notesRepo.loadAllNotes()
        if (localList.isEmpty()){
            notesRepo.add(note)
            return@withContext
        }
        else{
            val localNote = notesRepo.byRemoteId(note.remoteId)
            if(localNote == null){
                notesRepo.add(note)
                return@withContext
            }
            else{
                note.id = localNote.id
                notesRepo.update(note)
                return@withContext
            }
        }
    }
}