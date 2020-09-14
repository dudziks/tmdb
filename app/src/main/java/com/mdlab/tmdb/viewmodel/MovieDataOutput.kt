package com.mdlab.tmdb.viewmodel

sealed class MovieDataOutput<out T : Any> {
    data class Success<out T : Any>(val output : T) : MovieDataOutput<T>()
    class Loading:  MovieDataOutput<Nothing>()
}