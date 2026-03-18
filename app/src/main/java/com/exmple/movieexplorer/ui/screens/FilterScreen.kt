package com.exmple.movieexplorer.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.exmple.movieexplorer.data.model.Movie
import com.exmple.movieexplorer.ui.components.MovieCard
import com.exmple.movieexplorer.ui.components.MovieDetailSheet
import com.exmple.movieexplorer.ui.theme.BackgroundDark
import com.exmple.movieexplorer.ui.theme.CardSurface
import com.exmple.movieexplorer.ui.theme.GlassBorder
import com.exmple.movieexplorer.ui.theme.PurpleContainer
import com.exmple.movieexplorer.ui.theme.PurpleLight
import com.exmple.movieexplorer.ui.theme.PurplePrimary
import com.exmple.movieexplorer.ui.theme.TextPrimary
import com.exmple.movieexplorer.ui.theme.TextSecondary
import com.exmple.movieexplorer.viewmodel.MovieViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavController, viewModel: MovieViewModel) {
    val movies by viewModel.movies.observeAsState(emptyList())
    val sortOptions = remember { listOf("Rating High", "Rating Low", "Title A-Z") }

    var minRating by remember { mutableStateOf(0f) }
    var shortOnly by remember { mutableStateOf(false) }
    var selectedSort by remember { mutableIntStateOf(0) }

    val filteredMovies = remember(movies, minRating, shortOnly, selectedSort) {
        applyFilters(
            movies = movies,
            minRating = minRating,
            shortOnly = shortOnly,
            selectedSort = selectedSort
        )
    }

    var selectedMovie by remember { mutableStateOf<Movie?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = BackgroundDark,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Filters",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Filter panel with glassmorphism card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .border(0.5.dp, GlassBorder, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface.copy(alpha = 0.85f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Minimum Rating: ${(minRating * 10).roundToInt() / 10f}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary
                        )

                        Slider(
                            value = minRating,
                            onValueChange = { minRating = it },
                            valueRange = 0f..10f,
                            steps = 9,
                            colors = SliderDefaults.colors(
                                thumbColor = PurplePrimary,
                                activeTrackColor = PurplePrimary,
                                inactiveTrackColor = CardSurface
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Quick Watch Only",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextSecondary
                            )
                            Switch(
                                checked = shortOnly,
                                onCheckedChange = { shortOnly = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = PurplePrimary,
                                    checkedTrackColor = PurpleContainer,
                                    uncheckedThumbColor = TextSecondary,
                                    uncheckedTrackColor = CardSurface
                                )
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            sortOptions.forEachIndexed { index, option ->
                                FilterChip(
                                    selected = selectedSort == index,
                                    onClick = { selectedSort = index },
                                    label = { Text(option) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PurplePrimary,
                                        selectedLabelColor = TextPrimary,
                                        containerColor = CardSurface,
                                        labelColor = TextSecondary
                                    ),
                                    shape = RoundedCornerShape(50),
                                    border = if (selectedSort == index) null else FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = false,
                                        borderColor = GlassBorder
                                    )
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = { navController.navigate("explore") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Apply and Explore")
                }

                if (filteredMovies.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No movies match these filters.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary
                        )
                    }
                } else {
                    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                        itemsIndexed(filteredMovies, key = { _, m -> m.id }) { index, movie ->
                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(movie.id) {
                                delay(index * 60L)
                                visible = true
                            }
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn(animationSpec = tween(350)) +
                                        slideInVertically(
                                            initialOffsetY = { it / 3 },
                                            animationSpec = tween(350)
                                        )
                            ) {
                                MovieCard(
                                    movie = movie,
                                    onRentClick = {
                                        viewModel.rentMovie(movie)
                                        navController.navigate("rental")
                                    },
                                    onCardClick = { selectedMovie = movie }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Detail sheet overlay
        selectedMovie?.let { movie ->
            MovieDetailSheet(
                movie = movie,
                relatedMovies = movies.filter { it.id != movie.id },
                onDismiss = { selectedMovie = null },
                onRentClick = {
                    viewModel.rentMovie(movie)
                    selectedMovie = null
                    navController.navigate("rental")
                },
                onRelatedMovieClick = { selectedMovie = it }
            )
        }
    }
}

private fun applyFilters(
    movies: List<Movie>,
    minRating: Float,
    shortOnly: Boolean,
    selectedSort: Int
): List<Movie> {
    val filtered = movies.filter { movie ->
        movie.rating >= minRating && (!shortOnly || movie.overview.length <= 140)
    }

    return when (selectedSort) {
        1 -> filtered.sortedBy { it.rating }
        2 -> filtered.sortedBy { it.title.lowercase() }
        else -> filtered.sortedByDescending { it.rating }
    }
}
