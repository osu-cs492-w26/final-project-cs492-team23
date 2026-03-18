package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Trace
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import edu.oregonstate.cs492.MovieWatchListManager.R
import edu.oregonstate.cs492.MovieWatchListManager.data.Genre
import edu.oregonstate.cs492.MovieWatchListManager.data.Movie
import edu.oregonstate.cs492.MovieWatchListManager.util.LoadingStatus
import kotlinx.coroutines.launch

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {
    private val videosViewModel: MovieVideosViewModel by viewModels()
    private val streamingViewModel: StreamingServicesViewModel by viewModels()
    private val detailsViewModel: MovieDetailsViewModel by viewModels()
    private val movieListViewModel: MovieListViewModel by viewModels()
    private val similarMoviesViewModel: SimilarMoviesViewModel by viewModels()
    private var youTubePlayer: YouTubePlayer? = null
    private var youtubePlayerView: YouTubePlayerView? = null
    private var currentVideoId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie = arguments?.getSerializable("movie") as? Movie ?: return

        val tvTitle = view.findViewById<TextView>(R.id.tv_movie_title)
        val tvReleaseDate = view.findViewById<TextView>(R.id.tv_release_date)
        val tvOverview = view.findViewById<TextView>(R.id.tv_overview)
        val tvRuntime = view.findViewById<TextView>(R.id.tv_runtime)
        val reviewButton = view.findViewById<TextView>(R.id.review_button)
        val rvSimilarMovies = view.findViewById<RecyclerView>(R.id.rv_similar_movies)
        val streamingContainer = view.findViewById<LinearLayout>(R.id.streaming_container)
        val genreContainer = view.findViewById<LinearLayout>(R.id.ll_genres)
        val addToListButton = view.findViewById<Button>(R.id.btn_add_to_list)
        val addToHistoryButton = view.findViewById<Button>(R.id.btn_add_to_history)

        tvTitle.text = movie.title
        tvReleaseDate.text = movie.releaseDate
        tvOverview.text = movie.overview
        genreContainer.removeAllViews()
        if (movie.genreIDs.isEmpty()) {
            genreContainer.visibility = View.GONE
            return
        } else {
            genreContainer.visibility = View.VISIBLE
        }
        val genreNames = Genre.getGenreNames()
        val genreColors = Genre.getGenreColors()
        movie.genreIDs.forEach { genreId ->
            val tv = TextView(context).apply {
                text = genreNames[genreId]
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                val hPadding = resources.getDimensionPixelSize(R.dimen.genre_padding_h)
                val vPadding = resources.getDimensionPixelSize(R.dimen.genre_padding_v)
                setPadding(hPadding, vPadding, hPadding, vPadding)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.marginEnd = resources.getDimensionPixelSize(R.dimen.genre_margin_end)
                layoutParams = params
                val drawable = ContextCompat.getDrawable(context, R.drawable.bg_genre)?.mutate()
                drawable?.let {
                    DrawableCompat.setTint(it, ContextCompat.getColor(context, genreColors[genreId] ?: R.color.genre_default))
                }
                background = drawable
            }
            genreContainer.addView(tv)
        }

        fun updateButtons() {
            val inList = movieListViewModel.movieList.value?.any { it.movieId == movie.movieId } == true
            val historyMovie = movieListViewModel.movieHistory.value?.find { it.movie.movieId == movie.movieId }
            if (inList) {
                addToListButton.text = "- My List"
                addToListButton.setOnClickListener {
                    movieListViewModel.removeMovieFromList(movie)
                    val snackbar = Snackbar.make(
                        view,
                        "Removed from watchlist",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("UNDO") {
                        movieListViewModel.addMovieToList(movie)
                    }
                    snackbar.show()
                }
            } else if (historyMovie != null) {
                addToListButton.text = "- My List"
                addToListButton.setOnClickListener {
                    val rating = historyMovie.history.rating
                    movieListViewModel.removeMovieFromHistory(movie)
                    val snackbar = Snackbar.make(
                        view,
                        "Removed from watch history",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("UNDO") {
                        movieListViewModel.addMovieToHistory(movie, rating)
                    }
                    snackbar.show()
                }
            } else {
                addToListButton.text = "+ My List"
                addToListButton.setOnClickListener {
                    movieListViewModel.addMovieToList(movie)
                    val snackbar = Snackbar.make(
                        view,
                        "Added to watchlist",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("UNDO") {
                        movieListViewModel.removeMovieFromList(movie)
                    }
                    snackbar.show()
                }
            }

            if (historyMovie != null) {
                addToHistoryButton.text = "- Watch History"
                addToHistoryButton.setOnClickListener {
                    val rating = historyMovie.history.rating
                    movieListViewModel.removeMovieFromHistory(movie)
                    val snackbar = Snackbar.make(
                        view,
                        "Removed from watch history",
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("UNDO") {
                        movieListViewModel.addMovieToHistory(movie, rating)
                    }
                    snackbar.show()
                }
            } else {
                addToHistoryButton.text = "+ Watch History"
                addToHistoryButton.setOnClickListener {
                    val numberPicker = NumberPicker(requireContext()).apply {
                        minValue = 1
                        maxValue = 10
                        value = 5
                    }
                    AlertDialog.Builder(requireContext())
                        .setTitle("Rate this movie")
                        .setMessage("Select a rating from 1 to 10")
                        .setView(numberPicker)
                        .setPositiveButton("OK") { dialog, _ ->
                            val rating = numberPicker.value
                            movieListViewModel.addMovieToHistory(movie, rating)
                            dialog.dismiss()
                            val snackbar = Snackbar.make(
                                view,
                                "Added to watch history",
                                Snackbar.LENGTH_LONG
                            )
                            snackbar.setAction("UNDO") {
                                movieListViewModel.removeMovieFromHistory(movie)
                            }
                            snackbar.show()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }

        movieListViewModel.movieList.observe(viewLifecycleOwner) { updateButtons() }
        movieListViewModel.movieHistory.observe(viewLifecycleOwner) { updateButtons() }

        youtubePlayerView = view.findViewById(R.id.youtube_player_view)
        youtubePlayerView?.let { ytpv ->
            lifecycle.addObserver(ytpv)
            ytpv.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    this@MovieDetailsFragment.youTubePlayer = youTubePlayer
                    currentVideoId?.let {
                        youTubePlayer.cueVideo(it, 0F)
                    }
                }
            })
        }

        videosViewModel.searchResults.observe(viewLifecycleOwner) { videos ->
            val trailer = videos?.find { it.type == "Trailer" } ?: videos?.firstOrNull()
            trailer?.let {
                currentVideoId = it.key
                youTubePlayer?.cueVideo(it.key, 0F)
            }
        }

        streamingViewModel.searchResults.observe(viewLifecycleOwner) { streamingServices ->
            streamingContainer.removeAllViews()
            val providers = streamingServices?.results?.UnitedStates?.flatRate

            providers?.forEach { provider ->
                val imageView = ImageView(requireContext()).apply {
                    val size = resources.getDimensionPixelSize(R.dimen.stream_logo_size)
                    layoutParams = LinearLayout.LayoutParams(size, size).apply {
                        marginEnd = resources.getDimensionPixelSize(R.dimen.stream_logo_margin)
                    }
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    setBackgroundResource(R.color.gray_placeholder)
                }

                val logoUrl = "https://image.tmdb.org/t/p/original${provider.logoPath}"
                Glide.with(this)
                    .load(logoUrl)
                    .into(imageView)

                streamingContainer.addView(imageView)
            }
        }

        // get n convert runtime
        detailsViewModel.searchResults.observe(viewLifecycleOwner) { details ->
            details?.let {
                val hours = it.runtime / 60
                val minutes = it.runtime % 60
                tvRuntime.text = "Runtime: ${hours}hr ${minutes}min"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            videosViewModel.loadMovieVideos(movie.movieId)
            streamingViewModel.loadStreamingServices(movie.movieId)
            detailsViewModel.loadMovieDetails(movie.movieId)
            similarMoviesViewModel.loadSearchResults(movie.movieId)
        }

        reviewButton.setOnClickListener {
            val uri = "https://www.themoviedb.org/movie/${movie.movieId}/reviews".toUri()
            val intent = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Log.d("MovieDetailsFragment", "Error on the review intent ${e.message}")
            }
        }

        rvSimilarMovies.layoutManager = GridLayoutManager(requireContext(), 3)
        similarMoviesViewModel.searchResults.observe(viewLifecycleOwner) { movies ->
            movies?.let {
                rvSimilarMovies.adapter = PosterAdapter(movies, R.layout.movie_poster_search) { movie ->
                    val action = MovieDetailsFragmentDirections.actionMovieDetailsSelf(movie)
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onDestroyView() {
        youtubePlayerView?.let { ytpv ->
            lifecycle.removeObserver(ytpv)
            ytpv.release()
        }
        youTubePlayer = null
        youtubePlayerView = null

        super.onDestroyView()
    }
}