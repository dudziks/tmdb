package com.mdlab.tmdb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mdlab.tmdb.serviceroom.Favorite
import com.mdlab.tmdb.model.configuration.Configuration
import com.mdlab.tmdb.model.details.MovieDetails

object DetailsJoiner {
    private fun joinData(
        movieDetailsLD: LiveData<MovieDetails>,
        configurationLD: LiveData<Configuration>,
        favLD: LiveData<List<Favorite>>
    ): MovieDataOutput<MovieData> {
        // now playing have to be loaded, to get movieDetails
        val movieDetails = movieDetailsLD.value
        val config = configurationLD.value
        val favList = favLD.value

        if (movieDetails == null || config == null || favList == null) {
            return MovieDataOutput.Loading()
        }
        // got all the needed data
        return MovieDataOutput.Success<MovieData>(
            Mapper.map(movieDetails, config, favList)
        )
    }

    fun mediate(
        movieDetailsLiveData: LiveData<MovieDetails>,
        configurationLiveData: LiveData<Configuration>,
        favLiveData: LiveData<List<Favorite>>
    ): LiveData<MovieDataOutput<MovieData>> = MediatorLiveData<MovieDataOutput<MovieData>>()
        .apply {
            addSource(movieDetailsLiveData) {
                value = DetailsJoiner.joinData(
                    movieDetailsLiveData,
                    configurationLiveData,
                    favLiveData
                )
            }
            addSource(configurationLiveData) {
                value = DetailsJoiner.joinData(
                    movieDetailsLiveData,
                    configurationLiveData,
                    favLiveData
                )
            }
            addSource(favLiveData) {
                value = DetailsJoiner.joinData(
                    movieDetailsLiveData,
                    configurationLiveData,
                    favLiveData
                )
            }
        }

}