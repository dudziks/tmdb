package com.mdlab.tmdb.serviceretro

import com.mdlab.tmdb.model.configuration.Configuration
import com.mdlab.tmdb.model.details.MovieDetails
import com.mdlab.tmdb.model.movies.NowPlaying
import com.mdlab.tmdb.model.movies.SearchResult
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMdbApiInterface {
    @GET("3/movie/now_playing")
    fun fetchNowPlayingAsync() : Deferred<Response<NowPlaying>>
    @GET("3/configuration")
    fun fetchConfigurationAsync() : Deferred<Response<Configuration>>
    @GET("3/search/movie")
    fun fetchSearchResultAsync(@Query("query") query: String) : Deferred<Response<SearchResult>>
    @GET("3/movie/{movie_id}")
    fun fetchMovieDetailsAsync(@Path("movie_id") movie_id: Int) : Deferred<Response<MovieDetails>>
}
