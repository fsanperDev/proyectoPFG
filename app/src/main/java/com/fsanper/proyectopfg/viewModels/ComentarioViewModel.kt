package com.fsanper.proyectopfg.viewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.fsanper.proyectopfg.modelo.comentario.Comentario
import com.fsanper.proyectopfg.navegacion.Pantallas
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
                    // Necesitamos mover esto al hilo principal
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Comentario insertado correctamente", Toast.LENGTH_SHORT).show()
                        navController.navigate("${Pantallas.GameScreen.name}/$idJuego")
                    }
                }
                .addOnFailureListener { e ->
                    // Necesitamos mover esto al hilo principal
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Error. No se ha podido guardar comentario correctamentes", Toast.LENGTH_SHORT).show()
                        navController.navigate("${Pantallas.GameScreen.name}/$idJuego")
                    }
                }
        } catch (e: Exception) {
            // Necesitamos mover esto al hilo principal
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
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
}