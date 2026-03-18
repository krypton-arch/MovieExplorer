package com.exmple.movieexplorer.data.repository

import com.exmple.movieexplorer.data.local.RentalDao
import com.exmple.movieexplorer.data.model.Movie
import com.exmple.movieexplorer.data.model.RentalEntity
import com.exmple.movieexplorer.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val dao: RentalDao) {

    // Original method — kept for backward compatibility
    suspend fun fetchMovies(): List<Movie> = RetrofitInstance.api.getMovies().results

    // Category-based fetching
    suspend fun fetchPopularMovies(): List<Movie> = RetrofitInstance.api.getPopularMovies().results
    suspend fun fetchTrendingMovies(): List<Movie> = RetrofitInstance.api.getTrendingMovies().results
    suspend fun fetchTopRatedMovies(): List<Movie> = RetrofitInstance.api.getTopRatedMovies().results
    suspend fun fetchNowPlayingMovies(): List<Movie> = RetrofitInstance.api.getNowPlayingMovies().results
    suspend fun fetchUpcomingMovies(): List<Movie> = RetrofitInstance.api.getUpcomingMovies().results

    // Rental methods — unchanged
    fun getRentals(): Flow<List<RentalEntity>> = dao.getAllRentals()
    suspend fun rentMovie(rental: RentalEntity) = dao.insertRental(rental)
    suspend fun updateDays(id: Int, days: Int) = dao.updateRentalDays(id, days)
    suspend fun removeRental(rental: RentalEntity) = dao.deleteRental(rental)
    suspend fun getRentalsOnce(): List<RentalEntity> = dao.getAllRentalsOnce()
}
