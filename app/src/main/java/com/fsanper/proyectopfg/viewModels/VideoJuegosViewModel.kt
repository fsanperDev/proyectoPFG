package com.fsanper.proyectopfg.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsanper.proyectopfg.modelo.videojuego.DetallesJuego
import com.fsanper.proyectopfg.modelo.videojuego.Genero
import com.fsanper.proyectopfg.modelo.videojuego.GeneroList
import com.fsanper.proyectopfg.modelo.videojuego.VideoJuegosLista
import com.fsanper.proyectopfg.network.RetrofitCliente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona los datos relacionados con los videojuegos en la aplicación.
 */
class VideojuegosViewModel : ViewModel() {
    // Lista mutable de juegos
    private val _juegos = MutableStateFlow<List<VideoJuegosLista>>(emptyList())
    val juegos: StateFlow<List<VideoJuegosLista>> = _juegos

    // Detalles del juego seleccionado
    private val _detalleJuego = MutableStateFlow<DetallesJuego?>(null)
    val detalleJuego: StateFlow<DetallesJuego?> = _detalleJuego

    // Lista mutable de géneros
    private val _genero = MutableStateFlow<List<GeneroList>>(emptyList())
    val genero: StateFlow<List<GeneroList>> = _genero

    // Lista mutable de imágenes de juegos
    private val _imagenes = MutableStateFlow<List<VideoJuegosLista>>(emptyList())
    val imagen: StateFlow<List<VideoJuegosLista>> = _imagenes

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Página actual
    private var currentPage = 1

    // Género seleccionado
    private var currentGenre: String? = null

    /**
     * Inicializa el ViewModel y obtiene los juegos y géneros.
     */
    init {
        obtenerJuegos()
        obtenerGeneros()
    }

    /**
     * Función para obtener juegos.
     */
    fun obtenerJuegos() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val response = RetrofitCliente.retrofit.obtenerJuegos(currentPage)
            val nuevosJuegos = response.body()?.listaVideojuegos ?: emptyList()
            _juegos.value = _juegos.value + nuevosJuegos
            _isLoading.value = false
        }
    }

    /**
     * Función para obtener juegos filtrados por género.
     * @param genero Género por el que filtrar los juegos.
     */
    fun obtenerJuegos(genero: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            currentGenre = genero
            currentPage = 1
            val response = if (genero != null) {
                RetrofitCliente.retrofit.obtenerJuegosPorGenero(genero)
            } else {
                RetrofitCliente.retrofit.obtenerJuegos(currentPage)
            }
            val nuevosJuegos = response.body()?.listaVideojuegos ?: emptyList()
            _juegos.value = nuevosJuegos
            _isLoading.value = false
        }
    }

    /**
     * Función para obtener los detalles de un juego.
     * @param id ID del juego del que se desean obtener los detalles.
     */
    fun obtenerDetallesJuego(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val response = RetrofitCliente.retrofit.obtenerDetallesJuego(id)
            if (response.isSuccessful) {
                val detalles = response.body()
                _detalleJuego.value = detalles
            } else {
                // Manejar el caso de respuesta no exitosa
            }
            _isLoading.value = false
        }
    }

    /**
     * Función para obtener los géneros de los juegos.
     */
    fun obtenerGeneros() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val response = RetrofitCliente.retrofit.obtenerGeneros()
            if (response.isSuccessful) {
                val listaGeneros = response.body()?.listaGenero ?: emptyList()
                _genero.value = listaGeneros
            } else {
                // Manejar el caso de respuesta no exitosa
            }
            _isLoading.value = false
        }
    }

    /**
     * Función para obtener las imágenes de un juego.
     * @param nombreJuego Nombre del juego del que se desean obtener las imágenes.
     */
    fun obtenerPantallazos(nombreJuego: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val response = RetrofitCliente.retrofit.buscarJuegosPorNombre(nombreJuego)
            if (response.isSuccessful) {
                val listaImagenes = response.body()?.listaVideojuegos ?: emptyList()
                _imagenes.value = listaImagenes
            } else {
                // Manejar el caso de respuesta no exitosa
            }
            _isLoading.value = false
        }
    }

    /**
     * Función para cargar más juegos.
     */
    fun loadMore() {
        currentPage++
        obtenerJuegos()
    }

    /**
     * Función para restablecer el filtro de género.
     */
    fun resetearFiltro() {
        currentGenre = null
    }
}