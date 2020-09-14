package com.mdlab.tmdb.viewmodel


data class MovieData(
    val id: Int,
    val fav: Boolean,
    val photoUrl: String,
    val title: String,
    val release_date: String,
    val vote_average: Double,
    val overview: String
)