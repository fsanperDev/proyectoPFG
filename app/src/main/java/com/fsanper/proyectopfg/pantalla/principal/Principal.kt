package com.fsanper.proyectopfg.pantalla.principal

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fsanper.proyectopfg.R
import com.fsanper.proyectopfg.componente.CardJuego
import com.fsanper.proyectopfg.componente.MyDrawerContent
import com.fsanper.proyectopfg.componente.MyTopBar
import com.fsanper.proyectopfg.navegacion.Pantallas
import com.fsanper.proyectopfg.viewModels.VideojuegosViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    // Estado de desplazamiento para la lista de juegos
    val gridState = rememberLazyGridState()

    DisposableEffect(Unit) {
        juegoViewModel.resetearFiltro()
        juegoViewModel.obtenerJuegos(null)
        onDispose {}
    }

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
        Column(modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.letra))) {
            MyTopBar(
                onMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                titulo = "Inicio"
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre el TopBar y el Filtro
            Filtro(juegoViewModel = juegoViewModel, gridState = gridState)
            Spacer(modifier = Modifier.height(8.dp)) // Espacio entre el Filtro y el contenido principal
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                },
                topBar = { },
                containerColor = colorResource(id = R.color.boton)
            ) { paddingValues ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(colorResource(id = R.color.letra))
                ) {
                    Contenido(navController = navController, juegoViewModel = juegoViewModel, gridState = gridState)
                }
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
 * Composable que representa el filtro de juegos.
 * Muestra una lista de géneros y permite al usuario seleccionar uno.
 * @param juegoViewModel Modelo de vista para los juegos.
 */
@Composable
fun Filtro(juegoViewModel: VideojuegosViewModel, gridState: LazyGridState) {
    var selectedGenre by remember { mutableStateOf<String?>(null) }
    val genero by juegoViewModel.genero.collectAsState()
    var filterMenuExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedGenre != null){
                Text(
                    text = "Filtrar por: ${selectedGenre ?: "Ninguno"}",
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .clickable { filterMenuExpanded = true }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = Icons.Filled.FilterAlt,
                        contentDescription = "Filtro Genero",
                        tint = colorResource(id = R.color.boton)
                    )
                }
                Spacer(Modifier.width(8.dp))
                DropdownMenu(
                    expanded = filterMenuExpanded,
                    onDismissRequest = { filterMenuExpanded = false }
                ) {
                    genero.forEach { genre ->
                        DropdownMenuItem(
                            onClick = {
                                selectedGenre = genre.slug
                                filterMenuExpanded = false
                            },
                            text = { Text(text = genre.slug, fontWeight = FontWeight.Bold) },
                            colors = MenuDefaults.itemColors(
                                textColor = colorResource(id = R.color.cuerpo)
                            )
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                modifier = Modifier.padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.boton),
                    contentColor = colorResource(id = R.color.cuerpo)
                ),
                onClick = {
                    scope.launch {
                        gridState.scrollToItem(0)
                    }
                    juegoViewModel.obtenerJuegos(selectedGenre)
                }
            ) {
                Text("Aplicar filtro")
            }
            Spacer(modifier = Modifier.width(8.dp)) // Espacio entre los botones
            Button(
                modifier = Modifier.padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.boton),
                    contentColor = colorResource(id = R.color.cuerpo)
                ),
                onClick = {
                    selectedGenre = null
                    scope.launch {
                        gridState.scrollToItem(0)
                    }
                    juegoViewModel.obtenerJuegos(null)
                }
            ) {
                Text("Quitar filtro")
            }
        }
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
    juegoViewModel: VideojuegosViewModel,
    gridState: LazyGridState
) {
    val juegos by juegoViewModel.juegos.collectAsState()
    val isLoading by juegoViewModel.isLoading.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.letra)),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(juegos.size) { index ->
            val juego = juegos[index]
            CardJuego(juego = juego, navController = navController)
        }
        item(span = { GridItemSpan(2) }) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            } else {
                if (juegos.isEmpty()) {
                    Text(
                        text = "No se encontraron juegos.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        color = Color.White
                    )
                } else {
                    Button(
                        onClick = { juegoViewModel.loadMore() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp)
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
}