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
