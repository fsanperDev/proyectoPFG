package com.fsanper.proyectopfg.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fsanper.proyectopfg.pantalla.help.HelpScreen
import com.fsanper.proyectopfg.pantalla.juego.GameScreen
import com.fsanper.proyectopfg.pantalla.login.LoginScreen
import com.fsanper.proyectopfg.pantalla.principal.HomeScreen
import com.fsanper.proyectopfg.pantalla.splash.SplashScreen
import com.fsanper.proyectopfg.viewModels.VideojuegosViewModel

/**
 * Composable que define la estructura de navegación de la aplicación utilizando Jetpack Navigation Compose.
 * @param navController Controlador de navegación para gestionar la navegación entre pantallas.
 * @param adminViewModel ViewModel asociado a la funcionalidad de administrador.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    juegoViewModel: VideojuegosViewModel
) {
    // Componente de navegación que contiene las pantallas de la aplicación
    NavHost(
        navController = navController,
        startDestination = Pantallas.SplashScreen.name
    ) {
        // Definición de las composables asociadas a cada pantalla
        composable(Pantallas.SplashScreen.name) {
            SplashScreen(
                navController = navController
            )
        }

        composable(Pantallas.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        composable(Pantallas.HomeScreen.name) {
            HomeScreen(navController = navController, juegoViewModel = juegoViewModel)
        }
        composable(
            route = "${Pantallas.GameScreen.name}/{juegoId}",
            arguments = listOf(navArgument("juegoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val juegoId = backStackEntry.arguments?.getInt("juegoId")
            if (juegoId != null) {
                GameScreen(navController = navController, juegoViewModel = juegoViewModel, juegoId = juegoId)
            } else {
                // Manejar el caso donde no se proporciona un ID de juego válido
            }
        }
        composable(Pantallas.HelpScreen.name) {
            HelpScreen(navController = navController)
        }
    }
}