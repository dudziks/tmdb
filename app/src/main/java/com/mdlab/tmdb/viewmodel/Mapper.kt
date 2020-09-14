package com.mdlab.tmdb.viewmodel

import com.mdlab.tmdb.serviceroom.Favorite
import com.mdlab.tmdb.model.configuration.Configuration
import com.mdlab.tmdb.model.details.MovieDetails

object Mapper {
    fun map(result: MovieDetails, config: Configuration, favList: List<Favorite>): MovieData =
        MovieData(
            id = result.id,
            fav = favList.map { m-> m.id }.contains(result.id),
            photoUrl = "${config.images.base_url}${getSafePictSize(config.images.still_sizes)}${result.poster_path}",
            title = result.title,
            release_date = result.release_date,
            vote_average = result.vote_average,
            overview = result.overview
        )

    private fun getSafePictSize(stillSizes: List<String>): String {
        return when {
            stillSizes.size >= 1 -> stillSizes[stillSizes.size -1]
            else -> ""
        }
    }
}