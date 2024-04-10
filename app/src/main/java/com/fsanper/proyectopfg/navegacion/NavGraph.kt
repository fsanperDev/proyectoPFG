package com.fsanper.proyectopfg.navegacion

import androidx.compose.runtime.Composable

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
        startDestination = Screens.SplashScreen.name
    ) {
        // Definición de las composables asociadas a cada pantalla
        composable(Screens.SplashScreen.name) {
            SplashScreen(
                navController = navController
            )
        }

        composable(Screens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        composable(Screens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }

        composable(Screens.AdminScreen.name) {
            AdminScreen(navController = navController)
        }

        composable(Screens.AddGameScreen.name) {
            AddGameScreen(navController = navController, adminViewModel = adminViewModel)
        }

        composable(Screens.GetGameScreen.name) {
            GetGameScreen(navController = navController, adminViewModel = adminViewModel)
        }

        composable(Screens.OrderScreen.name) {
            OrderScreen(navController = navController)
        }

        composable(Screens.VerifiedPayment.name) {
            VerifiedPayment(navController = navController)
        }

        composable(Screens.GuardScreen.name) {
            GuardScreen(navController = navController)
        }
    }
}