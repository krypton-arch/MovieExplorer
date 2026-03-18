package com.exmple.movieexplorer.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.exmple.movieexplorer.data.model.totalPrice
import com.exmple.movieexplorer.ui.components.RentalCard
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
fun RentalScreen(navController: NavController, viewModel: MovieViewModel) {
    val rentals by viewModel.rentals.collectAsState()
    val totalRentalPrice by remember(rentals) {
        derivedStateOf { rentals.sumOf { totalPrice(it) } }
    }

    Scaffold(
        containerColor = BackgroundDark,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Rentals",
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
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 0.5.dp,
                        color = GlassBorder,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    ),
                color = CardSurface.copy(alpha = 0.9f),
                tonalElevation = 8.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Rental Price",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = "\u20B9$totalRentalPrice",
                        style = MaterialTheme.typography.headlineMedium,
                        color = PurplePrimary
                    )
                }
            }
        }
    ) { innerPadding ->
        if (rentals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No rentals yet. Rent a movie from Home.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                itemsIndexed(rentals, key = { _, rental -> rental.id }) { index, rental ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(rental.id) {
                        delay(index * 100L)
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
                        RentalCard(
                            rental = rental,
                            onIncrease = { viewModel.increaseDays(rental) },
                            onDecrease = { viewModel.decreaseDays(rental) },
                            onRemove = { viewModel.removeRental(rental) }
                        )
                    }
                }
            }
        }
    }
}
