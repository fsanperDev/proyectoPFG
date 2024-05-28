package com.fsanper.proyectopfg.modelo.videojuego

import com.google.gson.annotations.SerializedName

/**
 * Data class que representa los detalles de un juego.
 * @param nombre Nombre del juego.
 * @param descripcion Descripción del juego.
 * @param lanzamiento Fecha de lanzamiento del juego.
 * @param imagen URL de la imagen de fondo del juego.
 * @param plataformas Lista de plataformas en las que está disponible el juego.
 */
data class DetallesJuego(
    @SerializedName("name")
    val nombre: String,           // Nombre del juego
    @SerializedName("description")
    val descripcion: String,      // Descripción del juego
    @SerializedName("released")
    val lanzamiento: String,      // Fecha de lanzamiento del juego
    @SerializedName("background_image")
    val imagen: String,           // URL de la imagen de fondo del juego
    @SerializedName("platforms")
    val plataformas: List<Plataforma> // Lista de plataformas del juego
)

/**
 * Data class que representa una plataforma en la que está disponible un juego.
 * @param plataforma Detalles de la plataforma.
 */
data class Plataforma(
    @SerializedName("platform")
    val plataforma: PlatformaDetalle // Detalles de la plataforma
)

/**
 * Data class que representa los detalles de una plataforma.
 * @param nombre Nombre de la plataforma.
 */
data class PlatformaDetalle(
    @SerializedName("name")
    val nombre: String // Nombre de la plataforma
)