package com.fsanper.proyectopfg.pantalla.login

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.fsanper.proyectopfg.R
import com.fsanper.proyectopfg.navegacion.Pantallas
import com.fsanper.proyectopfg.viewModels.LoginScreenViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

/**
 * Pantalla de inicio de sesión.
 *
 * @param navController controlador de navegación para cambiar entre pantallas
 * @param viewModel modelo de vista para la pantalla de inicio de sesión
 */
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val borderWidth = 4.dp

    // Estado para recordar si se debe mostrar el formulario de inicio de sesión o registro
    val showLoginForm = rememberSaveable() {
        mutableStateOf(true)
    }
    val context = LocalContext.current

    // Diseño de la superficie que ocupa toda la pantalla
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        // Columna que organiza los elementos en una disposición vertical y centrada
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.letra))
        ) {
            // Muestra el logotipo de la aplicación con un borde circular
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

            // Condición basada en el estado para mostrar el formulario de inicio de sesión o registro
            if (showLoginForm.value) {
                // Formulario de inicio de sesión
                UserForm(isCreateAccount = false) { email, password ->
                    Log.d("My Login", "Logueando con $email y $password")
                    // Lógica para navegar a la pantalla de usuario regular
                    viewModel.signInWithEmailAndPassword(
                        context,
                        email,
                        password
                    ) {
                        navController.navigate(Pantallas.HomeScreen.name)
                    }
                }
            } else {
                // Formulario de registro
                RegisterUserForm(isCreateAccount = true) { user, name, email, password ->
                    Log.d("My Login", "Logueando con $email y $password")
                    // Lógica para crear un usuario y navegar a la pantalla principal
                    viewModel.createUserWithEmailAndPassword(
                        context,
                        user,
                        name,
                        email,
                        password
                    ) {
                        navController.navigate(Pantallas.HomeScreen.name)
                    }
                }
            }

            // Añade un espacio vertical entre los formularios y los botones
            Spacer(modifier = Modifier.height(15.dp))

            // Row que contiene textos y lógica para cambiar entre el formulario de inicio de sesión y el de registro
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text1 = if (showLoginForm.value) "¿No tienes cuenta?" else "¿Ya tienes cuenta?"
                val text2 =
                    if (showLoginForm.value) stringResource(id = R.string.register) else stringResource(
                        id = R.string.login
                    )
                Text(text = text1)
                Text(
                    text = text2,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 5.dp),
                    color = colorResource(id = R.color.cuerpo)
                )
            }
        }
    }
}

/**
 * Diálogo para restablecer la contraseña.
 *
 * @param onDimissRequest función de llamada cuando se solicita cerrar el diálogo
 * @param onConfirmation función de llamada cuando se confirma la acción
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgottenPassword(
    onDimissRequest: () -> Unit, // Función de llamada cuando se solicita cerrar el diálogo
    onConfirmation: () -> Unit // Función de llamada cuando se confirma la acción
) {
    // Obtiene el contexto actual
    val context = LocalContext.current

    // Estado para almacenar el valor del campo de correo electrónico
    var email: String by remember { mutableStateOf("") }

    // Estado para habilitar o deshabilitar el botón de enviar basado en la validez del correo electrónico
    var enviarEnable = remember(email) {
        email.trim().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Diálogo que se muestra para restablecer la contraseña
    Dialog(onDismissRequest = { onDimissRequest() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                contentColor = colorResource(id = R.color.cuerpo)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Texto explicativo
                Text(text = "Introduce tu email", fontWeight = FontWeight.Bold)

                // Campo de texto para ingresar la dirección de correo electrónico
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 25.dp),
                    placeholder = {
                        Text(
                            text = "Email address"
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = colorResource(id = R.color.cuerpo),
                    )

                )
                // Fila con botones de Cancelar y Enviar
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    // Botón Cancelar
                    TextButton(
                        onClick = { onDimissRequest() },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = colorResource(id = R.color.cuerpo)
                        )
                    ) {
                        Text("Cancelar")
                    }

                    // Botón Enviar
                    TextButton(
                        onClick = {
                            onConfirmation()
                            // Se muestra un mensaje Toast al usuario
                            Toast.makeText(context, "Se ha enviado un mensaje a su email", Toast.LENGTH_SHORT).show()
                            Firebase.auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "Email sent.")
                                    }
                                }
                        },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = colorResource(id = R.color.cuerpo)
                        ),
                        enabled = enviarEnable // Se habilita o deshabilita según la validez del correo electrónico
                    ) {
                        Text("Enviar")
                    }
                }
            }
        }
    }
}

/**
 * Formulario de registro de usuario.
 *
 * @param isCreateAccount indica si se está creando una cuenta nueva o no
 * @param onDone función de llamada cuando se completa el formulario
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterUserForm(
    isCreateAccount: Boolean,
    onDone: (String, String, String, String) -> Unit = { user, name, email, pwd -> }
) {
    // Estados para almacenar los valores de los campos del formulario
    val user = rememberSaveable { mutableStateOf("") }
    val name = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable() {
        mutableStateOf(false)
    }

    // Obtener el controlador del teclado
    val keyboardController = LocalSoftwareKeyboardController.current

    // Estado para verificar si el formulario es válido
    val valido = remember(email.value, password.value) {
        user.value.trim().isNotBlank() && name.value.trim().isNotBlank() && email.value.trim()
            .isNotBlank() && password.value.trim().isNotBlank() && password.value.length > 5
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Componentes reutilizables para cada campo del formulario
        UserInput(userState = user)
        NameInput(nameState = name)
        EmailInput(emailState = email)
        PasswordInput(
            passwordState = password,
            passwordVisible = passwordVisible
        )

        // Botón de envío del formulario
        SubmitButton(
            textId = if (isCreateAccount) "Crear Cuenta" else "Login",
            inputValido = valido
        ) {
            onDone(user.value.trim(), name.value.trim(), email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

/**
 * Formulario de inicio de sesión de usuario.
 *
 * @param isCreateAccount indica si se está creando una cuenta nueva o no
 * @param onDone función de llamada cuando se completa el formulario
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    isCreateAccount: Boolean,
    onDone: (String, String) -> Unit = { email, pwd -> }
) {
    // Estados para almacenar los valores de los campos del formulario
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable() {
        mutableStateOf(false)
    }

    // Obtener el controlador del teclado
    val keyboardController = LocalSoftwareKeyboardController.current

    // Estado para verificar si el formulario es válido
    val valido = remember(email.value, password.value) {
        email.value.trim().isNotBlank() && password.value.trim()
            .isNotBlank() && password.value.length > 5
    }

    // Estado para manejar la apertura del diálogo de "Forgotten Password"
    var openForgottenPass = rememberSaveable() {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Componentes reutilizables para cada campo del formulario
        EmailInput(emailState = email)
        PasswordInput(
            passwordState = password,
            passwordVisible = passwordVisible
        )

        // Botón para abrir el diálogo de "Forgotten Password"
        TextButton(
            onClick = { openForgottenPass.value = !openForgottenPass.value },
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 23.dp)
        ) {
            Text(
                text = "Forgotten password?",
                fontSize = 12.sp,
                color = colorResource(id = R.color.cuerpo)
            )
        }

        // Mostrar el diálogo de "Forgotten Password" si se ha abierto
        when {
            openForgottenPass.value -> {
                ForgottenPassword(
                    onDimissRequest = { openForgottenPass.value = false },
                    onConfirmation = { openForgottenPass.value = false }
                )
            }
        }

        // Botón de envío del formulario
        SubmitButton(
            textId = if (isCreateAccount) "Crear Cuenta" else "Login",
            inputValido = valido
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

// Componente reutilizable para el botón de envío del formulario
@Composable
fun SubmitButton(
    textId: String,
    inputValido: Boolean,
    onClic: () -> Unit
) {
    Button(
        onClick = onClic,
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
        shape = CircleShape,
        enabled = inputValido,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.boton),
            contentColor = colorResource(id = R.color.cuerpo)
        )
    ) {
        Text(
            text = textId,
            modifier = Modifier.padding(5.dp)
        )
    }
}

// Componente reutilizable para el campo de contraseña
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    passwordVisible: MutableState<Boolean>,
    labelID: String = "Password"
) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()

    // Campo de texto para la contraseña
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelID) },
        singleLine = true,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                // Icono para alternar la visibilidad de la contraseña
                PasswordVisibleIcon(passwordVisible)
            } else {
                null
            }
        })
}

// Componente reutilizable para el icono de visibilidad de la contraseña
@Composable
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    val image = if (passwordVisible.value) {
        Icons.Filled.VisibilityOff
    } else {
        Icons.Filled.Visibility
    }

    // Icono que alterna la visibilidad de la contraseña
    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
        Icon(imageVector = image, contentDescription = "")
    }
}

// Componente reutilizable para el campo de correo electrónico
@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Email"
) {
    InputField(valueestate = emailState, labelId = labelId, keyboardType = KeyboardType.Email)
}

// Componente reutilizable para el campo de nombre de usuario
@Composable
fun UserInput(
    userState: MutableState<String>,
    labelId: String = "User"
) {
    InputField(valueestate = userState, labelId = labelId, keyboardType = KeyboardType.Text)
}

// Componente reutilizable para el campo de nombre
@Composable
fun NameInput(
    nameState: MutableState<String>,
    labelId: String = "Name"
) {
    InputField(valueestate = nameState, labelId = labelId, keyboardType = KeyboardType.Text)
}

// Componente reutilizable para un campo de entrada de texto
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    valueestate: MutableState<String>,
    labelId: String,
    keyboardType: KeyboardType,
    isSingleLine: Boolean = true
) {
    // Campo de texto con contorno
    OutlinedTextField(
        value = valueestate.value,
        onValueChange = { valueestate.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}