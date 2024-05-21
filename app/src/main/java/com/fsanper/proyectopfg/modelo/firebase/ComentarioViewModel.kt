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
    fun guardarComentario(
        navController: NavHostController,
        comentario: Comentario,
        context: Context,
        idJuego: Int
    ) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("comentarios")
            .document(comentario.idComentario.toString())

        try {
            fireStoreRef.set(comentario)
                .addOnSuccessListener {
                    navController.navigate("${Pantallas.GameScreen.name}/${idJuego}")
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}