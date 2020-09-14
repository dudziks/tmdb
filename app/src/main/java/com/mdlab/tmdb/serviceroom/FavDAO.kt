package com.mdlab.tmdb.serviceroom

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavDAO {
    @Query("SELECT * FROM FAV_TABLE ORDER BY ID")
    fun getAllFav(): LiveData<List<Favorite>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFav(fav: Favorite)
    @Delete
    fun deleteFav(fav: Favorite)
}