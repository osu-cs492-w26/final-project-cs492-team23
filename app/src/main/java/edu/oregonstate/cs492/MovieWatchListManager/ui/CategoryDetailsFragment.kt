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

class CategoryDetailsFragment : Fragment(R.layout.fragment_category_details) {

    private val nowPlayingViewModel: NowPlayingMoviesViewModel by viewModels()
    private val upcomingViewModel: UpcomingMoviesViewModel by viewModels()
    private val popularViewModel: PopularMoviesViewModel by viewModels()
    private val topRatedViewModel: TopRatedMoviesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryTitle = arguments?.getString("categoryTitle")
        val rvMovies = view.findViewById<RecyclerView>(R.id.rv_category_movies)
        rvMovies.layoutManager = LinearLayoutManager(requireContext())

        when (categoryTitle) {
            "Now Playing" -> {
                nowPlayingViewModel.searchResults.observe(viewLifecycleOwner) { movies ->
                    movies?.let { 
                        rvMovies.adapter = CategoryMovieAdapter(it) { movie ->
                            val action = CategoryDetailsFragmentDirections.actionCategoryDetailsToMovieDetails(movie)
                            findNavController().navigate(action)
                        } 
                    }
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    nowPlayingViewModel.loadNowPlayingMovies()
                }
            }
            "Upcoming" -> {
                upcomingViewModel.searchResults.observe(viewLifecycleOwner) { movies ->
                    movies?.let { 
                        rvMovies.adapter = CategoryMovieAdapter(it) { movie ->
                            val action = CategoryDetailsFragmentDirections.actionCategoryDetailsToMovieDetails(movie)
                            findNavController().navigate(action)
                        } 
                    }
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    upcomingViewModel.loadUpcomingMovies()
                }
            }
            "Popular" -> {
                popularViewModel.searchResults.observe(viewLifecycleOwner) { movies ->
                    movies?.let { 
                        rvMovies.adapter = CategoryMovieAdapter(it) { movie ->
                            val action = CategoryDetailsFragmentDirections.actionCategoryDetailsToMovieDetails(movie)
                            findNavController().navigate(action)
                        } 
                    }
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    popularViewModel.loadPopularMovies()
                }
            }
            "Top Rated" -> {
                topRatedViewModel.searchResults.observe(viewLifecycleOwner) { movies ->
                    movies?.let { 
                        rvMovies.adapter = CategoryMovieAdapter(it) { movie ->
                            val action = CategoryDetailsFragmentDirections.actionCategoryDetailsToMovieDetails(movie)
                            findNavController().navigate(action)
                        } 
                    }
                }
                viewLifecycleOwner.lifecycleScope.launch {
                    topRatedViewModel.loadTopRatedMovies()
                }
            }
        }
    }
}
