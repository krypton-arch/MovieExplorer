package com.exmple.movieexplorer.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.exmple.movieexplorer.data.model.Movie
import com.exmple.movieexplorer.data.model.getRentalPricePerDay
import com.exmple.movieexplorer.ui.components.MovieCard
import com.exmple.movieexplorer.ui.components.MovieDetailSheet
import com.exmple.movieexplorer.ui.components.glassmorphism
import com.exmple.movieexplorer.ui.theme.BackgroundDark
import com.exmple.movieexplorer.ui.theme.CardSurface
import com.exmple.movieexplorer.ui.theme.GlassBorder
import com.exmple.movieexplorer.ui.theme.PurpleLight
import com.exmple.movieexplorer.ui.theme.PurplePrimary
import com.exmple.movieexplorer.ui.theme.RatingGold
import com.exmple.movieexplorer.ui.theme.TextMuted
import com.exmple.movieexplorer.ui.theme.TextPrimary
import com.exmple.movieexplorer.ui.theme.TextSecondary
import com.exmple.movieexplorer.viewmodel.MovieViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: MovieViewModel) {
    val movies by viewModel.movies.observeAsState(emptyList())
    val popularMovies by viewModel.popularMovies.observeAsState(emptyList())
    val trendingMovies by viewModel.trendingMovies.observeAsState(emptyList())
    val topRatedMovies by viewModel.topRatedMovies.observeAsState(emptyList())
    val nowPlayingMovies by viewModel.nowPlayingMovies.observeAsState(emptyList())

    var query by remember { mutableStateOf("") }
    val categories = remember { listOf("All", "Popular", "Trending", "Top Rated", "Now Playing") }
    var selectedCategory by remember { mutableStateOf("All") }

    val searchPool = when (selectedCategory) {
        "Popular" -> popularMovies
        "Trending" -> trendingMovies
        "Top Rated" -> topRatedMovies
        "Now Playing" -> nowPlayingMovies
        else -> {
            // Merge all categories and deduplicate by id
            val allMovies = movies + popularMovies + trendingMovies + topRatedMovies + nowPlayingMovies
            allMovies.distinctBy { it.id }
        }
    }

    val filteredMovies = remember(searchPool, query) {
        if (query.isBlank()) {
            searchPool
        } else {
            searchPool.filter { movie ->
                movie.title.contains(query, ignoreCase = true) ||
                    movie.overview.contains(query, ignoreCase = true)
            }
        }
    }

    var selectedMovie by remember { mutableStateOf<Movie?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = BackgroundDark,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Search",
                            style = MaterialTheme.typography.headlineLarge,
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
                    actions = {
                        IconButton(onClick = { navController.navigate("filter") }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Open Filters",
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
                // Search field with clear button
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = {
                        Text(
                            "Search movies, actors, genres...",
                            color = TextMuted
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = TextMuted
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = TextMuted
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurplePrimary,
                        unfocusedBorderColor = GlassBorder,
                        focusedLabelColor = PurpleLight,
                        unfocusedLabelColor = TextSecondary,
                        cursorColor = PurpleLight,
                        focusedContainerColor = CardSurface.copy(alpha = 0.5f),
                        unfocusedContainerColor = CardSurface.copy(alpha = 0.3f),
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                // Category chips
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = {
                                Text(category, style = MaterialTheme.typography.labelMedium)
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PurplePrimary,
                                selectedLabelColor = TextPrimary,
                                containerColor = CardSurface,
                                labelColor = TextSecondary
                            ),
                            shape = RoundedCornerShape(50),
                            border = if (selectedCategory == category) null
                            else FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = false,
                                borderColor = GlassBorder
                            )
                        )
                    }
                }

                // Results count
                Text(
                    text = "${filteredMovies.size} movies found",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextMuted,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp)
                )

                if (filteredMovies.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🔍",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No results found",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Text(
                            text = "Try a different search or category",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                } else if (query.isBlank()) {
                    // Show compact grid-style browsing when not searching
                    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                        // Quick picks horizontal row
                        item {
                            Text(
                                text = "Quick Picks",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                modifier = Modifier.padding(start = 18.dp, top = 8.dp, bottom = 8.dp)
                            )
                        }
                        item {
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(
                                    filteredMovies.take(6),
                                    key = { "quick_${it.id}" }
                                ) { movie ->
                                    CompactPosterCard(
                                        movie = movie,
                                        onClick = { selectedMovie = movie }
                                    )
                                }
                            }
                        }

                        // Full list
                        item {
                            Text(
                                text = "Browse All",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                modifier = Modifier.padding(
                                    start = 18.dp,
                                    top = 16.dp,
                                    bottom = 8.dp
                                )
                            )
                        }
                        itemsIndexed(
                            filteredMovies,
                            key = { _, m -> "browse_${m.id}" }
                        ) { index, movie ->
                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(movie.id) {
                                delay(index * 50L)
                                visible = true
                            }
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn(tween(300)) +
                                        slideInVertically(
                                            initialOffsetY = { it / 4 },
                                            animationSpec = tween(300)
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
                } else {
                    // Show search results
                    LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                        itemsIndexed(
                            filteredMovies,
                            key = { _, m -> "result_${m.id}" }
                        ) { index, movie ->
                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(movie.id) {
                                delay(index * 50L)
                                visible = true
                            }
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn(tween(300)) +
                                        slideInVertically(
                                            initialOffsetY = { it / 4 },
                                            animationSpec = tween(300)
                                        )
                            ) {
                                SearchResultCard(
                                    movie = movie,
                                    onClick = { selectedMovie = movie }
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
                relatedMovies = searchPool.filter { it.id != movie.id },
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

// Compact poster card for horizontal "Quick Picks"
@Composable
private fun CompactPosterCard(movie: Movie, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .border(0.5.dp, GlassBorder.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.posterUrl}",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Transparent,
                            BackgroundDark.copy(alpha = 0.8f)
                        )
                    )
                )
        )
        // Rating chip
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .glassmorphism(
                    shape = RoundedCornerShape(6.dp),
                    backgroundColor = Color(0x80000000),
                    borderColor = Color(0x33FFFFFF)
                )
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = RatingGold,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "${movie.rating}",
                    style = MaterialTheme.typography.labelSmall,
                    color = RatingGold
                )
            }
        }
        Text(
            text = movie.title,
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        )
    }
}

// Horizontal search result card (used when user is actively typing)
@Composable
private fun SearchResultCard(movie: Movie, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .glassmorphism(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = CardSurface.copy(alpha = 0.6f),
                borderColor = GlassBorder
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.posterUrl}",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(60.dp)
                .height(85.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = RatingGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${movie.rating}",
                    style = MaterialTheme.typography.labelMedium,
                    color = RatingGold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "₹${getRentalPricePerDay(movie.rating)}/day",
                    style = MaterialTheme.typography.labelMedium,
                    color = PurpleLight
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = movie.overview,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
