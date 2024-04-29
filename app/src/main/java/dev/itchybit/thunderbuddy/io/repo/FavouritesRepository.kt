package dev.itchybit.thunderbuddy.io.repo

import dev.itchybit.thunderbuddy.io.db.Database
import dev.itchybit.thunderbuddy.io.db.entity.Favourite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface FavouritesRepository {
    suspend fun getAll(): Flow<List<Favourite>>
    suspend fun addToFavourites(favourite: Favourite): Flow<Unit>
    suspend fun removeFavourite(favourite: Favourite): Flow<Unit>
}

class FavouritesRepositoryImpl(private val database: Database) : FavouritesRepository {

    override suspend fun getAll() = flow {
        emit(database.getFavouritesDao().getAll())
    }

    override suspend fun addToFavourites(favourite: Favourite) = flow {
        database.getFavouritesDao().insertOrUpdate(favourite)
        emit(Unit)
    }

    override suspend fun removeFavourite(favourite: Favourite) = flow {
        database.getFavouritesDao().delete(favourite)
        emit(Unit)
    }
}