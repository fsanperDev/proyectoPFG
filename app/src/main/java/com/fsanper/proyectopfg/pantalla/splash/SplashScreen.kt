package com.fsanper.proyectopfg.pantalla.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fsanper.proyectopfg.R
import com.fsanper.proyectopfg.navegacion.Pantallas
import kotlinx.coroutines.delay

/**
 * Composable que representa la pantalla de presentación de la aplicación.
 * @param navController Controlador de navegación para gestionar la transición a otras pantallas.
 */
@Composable
fun SplashScreen(navController: NavController) {
    // Animación de escala para la imagen del logo
    val scale = remember { Animatable(0f) }

    // Contexto local necesario para crear el reproductor de medios
    val context = LocalContext.current


    // Alcance de la corrutina para manejar la ejecución de tareas asincrónicas
    val coroutineScope = rememberCoroutineScope()


    // Efecto lanzado para realizar animaciones y acciones al cargarse el composable
    LaunchedEffect(key1 = true) {
        // Animar la escala de la imagen del logo
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(
                delayMillis = 800,
                easing = {
                    OvershootInterpolator(8f).getInterpolation(it)
                }
            )
        )

        // Aguardar un breve periodo antes de navegar a la siguiente pantalla
        delay(1000)

        // Navegar a la pantalla de inicio de sesión
        navController.navigate(Pantallas.LoginScreen.name)
    }

    // Definir el ancho del borde alrededor de la imagen del logo
    val borderWidth = 4.dp

    // Diseño de columna principal que ocupa toda la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.cuerpo)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Superficie contenedora de la imagen del logo con animación de escala
        Surface(
            modifier = Modifier
                .padding(15.dp)
                .size(400.dp)
                .scale(scale.value)
        ) {
            // Columna interna con la imagen del logo dentro de un círculo con borde
            Column(
                modifier = Modifier
                    .background(colorResource(id = R.color.cuerpo)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen del logo con borde circular
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(500.dp)
                        .border(
                            BorderStroke(borderWidth, colorResource(id = R.color.cuerpo)),
                            CircleShape
                        )
                        .padding(borderWidth)
                        .clip(CircleShape)
                )
            }
        }

        // Columna con texto de carga y barra de progreso lineal
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Cargando....")
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                modifier = Modifier.width(300.dp),
                color = colorResource(id = R.color.menu)
            )
        }
    }
}