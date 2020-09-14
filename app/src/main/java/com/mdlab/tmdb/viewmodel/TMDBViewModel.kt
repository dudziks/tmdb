package com.mdlab.tmdb.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.mdlab.tmdb.serviceroom.Favorite
import com.mdlab.tmdb.model.configuration.Configuration
import com.mdlab.tmdb.model.details.MovieDetails
import com.mdlab.tmdb.model.movies.Result
import com.mdlab.tmdb.repository.FavRepository
import com.mdlab.tmdb.repository.TMDBRepository
import com.mdlab.tmdb.serviceroom.FavRoomDatabase
import com.mdlab.tmdb.serviceretro.TMdbApiService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TMDBViewModel(application: Application) : AndroidViewModel(application) {
    // coroutine context with the job and the dispatcher
    private val coroutineContext: CoroutineContext get() = Job() + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    fun cancelRequests() = coroutineContext.cancel()
    // TMDB repository
    private val tmdbRepository: TMDBRepository = TMDBRepository(TMdbApiService.TMDB_API)
    // fav repository
    private val favRepository: FavRepository by lazy {
        val favDAO = FavRoomDatabase.getDatabase(application).favDAO()
        FavRepository(favDAO)
    }

    val favLiveData: LiveData<List<Favorite>> by lazy {
        favRepository.allFavs
    }

    // add favourite movie
    fun insertFav(fav: Favorite) = scope.launch { favRepository.insert(fav) }

    // remove favourite movie
    fun deleteFav(fav: Favorite) = scope.launch { favRepository.delete(fav) }

    // live data - movies
    private val _nowPlayingLiveData: MutableLiveData<List<Result>> by lazy {
        MutableLiveData<List<Result>>().apply {
            loadNowPlaying()
        }
    }
    val nowPlayingLiveData: LiveData<List<Result>> get() = _nowPlayingLiveData

    // live data - configuration
    private val _configurationLiveData: MutableLiveData<Configuration> by lazy {
        MutableLiveData<Configuration>().apply {
            scope.launch {
                postValue(tmdbRepository.getConfiguration())
            }
        }
    }

    val configurationLiveData: LiveData<Configuration> get() = _configurationLiveData

    // movie details
    private val _movieDetailsLiveData = MutableLiveData<MovieDetails>()
    val movieDetailsLiveData: LiveData<MovieDetails> get() = _movieDetailsLiveData

    fun getMovie(movieId: Int) {
        scope.launch {
            _movieDetailsLiveData.postValue(
                tmdbRepository.getMovieDetails(movieId)
            )
        }
    }

    // load now playing
    fun loadNowPlaying() {
        scope.launch {
            _nowPlayingLiveData.postValue(
                tmdbRepository.getNowPlaying()
            )
        }
    }

    // search
    fun runSearch(search: String) {
        scope.launch {
            _nowPlayingLiveData.postValue(
                tmdbRepository.searchResult(search)
            )
        }
    }

    fun getDetails(): LiveData<MovieDataOutput<MovieData>> = DetailsJoiner.mediate(
        movieDetailsLiveData,
        configurationLiveData,
        favLiveData)

    fun getNowPlayingFav(): MediatorLiveData<List<Result>> = NowPlayingJoiner.mediate(favLiveData, nowPlayingLiveData)
}

