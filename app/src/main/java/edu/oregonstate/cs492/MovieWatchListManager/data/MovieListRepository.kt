package edu.oregonstate.cs492.MovieWatchListManager.data

class MovieListRepository(
    private val dao: MovieDao,
    private val daoHistory: WatchedHistoryDao
) {
    suspend fun insertMovieIntoList(movie: MovieEntity) =
        dao.insert(movie)

    suspend fun deleteMovieFromList(movie: MovieEntity) =
        dao.delete(movie)

    fun getAllMoviesInList() = dao.getAllMoviesInList()

    suspend fun insertMovieIntoHistory(movie: WatchedHistoryEntity) =
        daoHistory.insert(movie)

    suspend fun deleteMovieFromHistory(movieId: Int) =
        daoHistory.delete(movieId)

    fun getAllMoviesInHistory() = daoHistory.getAllWatchedMoviesInList()
}