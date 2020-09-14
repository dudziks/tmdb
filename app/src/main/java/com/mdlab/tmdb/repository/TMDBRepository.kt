package com.mdlab.tmdb.repository

import com.mdlab.tmdb.model.configuration.Configuration
import com.mdlab.tmdb.model.details.MovieDetails
import com.mdlab.tmdb.model.movies.Result
import com.mdlab.tmdb.serviceretro.TMdbApiInterface

class TMDBRepository (private val apiInterface: TMdbApiInterface) : BaseRepository() {

    suspend fun getNowPlaying(): MutableList<Result>? {
        return safeApiCall(
            call = {apiInterface.fetchNowPlayingAsync().await()},
            error = "Error fetching now playing movies."
        )?.results?.toMutableList()
    }

    suspend fun getConfiguration(): Configuration? {
        return safeApiCall(
            call = {apiInterface.fetchConfigurationAsync().await()},
            error = "Error fetching configuration."
        )
    }

    suspend fun searchResult(query: String): MutableList<Result>? {
        return safeApiCall(
            call = {apiInterface.fetchSearchResultAsync(query).await()},
            error = "Error fetching configuration."
        )?.results?.toMutableList()
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetails? {
        return safeApiCall(
            call = {apiInterface.fetchMovieDetailsAsync(movieId).await()},
            error = "Error fetching configuration."
        )
    }
}