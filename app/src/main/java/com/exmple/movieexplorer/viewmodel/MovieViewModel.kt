package com.exmple.movieexplorer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.exmple.movieexplorer.data.model.Movie
import com.exmple.movieexplorer.data.model.RentalEntity
import com.exmple.movieexplorer.data.repository.MovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    // Original combined list — still used by Search, Filter, Explore screens
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    // Category-specific lists
    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> = _popularMovies

    private val _trendingMovies = MutableLiveData<List<Movie>>()
    val trendingMovies: LiveData<List<Movie>> = _trendingMovies

    private val _topRatedMovies = MutableLiveData<List<Movie>>()
    val topRatedMovies: LiveData<List<Movie>> = _topRatedMovies

    private val _nowPlayingMovies = MutableLiveData<List<Movie>>()
    val nowPlayingMovies: LiveData<List<Movie>> = _nowPlayingMovies

    private val _upcomingMovies = MutableLiveData<List<Movie>>()
    val upcomingMovies: LiveData<List<Movie>> = _upcomingMovies

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    val rentals = repository.getRentals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadMovies()
        loadAllCategories()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _movies.value = repository.fetchMovies()
            } catch (_: Exception) {
                _movies.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadAllCategories() {
        viewModelScope.launch {
            try {
                val popular = async { repository.fetchPopularMovies() }
                val trending = async { repository.fetchTrendingMovies() }
                val topRated = async { repository.fetchTopRatedMovies() }
                val nowPlaying = async { repository.fetchNowPlayingMovies() }
                val upcoming = async { repository.fetchUpcomingMovies() }

                _popularMovies.value = popular.await()
                _trendingMovies.value = trending.await()
                _topRatedMovies.value = topRated.await()
                _nowPlayingMovies.value = nowPlaying.await()
                _upcomingMovies.value = upcoming.await()
            } catch (_: Exception) {
                // Individual categories that fail will remain as empty lists
            }
        }
    }

    fun rentMovie(movie: Movie) {
        viewModelScope.launch {
            repository.rentMovie(
                RentalEntity(
                    movieId = movie.id,
                    title = movie.title,
                    posterUrl = "https://image.tmdb.org/t/p/w500${movie.posterUrl}",
                    rating = movie.rating,
                    days = 1
                )
            )
        }
    }

    fun increaseDays(rental: RentalEntity) {
        viewModelScope.launch {
            repository.updateDays(rental.id, rental.days + 1)
        }
    }

    fun decreaseDays(rental: RentalEntity) {
        if (rental.days > 1) {
            viewModelScope.launch {
                repository.updateDays(rental.id, rental.days - 1)
            }
        }
    }

    fun removeRental(rental: RentalEntity) {
        viewModelScope.launch {
            repository.removeRental(rental)
        }
    }
}

class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
