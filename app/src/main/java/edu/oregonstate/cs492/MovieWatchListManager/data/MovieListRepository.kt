package edu.oregonstate.cs492.MovieWatchListManager.data

class MovieListRepository(
    private val dao: MovieDao
) {
    suspend fun insertMovieIntoList(movie: MovieEntity) =
        dao.insert(movie)

    suspend fun deleteMovieFromList(movie: MovieEntity) =
        dao.delete(movie)

    fun getAllMoviesInList() = dao.getAllMoviesInList()
}