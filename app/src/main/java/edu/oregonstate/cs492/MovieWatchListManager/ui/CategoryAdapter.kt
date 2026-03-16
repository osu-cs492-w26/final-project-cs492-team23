package edu.oregonstate.cs492.MovieWatchListManager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.MovieWatchListManager.R
import edu.oregonstate.cs492.MovieWatchListManager.data.Movie
import edu.oregonstate.cs492.MovieWatchListManager.data.MovieCategory

class CategoryAdapter(
    private var categories: List<MovieCategory>,
    private val onCategoryClick: (MovieCategory) -> Unit,
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val header: View = view.findViewById(R.id.rl_category_header)
        val title: TextView = view.findViewById(R.id.tv_category_title)
        val forwardArrow: ImageView = view.findViewById(R.id.iv_forward_arrow)
        val moviesRecyclerView: RecyclerView = view.findViewById(R.id.rv_movies)

        init {
            header.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onCategoryClick(categories[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.title.text = category.title

        val movieAdapter = PosterAdapter(category.movies, onMovieClick = onMovieClick)

        holder.moviesRecyclerView.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setRecycledViewPool(viewPool)
        }
    }

    override fun getItemCount() = categories.size

    fun updateCategories(newCategories: List<MovieCategory>) {
        this.categories = newCategories
        notifyDataSetChanged()
    }
}
