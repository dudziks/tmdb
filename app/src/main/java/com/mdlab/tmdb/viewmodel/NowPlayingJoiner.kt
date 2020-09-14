package com.mdlab.tmdb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mdlab.tmdb.serviceroom.Favorite
import com.mdlab.tmdb.model.movies.Result

object NowPlayingJoiner {
    private fun joinData(
        favLD: LiveData<List<Favorite>>,
        nowPlayingLD: LiveData<List<Result>>
    ): List<Result> {
        val resultList = nowPlayingLD.value
        val favList = favLD.value
        if(resultList == null || favList == null) {
            return listOf<Result>()
        }
        val favListInt  = favList.map { it.id }
        return   resultList.map {
                m -> m.apply {
            fav = favListInt.contains(m.id)
        }
        }
    }

    fun mediate(
        favLiveData: LiveData<List<Favorite>>,
        nowPlayingLiveData: LiveData<List<Result>>
    ): MediatorLiveData<List<Result>>  = MediatorLiveData<List<Result>>()
        .apply {
            addSource(favLiveData) {
                value = NowPlayingJoiner.joinData(favLiveData, nowPlayingLiveData)
            }
            addSource(nowPlayingLiveData) {
                value =  NowPlayingJoiner.joinData(favLiveData, nowPlayingLiveData)
            }
        }
}