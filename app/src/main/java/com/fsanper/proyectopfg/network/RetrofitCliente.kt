package com.fsanper.proyectopfg.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto que proporciona una instancia única de Retrofit para interactuar con la API de videojuegos.
 */
object RetrofitCliente {
    /**
     * Instancia única de Retrofit configurada para la API de videojuegos.
     */
    val retrofit: APIVideoJuegos by lazy {
        // Configuración de Retrofit con la URL base y el convertidor Gson
        Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL) // URL base de la API
            .addConverterFactory(GsonConverterFactory.create()) // Convertidor Gson para interpretar JSON
            .build()
            .create(APIVideoJuegos::class.java) // Creación de la instancia de la interfaz APIVideoJuegos
    }
}