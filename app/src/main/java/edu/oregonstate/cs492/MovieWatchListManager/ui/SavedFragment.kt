package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.MovieWatchListManager.R
import kotlinx.coroutines.launch

class SavedFragment : Fragment(R.layout.fragment_saved) {
    private val viewModel: PopularMoviesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvSavedMovies = view.findViewById<RecyclerView>(R.id.rv_saved_movies)
        rvSavedMovies.layoutManager = LinearLayoutManager(requireContext())

        viewModel.searchResults.observe(viewLifecycleOwner) { movies ->
            movies?.let {
                // "random" saved movies
                rvSavedMovies.adapter = CategoryMovieAdapter(it.shuffled().take(6)) { movie ->
                    val action = SavedFragmentDirections.actionSavedToMovieDetails(movie)
                    findNavController().navigate(action)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadPopularMovies()
        }
    }
}