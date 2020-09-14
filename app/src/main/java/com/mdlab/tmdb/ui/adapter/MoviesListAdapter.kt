package com.mdlab.tmdb.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mdlab.tmdb.databinding.MovieListItemBinding
import com.mdlab.tmdb.model.movies.Result
import com.mdlab.tmdb.viewmodel.TMDBViewModel

class MoviesListAdapter(val viewModel: TMDBViewModel) : RecyclerView.Adapter<MovieViewHolder>() {

    var moviesList = listOf<Result>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return   MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = moviesList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(moviesList[position], viewModel)
    }
}

