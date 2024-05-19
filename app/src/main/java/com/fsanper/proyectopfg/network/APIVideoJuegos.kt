package com.fsanper.proyectopfg.network

import com.fsanper.proyectopfg.modelo.videojuego.DetallesJuego
import com.fsanper.proyectopfg.modelo.videojuego.Genero
import com.fsanper.proyectopfg.modelo.videojuego.VideoJuego
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIVideoJuegos {
    @GET("games${Constantes.API_KEY}")
    suspend fun obtenerJuegos(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 20 // Tamaño de la página
    ): Response<VideoJuego>

    @GET("games${Constantes.API_KEY}")
    suspend fun obtenerJuegosPorGenero(
        @Query("genres") genero: String
    ): Response<VideoJuego>

    @GET("games/{id}${Constantes.API_KEY}")
    suspend fun obtenerDetallesJuego(@Path("id") id: Int): Response<DetallesJuego>

    @GET("genres${Constantes.API_KEY}")
    suspend fun obtenerGeneros(): Response<Genero>
}