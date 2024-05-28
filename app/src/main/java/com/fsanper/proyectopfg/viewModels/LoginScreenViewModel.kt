package com.fsanper.proyectopfg.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.fsanper.proyectopfg.modelo.usuario.Usuario
import com.fsanper.proyectopfg.navegacion.Pantallas
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.SecureRandom
import android.content.Context
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class LoginScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth // Instancia de FirebaseAuth para autenticación en Firebase.
    private val _loading = MutableLiveData(false) // Indicador para prevenir la creación accidental de varios usuarios.

    /**
     * Inicia sesión con correo electrónico y contraseña.
     * @param email: Dirección de correo electrónico.
     * @param password: Contraseña.
     * @param home: Función de retorno para manejar la navegación después del inicio de sesión.
     */
    fun signInWithEmailAndPassword(context: Context,email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("MyLogin", "signInWithEmailAndPassword: ¡Logueado!")
                            home()
                        } else {
                            Log.d(
                                "MyLogin",
                                "signInWithEmailAndPassword: ${task.exception?.message}"
                            )
                            // Mostrar un mensaje de error al usuario
                            Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
            } catch (ex: Exception) {
                Log.d("MyLogin", "signInWithEmailAndPassword: ${ex.message}")
                // Mostrar un mensaje de error al usuario
                Toast.makeText(context, "Error: ${ex.message}", Toast.LENGTH_SHORT).show()
            }
        }

    /**
     * Crea un nuevo usuario con correo electrónico y contraseña.
     * @param user: Nombre de usuario.
     * @param name: Nombre completo del usuario.
     * @param email: Dirección de correo electrónico.
     * @param password: Contraseña.
     * @param home: Función de retorno para manejar la navegación después de la creación del usuario.
     */
    fun createUserWithEmailAndPassword(context: Context, user: String, name: String, email: String, password: String, home: () -> Unit) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    try {
                        if (task.isSuccessful) {
                            createUser(user, name, email, password)
                            home()
                        } else {
                            Log.d(
                                "MyLogin",
                                "createUserWithEmailAndPassword: ${task.result.toString()}"
                            )
                            // Aquí manejamos la excepción específica para el caso de que el correo ya esté en uso
                            if (task.exception is FirebaseAuthUserCollisionException) {
                                Toast.makeText(context, "El correo electrónico ya está en uso", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Registro fallido: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("MyLogin", "Error al crear usuario: ${e.message}")
                        Toast.makeText(context, "Error al crear usuario: El email ya esta en uso", Toast.LENGTH_SHORT).show()
                    } finally {
                        _loading.value = false
                    }
                }
        }
    }

    /**
     * Crea un nuevo usuario en la base de datos con información adicional.
     * @param user: Nombre de usuario.
     * @param name: Nombre completo del usuario.
     * @param email: Dirección de correo electrónico.
     * @param password: Contraseña.
     */
    private fun createUser(user: String, name: String, email: String, password: String) {
        val userId = auth.currentUser?.uid
        val salt = generateSalt()
        val hashedPassword = hashPassword(password, salt)

        val userObject = Usuario(
            userId = userId.toString(),
            user = user,
            name = name,
            email = email,
            password = hashedPassword
        )

        FirebaseFirestore.getInstance()
            .collection("usuarios")
            .add(userObject)
            .addOnSuccessListener {
                Log.d("MyLogin", "Creado ${it.id}")
            }
            .addOnFailureListener {
                Log.d("MyLogin", "Ocurrió un error: ${it}")
            }
    }

    /**
     * Genera una sal aleatoria para fortalecer la seguridad de la contraseña.
     * @return Valor de sal aleatorio.
     */
    private fun generateSalt(): String {
        val secureRandom = SecureRandom()
        val salt = ByteArray(16)
        secureRandom.nextBytes(salt)
        return salt.toString()
    }

    /**
     * Genera un hash de contraseña utilizando la función de hash SHA-512 y la sal proporcionada.
     * @param password: Contraseña a hashear.
     * @param salt: Sal aleatoria.
     * @return Contraseña hasheada.
     */
    private fun hashPassword(password: String?, salt: String): String {
        val saltedPassword = "$password$salt"
        val messageDigest = MessageDigest.getInstance("SHA-512")
        val hashedBytes = messageDigest.digest(saltedPassword.toByteArray())
        return hashedBytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Cierra la sesión del usuario y navega de regreso a la pantalla de inicio de sesión.
     * @param navController: Controlador de navegación.
     */
    fun logout(navController: NavHostController) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                // Ir a la pantalla de inicio de sesión
                navController.navigate(Pantallas.LoginScreen.name)
            }
        }
        firebaseAuth.addAuthStateListener(authStateListener)
    }
}