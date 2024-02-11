package ru.protei.grigorevaed.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.protei.grigorevaed.compose.domain.NotesUseCase
import ru.protei.grigorevaed.compose.ui.notes.NotesScreen
import ru.protei.grigorevaed.compose.ui.notes.NotesVeiwModel
import ru.protei.grigorevaed.compose.ui.theme.GrigorevaedTheme
import ru.protei.myapplication.data.local.NotesDatabase
import ru.protei.myapplication.data.local.NotesRepositoryDB
import ru.protei.myapplication.data.remote.NotesGitHubApi
import ru.protei.myapplication.data.remote.NotesGitHubRepository

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val database: NotesDatabase by lazy {
//        Room.databaseBuilder(
//            applicationContext,
//            NotesDatabase::class.java, "note_database"
//        ).fallbackToDestructiveMigration()
//            .build()
//    }
//
//    var httpClient: OkHttpClient = OkHttpClient.Builder()
//        .addInterceptor { chain ->
//        val request: Request = chain.request().newBuilder()
//        .addHeader (
//            "Authorization",
//            "Bearer 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"
//        )
//            .build()
//        chain.proceed(request)
//    }
//        .build()
//
//    var retrofit = Retrofit.Builder()
//        .baseUrl("https://api.github.com/repos/GrigorevaLizaveta/Protei.GrigorevaLiza/")
//        .client(httpClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//    private val notesRepo by lazy { NotesRepositoryDB(database.notesDao()) }
//    private val notesApi by lazy { NotesGitHubRepository(retrofit.create(NotesGitHubApi::class.java)) }
//    private val notesUseCase by lazy { NotesUseCase(notesRepo, notesApi) }


    private val notesViewModel: NotesVeiwModel by viewModels()
//    {
//        viewModelFactory {
//            initializer {
//                NotesVeiwModel(notesUseCase)
//            }
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GrigorevaedTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    NotesScreen(notesViewModel)
                }
            }
        }
    }
}




