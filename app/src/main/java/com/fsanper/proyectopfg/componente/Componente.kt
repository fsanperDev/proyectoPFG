package com.fsanper.proyectopfg.componente

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.fsanper.proyectopfg.R
import com.fsanper.proyectopfg.modelo.menu.MenuItem
import com.fsanper.proyectopfg.modelo.videojuego.VideoJuegosLista
import com.fsanper.proyectopfg.navegacion.Pantallas
import com.fsanper.proyectopfg.viewModels.LoginScreenViewModel
import com.fsanper.proyectopfg.viewModels.VideojuegosViewModel

/**
 * Composable que representa la barra superior personalizada.
 * @param onMenuClick Función de devolución de llamada cuando se hace clic en el icono del menú.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    onMenuClick: () -> Unit,
    titulo: String
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        navigationIcon = {
            // Icono de menú
            IconButton(onClick = {
                onMenuClick()
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.menu),
                    tint = colorResource(id = R.color.boton),
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
                    text = titulo,
                    color = colorResource(id = R.color.boton),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorResource(id = R.color.cuerpo)
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
    loginViewModel: LoginScreenViewModel = viewModel(),
    juegoViewModel: VideojuegosViewModel = viewModel()
) {
    // Estilo de borde y elementos del menú
    val borderWidth = 4.dp
    val menu = listOf(
        MenuItem(
            title = stringResource(R.string.home),
            icon = Icons.Default.Home,
        ),
        MenuItem(
            title = stringResource(R.string.contact),
            icon = Icons.Default.Mail,
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
                    .background(colorResource(id = R.color.cuerpo)),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .border(
                            BorderStroke(borderWidth, Color.White),
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
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold
                            )
                        },
                        selected = menuList == menu[0],
                        icon = {
                            Icon(
                                imageVector = menuList.icon,
                                contentDescription = menuList.title,
                                tint = Color.Black
                            )
                        },
                        onClick = {
                            onItemSelected.invoke(menuList.title)
                            when (menuList.title) {
                                "Home" -> {
                                    navController.navigate("homeScreen")
                                    onBackPress() // Cierra el cajón de navegación después de la navegación
                                }
                                "Contact" -> {
                                    navController.navigate("helpScreen")
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

@Composable
fun CardJuego(
    juego: VideoJuegosLista,
    navController: NavController,
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .shadow(40.dp)
            .clickable { navController.navigate("${Pantallas.GameScreen.name}/${juego.id}") },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.cuerpo),
            contentColor = Color.White
        )
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imagen = rememberImagePainter(data = juego.imagen)

            Image(
                painter = imagen,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "  ${juego.nombre}", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(5.dp))

        }
    }
}

@Composable
fun CardComentario(
    contenido: String,
    usuario: String
) {
    OutlinedCard(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .shadow(40.dp),
        border = BorderStroke(1.5.dp, colorResource(id = R.color.cuerpo))
    ) {
        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = "Comentario usuario ${usuario}:", fontWeight = FontWeight.Bold)
            Text(text = "${contenido}")

        }
    }
}