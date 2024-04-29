package dev.itchybit.thunderbuddy.io.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.itchybit.thunderbuddy.io.db.entity.Favourite

@Dao
interface FavouritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(favourite: Favourite)

    @Query("SELECT * FROM favourites")
    fun getAll(): List<Favourite>

    @Delete
    fun delete(favourite: Favourite)
}