package com.mdlab.tmdb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mdlab.tmdb.R
import com.mdlab.tmdb.databinding.FragmentMovieDetailsBinding
import com.mdlab.tmdb.serviceroom.Favorite
import com.mdlab.tmdb.viewmodel.MovieData
import com.mdlab.tmdb.viewmodel.MovieDataOutput
import com.mdlab.tmdb.viewmodel.TMDBViewModel

/**
 *
 */
class MovieDetailsFragment : androidx.fragment.app.Fragment() {
    private var movieId: Int? = null
    private lateinit var tmdbViewModel: TMDBViewModel
    private lateinit var binding: FragmentMovieDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt("movieId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let{
            // to have the same view model as NowPlayingFragment
            tmdbViewModel = ViewModelProvider(this).get(TMDBViewModel::class.java)
        }
        binding.lifecycleOwner = this@MovieDetailsFragment
        tmdbViewModel.nowPlayingLiveData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                movieId?.let { mId -> tmdbViewModel.getMovie(mId) }
            })
        tmdbViewModel.getDetails().observe(viewLifecycleOwner, androidx.lifecycle.Observer { movieDetailsData ->
            fillScreen(movieDetailsData)
        })
    }

    private fun fillScreen(movieDataOutput: MovieDataOutput<MovieData>) {
        when (movieDataOutput) {
            is MovieDataOutput.Success -> {
                binding.movieDetails = movieDataOutput.output
                Glide.with(binding.root.context)
                    .load(movieDataOutput.output.photoUrl).apply(RequestOptions())
                    .into(binding.imageViewPoster)
                binding.checkBoxFav.setOnClickListener { favClicked(it, movieDataOutput.output.id) }

                binding.executePendingBindings()
            }
            is MovieDataOutput.Loading -> binding.loading = "Loading..."
        }
    }

    private fun favClicked(favCheckBox: View, movieId: Int) {
        (favCheckBox as? CheckBox)?.let{ cb ->
            if(cb.isChecked) {
                tmdbViewModel.insertFav(Favorite(movieId))
            } else {
                tmdbViewModel.deleteFav(Favorite(movieId))
            }
        }
    }
}
