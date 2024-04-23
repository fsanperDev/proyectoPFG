package com.fsanper.proyectopfg.network

import com.fsanper.proyectopfg.modelo.videojuego.VideoJuego
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIVideoJuegos {
    @GET("games${Constantes.API_KEY}")
    suspend fun obtenerJuegos(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 20 // Tamaño de la página (20 por defecto según la documentación)
    ): Response<VideoJuego>
}