package com.fsanper.proyectopfg.pantalla.juego

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.fsanper.proyectopfg.R
import com.fsanper.proyectopfg.componente.CardComentario
import com.fsanper.proyectopfg.componente.MyDrawerContent
import com.fsanper.proyectopfg.componente.MyTopBar
import com.fsanper.proyectopfg.modelo.comentario.Comentario
import com.fsanper.proyectopfg.modelo.firebase.ComentarioViewModel
import com.fsanper.proyectopfg.modelo.usuario.Usuario
import com.fsanper.proyectopfg.modelo.videojuego.DetallesJuego
import com.fsanper.proyectopfg.navegacion.Pantallas
import com.fsanper.proyectopfg.viewModels.VideojuegosViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    navController: NavHostController,
    juegoViewModel: VideojuegosViewModel,
    juegoId: Int
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
                    titulo = "Videojuego"
                )
            },
            containerColor = colorResource(id = R.color.cuerpo)
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Contenido(
                        idJuego = juegoId,
                        juegoViewModel = juegoViewModel,
                        navController = navController
                    )
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

@Composable
fun Contenido(
    idJuego: Int,
    juegoViewModel: VideojuegosViewModel,
    navController: NavHostController
) {
    val detalleJuego by juegoViewModel.detalleJuego.collectAsState()

    LaunchedEffect(idJuego) {
        juegoViewModel.obtenerDetallesJuego(idJuego)
    }

    ImprimirInformacion(
        detalleJuego = detalleJuego,
        navController = navController,
        idJuego = idJuego
    )
}

@Composable
fun ImprimirInformacion(
    detalleJuego: DetallesJuego?,
    navController: NavHostController,
    idJuego: Int
) {
    detalleJuego?.let { juego ->
        val idCorreo = Firebase.auth.currentUser?.uid
        val imagen = rememberImagePainter(data = juego.imagen)
        Image(
            painter = imagen,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
        Text(text = juego.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(16.dp))
        val descripcion = HtmlCompat.fromHtml(juego.descripcion, HtmlCompat.FROM_HTML_MODE_COMPACT)
        Text(text = "Descripción:", style = MaterialTheme.typography.titleMedium)
        Text(text = "${descripcion}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Lanzamiento:", style = MaterialTheme.typography.titleMedium)
        Text(text = "${juego.lanzamiento}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Plataformas:", style = MaterialTheme.typography.titleMedium)
        juego.plataformas.forEach { plataforma ->
            Text(
                text = "- ${plataforma.plataforma.nombre}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        CuadroComentarios(
            nombreJuego = juego.nombre,
            navController = navController,
            idCorreo = idCorreo.toString(),
            idJuego = idJuego
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuadroComentarios(
    comentario: ComentarioViewModel = viewModel(),
    nombreJuego: String,
    navController: NavHostController,
    idCorreo: String,
    idJuego: Int
) {
    var comentarios: List<Comentario> by remember { mutableStateOf(emptyList()) }
    var comentarioUsuario = rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val valido = remember(comentarioUsuario.value) {
        comentarioUsuario.value.trim().isNotBlank()
    }

    Text(
        text = "Tablon de comentario ${nombreJuego}",
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.ExtraBold
    )

    LaunchedEffect(nombreJuego) {
        ObtenerComentariosFirestore(nombreJuego) { listaComentario ->
            comentarios = listaComentario
        }
    }

    if (comentarios.isEmpty()) {
        Text(text = "No se han registrado ningún comentario para este juego.")
    } else {
        comentarios.forEach { comment ->
            CardComentario(contenido = comment.comentario, usuario = comment.usuario)
        }
    }

    TextField(
        value = comentarioUsuario.value,
        onValueChange = { comentarioUsuario.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        textStyle = LocalTextStyle.current.copy(
            fontSize = MaterialTheme.typography.bodyMedium.fontSize
        ),
        maxLines = 10,
        label = { Text("Comentario") }
    )

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
        shape = CircleShape,
        enabled = valido,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.boton),
            contentColor = colorResource(id = R.color.cuerpo)
        ),
        onClick = {
            obtenerUltimoId { ultimoID ->
                val nuevoComentario = Comentario(
                    idComentario = ultimoID + 1,
                    nombreJuego = nombreJuego,
                    comentario = comentarioUsuario.value,
                    usuario = "Anonimo",
                    idCorreo = idCorreo
                )
                comentario.saveCompra(
                    navController = navController,
                    comentario = nuevoComentario,
                    context = context,
                    idJuego = idJuego
                )
            }
        }
    ) {
        Text("Enviar Comentario")
    }
}

fun ObtenerComentariosFirestore(nombreJuego: String, callback: (List<Comentario>) -> Unit) {
    val comentariosList = mutableListOf<Comentario>()

    // Referencia a la colección "comentario" en Firestore
    val query = FirebaseFirestore.getInstance().collection("comentarios")
        .whereEqualTo("nombreJuego", nombreJuego) // Filtrar por nombre de juego específico

    query.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // Procesar documentos y construir la lista de comentarios
            for (document in task.result!!.documents) {
                val comentario = document.toObject(Comentario::class.java)
                comentario?.let { comentariosList.add(it) }
            }
            // Llamar a la función de devolución de llamada con la lista de comentarios
            callback(comentariosList)
        } else {
            // Manejar errores
            val exception = task.exception
            Log.w(
                "ObtenerComentariosPorJuegoFirestore",
                "Error fetching data: ${exception?.message}"
            )
        }
    }
}

fun obtenerUltimoId(callback: (Long) -> Unit) {
    val query = FirebaseFirestore.getInstance()
        .collection("comentarios")
        .orderBy("idComentario", Query.Direction.DESCENDING)
        .limit(1)

    query.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // Obtener el último documento y extraer el ID
            val ultimaCompra = task.result!!.documents.firstOrNull()
            val ultimoId = ultimaCompra?.getLong("idComentario") ?: 0L
            // Llamar a la función de devolución de llamada con el último ID
            callback(ultimoId)
        } else {
            // Manejar errores, como la falta de conexión a Internet
            val exception = task.exception
            Log.w("obtenerUltimoId", "Error fetching data: ${exception?.message}")
            // Proporcionar otro valor predeterminado en caso de error
            callback(0)
        }
    }
}