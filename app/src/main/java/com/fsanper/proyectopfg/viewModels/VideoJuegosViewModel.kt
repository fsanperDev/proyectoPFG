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

class VideojuegosViewModel : ViewModel() {
    private val _juegos = MutableStateFlow<List<VideoJuegosLista>>(emptyList())
    val juegos: StateFlow<List<VideoJuegosLista>> = _juegos
    private val _detalleJuego = MutableStateFlow<DetallesJuego?>(null)
    val detalleJuego: StateFlow<DetallesJuego?> = _detalleJuego
    private val _genero = MutableStateFlow<List<GeneroList>>(emptyList())
    val genero: StateFlow<List<GeneroList>> = _genero

    private var currentPage = 1 // Página actual
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var currentGenre: String? = null // Género seleccionado

    init {
        obtenerJuegos()
        obtenerGeneros()
    }

    fun obtenerJuegos() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val response = RetrofitCliente.retrofit.obtenerJuegos(currentPage)
            val nuevosJuegos = response.body()?.listaVideojuegos ?: emptyList()
            _juegos.value = _juegos.value + nuevosJuegos // Actualizar el valor del MutableStateFlow
            _isLoading.value = false
        }
    }

    fun obtenerJuegos(genero: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            currentGenre = genero
            currentPage = 1 // Reiniciar la página actual para el nuevo filtro
            val response = if (genero != null) {
                RetrofitCliente.retrofit.obtenerJuegosPorGenero(genero)
            } else {
                RetrofitCliente.retrofit.obtenerJuegos(currentPage)
            }
            val nuevosJuegos = response.body()?.listaVideojuegos ?: emptyList()
            _juegos.value = nuevosJuegos // Sobrescribir la lista de juegos con los nuevos juegos filtrados
            _isLoading.value = false
        }
    }

    fun obtenerDetallesJuego(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val response = RetrofitCliente.retrofit.obtenerDetallesJuego(id)
            if (response.isSuccessful) {
                val detalles = response.body()
                _detalleJuego.value = detalles
            } else {
                // Manejar el caso de respuesta no exitosa, por ejemplo, mostrar un mensaje de error
            }
            _isLoading.value = false
        }
    }

    fun obtenerGeneros() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val response = RetrofitCliente.retrofit.obtenerGeneros()
            if (response.isSuccessful) {
                val listaGeneros = response.body()?.listaGenero ?: emptyList()
                _genero.value = listaGeneros
            } else {
                // Manejar el caso de respuesta no exitosa, por ejemplo, mostrar un mensaje de error
            }
            _isLoading.value = false
        }
    }

    fun loadMore() {
        currentPage++
        obtenerJuegos()
    }

    fun resetearFiltro() {
        currentGenre = null // Restablecer el género seleccionado a null
    }
}