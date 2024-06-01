package com.fsanper.proyectopfg.modelo.videojuego

import com.google.gson.annotations.SerializedName

/**
 * Data class que representa la respuesta que contiene una lista de videojuegos.
 * @param listaVideojuegos Lista de videojuegos obtenida de la respuesta.
 */
data class VideoJuego(
    @SerializedName("results")
    val listaVideojuegos: List<VideoJuegosLista> // Lista de videojuegos obtenida de la respuesta
)

/**
 * Data class que representa un videojuego individual en la lista de videojuegos.
 * @param id Identificador único del videojuego.
 * @param nombre Nombre del videojuego.
 * @param released Fecha de lanzamiento del videojuego.
 * @param metacritic Puntuación del videojuego en Metacritic.
 * @param imagen URL de la imagen de fondo del videojuego.
 */
data class VideoJuegosLista(
    @SerializedName("id")
    val id: Int,                     // Identificador único del videojuego
    @SerializedName("name")
    val nombre: String,              // Nombre del videojuego
    @SerializedName("released")
    val released: String,            // Fecha de lanzamiento del videojuego
    @SerializedName("metacritic")
    val metacritic: String,          // Puntuación del videojuego en Metacritic
    @SerializedName("background_image")
    val imagen: String,              // URL de la imagen de fondo del videojuego
    @SerializedName("short_screenshots")
    val short_screenshot: List<Screenshot>
)

data class Screenshot(
    @SerializedName("image")
    val screenshot: String
)