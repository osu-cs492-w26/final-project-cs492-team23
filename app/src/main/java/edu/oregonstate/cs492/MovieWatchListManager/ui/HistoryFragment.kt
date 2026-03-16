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

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val viewModel: TopRatedMoviesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvHistoryMovies = view.findViewById<RecyclerView>(R.id.rv_history_movies)
        rvHistoryMovies.layoutManager = LinearLayoutManager(requireContext())

        viewModel.searchResults.observe(viewLifecycleOwner) { movies ->
            movies?.let {
                //  "random" watched movies
                rvHistoryMovies.adapter = CategoryMovieAdapter(it.shuffled().take(8)) { movie ->
                    val action = HistoryFragmentDirections.actionHistoryToMovieDetails(movie)
                    findNavController().navigate(action)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadTopRatedMovies()
        }
    }
}