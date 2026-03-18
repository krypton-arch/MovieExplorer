package com.exmple.movieexplorer.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.exmple.movieexplorer.ui.components.AppBottomBar
import com.exmple.movieexplorer.ui.theme.BackgroundDark
import com.exmple.movieexplorer.ui.theme.CardSurface
import com.exmple.movieexplorer.ui.theme.GlassBorder
import com.exmple.movieexplorer.ui.theme.PurpleContainer
import com.exmple.movieexplorer.ui.theme.PurpleLight
import com.exmple.movieexplorer.ui.theme.PurplePrimary
import com.exmple.movieexplorer.ui.theme.StoryRingEnd
import com.exmple.movieexplorer.ui.theme.StoryRingStart
import com.exmple.movieexplorer.ui.theme.TextMuted
import com.exmple.movieexplorer.ui.theme.TextPrimary
import com.exmple.movieexplorer.ui.theme.TextSecondary
import com.exmple.movieexplorer.viewmodel.MovieViewModel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private const val PREFS_NAME = "movie_explorer_profile"
private const val KEY_NAME = "user_name"
private const val KEY_BIO = "user_bio"
private const val KEY_FAVORITE_GENRE = "favorite_genre"

private fun loadProfile(context: Context): Triple<String, String, String> {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return Triple(
        prefs.getString(KEY_NAME, "Movie Explorer User") ?: "Movie Explorer User",
        prefs.getString(KEY_BIO, "Keep discovering great cinema.") ?: "Keep discovering great cinema.",
        prefs.getString(KEY_FAVORITE_GENRE, "Action") ?: "Action"
    )
}

private fun saveProfile(context: Context, name: String, bio: String, genre: String) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(KEY_NAME, name)
        .putString(KEY_BIO, bio)
        .putString(KEY_FAVORITE_GENRE, genre)
        .apply()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: MovieViewModel) {
    val context = LocalContext.current
    val rentals by viewModel.rentals.collectAsState()
    val movies by viewModel.movies.observeAsState(emptyList())

    // Load profile from SharedPreferences
    val (savedName, savedBio, savedGenre) = remember { loadProfile(context) }
    var displayName by remember { mutableStateOf(savedName) }
    var bio by remember { mutableStateOf(savedBio) }
    var favoriteGenre by remember { mutableStateOf(savedGenre) }
    var isEditing by remember { mutableStateOf(false) }

    // Editing fields
    var editName by remember { mutableStateOf(savedName) }
    var editBio by remember { mutableStateOf(savedBio) }
    var editGenre by remember { mutableStateOf(savedGenre) }

    val totalDays = rentals.sumOf { it.days }
    val averageRating = if (rentals.isNotEmpty()) {
        ((rentals.map { it.rating }.average() * 10).roundToInt()) / 10f
    } else {
        0f
    }

    // Initials for avatar
    val initials = remember(displayName) {
        displayName.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.take(1).uppercase() }
            .ifEmpty { "ME" }
    }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextPrimary
                    )
                },
                actions = {
                    // Edit / Save button
                    IconButton(onClick = {
                        if (isEditing) {
                            // Save
                            displayName = editName.ifBlank { "Movie Explorer User" }
                            bio = editBio.ifBlank { "Keep discovering great cinema." }
                            favoriteGenre = editGenre.ifBlank { "Action" }
                            saveProfile(context, displayName, bio, favoriteGenre)
                        } else {
                            // Enter edit mode
                            editName = displayName
                            editBio = bio
                            editGenre = favoriteGenre
                        }
                        isEditing = !isEditing
                    }) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = if (isEditing) "Save" else "Edit",
                            tint = if (isEditing) PurpleLight else TextPrimary
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ── Profile Card ──
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, GlassBorder, RoundedCornerShape(22.dp)),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface.copy(alpha = 0.85f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Gradient ring avatar
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(
                                    brush = Brush.sweepGradient(
                                        listOf(StoryRingStart, StoryRingEnd, StoryRingStart)
                                    ),
                                    shape = CircleShape
                                )
                        )
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(PurpleContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedContent(
                                targetState = initials,
                                transitionSpec = {
                                    fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                                },
                                label = "avatarInitials"
                            ) { text ->
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        if (isEditing) {
                            // Editable name
                            OutlinedTextField(
                                value = editName,
                                onValueChange = { editName = it },
                                label = { Text("Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = profileFieldColors()
                            )
                        } else {
                            Text(
                                text = displayName,
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = bio,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // ── Editable Bio & Genre (shown only in edit mode) ──
            AnimatedVisibility(visible = isEditing) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, PurplePrimary.copy(alpha = 0.4f), RoundedCornerShape(22.dp)),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface.copy(alpha = 0.85f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Edit Profile",
                            style = MaterialTheme.typography.titleMedium,
                            color = PurpleLight
                        )
                        OutlinedTextField(
                            value = editBio,
                            onValueChange = { editBio = it },
                            label = { Text("Bio") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            maxLines = 3,
                            shape = RoundedCornerShape(12.dp),
                            colors = profileFieldColors()
                        )
                        OutlinedTextField(
                            value = editGenre,
                            onValueChange = { editGenre = it },
                            label = { Text("Favorite Genre") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = profileFieldColors()
                        )
                        Text(
                            text = "Tap ✓ in the top bar to save changes",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }
            }

            // ── User Info Card (shown when NOT editing) ──
            AnimatedVisibility(visible = !isEditing) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, GlassBorder, RoundedCornerShape(22.dp)),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface.copy(alpha = 0.85f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "About Me",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        ProfileInfoRow(label = "Bio", value = bio)
                        ProfileInfoRow(label = "Fav Genre", value = favoriteGenre)
                    }
                }
            }

            // ── Stats card ──
            StatsCard(
                availableMovies = movies.size,
                rentedMovies = rentals.size,
                totalDays = totalDays,
                averageRating = averageRating
            )

            // ── Action buttons ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { navController.navigate("rental") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.BookmarkBorder, contentDescription = null)
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("My Rentals")
                }
                Button(
                    onClick = { navController.navigate("search") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleContainer),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = null)
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Search")
                }
            }

            // Bottom spacer for FAB clearance
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun profileFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = PurplePrimary,
    unfocusedBorderColor = GlassBorder,
    focusedLabelColor = PurpleLight,
    unfocusedLabelColor = TextSecondary,
    cursorColor = PurpleLight,
    focusedContainerColor = CardSurface.copy(alpha = 0.3f),
    unfocusedContainerColor = CardSurface.copy(alpha = 0.15f),
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary
)

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = PurpleLight,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun StatsCard(
    availableMovies: Int,
    rentedMovies: Int,
    totalDays: Int,
    averageRating: Float
) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(200)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) +
                slideInVertically(initialOffsetY = { it / 4 }, animationSpec = tween(500))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, GlassBorder, RoundedCornerShape(22.dp)),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurface.copy(alpha = 0.85f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Account Snapshot",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                ProfileStat(label = "Movies Available", value = availableMovies.toString())
                ProfileStat(label = "Active Rentals", value = rentedMovies.toString())
                ProfileStat(label = "Total Rental Days", value = totalDays.toString())
                ProfileStat(label = "Avg Rental Rating", value = averageRating.toString())
            }
        }
    }
}

@Composable
private fun ProfileStat(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = PurpleLight
        )
    }
}
