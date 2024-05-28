package com.fsanper.proyectopfg.network

import com.fsanper.proyectopfg.modelo.videojuego.DetallesJuego
import com.fsanper.proyectopfg.modelo.videojuego.Genero
import com.fsanper.proyectopfg.modelo.videojuego.VideoJuego
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz que define los métodos para interactuar con la API de videojuegos.
 */
interface APIVideoJuegos {
    /**
     * Obtiene una lista de videojuegos desde la API.
     * @param page Número de página a recuperar.
     * @param pageSize Tamaño de la página (por defecto: 20).
     * @return Respuesta que contiene una lista de videojuegos.
     */
    @GET("games${Constantes.API_KEY}")
    suspend fun obtenerJuegos(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 20 // Tamaño de la página
    ): Response<VideoJuego>

    /**
     * Obtiene una lista de videojuegos filtrados por género desde la API.
     * @param genero El género por el cual filtrar los videojuegos.
     * @return Respuesta que contiene una lista de videojuegos filtrados por género.
     */
    @GET("games${Constantes.API_KEY}")
    suspend fun obtenerJuegosPorGenero(
        @Query("genres") genero: String
    ): Response<VideoJuego>

    /**
     * Obtiene los detalles de un juego específico desde la API.
     * @param id El ID del juego del cual se obtienen los detalles.
     * @return Respuesta que contiene los detalles del juego.
     */
    @GET("games/{id}${Constantes.API_KEY}")
    suspend fun obtenerDetallesJuego(@Path("id") id: Int): Response<DetallesJuego>

    /**
     * Obtiene una lista de géneros de videojuegos desde la API.
     * @return Respuesta que contiene una lista de géneros de videojuegos.
     */
    @GET("genres${Constantes.API_KEY}")
    suspend fun obtenerGeneros(): Response<Genero>
}