package com.exmple.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.exmple.movieexplorer.data.local.AppDatabase
import com.exmple.movieexplorer.data.repository.MovieRepository
import com.exmple.movieexplorer.ui.screens.ExploreScreen
import com.exmple.movieexplorer.ui.screens.FilterScreen
import com.exmple.movieexplorer.ui.screens.HomeScreen
import com.exmple.movieexplorer.ui.screens.ProfileScreen
import com.exmple.movieexplorer.ui.screens.RentalScreen
import com.exmple.movieexplorer.ui.screens.SearchScreen
import com.exmple.movieexplorer.ui.theme.MovieExplorerTheme
import com.exmple.movieexplorer.viewmodel.MovieViewModel
import com.exmple.movieexplorer.viewmodel.MovieViewModelFactory
import com.exmple.movieexplorer.worker.RentalReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        scheduleRentalReminder()

        setContent {
            MovieExplorerTheme {
                val navController = rememberNavController()
                val db = remember { AppDatabase.getInstance(applicationContext) }
                val repo = remember { MovieRepository(db.rentalDao()) }
                val movieViewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(repo))

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController = navController, viewModel = movieViewModel)
                    }
                    composable("explore") {
                        ExploreScreen(navController = navController, viewModel = movieViewModel)
                    }
                    composable("search") {
                        SearchScreen(navController = navController, viewModel = movieViewModel)
                    }
                    composable("filter") {
                        FilterScreen(navController = navController, viewModel = movieViewModel)
                    }
                    composable("rental") {
                        RentalScreen(navController = navController, viewModel = movieViewModel)
                    }
                    composable("profile") {
                        ProfileScreen(navController = navController, viewModel = movieViewModel)
                    }
                }
            }
        }
    }

    private fun scheduleRentalReminder() {
        val request = PeriodicWorkRequestBuilder<RentalReminderWorker>(1, TimeUnit.DAYS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "rental_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
