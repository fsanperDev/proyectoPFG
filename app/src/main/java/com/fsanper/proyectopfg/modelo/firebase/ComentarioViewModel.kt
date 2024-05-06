package com.fsanper.proyectopfg.modelo.firebase

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.fsanper.proyectopfg.modelo.comentario.Comentario
import com.fsanper.proyectopfg.navegacion.Pantallas
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComentarioViewModel: ViewModel() {
    /**
     * Guarda la compra en la cesta.
     * @param navController Un objeto NavHostController que controla la navegación en la aplicación.
     * @param gameData Datos del juego que se va a agregar a la cesta.
     * @param context Contexto de la aplicación.
     */
    fun saveCompra(
        navController: NavHostController,
        comentario: Comentario,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("comentario")
            .document(comentario.idComentario.toString())

        try {
            fireStoreRef.set(comentario)
                .addOnSuccessListener {
                    navController.navigate(Pantallas.HomeScreen.name)
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}