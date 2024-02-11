import android.content.Context
import androidx.room.Room
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.protei.grigorevaed.compose.domain.NotesUseCase
import ru.protei.myapplication.data.local.NotesDao
import ru.protei.myapplication.data.local.NotesDatabase
import ru.protei.myapplication.data.local.NotesRepositoryDB
import ru.protei.myapplication.data.remote.NotesGitHubApi
import ru.protei.myapplication.data.remote.NotesGitHubRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent:: class)
object NotesModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NotesDatabase {
        return Room.databaseBuilder(
            context,
            NotesDatabase::class.java, "note_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNotesDao(db: NotesDatabase): NotesDao {
        return db.notesDao()
    }
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request: Request = chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        "Bearer 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"
                    )
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/repos/GrigorevaLizaveta/Protei.GrigorevaLiza/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideNotesRepositoryDB(notesDao: NotesDao): NotesRepositoryDB {
        return NotesRepositoryDB(notesDao)
    }

    @Singleton
    @Provides
    fun provideNotesGitHubRepository(api: NotesGitHubApi): NotesGitHubRepository {
        return NotesGitHubRepository(api)
    }

    @Singleton
    @Provides
    fun provideNotesUseCase(notesRepo: NotesRepositoryDB, notesApi: NotesGitHubRepository): NotesUseCase {
        return NotesUseCase(notesRepo, notesApi)
    }
}