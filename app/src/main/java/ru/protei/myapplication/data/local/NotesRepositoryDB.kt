package ru.protei.myapplication.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.protei.grigorevaed.compose.domain.Note
import ru.protei.grigorevaed.compose.domain.NotesRepository

class NotesRepositoryDB(
    private val dao: NotesDao
):
    NotesRepository {

    override suspend fun loadAllNotes(): List<Note> = withContext(Dispatchers.IO) {
        return@withContext dao.all()
    }

    override fun loadALlNotesFlow(): Flow<List<Note>> = dao.allFlow()

    override suspend fun clearDatabase() = withContext(Dispatchers.IO){
        dao.deleteAll()
    }
    override suspend fun fillDatabase(notes: List<Note>) {
        notes.forEach{
            dao.insert(it)
        }
    }
    override suspend fun add(note: Note): Unit = withContext(Dispatchers.IO) {
        dao.insert(note)
    }
    override suspend fun update(note: Note) = withContext(Dispatchers.IO){
        dao.update(note)
    }

    override suspend fun byRemoteId(remoteId: Long): Note? {
        return withContext(Dispatchers.IO) {
            dao.byRemoteId(remoteId)
        }
    }
}

