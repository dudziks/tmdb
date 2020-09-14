package com.mdlab.tmdb.ui.adapter

import android.view.View
import android.widget.CheckBox
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mdlab.tmdb.R
import com.mdlab.tmdb.databinding.MovieListItemBinding
import com.mdlab.tmdb.serviceroom.Favorite
import com.mdlab.tmdb.model.movies.Result
import com.mdlab.tmdb.viewmodel.TMDBViewModel

class MovieViewHolder(val binding: MovieListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        result: Result,
        viewModel: TMDBViewModel
    ) {
        with(binding) {
            movieItem.setOnClickListener { onClick(it, result) }
            checkBoxFav.setOnClickListener { favClicked(it, result, viewModel) }
            setVariable(com.mdlab.tmdb.BR.item, result)
            executePendingBindings()
        }
    }

    private fun favClicked(favCheckBox: View, result: Result, viewModel: TMDBViewModel) {
        (favCheckBox as? CheckBox)?.let{cb ->
            if(cb.isChecked) {
                viewModel.insertFav(Favorite(result.id))
            } else {
                viewModel.deleteFav(Favorite(result.id))
            }
        }
    }

    private fun onClick(view: View, result: Result) {
        val bundle = bundleOf("movieId" to result.id)
        view.findNavController().navigate(R.id.action_nowPlaying_to_movieDetailsFragment, bundle)
    }

}