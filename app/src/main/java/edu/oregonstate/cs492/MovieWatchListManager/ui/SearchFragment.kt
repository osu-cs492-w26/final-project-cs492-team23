package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.MovieWatchListManager.R
import edu.oregonstate.cs492.MovieWatchListManager.data.Movie

class SearchFragment : Fragment(R.layout.fragment_search) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_search_results)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        // dumahhh movie placeholders
        val dummyMovies = List(8) { 
            Movie(
                movieId = it,
                overview = "",
                posterPath = "",
                releaseDate = "",
                title = "",
                video = false
            )
        }

        recyclerView.adapter = PosterAdapter(dummyMovies, R.layout.movie_poster_search)
    }
}