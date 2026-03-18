package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.MovieWatchListManager.R
import edu.oregonstate.cs492.MovieWatchListManager.data.toMovie
import kotlinx.coroutines.launch

class SavedFragment : Fragment(R.layout.fragment_saved) {
    private val viewModel: MovieListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvSavedMovies = view.findViewById<RecyclerView>(R.id.rv_saved_movies)
        rvSavedMovies.layoutManager = LinearLayoutManager(requireContext())

        viewModel.movieList.observe(viewLifecycleOwner) { movieEntities ->
            movieEntities?.let {
                val movies = it.map { entity -> entity.toMovie() }
                rvSavedMovies.adapter = CategoryMovieAdapter(movies) { movie ->
                    val action = SavedFragmentDirections.actionSavedToMovieDetails(movie)
                    findNavController().navigate(action)
                }
            }
        }
    }
}