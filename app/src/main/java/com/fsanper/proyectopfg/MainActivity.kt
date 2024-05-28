package com.fsanper.proyectopfg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fsanper.proyectopfg.navegacion.NavGraph
import com.fsanper.proyectopfg.ui.theme.ProyectoPFGTheme
import com.fsanper.proyectopfg.viewModels.VideojuegosViewModel

/**
 * Actividad principal que representa la entrada de la aplicación.
 */
class MainActivity : ComponentActivity() {
    // Controlador de navegación para gestionar la navegación entre pantallas
    private lateinit var navController: NavHostController

    // ViewModel para la gestión de videojuegos
    val juegoViewModel: VideojuegosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establecer el contenido de la actividad
        setContent {
            // Establecer el tema de la aplicación
            ProyectoPFGTheme {
                // Contenedor Surface que utiliza el color de fondo del tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Recordar el NavController para mantener el estado de la navegación
                    navController = rememberNavController()

                    // Crear el gráfico de navegación y pasar el controlador de navegación y ViewModel de videojuegos
                    NavGraph(navController = navController, juegoViewModel = juegoViewModel)
                }
            }
        }
    }
}