package ru.protei.myapplication.domain

import ru.protei.grigorevaed.compose.domain.Note

interface NotesRemoteRepository {
    suspend fun list(): List<Note>
    suspend fun add(note: Note): Long?
    suspend fun update(note: Note): Boolean
    suspend fun delete(note: Note): Boolean
}