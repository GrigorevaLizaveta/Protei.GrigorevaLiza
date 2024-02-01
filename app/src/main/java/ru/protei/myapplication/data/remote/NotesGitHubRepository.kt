package ru.protei.myapplication.data.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.protei.grigorevaed.compose.domain.Note
import ru.protei.myapplication.domain.NotesRemoteRepository

class NotesGitHubRepository(private val notesApi: NotesGitHubApi) : NotesRemoteRepository {

    override suspend fun list(): List<Note> = withContext(Dispatchers.IO) {
        var issues: List<GitHubIssue>?
        try {
            val result = notesApi.getList()

            if (!result.isSuccessful) {
                Log.w("NotesRepositoryApi", "Can not get issues $result")
                return@withContext emptyList()
            }

            issues = result.body()
        } catch (e: Exception) {
            Log.w("Notes6itHubRepository", "Can't get issues ", e)
            return@withContext emptyList()
        }
        val notes = issues?.map {
            toNote(it)
        } ?: emptyList()
        notes
    }

    override suspend fun add(note: Note): Long? {
        try {
            val issue = toGitHubIssue(note)
            val result = notesApi.add(issue)
            if (result.isSuccessful) {
                return result.body()?.number
            }
        } catch (e: Exception) {
            Log.w("NotesGitHubRepository", "Can't add issue ", e)
        }
        return null
    }

    override suspend fun update(note: Note): Boolean {
        try {
            val issue = toGitHubIssue(note)
            val result = issue.number?.let { notesApi.update(it, issue) }
            if (result != null) {
                return result.isSuccessful
            }
        } catch (e: Exception) {
            Log.w("NotesGitHubRepository", "Can't update issue ", e)
        }
        return false
    }

    override suspend fun delete(note: Note): Boolean {
            try {
                val issue = toGitHubIssue(note)
                val result = issue.number?.let { notesApi.delete(it) }
                if (result != null) {
                    return result.isSuccessful
                }
            } catch (e: Exception) {
                Log.w("NotesGitHubRepository", "Can't delete issue", e)
            }
            return false
    }

    private fun toNote(issue: GitHubIssue) : Note {
        return Note(
            title = issue.title,
            text = issue.body,
            remoteId = issue.number
        )
    }

    private fun toGitHubIssue(note: Note) : GitHubIssue {
        return GitHubIssue(
            number = note.remoteId,
            title = note.title,
            body = note.text
        )
    }
}

