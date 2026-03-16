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
import edu.oregonstate.cs492.MovieWatchListManager.data.MovieCategory
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val nowPlayingViewModel: NowPlayingMoviesViewModel by viewModels()
    private val upcomingViewModel: UpcomingMoviesViewModel by viewModels()
    private val popularViewModel: PopularMoviesViewModel by viewModels()
    private val topRatedViewModel: TopRatedMoviesViewModel by viewModels()

    private lateinit var categoryAdapter: CategoryAdapter
    private val categoriesMap = mutableMapOf<Int, MovieCategory>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvCategories = view.findViewById<RecyclerView>(R.id.rv_categories)
        categoryAdapter = CategoryAdapter(emptyList<MovieCategory>()) { category ->
            val bundle = Bundle().apply {
                putString("categoryTitle", category.title)
            }
            findNavController().navigate(R.id.action_home_to_category_details, bundle)
        }
        rvCategories.layoutManager = LinearLayoutManager(requireContext())
        rvCategories.adapter = categoryAdapter

        setupObservers()
        loadData()
    }

    private fun setupObservers() {
        nowPlayingViewModel.searchResults.observe(viewLifecycleOwner) { movies ->
            movies?.let { updateCategory(0, "Now Playing", it) }
        }
        upcomingViewModel.searchResults.observe(viewLifecycleOwner) { movies ->
            movies?.let { updateCategory(1, "Upcoming", it) }
        }
        popularViewModel.searchResults.observe(viewLifecycleOwner) { movies ->
            movies?.let { updateCategory(2, "Popular", it) }
        }
        topRatedViewModel.searchResults.observe(viewLifecycleOwner) { movies ->
            movies?.let { updateCategory(3, "Top Rated", it) }
        }
    }

    private fun updateCategory(index: Int, title: String, movies: List<edu.oregonstate.cs492.MovieWatchListManager.data.Movie>) {
        categoriesMap[index] = MovieCategory(title, movies)
        val sortedCategories = categoriesMap.toSortedMap().values.toList()
        categoryAdapter.updateCategories(sortedCategories)
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            nowPlayingViewModel.loadNowPlayingMovies()
            upcomingViewModel.loadUpcomingMovies()
            popularViewModel.loadPopularMovies()
            topRatedViewModel.loadTopRatedMovies()
        }
    }
}
