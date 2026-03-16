package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import edu.oregonstate.cs492.MovieWatchListManager.R
import edu.oregonstate.cs492.MovieWatchListManager.data.Movie
import kotlinx.coroutines.launch

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {
    private val videosViewModel: MovieVideosViewModel by viewModels()
    private val streamingViewModel: StreamingServicesViewModel by viewModels()
    private val detailsViewModel: MovieDetailsViewModel by viewModels()
    private var youTubePlayer: YouTubePlayer? = null
    private var currentVideoId: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie = arguments?.getSerializable("movie") as? Movie ?: return

        val tvTitle = view.findViewById<TextView>(R.id.tv_movie_title)
        val tvReleaseDate = view.findViewById<TextView>(R.id.tv_release_date)
        val tvOverview = view.findViewById<TextView>(R.id.tv_overview)
        val tvRuntime = view.findViewById<TextView>(R.id.tv_runtime)
        val tvGenre1 = view.findViewById<TextView>(R.id.tv_genre1)
        val tvGenre2 = view.findViewById<TextView>(R.id.tv_genre2)
        val reviewButton = view.findViewById<TextView>(R.id.review_button)
        val rvSimilarMovies = view.findViewById<RecyclerView>(R.id.rv_similar_movies)
        val streamingContainer = view.findViewById<LinearLayout>(R.id.streaming_container)

        tvTitle.text = movie.title
        tvReleaseDate.text = movie.releaseDate
        tvOverview.text = movie.overview
        tvGenre1.text = "Action"
        tvGenre2.text = "Drama"

        val youtubePlayerView = view.findViewById<YouTubePlayerView>(R.id.youtube_player_view)
        lifecycle.addObserver(youtubePlayerView)

        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@MovieDetailsFragment.youTubePlayer = youTubePlayer
                currentVideoId?.let { 
                    youTubePlayer.cueVideo(it, 0F)
                }
            }
        })

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
        val dummySimilarMovies = List(6) {
            Movie(
                movieId = it + 500,
                overview = "Similar movie overview.",
                posterPath = "",
                releaseDate = "2024-01-01",
                title = "Similar Movie ${it + 1}",
                video = false
            )
        }
        rvSimilarMovies.adapter = PosterAdapter(dummySimilarMovies, R.layout.movie_poster_search) { similarMovie ->
            val bundle = Bundle().apply {
                putSerializable("movie", similarMovie)
            }
            findNavController().navigate(R.id.action_movie_details_self, bundle)
        }
    }
}