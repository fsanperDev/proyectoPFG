package com.fsanper.proyectopfg.navegacion

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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
 * @param juegoViewModel ViewModel asociado a la funcionalidad de gestión de videojuegos.
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

        // Pantalla de splash
        composable(Pantallas.SplashScreen.name) {
            SplashScreen(
                navController = navController
            )
        }

        // Pantalla de inicio de sesión
        composable(Pantallas.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        // Pantalla principal
        composable(Pantallas.HomeScreen.name) {
            HomeScreen(navController = navController, juegoViewModel = juegoViewModel)
        }

        // Pantalla de detalles del juego
        composable(
            route = "${Pantallas.GameScreen.name}/{juegoId}",
            arguments = listOf(navArgument("juegoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val juegoId = backStackEntry.arguments?.getInt("juegoId")
            if (juegoId != null) {
                GameScreen(navController = navController, juegoViewModel = juegoViewModel, juegoId = juegoId)
            } else {
                // Mostrar un Toast si no se proporciona un ID de juego válido
                val context = LocalContext.current
                Toast.makeText(context, "ID de juego no proporcionado", Toast.LENGTH_SHORT).show()
            }
        }

        // Pantalla de ayuda
        composable(Pantallas.HelpScreen.name) {
            HelpScreen(navController = navController)
        }
    }
}