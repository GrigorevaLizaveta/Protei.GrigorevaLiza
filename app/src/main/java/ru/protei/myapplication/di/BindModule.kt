package ru.protei.myapplication.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.protei.grigorevaed.compose.domain.NotesRepository
import ru.protei.myapplication.data.local.NotesRepositoryDB
import ru.protei.myapplication.data.remote.NotesGitHubRepository
import ru.protei.myapplication.domain.NotesRemoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BindModule {
        @Singleton
        @Binds
        fun bindNotesRepository(notesRepository: NotesRepositoryDB): NotesRepository
        @Singleton
        @Binds
        fun bindNotesRemoteRepository(notesRepo: NotesGitHubRepository): NotesRemoteRepository
}