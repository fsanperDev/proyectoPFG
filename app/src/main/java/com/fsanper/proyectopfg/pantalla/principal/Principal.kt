package com.fsanper.proyectopfg.pantalla.principal

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.fsanper.proyectopfg.R
import com.fsanper.proyectopfg.modelo.menu.MenuItem
import com.fsanper.proyectopfg.pantalla.login.LoginScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Composable que representa la pantalla principal de la aplicación.
 * Muestra un cajón de navegación y contenido principal.
 * @param navController Objeto NavController que controla la navegación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
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
                modifier = Modifier.background(colorResource(id = R.color.boton)),
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
                    }
                )
            },
            containerColor = colorResource(id = R.color.cuerpo)
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = colorResource(id = R.color.cuerpo)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                // Contenido principal de la pantalla
                Contenido(navController = navController)
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
fun Contenido(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.catalogo),
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(15.dp))

    }
}

/**
 * Composable que representa la barra superior personalizada.
 * @param onMenuClick Función de devolución de llamada cuando se hace clic en el icono del menú.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    onMenuClick: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.boton)),
        navigationIcon = {
            // Icono de menú
            IconButton(onClick = {
                onMenuClick()
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu),
                    tint = colorResource(id = R.color.cuerpo),
                )
            }
        },
        title = {
            // Título de la barra superior
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.home),
                    color = colorResource(id = R.color.cuerpo),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(id = R.color.menu)
        )
    )
}

/**
 * Composable que representa el contenido del cajón de navegación.
 * @param modifier Modificador para el contenido del cajón.
 * @param onItemSelected Función de devolución de llamada cuando se selecciona un elemento.
 * @param onBackPress Función de devolución de llamada cuando se presiona el botón de retroceso.
 * @param navController Objeto NavController que controla la navegación.
 * @param loginViewModel ViewModel para la pantalla de inicio de sesión.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDrawerContent(
    modifier: Modifier = Modifier,
    onItemSelected: (title: String) -> Unit,
    onBackPress: () -> Unit,
    navController: NavHostController,
    loginViewModel: LoginScreenViewModel = viewModel()
) {
    // Estilo de borde y elementos del menú
    val borderWidth = 4.dp
    val menu = listOf(
        MenuItem(
            title = stringResource(R.string.home),
            icon = Icons.Default.Home,
        ),
        MenuItem(
            title = stringResource(id = R.string.logout),
            icon = Icons.Filled.Logout
        )
    )

    // Diseño del cajón de navegación
    ModalDrawerSheet(modifier) {
        Column(modifier.fillMaxSize()) {
            // Cabecera del cajón con el logo de la aplicación
            Box(
                modifier = Modifier
                    .height(190.dp)
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.boton)),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .border(
                            BorderStroke(borderWidth, colorResource(id = R.color.cuerpo)),
                            CircleShape
                        )
                        .padding(borderWidth)
                        .clip(CircleShape)
                )
            }
            // Elementos del menú
            LazyColumn {
                items(menu) { menuList ->
                    NavigationDrawerItem(
                        shape = MaterialTheme.shapes.small,
                        label = {
                            Text(
                                text = menuList.title,
                                color = colorResource(id = R.color.cuerpo),
                            )
                        },
                        selected = menuList == menu[0],
                        icon = {
                            Icon(
                                imageVector = menuList.icon,
                                contentDescription = menuList.title,
                                tint = colorResource(id = R.color.cuerpo),
                            )
                        },
                        onClick = {
                            onItemSelected.invoke(menuList.title)
                            when (menuList.title) {
                                "Home" -> {
                                    navController.navigate("homeScreen")
                                    onBackPress() // Cierra el cajón de navegación después de la navegación
                                }
                                "Logout" -> {
                                    loginViewModel.logout(navController)
                                }
                            }
                        },
                    )
                }
            }
            Divider()
        }

    }
    // Manejador de retroceso personalizado
    BackPressHandler {
        onBackPress()
    }
}

/**
 * Composable que maneja el evento de retroceso en la aplicación.
 * @param enabled Indica si el manejador está habilitado.
 * @param onBackPressed Función de devolución de llamada cuando se presiona el botón de retroceso.
 */
@Composable
fun BackPressHandler(enabled: Boolean = true, onBackPressed: () -> Unit) {
    // Estado actualizado de la función de retroceso
    val currentOnBackPressed by rememberUpdatedState(onBackPressed)
    // Creación de un callback para el evento de retroceso
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }
    // Actualización de la capacidad del callback según la activación
    SideEffect {
        backCallback.isEnabled = enabled
    }

    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val lifecycleOwner = LocalLifecycleOwner.current
    // Registro del callback en el Dispatcher
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        // Eliminación del callback cuando el Composable se destruye
        onDispose {
            backCallback.remove()
        }
    }
}