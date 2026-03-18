package com.exmple.movieexplorer.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.exmple.movieexplorer.data.model.Movie
import com.exmple.movieexplorer.data.model.getRentalPricePerDay
import com.exmple.movieexplorer.ui.components.AppBottomBar
import com.exmple.movieexplorer.ui.components.MovieCard
import com.exmple.movieexplorer.ui.components.MovieDetailSheet
import com.exmple.movieexplorer.ui.components.glassmorphism
import com.exmple.movieexplorer.ui.theme.BackgroundDark
import com.exmple.movieexplorer.ui.theme.CardSurface
import com.exmple.movieexplorer.ui.theme.GlassBorder
import com.exmple.movieexplorer.ui.theme.PurpleContainer
import com.exmple.movieexplorer.ui.theme.PurpleLight
import com.exmple.movieexplorer.ui.theme.PurplePrimary
import com.exmple.movieexplorer.ui.theme.RatingGold
import com.exmple.movieexplorer.ui.theme.StoryRingEnd
import com.exmple.movieexplorer.ui.theme.StoryRingStart
import com.exmple.movieexplorer.ui.theme.TextMuted
import com.exmple.movieexplorer.ui.theme.TextPrimary
import com.exmple.movieexplorer.ui.theme.TextSecondary
import com.exmple.movieexplorer.viewmodel.MovieViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: MovieViewModel) {
    val movies by viewModel.movies.observeAsState(emptyList())
    val popularMovies by viewModel.popularMovies.observeAsState(emptyList())
    val trendingMovies by viewModel.trendingMovies.observeAsState(emptyList())
    val topRatedMovies by viewModel.topRatedMovies.observeAsState(emptyList())
    val nowPlayingMovies by viewModel.nowPlayingMovies.observeAsState(emptyList())
    val upcomingMovies by viewModel.upcomingMovies.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)

    val genres = remember { listOf("Trending", "Now Playing", "Top Rated", "Upcoming", "Popular") }
    var selectedGenre by remember { mutableStateOf("Trending") }

    var selectedMovie by remember { mutableStateOf<Movie?>(null) }

    // Pick the list based on selected genre chip
    val activeMovies = when (selectedGenre) {
        "Trending" -> trendingMovies
        "Now Playing" -> nowPlayingMovies
        "Top Rated" -> topRatedMovies
        "Upcoming" -> upcomingMovies
        "Popular" -> popularMovies
        else -> movies
    }

    // All movies for the detail sheet "related" list
    val allMovies = movies.ifEmpty { popularMovies }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = BackgroundDark,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Home",
                            style = MaterialTheme.typography.headlineLarge,
                            color = TextPrimary
                        )
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("search") }) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .glassmorphism(
                                        shape = CircleShape,
                                        backgroundColor = CardSurface.copy(alpha = 0.6f),
                                        borderColor = GlassBorder
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        IconButton(onClick = { navController.navigate("profile") }) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .glassmorphism(
                                        shape = CircleShape,
                                        backgroundColor = CardSurface.copy(alpha = 0.6f),
                                        borderColor = GlassBorder
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Alerts",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
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
                        .size(62.dp)
                        .border(
                            width = 2.5.dp,
                            brush = Brush.radialGradient(
                                listOf(PurpleLight, PurplePrimary.copy(alpha = 0.2f))
                            ),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Rental",
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = { AppBottomBar(navController) }
        ) { innerPadding ->
            if (isLoading && movies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PurplePrimary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(BackgroundDark),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    // Story row
                    item { StoryRow() }

                    // Genre / category chips
                    item {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(genres) { genre ->
                                FilterChip(
                                    selected = selectedGenre == genre,
                                    onClick = { selectedGenre = genre },
                                    label = {
                                        Text(
                                            text = genre,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PurplePrimary,
                                        selectedLabelColor = TextPrimary,
                                        containerColor = CardSurface,
                                        labelColor = TextSecondary
                                    ),
                                    shape = RoundedCornerShape(50),
                                    border = if (selectedGenre == genre) null
                                    else FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = false,
                                        borderColor = GlassBorder
                                    )
                                )
                            }
                        }
                    }

                    // ── Now Playing horizontal carousel ──
                    if (nowPlayingMovies.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Now Playing",
                                subtitle = "In theaters right now"
                            )
                        }
                        item {
                            HorizontalPosterCarousel(
                                movies = nowPlayingMovies.take(8),
                                onMovieClick = { selectedMovie = it }
                            )
                        }
                    }

                    // ── Trending This Week horizontal carousel ──
                    if (trendingMovies.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Trending This Week",
                                subtitle = "What everyone's watching"
                            )
                        }
                        item {
                            HorizontalPosterCarousel(
                                movies = trendingMovies.take(8),
                                onMovieClick = { selectedMovie = it }
                            )
                        }
                    }

                    // ── Active category vertical list ──
                    item {
                        SectionHeader(
                            title = selectedGenre,
                            action = "${activeMovies.size} movies"
                        )
                    }

                    if (activeMovies.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Loading...",
                                    color = TextSecondary,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    } else {
                        itemsIndexed(
                            activeMovies,
                            key = { _, movie -> "vertical_${movie.id}" }
                        ) { index, movie ->
                            AnimatedMovieCardItem(index = index, movie = movie) {
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

                    // ── Top Rated horizontal carousel ──
                    if (topRatedMovies.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Top Rated",
                                subtitle = "Critically acclaimed"
                            )
                        }
                        item {
                            HorizontalPosterCarousel(
                                movies = topRatedMovies.take(8),
                                onMovieClick = { selectedMovie = it }
                            )
                        }
                    }

                    // ── Upcoming horizontal carousel ──
                    if (upcomingMovies.isNotEmpty()) {
                        item {
                            SectionHeader(
                                title = "Coming Soon",
                                subtitle = "Mark your calendar"
                            )
                        }
                        item {
                            HorizontalPosterCarousel(
                                movies = upcomingMovies.take(8),
                                onMovieClick = { selectedMovie = it }
                            )
                        }
                    }
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

// ──────────────── Section Header ────────────────

@Composable
private fun SectionHeader(title: String, subtitle: String? = null, action: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }
        if (action != null) {
            Text(
                text = action,
                style = MaterialTheme.typography.labelMedium,
                color = PurpleLight
            )
        }
    }
}

// ──────────────── Horizontal Poster Carousel ────────────────

@Composable
private fun HorizontalPosterCarousel(movies: List<Movie>, onMovieClick: (Movie) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        itemsIndexed(movies, key = { _, m -> "carousel_${m.id}" }) { index, movie ->
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(movie.id) {
                delay(index * 120L)
                visible = true
            }
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(500)) + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )
            ) {
                PosterCard(movie = movie, onClick = { onMovieClick(movie) })
            }
        }
    }
}

@Composable
private fun PosterCard(movie: Movie, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "posterScale"
    )

    Box(
        modifier = Modifier
            .width(180.dp)
            .height(270.dp)
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .border(1.dp, GlassBorder.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.posterUrl}",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            BackgroundDark.copy(alpha = 0.5f),
                            BackgroundDark.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        // Rating badge
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .glassmorphism(
                    shape = RoundedCornerShape(8.dp),
                    backgroundColor = Color(0x80000000),
                    borderColor = Color(0x33FFFFFF)
                )
                .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = RatingGold,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "${movie.rating}",
                    style = MaterialTheme.typography.labelSmall,
                    color = RatingGold
                )
            }
        }

        // Price badge
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .background(
                    PurplePrimary.copy(alpha = 0.8f),
                    RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "₹${getRentalPricePerDay(movie.rating)}/d",
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary
            )
        }

        // Title overlay at bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, end = 50.dp, bottom = 12.dp)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp
                ),
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${(movie.rating * 10).toInt()}m views",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary.copy(alpha = 0.8f)
            )
        }
    }
}

// ──────────────── Animated Card Item ────────────────

@Composable
private fun AnimatedMovieCardItem(
    index: Int,
    movie: Movie,
    content: @Composable () -> Unit
) {
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
        content()
    }
}

// ──────────────── Story Row ────────────────

@Composable
private fun StoryRow() {
    val stories = remember {
        listOf(
            "DSL" to true,
            "Alex" to false,
            "Sam" to false,
            "Lily" to false,
            "Max" to false,
            "Nora" to false
        )
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(stories) { (name, isSpecial) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(
                                brush = Brush.sweepGradient(
                                    listOf(StoryRingStart, StoryRingEnd, StoryRingStart)
                                ),
                                shape = CircleShape
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(BackgroundDark, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                if (isSpecial) Brush.linearGradient(
                                    listOf(PurplePrimary, PurpleLight)
                                )
                                else Brush.linearGradient(
                                    listOf(PurpleContainer, CardSurface)
                                ),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name.take(if (isSpecial) 3 else 1),
                            style = if (isSpecial) MaterialTheme.typography.labelLarge
                            else MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    }
                    if (isSpecial) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .offset(x = 2.dp, y = 2.dp)
                                .size(20.dp)
                                .background(PurplePrimary, CircleShape)
                                .border(2.dp, BackgroundDark, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add",
                                tint = TextPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isSpecial) "" else name,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
