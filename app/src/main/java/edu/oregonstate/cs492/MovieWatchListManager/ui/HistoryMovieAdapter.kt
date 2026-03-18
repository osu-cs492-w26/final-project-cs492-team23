package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.core.view.marginRight
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.oregonstate.cs492.MovieWatchListManager.R
import edu.oregonstate.cs492.MovieWatchListManager.data.Genre
import edu.oregonstate.cs492.MovieWatchListManager.data.Movie
import edu.oregonstate.cs492.MovieWatchListManager.data.WatchHistoryWithMovie

class HistoryMovieAdapter(
    private val movies: List<WatchHistoryWithMovie>,
    private val onMovieClick: (WatchHistoryWithMovie) -> Unit
) : RecyclerView.Adapter<HistoryMovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.iv_movie_poster)
        val title: TextView = view.findViewById(R.id.tv_movie_title)
        val date: TextView = view.findViewById(R.id.tv_movie_date)
        val genreContainer: LinearLayout = view.findViewById(R.id.ll_genres)
        val rating: TextView = view.findViewById(R.id.tv_movie_rating)

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
            .inflate(R.layout.item_category_movie_history, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position].movie
        holder.title.text = movie.title
        holder.date.text = movie.releaseDate
        holder.rating.text = "${movies[position].history.rating}/10"

        val fullPosterUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
        Glide.with(holder.poster.context)
            .load(fullPosterUrl)
            .placeholder(android.R.drawable.progress_horizontal)
            .error(android.R.drawable.stat_notify_error)
            .into(holder.poster)

        holder.genreContainer.removeAllViews()
        if (movie.genreIDs.isEmpty()) {
            holder.genreContainer.visibility = View.GONE
            return
        } else {
            holder.genreContainer.visibility = View.VISIBLE
        }
        val genreNames = Genre.getGenreNames()
        val genreColors = Genre.getGenreColors()
        val resources = holder.genreContainer.context.resources
        movie.genreIDs.forEach { genreId ->
            val tv = TextView(holder.genreContainer.context).apply {
                text = genreNames[genreId]
                setTextColor(ContextCompat.getColor(holder.genreContainer.context, R.color.white))
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
                val drawable = GradientDrawable().apply {
                    cornerRadius = resources.getDimensionPixelSize(R.dimen.genre_corner).toFloat()
                    setColor(ContextCompat.getColor(holder.genreContainer.context, genreColors[genreId] ?: R.color.genre_default))
                }
                background = drawable
            }
            holder.genreContainer.addView(tv)
        }
    }

    override fun getItemCount() = movies.size
}
