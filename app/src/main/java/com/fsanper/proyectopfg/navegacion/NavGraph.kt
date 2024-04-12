package com.fsanper.proyectopfg.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fsanper.e10.screen.login.LoginScreen
import com.fsanper.proyectopfg.pantalla.principal.HomeScreen
import com.fsanper.proyectopfg.pantalla.splash.SplashScreen

/**
 * Composable que define la estructura de navegación de la aplicación utilizando Jetpack Navigation Compose.
 * @param navController Controlador de navegación para gestionar la navegación entre pantallas.
 * @param adminViewModel ViewModel asociado a la funcionalidad de administrador.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
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
            HomeScreen(navController = navController)
        }
    }
}