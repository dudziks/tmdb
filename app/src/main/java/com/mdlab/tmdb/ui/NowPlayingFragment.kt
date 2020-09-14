package com.mdlab.tmdb.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdlab.tmdb.R
import com.mdlab.tmdb.ui.adapter.MoviesListAdapter
import com.mdlab.tmdb.viewmodel.TMDBViewModel
import kotlinx.android.synthetic.main.fragment_now_playing.*


/**
 * List with currently playing movies.
 */
class NowPlayingFragment : Fragment() {
    private lateinit var tmdbViewModel: TMDBViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.apply {
            tmdbViewModel = ViewModelProvider(this).get(TMDBViewModel::class.java)
            val nowPlayingAdapter = MoviesListAdapter(tmdbViewModel)
            initRecyclerView(baseContext, nowPlayingAdapter)
            tmdbViewModel.getNowPlayingFav().observe(viewLifecycleOwner, Observer { movies ->
                nowPlayingAdapter.run {
                    moviesList = movies
                    notifyDataSetChanged()
                }
                // init autocomplete
                initSearchAutocomplete(movies.map { m -> m.title })
            })
        }
    }

    private fun initSearchAutocomplete(listTitles: List<String>) {
        autoCompleteTextView?.let { actv ->
            actv.setAdapter<ArrayAdapter<String>?>(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    listTitles
                )
            )
            actv.setOnItemClickListener { _, view, _, _ ->
                (view as? AppCompatTextView)?.run {
                    tmdbViewModel.runSearch(text.toString())
                }
            }
            actv.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (v.text.isNullOrBlank()) {
                        //show now playing
                        tmdbViewModel.loadNowPlaying()
                    } else {
                        tmdbViewModel.runSearch(v.text.toString())
                        v.text = ""
                    }
                    hideKeyboardFrom(requireContext(), v)
                }
                true
            }
        }
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun initRecyclerView(
        baseContext: Context,
        nowPlayingAdapter: MoviesListAdapter?
    ) {
        now_playing_recycler_view.apply {
            layoutManager = LinearLayoutManager(baseContext)
            adapter = nowPlayingAdapter
            addItemDecoration(
                DividerItemDecoration(
                    baseContext,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tmdbViewModel.cancelRequests()
    }
}
