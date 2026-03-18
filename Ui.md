# Ui.md — Movie Explorer UI Design Guide

## Design Reference
The UI is inspired by a **dark purple/navy streaming app** aesthetic with:
- Deep dark background (`#0D0D1A`)
- Purple/violet accent gradients (`#6C3FE8` → `#9B6DFF`)
- White text with translucent card surfaces
- Pill-shaped filter chips
- Bottom navigation bar with center FAB
- Movie poster cards with overlay rating badges

---

## Color Palette — `ui/theme/Color.kt`

```kotlin
import androidx.compose.ui.graphics.Color

// Backgrounds
val BackgroundDark   = Color(0xFF0D0D1A)
val SurfaceDark      = Color(0xFF161628)
val CardSurface      = Color(0xFF1E1E35)

// Accent / Brand
val PurplePrimary    = Color(0xFF6C3FE8)
val PurpleLight      = Color(0xFF9B6DFF)
val PurpleContainer  = Color(0xFF3A2070)

// Text
val TextPrimary      = Color(0xFFFFFFFF)
val TextSecondary    = Color(0xFFB0B0C8)
val TextMuted        = Color(0xFF6B6B8A)

// Status Colors
val RatingGold       = Color(0xFFFFD700)
val ErrorRed         = Color(0xFFFF4F4F)
val SuccessGreen     = Color(0xFF4CAF50)
```

---

## Theme Setup — `ui/theme/Theme.kt`

```kotlin
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary          = PurplePrimary,
    onPrimary        = TextPrimary,
    primaryContainer = PurpleContainer,
    secondary        = PurpleLight,
    background       = BackgroundDark,
    surface          = SurfaceDark,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    surfaceVariant   = CardSurface,
    error            = ErrorRed
)

@Composable
fun MovieExplorerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography  = MovieTypography,
        content     = content
    )
}
```

---

## Typography — `ui/theme/Type.kt`

```kotlin
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val MovieTypography = Typography(
    headlineLarge = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold,   color = TextPrimary),
    headlineMedium= TextStyle(fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary),
    titleLarge    = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold,   color = TextPrimary),
    titleMedium   = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium, color = TextPrimary),
    bodyLarge     = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal, color = TextSecondary),
    bodySmall     = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal, color = TextMuted),
    labelSmall    = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Medium, color = TextMuted)
)
```

---

## Screen 1 — HomeScreen (`ui/screens/HomeScreen.kt`)

### Layout Structure
```
Scaffold
├── TopAppBar
│   ├── Title: "Movie Explorer"
│   ├── Action: Search Icon (🔍)
│   └── Action: Notifications Icon (🔔)
├── Column
│   ├── User Story Row (circular avatar chips — horizontal scroll)
│   ├── Genre Filter Chips Row (Trending, Comedy, Drama, etc.)
│   └── LazyColumn → MovieCard per item
└── BottomNavigationBar
    ├── Home
    ├── Explore
    ├── [FAB +]      ← center floating action button
    ├── Rental (Watchlist icon)
    └── Profile
```

### TopAppBar Code
```kotlin
TopAppBar(
    title = {
        Text(
            "Movie Explorer",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )
    },
    actions = {
        IconButton(onClick = { /* search */ }) {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = TextPrimary)
        }
        IconButton(onClick = { /* notifications */ }) {
            Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = TextPrimary)
        }
    },
    colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
)
```

### Genre Filter Chips
```kotlin
val genres = listOf("Trending", "Comedy", "Drama", "Action", "Sci-Fi")
var selectedGenre by remember { mutableStateOf("Trending") }

LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
    items(genres) { genre ->
        FilterChip(
            selected = selectedGenre == genre,
            onClick  = { selectedGenre = genre },
            label    = { Text(genre) },
            colors   = FilterChipDefaults.filterChipColors(
                selectedContainerColor    = PurplePrimary,
                selectedLabelColor        = TextPrimary,
                containerColor            = CardSurface,
                labelColor                = TextSecondary
            ),
            shape = RoundedCornerShape(50)
        )
    }
}
```

### MovieCard Component (`ui/components/MovieCard.kt`)
```kotlin
@Composable
fun MovieCard(movie: Movie, onRentClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // Poster
            AsyncImage(
                model              = movie.posterUrl,
                contentDescription = movie.title,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .width(90.dp)
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(movie.title, style = MaterialTheme.typography.titleMedium, maxLines = 2)
                Spacer(Modifier.height(4.dp))
                // Rating badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null,
                         tint = RatingGold, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("${movie.rating}", style = MaterialTheme.typography.bodySmall,
                         color = RatingGold)
                }
                Spacer(Modifier.height(4.dp))
                Text(movie.overview, style = MaterialTheme.typography.bodySmall,
                     maxLines = 3, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(8.dp))
                // Price badge
                val price = getRentalPricePerDay(movie.rating)
                Text("₹$price/day", style = MaterialTheme.typography.labelSmall,
                     color = PurpleLight)
                Spacer(Modifier.height(6.dp))
                Button(
                    onClick = onRentClick,
                    colors  = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                    shape   = RoundedCornerShape(8.dp)
                ) {
                    Text("Rent Movie", color = TextPrimary)
                }
            }
        }
    }
}
```

---

## Screen 2 — RentalScreen (`ui/screens/RentalScreen.kt`)

### Layout Structure
```
Scaffold
├── TopAppBar  →  "My Rentals" + back arrow
├── LazyColumn → RentalCard per rented movie
└── Sticky Bottom Bar → Total Rental Price display
```

### RentalCard Component (`ui/components/RentalCard.kt`)
```kotlin
@Composable
fun RentalCard(
    rental: RentalEntity,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove:   () -> Unit
) {
    val pricePerDay = getRentalPricePerDay(rental.rating)
    val total       = pricePerDay * rental.days

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model              = rental.posterUrl,
                contentDescription = rental.title,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .width(70.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(rental.title, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                Spacer(Modifier.height(4.dp))
                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null,
                         tint = RatingGold, modifier = Modifier.size(14.dp))
                    Text(" ${rental.rating}", style = MaterialTheme.typography.bodySmall,
                         color = RatingGold)
                }
                Spacer(Modifier.height(6.dp))
                // Day counter
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease",
                             tint = PurpleLight)
                    }
                    Text("${rental.days} day${if (rental.days > 1) "s" else ""}",
                         style = MaterialTheme.typography.bodyLarge,
                         modifier = Modifier.padding(horizontal = 8.dp))
                    IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Add, contentDescription = "Increase",
                             tint = PurpleLight)
                    }
                }
                Text("₹$pricePerDay/day × ${rental.days} = ₹$total",
                     style = MaterialTheme.typography.bodySmall, color = PurpleLight)
            }
            // Remove button
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = ErrorRed)
            }
        }
    }
}
```

### Total Price Bottom Bar
```kotlin
// Inside RentalScreen Scaffold bottom bar
val totalPrice by remember(rentals) {
    derivedStateOf { rentals.sumOf { totalPrice(it) } }
}

Surface(
    modifier = Modifier.fillMaxWidth(),
    color     = CardSurface,
    tonalElevation = 8.dp
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text("Total Rental Price", style = MaterialTheme.typography.titleMedium)
        Text("₹$totalPrice", style = MaterialTheme.typography.headlineMedium,
             color = PurplePrimary)
    }
}
```

---

## Bottom Navigation Bar

```kotlin
NavigationBar(containerColor = SurfaceDark) {
    NavigationBarItem(
        selected = currentRoute == "home",
        onClick  = { navController.navigate("home") },
        icon     = { Icon(Icons.Default.Home, contentDescription = "Home") },
        label    = { Text("Home") },
        colors   = NavigationBarItemDefaults.colors(
            selectedIconColor   = PurplePrimary,
            unselectedIconColor = TextMuted,
            indicatorColor      = PurpleContainer
        )
    )
    NavigationBarItem(
        selected = currentRoute == "rental",
        onClick  = { navController.navigate("rental") },
        icon     = { Icon(Icons.Default.BookmarkBorder, contentDescription = "Rentals") },
        label    = { Text("Rentals") },
        colors   = NavigationBarItemDefaults.colors(
            selectedIconColor   = PurplePrimary,
            unselectedIconColor = TextMuted,
            indicatorColor      = PurpleContainer
        )
    )
}
```

---

## UI Dos and Don'ts

| ✅ DO | ❌ DON'T |
|---|---|
| Use `RoundedCornerShape(16.dp)` for cards | Use sharp `RectangleShape` for cards |
| Maintain `BackgroundDark` as scaffold background | Use white or grey backgrounds |
| Show rating with `⭐ RatingGold` color | Use plain grey text for rating |
| Show price per day on MovieCard | Hide pricing until rental screen |
| Use `PurplePrimary` for all primary buttons | Use default Material3 blue tones |
| Use `ErrorRed` for delete/remove actions | Use purple for destructive actions |
| Keep poster aspect ratio ~2:3 (portrait) | Stretch posters to square |
