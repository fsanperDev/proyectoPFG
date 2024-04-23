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

class MainActivity : ComponentActivity() {
    // Controlador de navegación para gestionar la navegación entre pantallas
    private lateinit var navController: NavHostController
    val viewModel: VideojuegosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoPFGTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()

                    // Crear el gráfico de navegación y pasar el controlador de navegación y ViewModel de administrador
                    NavGraph(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}