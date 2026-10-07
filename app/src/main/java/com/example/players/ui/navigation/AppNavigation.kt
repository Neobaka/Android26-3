package com.example.players.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.players.ui.details.PlayerDetailsScreen
import com.example.players.ui.list.AppOrange
import com.example.players.ui.list.PlayerListScreen

object Routes {
    const val HOME = "home"
    const val LIST = "list"
    const val VIDEO = "video"
    const val NOTIFICATIONS = "notifications"
    const val DETAILS = "details/{playerId}"
    fun details(id: Int) = "details/$id"
}

private data class BottomItem(val route: String, val label: String, val icon: ImageVector)

private val bottomItems = listOf(
    BottomItem(Routes.HOME, "Главная", Icons.Default.Home),
    BottomItem(Routes.LIST, "Список", Icons.AutoMirrored.Filled.List),
    BottomItem(Routes.VIDEO, "Видео", Icons.Default.PlayArrow),
    BottomItem(Routes.NOTIFICATIONS, "Уведомления", Icons.Default.Notifications)
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // На экране деталей нижняя навигация скрыта (как на макете)
            if (currentRoute != Routes.DETAILS) {
                NavigationBar(containerColor = Color.White) {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AppOrange,
                                selectedTextColor = AppOrange,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.LIST,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(Routes.HOME) { Placeholder("Главная") }
            composable(Routes.LIST) {
                PlayerListScreen(onPlayerClick = { navController.navigate(Routes.details(it)) })
            }
            composable(Routes.VIDEO) { Placeholder("Видео") }
            composable(Routes.NOTIFICATIONS) { Placeholder("Уведомления") }
            composable(
                route = Routes.DETAILS,
                arguments = listOf(navArgument("playerId") { type = NavType.IntType })
            ) {
                PlayerDetailsScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
private fun Placeholder(title: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(title)
    }
}
