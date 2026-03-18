package com.exmple.movieexplorer.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.exmple.movieexplorer.data.model.Movie
import com.exmple.movieexplorer.ui.components.AppBottomBar
import com.exmple.movieexplorer.ui.components.MovieCard
import com.exmple.movieexplorer.ui.components.MovieDetailSheet
import com.exmple.movieexplorer.ui.theme.BackgroundDark
import com.exmple.movieexplorer.ui.theme.CardSurface
import com.exmple.movieexplorer.ui.theme.GlassBorder
import com.exmple.movieexplorer.ui.theme.PurpleLight
import com.exmple.movieexplorer.ui.theme.PurplePrimary
import com.exmple.movieexplorer.ui.theme.TextPrimary
import com.exmple.movieexplorer.ui.theme.TextSecondary
import com.exmple.movieexplorer.viewmodel.MovieViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(navController: NavController, viewModel: MovieViewModel) {
    val movies by viewModel.movies.observeAsState(emptyList())
    val popularMovies by viewModel.popularMovies.observeAsState(emptyList())
    val trendingMovies by viewModel.trendingMovies.observeAsState(emptyList())
    val topRatedMovies by viewModel.topRatedMovies.observeAsState(emptyList())
    val nowPlayingMovies by viewModel.nowPlayingMovies.observeAsState(emptyList())
    val upcomingMovies by viewModel.upcomingMovies.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)

    val tabs = remember { listOf("Popular", "Trending", "Top Rated", "Now Playing", "Upcoming") }
    var selectedTab by remember { mutableStateOf("Popular") }

    val filteredMovies = when (selectedTab) {
        "Popular" -> popularMovies
        "Trending" -> trendingMovies
        "Top Rated" -> topRatedMovies
        "Now Playing" -> nowPlayingMovies
        "Upcoming" -> upcomingMovies
        else -> movies
    }

    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    val allMovies = movies.ifEmpty { popularMovies }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = BackgroundDark,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Top Choices",
                            style = MaterialTheme.typography.headlineLarge,
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("search") }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = TextPrimary
                            )
                        }
                        IconButton(onClick = { navController.navigate("filter") }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter",
                                tint = TextPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("rental") },
                    containerColor = PurplePrimary,
                    contentColor = TextPrimary,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(60.dp)
                        .border(
                            width = 2.dp,
                            brush = Brush.radialGradient(
                                listOf(PurpleLight, PurplePrimary.copy(alpha = 0.3f))
                            ),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Rentals",
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = { AppBottomBar(navController) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(BackgroundDark)
            ) {
                // Category chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(tabs) { tab ->
                        FilterChip(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            label = { Text(tab) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PurplePrimary,
                                selectedLabelColor = TextPrimary,
                                containerColor = CardSurface,
                                labelColor = TextSecondary
                            ),
                            shape = RoundedCornerShape(50),
                            border = if (selectedTab == tab) null else FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = false,
                                borderColor = GlassBorder
                            )
                        )
                    }
                }

                if (isLoading && filteredMovies.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(color = PurplePrimary)
                    }
                } else {
                    ExploreMovieList(
                        movies = filteredMovies,
                        allMovies = allMovies,
                        category = selectedTab,
                        onRentMovie = { movie ->
                            viewModel.rentMovie(movie)
                            navController.navigate("rental")
                        },
                        onCardClick = { selectedMovie = it }
                    )
                }
            }
        }

        // Detail sheet overlay
        selectedMovie?.let { movie ->
            MovieDetailSheet(
                movie = movie,
                relatedMovies = allMovies.filter { it.id != movie.id },
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

@Composable
private fun ExploreMovieList(
    movies: List<Movie>,
    allMovies: List<Movie>,
    category: String,
    onRentMovie: (Movie) -> Unit,
    onCardClick: (Movie) -> Unit
) {
    if (movies.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Loading $category movies...",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Featured card for the first movie
            item {
                FeaturedMovieCard(
                    movie = movies.first(),
                    onClick = { onCardClick(movies.first()) }
                )
            }

            // Rest of the movies
            itemsIndexed(
                movies.drop(1),
                key = { _, movie -> movie.id }
            ) { index, movie ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(movie.id) {
                    delay(index * 80L)
                    visible = true
                }
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(400)) +
                            slideInVertically(
                                initialOffsetY = { it / 3 },
                                animationSpec = tween(400)
                            )
                ) {
                    MovieCard(
                        movie = movie,
                        onRentClick = { onRentMovie(movie) },
                        onCardClick = { onCardClick(movie) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedMovieCard(movie: Movie, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w780${movie.posterUrl}",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, GlassBorder.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            BackgroundDark.copy(alpha = 0.6f),
                            BackgroundDark.copy(alpha = 0.95f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Every story leaves a shadow.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            )

            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "★ ${movie.rating}  •  ${movie.overview.take(50)}...",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
