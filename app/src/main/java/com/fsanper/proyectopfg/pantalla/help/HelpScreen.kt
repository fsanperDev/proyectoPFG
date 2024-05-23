package com.fsanper.proyectopfg.pantalla.help

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.fsanper.proyectopfg.R
import com.fsanper.proyectopfg.componente.MyDrawerContent
import com.fsanper.proyectopfg.componente.MyTopBar
import com.fsanper.proyectopfg.navegacion.Pantallas
import com.sun.mail.smtp.SMTPTransport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(navController: NavHostController){
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
                    titulo = "Contacto"
                )
            },
            containerColor = colorResource(id = R.color.boton)
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Contenido(
    navController: NavController
) {
    var emailFrom = remember { mutableStateOf("") }
    var subject = remember { mutableStateOf("") }
    var message = remember { mutableStateOf("") }

    val valido = remember(emailFrom.value, subject.value, message.value) {
        emailFrom.value.trim().isNotBlank() && subject.value.trim().isNotBlank()
                && message.value.trim().isNotBlank()
    }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = emailFrom.value,
            onValueChange = { emailFrom.value = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
        )

        OutlinedTextField(
            value = subject.value,
            onValueChange = { subject.value = it },
            label = { Text("Asunto") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(onNext = { keyboardController?.hide() })
        )

        OutlinedTextField(
            value = message.value,
            onValueChange = { message.value = it },
            label = { Text("Mensaje") },
            maxLines = 5,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
        )

        Button(
            modifier = Modifier
                .padding(3.dp),
            shape = CircleShape,
            enabled = valido,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.boton),
                contentColor = colorResource(id = R.color.cuerpo)
            ),
            onClick = {
                coroutineScope.launch {
                    try {
                        sendEmail(emailFrom = emailFrom.value, asunto = subject.value, text = message.value)
                        navController.navigate(Pantallas.HelpScreen.name)
                        Toast.makeText(context, "Correo electrónico enviado con éxito", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error al enviar el correo: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        ) {
            Text("Enviar Correo")
        }
    }
}

suspend fun sendEmail(emailFrom: String, asunto: String, text: String) {
    withContext(Dispatchers.IO) {
        val userName = "proyectopfg16@gmail.com"
        val password = "gsls rmok doca bkqa" // Usa la contraseña de aplicación generada

        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587") // Cambiado a 587 para STARTTLS
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(userName, password)
            }
        })

        try {
            val msg = MimeMessage(session).apply {
                setFrom(InternetAddress(userName)) // Usando la dirección del remitente autenticado
                setReplyTo(arrayOf(InternetAddress(emailFrom))) // Configura el email del remitente como dirección de respuesta
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(userName))
                setSubject(asunto)
                setText(text)
            }

            Transport.send(msg)
            println("Email enviado correctamente a $userName")
        } catch (e: MessagingException) {
            e.printStackTrace()
            throw Exception("Error al enviar el correo: ${e.message}")
        }
    }
}