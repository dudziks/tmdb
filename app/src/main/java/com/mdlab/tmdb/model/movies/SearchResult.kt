package com.mdlab.tmdb.model.movies


data class SearchResult(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)