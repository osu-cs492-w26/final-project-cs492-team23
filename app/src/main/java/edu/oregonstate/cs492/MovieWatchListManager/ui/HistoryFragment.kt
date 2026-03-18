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
import edu.oregonstate.cs492.MovieWatchListManager.data.toMovie
import kotlinx.coroutines.launch

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val viewModel: MovieListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvHistoryMovies = view.findViewById<RecyclerView>(R.id.rv_history_movies)
        rvHistoryMovies.layoutManager = LinearLayoutManager(requireContext())

        viewModel.movieHistory.observe(viewLifecycleOwner) { movies ->
            movies?.let {
                rvHistoryMovies.adapter = HistoryMovieAdapter(movies) { movie ->
                    val action = HistoryFragmentDirections.actionHistoryToMovieDetails(movie.movie.toMovie())
                    findNavController().navigate(action)
                }
            }
        }
    }
}