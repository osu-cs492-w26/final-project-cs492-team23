package edu.oregonstate.cs492.MovieWatchListManager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchedHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: WatchedHistoryEntity)

    @Query("DELETE FROM WatchedHistoryEntity WHERE movieId = :movieId")
    suspend fun delete(movieId: Int)

    @Query("SELECT * FROM WatchedHistoryEntity")
    fun getAllWatchedMoviesInList(): Flow<List<WatchHistoryWithMovie>>
}