package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.oregonstate.cs492.MovieWatchListManager.R
import edu.oregonstate.cs492.MovieWatchListManager.data.Movie

class CategoryMovieAdapter(
    private val movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<CategoryMovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.iv_movie_poster)
        val title: TextView = view.findViewById(R.id.tv_movie_title)
        val date: TextView = view.findViewById(R.id.tv_movie_date)
        val genre1: TextView = view.findViewById(R.id.tv_genre1)
        val genre2: TextView = view.findViewById(R.id.tv_genre2)

        init {
            view.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onMovieClick(movies[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.title.text = movie.title
        holder.date.text = movie.releaseDate

        val fullPosterUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
        Glide.with(holder.poster.context)
            .load(fullPosterUrl)
            .placeholder(android.R.drawable.progress_horizontal)
            .error(android.R.drawable.stat_notify_error)
            .into(holder.poster)
            
        // placeholders for genres
        holder.genre1.text = "Action"
        holder.genre2.text = "Drama"
    }

    override fun getItemCount() = movies.size
}
