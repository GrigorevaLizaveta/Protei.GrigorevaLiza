package ru.protei.myapplication.data.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.protei.grigorevaed.compose.domain.Note
import ru.protei.myapplication.domain.NotesRemoteRepository
import javax.inject.Inject

class NotesGitHubRepository  @Inject constructor (private val notesApi: NotesGitHubApi) : NotesRemoteRepository {

    override suspend fun list(): List<Note> = withContext(Dispatchers.IO) {
        val issues: List<GitHubIssue>?
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

    override suspend fun delete(note: Note): Boolean  = withContext(Dispatchers.IO) {
        try {
            val issue = toGitHubIssue(note)
            val result = issue.number.let { notesApi.delete(it) }
            return@withContext result.isSuccessful
        } catch (e: Exception) {
            Log.w("NotesGitHubRepository", "Can't delete issue", e)
        }
    return@withContext false
    }

    override suspend fun add(note: Note): Long?  = withContext(Dispatchers.IO){
        var newIssue: GitHubIssue = toGitHubIssue(note)
        try{
            val result = notesApi.add(newIssue)

            if(!result.isSuccessful){
                Log.w("NotesRepositoryApi","Can't add issues $result")
                return@withContext null
            }
            newIssue = result.body()!!

        }catch (e: Exception){
            Log.w("NotesGitHubRepository","Can't get issues", e)
            return@withContext null
        }
        return@withContext newIssue.number
    }

    override suspend fun update(note: Note): Boolean = withContext(Dispatchers.IO) {
        val issue = toGitHubIssue(note)
        try{
            val result = notesApi.update(issue.number,issue)

            if(!result.isSuccessful){
                Log.w("NotesRepositoryApi","Can't add issues $result")
                return@withContext false
            }

        }catch (e: Exception){
            Log.w("NotesGitHubRepository","Can't get issues", e)
            return@withContext false
        }
        return@withContext true
    }

    private fun toNote(issue: GitHubIssue): Note {
        return Note(issue.title, issue.body, issue.number)
    }

    private fun toGitHubIssue(note: Note): GitHubIssue {
        return GitHubIssue(note.remoteId, note.title, note.text)
    }

}


