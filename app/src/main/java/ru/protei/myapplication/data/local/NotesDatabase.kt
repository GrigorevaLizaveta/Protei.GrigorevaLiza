package ru.protei.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.protei.grigorevaed.compose.domain.Note
import ru.protei.myapplication.data.local.NotesDao


@Database(
    entities = [Note:: class],
    version = 2,
    exportSchema = false
)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

}
