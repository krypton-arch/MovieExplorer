# Movie Explorer -- Project Report

> Complete source code listing for the Movie Explorer Android application.
> Generated on: 2026-03-18

---

## Table of Contents

1. Build Configuration
2. Android Manifest
3. Application Entry Point
4. Data Layer -- Models
5. Data Layer -- Local Database
6. Data Layer -- Remote API
7. Data Layer -- Repository
8. ViewModel
9. UI Theme
10. UI Components
11. UI Screens
12. Background Worker

---


## 1. Build Configuration


### build.gradle.kts (Project)

```kotlin
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

```


### settings.gradle.kts

```kotlin
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "movieexplorer"
include(":app")

```


### gradle/libs.versions.toml

```toml
[versions]
agp = "9.1.0"
coreKtx = "1.18.0"
junit = "4.13.2"
junitVersion = "1.3.0"
espressoCore = "3.7.0"
lifecycleRuntimeKtx = "2.10.0"
activityCompose = "1.13.0"
kotlin = "2.2.10"
composeBom = "2024.09.00"
room = "2.8.4"
retrofit = "2.11.0"
okhttp = "4.12.0"
coil = "2.7.0"
navigationCompose = "2.8.5"
workManager = "2.9.1"
lifecycleViewmodelCompose = "2.8.7"
materialIconsExtended = "1.7.8"

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-compose-ui-text-google-fonts = { group = "androidx.compose.ui", name = "ui-text-google-fonts" }
androidx-compose-runtime-livedata = { group = "androidx.compose.runtime", name = "runtime-livedata" }
# Room
androidx-room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
androidx-room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
androidx-room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
# Retrofit
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-gson = { group = "com.squareup.retrofit2", name = "converter-gson", version.ref = "retrofit" }
okhttp-logging = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp" }
# Coil
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }
# Navigation
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
# WorkManager
androidx-work-runtime-ktx = { group = "androidx.work", name = "work-runtime-ktx", version.ref = "workManager" }
# ViewModel Compose
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleViewmodelCompose" }
# Material Icons Extended
androidx-compose-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended", version.ref = "materialIconsExtended" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }

```


### app/build.gradle.kts

```kotlin
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.2.10-2.0.2"
}

android {
    namespace = "com.exmple.movieexplorer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.exmple.movieexplorer"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        val tmdbKey = gradleLocalProperties(rootDir, providers).getProperty("TMDB_API_KEY") ?: ""
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbKey\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Existing
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.text.google.fonts)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Retrofit + OkHttp
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    // Coil
    implementation(libs.coil.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    // ViewModel Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Material Icons Extended
    implementation(libs.androidx.compose.material.icons.extended)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

```


## 2. Android Manifest


### AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Movieexplorer">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.Movieexplorer">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>

```


## 3. Application Entry Point


### MainActivity.kt

```kotlin
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

```


## 4. Data Layer -- Models


### Movie.kt

```kotlin
package com.exmple.movieexplorer.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val title: String,
    @SerializedName("poster_path") val posterUrl: String?,
    @SerializedName("overview") val overview: String,
    @SerializedName("vote_average") val rating: Float
)

data class MovieResponse(val results: List<Movie>)

```


### RentalEntity.kt

```kotlin
package com.exmple.movieexplorer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rentals")
data class RentalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val movieId: Int,
    val title: String,
    val posterUrl: String,
    val rating: Float,
    val days: Int = 1
)

fun getRentalPricePerDay(rating: Float): Int = when {
    rating >= 8.0f -> 149
    rating >= 6.0f -> 99
    rating >= 4.0f -> 59
    else -> 29
}

fun totalPrice(rental: RentalEntity): Int = getRentalPricePerDay(rental.rating) * rental.days

```


## 5. Data Layer -- Local Database


### AppDatabase.kt

```kotlin
package com.exmple.movieexplorer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.exmple.movieexplorer.data.model.RentalEntity

@Database(entities = [RentalEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rentalDao(): RentalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_explorer_db"
                ).build().also { INSTANCE = it }
            }
    }
}

```


### RentalDao.kt

```kotlin
package com.exmple.movieexplorer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.exmple.movieexplorer.data.model.RentalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRental(rental: RentalEntity)

    @Query("SELECT * FROM rentals")
    fun getAllRentals(): Flow<List<RentalEntity>>

    @Query("SELECT * FROM rentals")
    suspend fun getAllRentalsOnce(): List<RentalEntity>

    @Query("UPDATE rentals SET days = :days WHERE id = :id")
    suspend fun updateRentalDays(id: Int, days: Int)

    @Delete
    suspend fun deleteRental(rental: RentalEntity)
}

```


## 6. Data Layer -- Remote API


### MovieApiService.kt

```kotlin
package com.exmple.movieexplorer.data.remote

import com.exmple.movieexplorer.BuildConfig
import com.exmple.movieexplorer.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse

    // Keep the old method signature for backward compatibility
    @GET("movie/popular")
    suspend fun getMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse
}

```


### RetrofitInstance.kt

```kotlin
package com.exmple.movieexplorer.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: MovieApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}

```


## 7. Data Layer -- Repository


### MovieRepository.kt

```kotlin
package com.exmple.movieexplorer.data.repository

import com.exmple.movieexplorer.data.local.RentalDao
import com.exmple.movieexplorer.data.model.Movie
import com.exmple.movieexplorer.data.model.RentalEntity
import com.exmple.movieexplorer.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val dao: RentalDao) {

    // Original method â€” kept for backward compatibility
    suspend fun fetchMovies(): List<Movie> = RetrofitInstance.api.getMovies().results

    // Category-based fetching
    suspend fun fetchPopularMovies(): List<Movie> = RetrofitInstance.api.getPopularMovies().results
    suspend fun fetchTrendingMovies(): List<Movie> = RetrofitInstance.api.getTrendingMovies().results
    suspend fun fetchTopRatedMovies(): List<Movie> = RetrofitInstance.api.getTopRatedMovies().results
    suspend fun fetchNowPlayingMovies(): List<Movie> = RetrofitInstance.api.getNowPlayingMovies().results
    suspend fun fetchUpcomingMovies(): List<Movie> = RetrofitInstance.api.getUpcomingMovies().results

    // Rental methods â€” unchanged
    fun getRentals(): Flow<List<RentalEntity>> = dao.getAllRentals()
    suspend fun rentMovie(rental: RentalEntity) = dao.insertRental(rental)
    suspend fun updateDays(id: Int, days: Int) = dao.updateRentalDays(id, days)
    suspend fun removeRental(rental: RentalEntity) = dao.deleteRental(rental)
    suspend fun getRentalsOnce(): List<RentalEntity> = dao.getAllRentalsOnce()
}

```


## 8. ViewModel


### MovieViewModel.kt

```kotlin
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

    // Original combined list â€” still used by Search, Filter, Explore screens
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

```


## 9. UI Theme


### Color.kt

```kotlin
package com.exmple.movieexplorer.ui.theme

import androidx.compose.ui.graphics.Color

// Backgrounds
val BackgroundDark = Color(0xFF0D0D1A)
val SurfaceDark = Color(0xFF161628)
val CardSurface = Color(0xFF1E1E35)

// Accent / Brand
val PurplePrimary = Color(0xFF6C3FE8)
val PurpleLight = Color(0xFF9B6DFF)
val PurpleContainer = Color(0xFF3A2070)

// Text
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB0B0C8)
val TextMuted = Color(0xFF6B6B8A)

// Status Colors
val RatingGold = Color(0xFFFFD700)
val ErrorRed = Color(0xFFFF4F4F)
val SuccessGreen = Color(0xFF4CAF50)

// Gradient Colors
val GradientStart = Color(0xFF6C3FE8)
val GradientMid = Color(0xFF8B5CF6)
val GradientEnd = Color(0xFF9B6DFF)
val GradientDarkStart = Color(0xFF0D0D1A)
val GradientDarkEnd = Color(0x000D0D1A)

// Glassmorphism Colors
val GlassSurface = Color(0x26FFFFFF)
val GlassBorder = Color(0x33FFFFFF)
val GlassSurfaceDark = Color(0x401E1E35)

// Story Ring Gradient
val StoryRingStart = Color(0xFF6C3FE8)
val StoryRingEnd = Color(0xFFE040FB)

// Shimmer
val ShimmerBase = Color(0xFF1E1E35)
val ShimmerHighlight = Color(0xFF2A2A4A)

```


### Type.kt

```kotlin
package com.exmple.movieexplorer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

private val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = emptyList()         // Works on real devices with GMS
)

private val outfitFont = GoogleFont("Outfit")
private val interFont = GoogleFont("Inter")

val OutfitFamily = FontFamily(
    Font(googleFont = outfitFont, fontProvider = fontProvider, weight = FontWeight.Light),
    Font(googleFont = outfitFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = outfitFont, fontProvider = fontProvider, weight = FontWeight.Medium),
    Font(googleFont = outfitFont, fontProvider = fontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = outfitFont, fontProvider = fontProvider, weight = FontWeight.Bold),
    Font(googleFont = outfitFont, fontProvider = fontProvider, weight = FontWeight.ExtraBold),
)

val InterFamily = FontFamily(
    Font(googleFont = interFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = interFont, fontProvider = fontProvider, weight = FontWeight.Medium),
    Font(googleFont = interFont, fontProvider = fontProvider, weight = FontWeight.SemiBold),
)

val MovieTypography = Typography(
    // Hero / screen titles
    headlineLarge = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary,
        letterSpacing = (-0.5).sp,
        lineHeight = 36.sp
    ),
    // Section headings
    headlineMedium = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary,
        letterSpacing = (-0.3).sp,
        lineHeight = 30.sp
    ),
    // Card titles
    titleLarge = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary,
        letterSpacing = (-0.2).sp,
        lineHeight = 24.sp
    ),
    // Subtitles
    titleMedium = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = TextPrimary,
        letterSpacing = 0.sp,
        lineHeight = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = TextSecondary,
        letterSpacing = 0.1.sp,
        lineHeight = 18.sp
    ),
    // Body text
    bodyLarge = TextStyle(
        fontFamily = InterFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = TextSecondary,
        letterSpacing = 0.1.sp,
        lineHeight = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFamily,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = TextSecondary,
        letterSpacing = 0.1.sp,
        lineHeight = 18.sp
    ),
    bodySmall = TextStyle(
        fontFamily = InterFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = TextMuted,
        letterSpacing = 0.2.sp,
        lineHeight = 16.sp
    ),
    // Label / badges / chips
    labelLarge = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary,
        letterSpacing = 0.3.sp
    ),
    labelSmall = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        color = TextMuted,
        letterSpacing = 0.4.sp
    )
)

```


### Theme.kt

```kotlin
package com.exmple.movieexplorer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = TextPrimary,
    primaryContainer = PurpleContainer,
    secondary = PurpleLight,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = CardSurface,
    error = ErrorRed
)

@Composable
fun MovieExplorerTheme(content: @Composable () -> Unit) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MovieTypography,
        content = content
    )
}

```


## 10. UI Components


### GlassModifiers.kt

```kotlin
package com.exmple.movieexplorer.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.exmple.movieexplorer.ui.theme.GlassBorder
import com.exmple.movieexplorer.ui.theme.GlassSurface
import com.exmple.movieexplorer.ui.theme.ShimmerBase
import com.exmple.movieexplorer.ui.theme.ShimmerHighlight

fun Modifier.glassmorphism(
    shape: Shape = RoundedCornerShape(20.dp),
    backgroundColor: Color = GlassSurface,
    borderColor: Color = GlassBorder,
    borderWidth: Dp = 0.5.dp
): Modifier = this
    .background(backgroundColor, shape)
    .border(borderWidth, borderColor, shape)

@Composable
fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateX by transition.animateFloat(
        initialValue = -300f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslateX"
    )

    val brush = Brush.linearGradient(
        colors = listOf(ShimmerBase, ShimmerHighlight, ShimmerBase),
        start = Offset(translateX, 0f),
        end = Offset(translateX + 300f, 0f)
    )

    this.background(brush, RoundedCornerShape(12.dp))
}

```


### AppBottomBar.kt

```kotlin
package com.exmple.movieexplorer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.exmple.movieexplorer.ui.theme.GlassBorder
import com.exmple.movieexplorer.ui.theme.GlassSurface
import com.exmple.movieexplorer.ui.theme.PurpleContainer
import com.exmple.movieexplorer.ui.theme.PurpleLight
import com.exmple.movieexplorer.ui.theme.PurplePrimary
import com.exmple.movieexplorer.ui.theme.SurfaceDark
import com.exmple.movieexplorer.ui.theme.TextMuted
import com.exmple.movieexplorer.ui.theme.TextPrimary

@Composable
fun AppBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box {
        NavigationBar(
            containerColor = SurfaceDark.copy(alpha = 0.92f),
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .border(
                    width = 0.5.dp,
                    color = GlassBorder,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
        ) {
            NavigationBarItem(
                selected = currentRoute == "home",
                onClick = { navController.navigateBottomRoute("home") },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") },
                colors = navItemColors()
            )
            NavigationBarItem(
                selected = currentRoute == "explore",
                onClick = { navController.navigateBottomRoute("explore") },
                icon = { Icon(Icons.Default.Explore, contentDescription = "Explore") },
                label = { Text("Explore") },
                colors = navItemColors()
            )
            // Spacer for center FAB
            Spacer(modifier = Modifier.width(56.dp))
            NavigationBarItem(
                selected = currentRoute == "rental",
                onClick = { navController.navigateBottomRoute("rental") },
                icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite") },
                label = { Text("Favorite") },
                colors = navItemColors()
            )
            NavigationBarItem(
                selected = currentRoute == "profile",
                onClick = { navController.navigateBottomRoute("profile") },
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile") },
                colors = navItemColors()
            )
        }
    }
}

@Composable
private fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = PurpleLight,
    unselectedIconColor = TextMuted,
    selectedTextColor = PurpleLight,
    unselectedTextColor = TextMuted,
    indicatorColor = PurpleContainer.copy(alpha = 0.5f)
)

private fun NavController.navigateBottomRoute(route: String) {
    navigate(route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
    }
}

```


### MovieCard.kt

```kotlin
package com.exmple.movieexplorer.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.exmple.movieexplorer.data.model.Movie
import com.exmple.movieexplorer.data.model.getRentalPricePerDay
import com.exmple.movieexplorer.ui.theme.BackgroundDark
import com.exmple.movieexplorer.ui.theme.CardSurface
import com.exmple.movieexplorer.ui.theme.PurpleLight
import com.exmple.movieexplorer.ui.theme.PurplePrimary
import com.exmple.movieexplorer.ui.theme.RatingGold
import com.exmple.movieexplorer.ui.theme.TextMuted
import com.exmple.movieexplorer.ui.theme.TextPrimary
import com.exmple.movieexplorer.ui.theme.TextSecondary

@Composable
fun MovieCard(movie: Movie, onRentClick: () -> Unit, onCardClick: (() -> Unit)? = null) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        label = "cardScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onCardClick?.invoke() ?: onRentClick() }
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            // Full-width poster
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterUrl}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
            )

            // Gradient overlay at bottom
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                BackgroundDark.copy(alpha = 0.7f),
                                BackgroundDark.copy(alpha = 0.95f)
                            ),
                            startY = 50f
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
            )

            // Rating badge top-right
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .glassmorphism(
                        shape = RoundedCornerShape(10.dp),
                        backgroundColor = Color(0x80000000),
                        borderColor = Color(0x33FFFFFF)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = RatingGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = movie.rating.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        color = RatingGold
                    )
                }
            }

            // Price badge top-left
            val price = getRentalPricePerDay(movie.rating)
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .background(
                        PurplePrimary.copy(alpha = 0.85f),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "â‚¹$price/day",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextPrimary
                )
            }

            // Bottom overlay info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, end = 40.dp, bottom = 16.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Action",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Text(text = "â€¢", color = TextMuted)
                    Text(
                        text = "${(movie.rating * 10).toInt()}m views",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            // MoreVert button bottom-right
            IconButton(
                onClick = { onCardClick?.invoke() ?: onRentClick() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = TextMuted
                )
            }
        }
    }
}

```


### MovieDetailSheet.kt

```kotlin
package com.exmple.movieexplorer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.exmple.movieexplorer.data.model.Movie
import com.exmple.movieexplorer.data.model.getRentalPricePerDay
import com.exmple.movieexplorer.ui.theme.BackgroundDark
import com.exmple.movieexplorer.ui.theme.CardSurface
import com.exmple.movieexplorer.ui.theme.GlassBorder
import com.exmple.movieexplorer.ui.theme.GlassSurface
import com.exmple.movieexplorer.ui.theme.PurpleLight
import com.exmple.movieexplorer.ui.theme.PurplePrimary
import com.exmple.movieexplorer.ui.theme.RatingGold
import com.exmple.movieexplorer.ui.theme.TextMuted
import com.exmple.movieexplorer.ui.theme.TextPrimary
import com.exmple.movieexplorer.ui.theme.TextSecondary

@Composable
fun MovieDetailSheet(
    movie: Movie,
    relatedMovies: List<Movie>,
    onDismiss: () -> Unit,
    onRentClick: () -> Unit,
    onRelatedMovieClick: (Movie) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC0D0D1A))
            .clickable(onClick = onDismiss)
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            ) + fadeIn(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = false) {} // Block click-through
                    .verticalScroll(rememberScrollState())
            ) {
                // Poster backdrop
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                ) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w780${movie.posterUrl}",
                        contentDescription = movie.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, BackgroundDark),
                                    startY = 200f
                                )
                            )
                    )
                    // Close button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .background(GlassSurface, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextPrimary
                        )
                    }
                }

                // Info section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundDark)
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Genre & meta row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GenrePill("Action")
                        Text("â€¢", color = TextMuted)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = RatingGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${movie.rating}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = RatingGold
                            )
                        }
                        Text("â€¢", color = TextMuted)
                        Text(
                            text = "â‚¹${getRentalPricePerDay(movie.rating)}/day",
                            style = MaterialTheme.typography.labelMedium,
                            color = PurpleLight
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action buttons row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ActionChip(icon = Icons.Default.ThumbUp, label = "${(movie.rating * 100).toInt()}K")
                        ActionChip(icon = Icons.Default.Share, label = "Share")
                        ActionChip(icon = Icons.Default.PlayArrow, label = "Trailer")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Overview
                    Text(
                        text = movie.overview,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Rent button
                    Button(
                        onClick = onRentClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "Rent Movie â€¢ â‚¹${getRentalPricePerDay(movie.rating)}/day",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Next Play section
                    if (relatedMovies.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Next Play",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimary
                            )
                            Text(
                                text = "See All",
                                style = MaterialTheme.typography.labelMedium,
                                color = PurpleLight
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(end = 16.dp)
                        ) {
                            items(relatedMovies.take(8), key = { it.id }) { related ->
                                RelatedMovieItem(
                                    movie = related,
                                    onClick = { onRelatedMovieClick(related) }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun GenrePill(genre: String) {
    Box(
        modifier = Modifier
            .glassmorphism(
                shape = RoundedCornerShape(50),
                backgroundColor = PurplePrimary.copy(alpha = 0.3f),
                borderColor = PurpleLight.copy(alpha = 0.3f)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = genre,
            style = MaterialTheme.typography.labelMedium,
            color = PurpleLight
        )
    }
}

@Composable
private fun ActionChip(icon: ImageVector, label: String) {
    Row(
        modifier = Modifier
            .glassmorphism(
                shape = RoundedCornerShape(12.dp),
                backgroundColor = CardSurface,
                borderColor = GlassBorder
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = PurpleLight,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = TextSecondary
        )
    }
}

@Composable
private fun RelatedMovieItem(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w300${movie.posterUrl}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
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
        }
    }
}

```


### RentalCard.kt

```kotlin
package com.exmple.movieexplorer.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.exmple.movieexplorer.data.model.RentalEntity
import com.exmple.movieexplorer.data.model.getRentalPricePerDay
import com.exmple.movieexplorer.ui.theme.CardSurface
import com.exmple.movieexplorer.ui.theme.ErrorRed
import com.exmple.movieexplorer.ui.theme.GlassBorder
import com.exmple.movieexplorer.ui.theme.PurpleLight
import com.exmple.movieexplorer.ui.theme.PurplePrimary
import com.exmple.movieexplorer.ui.theme.RatingGold

@Composable
fun RentalCard(
    rental: RentalEntity,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    val pricePerDay = getRentalPricePerDay(rental.rating)
    val total = pricePerDay * rental.days

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(
                width = 0.5.dp,
                brush = Brush.linearGradient(
                    listOf(PurplePrimary.copy(alpha = 0.4f), GlassBorder)
                ),
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface.copy(alpha = 0.85f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${rental.posterUrl}",
                contentDescription = rental.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(70.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = rental.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = RatingGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = " ${rental.rating}",
                        style = MaterialTheme.typography.bodySmall,
                        color = RatingGold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease",
                            tint = PurpleLight
                        )
                    }
                    Text(
                        text = "${rental.days} day${if (rental.days > 1) "s" else ""}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase",
                            tint = PurpleLight
                        )
                    }
                }
                Text(
                    text = "\u20B9$pricePerDay/day Ã— ${rental.days} = \u20B9$total",
                    style = MaterialTheme.typography.bodySmall,
                    color = PurpleLight
                )
            }
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = ErrorRed
                )
            }
        }
    }
}

```


## 11. UI Screens


### HomeScreen.kt

```kotlin
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

                    // â”€â”€ Now Playing horizontal carousel â”€â”€
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

                    // â”€â”€ Trending This Week horizontal carousel â”€â”€
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

                    // â”€â”€ Active category vertical list â”€â”€
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

                    // â”€â”€ Top Rated horizontal carousel â”€â”€
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

                    // â”€â”€ Upcoming horizontal carousel â”€â”€
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

// â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ Section Header â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

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

// â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ Horizontal Poster Carousel â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

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
                text = "â‚¹${getRentalPricePerDay(movie.rating)}/d",
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

// â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ Animated Card Item â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

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

// â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€ Story Row â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

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

```


### ExploreScreen.kt

```kotlin
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
                text = "â˜… ${movie.rating}  â€¢  ${movie.overview.take(50)}...",
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

```


### SearchScreen.kt

```kotlin
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
                            text = "ðŸ”",
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
                    text = "â‚¹${getRentalPricePerDay(movie.rating)}/day",
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

```


### FilterScreen.kt

```kotlin
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

```


### RentalScreen.kt

```kotlin
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

```


### ProfileScreen.kt

```kotlin
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
            // â”€â”€ Profile Card â”€â”€
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

            // â”€â”€ Editable Bio & Genre (shown only in edit mode) â”€â”€
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
                            text = "Tap âœ“ in the top bar to save changes",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }
            }

            // â”€â”€ User Info Card (shown when NOT editing) â”€â”€
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

            // â”€â”€ Stats card â”€â”€
            StatsCard(
                availableMovies = movies.size,
                rentedMovies = rentals.size,
                totalDays = totalDays,
                averageRating = averageRating
            )

            // â”€â”€ Action buttons â”€â”€
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

```


## 12. Background Worker


### RentalReminderWorker.kt

```kotlin
package com.exmple.movieexplorer.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.exmple.movieexplorer.data.local.AppDatabase

class RentalReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.getInstance(context)
        val rentals = db.rentalDao().getAllRentalsOnce()

        if (rentals.isNotEmpty()) {
            showNotification(
                title = "Active Rentals",
                message = "You have ${rentals.size} movie(s) rented. Don't forget to watch."
            )
        }
        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val channelId = "rental_reminder_channel"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Rental Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        manager.notify(1001, notification)
    }
}

```


---

> End of Project Report

