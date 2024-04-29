package dev.itchybit.thunderbuddy.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.itchybit.thunderbuddy.io.api.getHttpClient
import dev.itchybit.thunderbuddy.io.api.service.WeatherService
import dev.itchybit.thunderbuddy.io.db.Database
import dev.itchybit.thunderbuddy.io.repo.FavouritesRepository
import dev.itchybit.thunderbuddy.io.repo.FavouritesRepositoryImpl
import dev.itchybit.thunderbuddy.io.repo.WeatherRepository
import dev.itchybit.thunderbuddy.io.repo.WeatherRepositoryImpl
import io.ktor.client.HttpClient
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesJson() = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        useAlternativeNames = false
        encodeDefaults = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        @ApplicationContext appContext: Context, json: Json
    ): HttpClient = getHttpClient(appContext, json)

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): Database =
        Room.databaseBuilder(context, Database::class.java, Database.DB_NAME)
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providesWeatherService(@ApplicationContext context: Context, httpClient: HttpClient) =
        WeatherService(context, httpClient)

    @Provides
    @Singleton
    fun providesWeatherRepository(
        database: Database, weatherService: WeatherService
    ): WeatherRepository = WeatherRepositoryImpl(database, weatherService)

    @Provides
    @Singleton
    fun providesFavouritesRepository(database: Database): FavouritesRepository =
        FavouritesRepositoryImpl(database)
}