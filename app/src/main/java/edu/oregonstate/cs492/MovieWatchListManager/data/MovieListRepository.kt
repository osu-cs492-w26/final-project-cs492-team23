package edu.oregonstate.cs492.MovieWatchListManager.data

class MovieListRepository(
    private val dao: MovieDao
) {
    suspend fun insertMovieIntoList(movie: Movie) =
        dao.insert(movie)

    suspend fun deleteMovieFromList(movie: Movie) =
        dao.delete(movie)

    fun getAllMoviesInList() = dao.getAllMoviesInList()
}