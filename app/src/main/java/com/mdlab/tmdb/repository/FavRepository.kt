package com.mdlab.tmdb.repository

import androidx.lifecycle.LiveData
import com.mdlab.tmdb.serviceroom.Favorite
import com.mdlab.tmdb.serviceroom.FavDAO

class FavRepository(private val favDAO: FavDAO) {
    val allFavs: LiveData<List<Favorite>> = favDAO.getAllFav()

    suspend fun insert(fav: Favorite) = favDAO.insertFav(fav)
    suspend fun delete(fav: Favorite) = favDAO.deleteFav(fav)
}