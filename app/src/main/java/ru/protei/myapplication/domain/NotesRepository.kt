package ru.protei.grigorevaed.compose.domain

import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun loadAllNotes(): List<Note>

    fun loadALlNotesFlow(): Flow<List<Note>>

    suspend fun clearDatabase()

    suspend fun fillDatabase(notes: List<Note>)

    suspend fun add(note: Note)

    suspend fun update(note: Note)

    suspend fun byRemoteId(remoteId: Long): Note?

    suspend fun byEquals(title: String, text: String): Note?
}