package com.fsanper.proyectopfg.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController

/**
 * Composable que define la estructura de navegación de la aplicación utilizando Jetpack Navigation Compose.
 * @param navController Controlador de navegación para gestionar la navegación entre pantallas.
 * @param adminViewModel ViewModel asociado a la funcionalidad de administrador.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    adminViewModel: AdminViewModel
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