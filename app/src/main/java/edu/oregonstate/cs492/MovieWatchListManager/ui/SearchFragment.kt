package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.MovieWatchListManager.R
import edu.oregonstate.cs492.MovieWatchListManager.data.Movie
import edu.oregonstate.cs492.MovieWatchListManager.data.toMovie
import kotlin.getValue

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val searchViewModel: SearchForMovieViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etSearch = view.findViewById<EditText>(R.id.et_search)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_search_results)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim() ?: ""
                searchViewModel.loadSearchResults(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* no-op */ }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* no-op */ }
        })

        searchViewModel.searchResults.observe(viewLifecycleOwner) { movies ->
            movies?.let {
                recyclerView.adapter = PosterAdapter(movies, R.layout.movie_poster_search) { movie ->
                    val action = SearchFragmentDirections.actionSearchToMovieDetails(movie)
                    findNavController().navigate(action)
                }
            }
        }
    }
}