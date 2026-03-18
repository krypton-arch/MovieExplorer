# Agent.md — Movie Explorer & Watchlist App Implementation Guide

## Project Overview
Build a **Movie Explorer & Watchlist App** using Kotlin, Jetpack Compose, MVVM architecture, Room Database, Retrofit, and WorkManager. Follow every instruction in `Dependencies.md` for versions and `Ui.md` for design.

---

## Architecture: MVVM

```
UI Layer (Compose Screens)
    ↓ observes
ViewModel Layer (StateFlow / LiveData)
    ↓ calls
Repository Layer (single source of truth)
    ↙           ↘
Remote (Retrofit)   Local (Room DB)
```

---

## Step 1 — Project Package Structure

Create the following package structure under `com.exmple.movieexplorer`:

```
com.exmple.movieexplorer/
├── data/
│   ├── model/
│   │   ├── Movie.kt                  ← API response model
│   │   └── RentalEntity.kt           ← Room entity
│   ├── local/
│   │   ├── RentalDao.kt              ← DAO interface
│   │   └── AppDatabase.kt            ← Room database singleton
│   ├── remote/
│   │   ├── MovieApiService.kt        ← Retrofit interface
│   │   └── RetrofitInstance.kt       ← Retrofit singleton
│   └── repository/
│       └── MovieRepository.kt        ← Coordinates API + DB
├── ui/
│   ├── screens/
│   │   ├── HomeScreen.kt             ← Movie list screen
│   │   └── RentalScreen.kt           ← Rental CRUD screen
│   ├── components/
│   │   ├── MovieCard.kt              ← Reusable movie card
│   │   └── RentalCard.kt             ← Reusable rental card
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── viewmodel/
│   └── MovieViewModel.kt
├── worker/
│   └── RentalReminderWorker.kt
└── MainActivity.kt
```

---

## Step 2 — Data Models

### `data/model/Movie.kt`
```kotlin
data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val overview: String,
    val rating: Float
)
```

### `data/model/RentalEntity.kt`
```kotlin
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
```

### Rental Price Utility (place inside `RentalEntity.kt` or a `PriceUtil.kt`)
```kotlin
fun getRentalPricePerDay(rating: Float): Int = when {
    rating >= 8.0f -> 149   // Premium   — ₹149/day
    rating >= 6.0f -> 99    // Standard  — ₹99/day
    rating >= 4.0f -> 59    // Budget    — ₹59/day
    else           -> 29    // Economy   — ₹29/day
}

fun totalPrice(rental: RentalEntity): Int =
    getRentalPricePerDay(rental.rating) * rental.days
```

---

## Step 3 — Room Database

### `data/local/RentalDao.kt`
```kotlin
import androidx.room.*
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

### `data/local/AppDatabase.kt`
```kotlin
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RentalEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rentalDao(): RentalDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

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

---

## Step 4 — Remote / API Layer

### `data/remote/MovieApiService.kt`
```kotlin
import retrofit2.http.GET

interface MovieApiService {
    @GET("movies")
    suspend fun getMovies(): List<Movie>
}
```

### `data/remote/RetrofitInstance.kt`
```kotlin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: MovieApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://fooapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}
```

---

## Step 5 — Repository

### `data/repository/MovieRepository.kt`
```kotlin
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val dao: RentalDao) {

    // --- Remote ---
    suspend fun fetchMovies(): List<Movie> = RetrofitInstance.api.getMovies()

    // --- Local CRUD ---
    fun getRentals(): Flow<List<RentalEntity>> = dao.getAllRentals()

    suspend fun rentMovie(rental: RentalEntity) = dao.insertRental(rental)

    suspend fun updateDays(id: Int, days: Int) = dao.updateRentalDays(id, days)

    suspend fun removeRental(rental: RentalEntity) = dao.deleteRental(rental)

    suspend fun getRentalsOnce(): List<RentalEntity> = dao.getAllRentalsOnce()
}
```

---

## Step 6 — ViewModel

### `viewmodel/MovieViewModel.kt`
```kotlin
import androidx.lifecycle.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    // Remote movies
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // Room rentals as StateFlow
    val rentals = repository.getRentals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init { loadMovies() }

    fun loadMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _movies.value = repository.fetchMovies()
            } catch (e: Exception) {
                _movies.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun rentMovie(movie: Movie) {
        viewModelScope.launch {
            repository.rentMovie(
                RentalEntity(
                    movieId = movie.id,
                    title = movie.title,
                    posterUrl = movie.posterUrl,
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
        viewModelScope.launch { repository.removeRental(rental) }
    }
}

// ViewModelFactory
class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java))
            @Suppress("UNCHECKED_CAST") return MovieViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel")
    }
}
```

---

## Step 7 — WorkManager: Rental Reminder

### `worker/RentalReminderWorker.kt`
```kotlin
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class RentalReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val db = AppDatabase.getInstance(context)
        val rentals = db.rentalDao().getAllRentalsOnce()

        if (rentals.isNotEmpty()) {
            showNotification(
                context,
                "Active Rentals 🎬",
                "You have ${rentals.size} movie(s) rented. Don't forget to watch!"
            )
        }
        return Result.success()
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val channelId = "rental_reminder_channel"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Rental Reminders", NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        manager.notify(1001, notification)
    }
}
```

### Schedule in `MainActivity.kt`
```kotlin
import androidx.work.*
import java.util.concurrent.TimeUnit

private fun scheduleRentalReminder() {
    val request = PeriodicWorkRequestBuilder<RentalReminderWorker>(1, TimeUnit.DAYS)
        .build()
    WorkManager.getInstance(this).enqueueUniquePeriodicWork(
        "rental_reminder",
        ExistingPeriodicWorkPolicy.KEEP,
        request
    )
}
// Call scheduleRentalReminder() inside onCreate()
```

---

## Step 8 — Navigation Setup

### `MainActivity.kt`
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleRentalReminder()
        setContent {
            MovieExplorerTheme {
                val navController = rememberNavController()
                val db = AppDatabase.getInstance(applicationContext)
                val repo = MovieRepository(db.rentalDao())
                val viewModel: MovieViewModel = viewModel(factory = MovieViewModelFactory(repo))

                NavHost(navController, startDestination = "home") {
                    composable("home")   { HomeScreen(navController, viewModel) }
                    composable("rental") { RentalScreen(navController, viewModel) }
                }
            }
        }
    }
}
```

---

## Step 9 — AndroidManifest.xml Additions

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
```

---

## Evaluation Checklist

| Criteria | Marks | Implementation Reference |
|---|---|---|
| Material Design 3 UI | 5 | `Ui.md` + Theme.kt |
| API Integration & Display | 8 | Step 4 + Step 5 + HomeScreen |
| Room DB + MVVM | 15 | Step 2–6 |
| Rental Screen CRUD | 6 | Step 6 + RentalScreen |
| WorkManager | 6 | Step 7 |
| **Total** | **40** | |
