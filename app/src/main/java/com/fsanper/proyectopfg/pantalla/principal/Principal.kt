package com.fsanper.proyectopfg.pantalla.principal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.fsanper.proyectopfg.R
import com.fsanper.proyectopfg.componente.CardJuego
import com.fsanper.proyectopfg.componente.MyDrawerContent
import com.fsanper.proyectopfg.componente.MyTopBar
import com.fsanper.proyectopfg.navegacion.Pantallas
import com.fsanper.proyectopfg.viewModels.VideojuegosViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Composable que representa la pantalla principal de la aplicación.
 * Muestra un cajón de navegación y contenido principal.
 * @param navController Objeto NavController que controla la navegación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    juegoViewModel: VideojuegosViewModel
) {
    // Estado del cajón de navegación
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Configuración del cajón de navegación y contenido principal
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen || drawerState.isClosed,
        drawerContent = {
            // Contenido del cajón de navegación
            MyDrawerContent(
                onItemSelected = { title ->
                    scope.launch {
                        drawerState.close()
                    }
                },
                onBackPress = {
                    if (drawerState.isOpen) {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                },
                modifier = Modifier.background(colorResource(id = R.color.cuerpo)),
                navController = navController
            )
        },
    ) {
        // Contenido principal de la pantalla
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                // Barra superior personalizada
                MyTopBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    titulo = "Inicio"
                )
            },
            containerColor = colorResource(id = R.color.boton)
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Contenido(navController = navController, juegoViewModel = juegoViewModel)
            }
        }
    }

    // Efecto lanzado para cerrar automáticamente las notificaciones después de 2 segundos
    LaunchedEffect(snackbarHostState.currentSnackbarData?.visuals?.duration) {
        delay(2000)
        snackbarHostState.currentSnackbarData?.dismiss()
    }
}

/**
 * Composable que representa el contenido principal de la pantalla.
 * Muestra una lista de juegos.
 * @param navController Objeto NavController que controla la navegación.
 */
@Composable
fun Contenido(
    navController: NavController,
    juegoViewModel: VideojuegosViewModel
) {
    val juegos by juegoViewModel.juegos.collectAsState()
    val isLoading by juegoViewModel.isLoading.collectAsState()

    LazyColumn{
        items(juegos) {
            CardJuego(juego = it, navController = navController)
        }
        item {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White
                )
            } else {
                Button(
                    onClick = { juegoViewModel.loadMore() }, // Llama a la función para cargar más juegos
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.boton),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Cargar más",
                        color = colorResource(id = R.color.cuerpo)
                    )
                }
            }
        }
    }
}