package com.fsanper.proyectopfg.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsanper.proyectopfg.modelo.videojuego.VideoJuegosLista
import com.fsanper.proyectopfg.network.RetrofitCliente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideojuegosViewModel : ViewModel() {
    private val _juegos = MutableStateFlow<List<VideoJuegosLista>>(emptyList())
    val juegos: StateFlow<List<VideoJuegosLista>> = _juegos
    private var currentPage = 1 // Página actual
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        obtenerJuegos()
    }

    fun obtenerJuegos() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val response = RetrofitCliente.retrofit.obtenerJuegos(currentPage)
            val nuevosJuegos = response.body()?.listaVideojuegos ?: emptyList()
            _juegos.value = _juegos.value + nuevosJuegos // Actualizar el valor del MutableStateFlow
            currentPage++
            _isLoading.value = false
        }
    }

    fun loadMore() {
        obtenerJuegos()
    }
}